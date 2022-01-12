//Establish the WebSocket connection and set up event handlers
let rawReadings = [];
let soilTempReadings = [];
let airTempReadings = [];
let humidityReadings = [];
let lightReadings = [];
let moistureReadings = [];
let moistureTargets = [];
let soilTempChart, airTempChart, humidityChart, lightChart, moistureChart;
let chartsInit = false;
let maxPoints = 50;
let ticks = {
  callback: function(tick, index, array) {
    return (index % 3) ? "" : tick;
  },
  maxTicksLimit: 10,
};
let legend = {display: false};

let xAxes = [{
  legend: {display: false},
  ticks: ticks,
  type: 'time',
  time: {
    unit: 'second',
    displayFormats: {
      second: 'HH:mm:ss A'
    }
  }
}];

const initCharts = () => {
  soilTempChart = new Chart(document.getElementById('soilTempChart').getContext('2d'), {
    type: 'line',
    options: {
      annotation: {
        annotations: []
      },
      legend: legend,
      title: {text: "Soil Temps (\u00B0F)", display: true},
      scales: {
        xAxes: xAxes,
      },
    },
    data: {
      datasets: [{
        pointBackgroundColor: "#f14343",
        backgroundColor: "#ee8e8e",
        lineColor: "#CC0000",
        borderColor: "#CC0000",
        data: soilTempReadings,
      }]
    }
  });
  airTempChart = new Chart(document.getElementById('airTempChart').getContext('2d'), {
    type: 'line',
    options: {
      legend: legend,
      title: {text: "Air Temps (\u00B0F)", display: true},
      scales: {
        xAxes: xAxes,
      },
    },
    data: {
      datasets: [{
        pointBackgroundColor: "#527ac3",
        backgroundColor: "#8698cf",
        lineColor: "#0044cc",
        borderColor: "#0044cc",
        data: airTempReadings,
      }]
    }
  });
  humidityChart = new Chart(document.getElementById('humidityChart').getContext('2d'), {
    type: 'line',
    options: {
      legend: legend,
      title: {text: "Humidity (%)", display: true},
      scales: {
        xAxes: xAxes,
      },
    },
    data: {
      datasets: [{
        pointBackgroundColor: "#a25cca",
        backgroundColor: "#bd90cb",
        lineColor: "#a000cc",
        borderColor: "#a000cc",
        data: humidityReadings,
      }]
    }
  });
  moistureChart = new Chart(document.getElementById('moistureChart').getContext('2d'), {
    type: 'line',
    options: {
      legend: legend,
      title: {text: "Moisture (%)", display: true},
      scales: {
        xAxes: xAxes,
        yAxes: [{
          ticks: {
            beginAtZero: true
          }
        }],
      },
    },
    data: {
      datasets: [{
        fill: false,
        pointBackgroundColor: "#4f733f",
        backgroundColor: "#71966f",
        lineColor: "#336e00",
        borderColor: "#336e00",
        data: moistureReadings,
      },
      {
        fill: false,
        pointBackgroundColor: "#c040a7",
        backgroundColor: "#a22b86",
        lineColor: "#c0269a",
        borderColor: "#c0269a",
        data: moistureTargets,
      }]
    }
  });
  lightChart = new Chart(document.getElementById('lightChart').getContext('2d'), {
    type: 'line',
    options: {
      legend: legend,
      title: {text: "Light", display: true},
      scales: {
        xAxes: xAxes,
      },
    },
    data: {
      datasets: [{
        pointBackgroundColor: "#fd9755",
        backgroundColor: "#ffbe96",
        lineColor: "#ff6703",
        borderColor: "#ff6703",
        data: lightReadings,
      }]
    }
  });

  chartsInit = true;
  connect();
};

class Reading {
  soilTemp;
  airTemp;
  humidity;
  light;
  moisture;
  moistureTarget;
  outletState;
  pumpState;
  readAt;

