package codes.recursive.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.core.annotation.Creator;
import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.TypeDef;
import io.micronaut.data.model.DataType;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.sql.Date;
import java.util.Map;

@Entity
@Table(name = "greenthumb_readings")
public class Reading {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @TypeDef(type = DataType.JSON)
    private String reading;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @DateCreated
    private Date createdOn;

    @Creator
    public Reading(Long id, String reading, @Nullable Date createdOn) {
        this.id = id;
        this.reading = reading;
        this.createdOn = createdOn;
    }

    public Reading(Map reading) throws JsonProcessingException {
        this.reading = new ObjectMapper().writeValueAsString(reading);
    }

    public Reading(String reading) {
        this.reading = reading;
    }

    public Reading() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map getReadingAsMap() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(reading, Map.class);
    }

    public String getReading() {
        return reading;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
}
