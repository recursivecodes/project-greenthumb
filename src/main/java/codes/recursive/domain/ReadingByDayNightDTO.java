package codes.recursive.domain;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class ReadingByDayNightDTO {
    private int year;
    private String timePeriod;
    private float avgAirTemp;
    private float avgSoilTemp;
    private float avgMoisture;
    private float avgHumidity;
    private float avgLight;

    public ReadingByDayNightDTO() {}

    public ReadingByDayNightDTO(int year, String timePeriod, float avgAirTemp, float avgSoilTemp, float avgMoisture, float avgHumidity, float avgLight) {
        this.year = year;
        this.timePeriod = timePeriod;
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

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public float getAvgLight() {
        return avgLight;
    }

    public void setAvgLight(float avgLight) {
        this.avgLight = avgLight;
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