  constructor(reading) {
    this.soilTemp = +parseFloat(reading.soilTemp).toFixed(2);
    this.airTemp = +parseFloat(reading.airTemp).toFixed(2);
    this.humidity = +parseFloat(reading.humidity).toFixed(2);
    this.light = +parseFloat(reading.light).toFixed(2);
    this.moisture = +parseFloat(reading.moisture).toFixed(2);
    this.moistureTarget = +parseFloat(reading.waterTarget).toFixed(2);
    this.outletState = reading.outletState ? 'ON' : 'OFF';
    this.outletState = reading.pumpState ? 'ON' : 'OFF';
    this.readAt = new Date();
  }

}

const connect = () => {
  console.log('Connecting to WebSocket...')
  const ws = new WebSocket("ws://" + location.hostname + ":" + location.port + "/data/greenthumb");
  ws.onopen = (msg) => {
    console.log('Connected!')
  };
  ws.onmessage = (msg) => {
    const reading = new Reading(JSON.parse(msg.data));
    rawReadings.push(reading);
    soilTempReadings.push({y: reading.soilTemp, x: reading.readAt});
    airTempReadings.push({y: reading.airTemp, x: reading.readAt});
    humidityReadings.push({y: reading.humidity, x: reading.readAt});
    lightReadings.push({y: reading.light, x: reading.readAt});
    moistureReadings.push({y: reading.moisture, x: reading.readAt});
    moistureTargets.push({y: reading.moistureTarget, x: reading.readAt});

    // keep the latest `maxPoints`
    if( rawReadings.length > maxPoints ) rawReadings.shift();
    if( soilTempReadings.length > maxPoints ) soilTempReadings.shift();
    if( airTempReadings.length > maxPoints ) airTempReadings.shift();
    if( humidityReadings.length > maxPoints ) humidityReadings.shift();
    if( lightReadings.length > maxPoints ) lightReadings.shift();
    if( moistureReadings.length > maxPoints ) moistureReadings.shift();
    if( moistureTargets.length > maxPoints ) moistureTargets.shift();

    // annotate the soil chart if the outlet state has changed
    if( rawReadings.length > 1 ) {
      let latestReading = rawReadings[rawReadings.length-1]
      let prevReading = rawReadings[rawReadings.length-2]
      if( latestReading.outletState !== prevReading.outletState ) {
        let annotation = getAnnotation(`Outlet: ${latestReading.outletState}`, latestReading.readAt);
        soilTempChart.options.annotation.annotations.push(annotation)
      }
      soilTempChart.options.annotation.annotations.forEach((a, aIdx) => {
        if(soilTempReadings.findIndex((d) => d.x === a.value) === -1) {
          soilTempChart.options.annotation.annotations.splice(aIdx, 1);
          console.log('Removing annotation, it is no longer in range.')
        }
      })

    }
    if (chartsInit) {
      soilTempChart.update();
      airTempChart.update();
      humidityChart.update();
      moistureChart.update();
      lightChart.update();
    }
    document.querySelector('#currentOutletState').innerHTML = reading.outletState;
    document.querySelector('#currentPumpState').innerHTML = reading.pumpState;
    document.querySelector('#currentAirTemp').innerHTML = reading.airTemp;
    document.querySelector('#currentSoilTemp').innerHTML = reading.soilTemp;
    document.querySelector('#currentHumidity').innerHTML = reading.humidity;
    document.querySelector('#currentMoisture').innerHTML = reading.moisture;
    document.querySelector('#currentMoistureTarget').innerHTML = reading.moistureTarget;
    document.querySelector('#currentLight').innerHTML = reading.light;
  };
  ws.onclose = (e) => {
    console.log('Socket is closed. Reconnect will be attempted in 1 second.', e.reason);
    setTimeout(function() {
      connect();
    }, 1000);
  };
  ws.onerror = function(err) {
    console.error('Socket encountered error: ', err.message, 'Closing socket');
    ws.close();
  };
};

const getAnnotation = (label, value) => {
  return {
    drawTime: "afterDatasetsDraw",
    type: "line",
    mode: "vertical",
    scaleID: "x-axis-0",
    value: value,
    borderWidth: 5,
    borderColor: "red",
    label: {
      content: label,
      enabled: true,
      position: "top"
    }
  }
}

//Send a message if it's not empty, then clear the input field
const sendMessage = (message) => {
  if (message !== "") {
    webSocket.send(message);
  }
};

document.addEventListener('DOMContentLoaded', initCharts);