package codes.recursive.domain;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class ReadingByHourDTO {
    private int year;
    private int hour;
    private float avgAirTemp;
    private float avgSoilTemp;
    private float avgMoisture;
    private float avgHumidity;
    private float avgLight;

    public ReadingByHourDTO() {}

    public ReadingByHourDTO(int year, int hour, float avgAirTemp, float avgSoilTemp, float avgMoisture, float avgHumidity, float avgLight) {
        this.year = year;
        this.hour = hour;
        this.avgAirTemp = avgAirTemp;
        this.avgSoilTemp = avgSoilTemp;
        this.avgMoisture = avgMoisture;
        this.avgHumidity = avgHumidity;
        this.avgLight = avgLight;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public float getAvgLight() {
        return avgLight;
    }

    public void setAvgLight(float avgLight) {
        this.avgLight = avgLight;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public float getAvgAirTemp() {
        return avgAirTemp;
    }

    public void setAvgAirTemp(float avgAirTemp) {
        this.avgAirTemp = avgAirTemp;
    }

    public float getAvgSoilTemp() {
        return avgSoilTemp;
    }

    public void setAvgSoilTemp(float avgSoilTemp) {
        this.avgSoilTemp = avgSoilTemp;
    }

    public float getAvgMoisture() {
        return avgMoisture;
    }

    public void setAvgMoisture(float avgMoisture) {
        this.avgMoisture = avgMoisture;
    }

    public float getAvgHumidity() {
        return avgHumidity;
    }

    public void setAvgHumidity(float avgHumidity) {
        this.avgHumidity = avgHumidity;
    }
}
