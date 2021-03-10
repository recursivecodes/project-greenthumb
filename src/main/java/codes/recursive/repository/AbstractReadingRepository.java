package codes.recursive.repository;

import codes.recursive.domain.*;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.PageableRepository;
import org.hibernate.transform.Transformers;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public abstract class AbstractReadingRepository implements PageableRepository<Reading, Long> {
    private final EntityManager entityManager;

    public AbstractReadingRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public List getAvgReadingsByHour(Boolean today) {
        String sql = "select\n" +
                "       to_char(from_tz(created_on, 'GMT') at time zone 'America/New_York', 'YYYY') as \"year\",\n" +
                "       to_char(from_tz(created_on, 'GMT') at time zone 'America/New_York', 'HH24') as \"hour\",\n" +
                "       round(avg(gr.reading.airTemp), 2) as \"avgAirTemp\",\n" +
                "       round(avg(gr.reading.soilTemp), 2) as \"avgSoilTemp\",\n" +
                "       round(avg(gr.reading.moisture), 2) as \"avgMoisture\",\n" +
                "       round(avg(gr.reading.humidity), 2) as \"avgHumidity\",\n" +
                "       round(avg(gr.reading.light), 2) as \"avgLight\"\n" +
                "     from greenthumb_readings gr\n";
        if (today) {
            sql+= "where to_char(from_tz(created_on, 'GMT') at time zone 'America/New_York', 'YYYY-MM-DD') = to_char(sysdate, 'YYYY-MM-DD')\n";
        }
        sql +=  "     group by to_char(from_tz(created_on, 'GMT') at time zone 'America/New_York', 'YYYY'), to_char(created_on, 'HH24')\n" +
                "     order by to_char(from_tz(created_on, 'GMT') at time zone 'America/New_York', 'YYYY'), to_char(created_on, 'HH24')";
        return entityManager.createNativeQuery(sql)
                .unwrap(org.hibernate.query.NativeQuery.class)
                .addScalar("year", IntegerType.INSTANCE)
                .addScalar("hour", IntegerType.INSTANCE)
                .addScalar("avgAirTemp", FloatType.INSTANCE)
                .addScalar("avgSoilTemp", FloatType.INSTANCE)
                .addScalar("avgMoisture", FloatType.INSTANCE)
                .addScalar("avgHumidity", FloatType.INSTANCE)
                .addScalar("avgLight", FloatType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(ReadingByHourDTO.class))
                .getResultList();
    }

    @Transactional
    public List getAvgReadingsByDayNight() {
        String sql = "select \n" +
                "    to_char(from_tz(created_on, 'GMT') at time zone 'America/New_York', 'YYYY') as \"year\",\n" +
                "    ( case \n" +
                "        when cast( to_char(from_tz(created_on, 'GMT') at time zone 'America/New_York', 'HH24') as number ) > 07 and cast( to_char(created_on, 'HH24') as number ) < 21 then 'Day'\n" +
                "        else 'Night'\n" +
                "    end ) as \"timePeriod\",\n" +
                "    round(avg(gr.reading.airTemp), 2) as \"avgAirTemp\",\n" +
                "    round(avg(gr.reading.soilTemp), 2) as \"avgSoilTemp\",\n" +
                "    round(avg(gr.reading.humidity), 2) as \"avgHumidity\",\n" +
                "    round(avg(gr.reading.moisture), 2) as \"avgMoisture\",\n" +
                "    round(avg(gr.reading.light), 2) as \"avgLight\"\n" +
                "from greenthumb_readings gr\n" +
                "group by to_char(from_tz(created_on, 'GMT') at time zone 'America/New_York', 'YYYY'), ( case when cast( to_char(from_tz(created_on, 'GMT') at time zone 'America/New_York', 'HH24') as number ) > 07 and cast( to_char(from_tz(created_on, 'GMT') at time zone 'America/New_York', 'HH24') as number ) < 21 then 'Day' else 'Night' end )";
        return entityManager.createNativeQuery(sql)
                .unwrap(org.hibernate.query.NativeQuery.class)
                .addScalar("year", IntegerType.INSTANCE)
                .addScalar("timePeriod", StringType.INSTANCE)
                .addScalar("avgAirTemp", FloatType.INSTANCE)
                .addScalar("avgSoilTemp", FloatType.INSTANCE)
                .addScalar("avgMoisture", FloatType.INSTANCE)
                .addScalar("avgHumidity", FloatType.INSTANCE)
                .addScalar("avgLight", FloatType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(ReadingByDayNightDTO.class))
                .getResultList();
    }
    @Transactional
    public List getAvgReadingsByDayOfMonth() {
        String sql = "select \n" +
                "    to_char(from_tz(created_on, 'GMT') at time zone 'America/New_York', 'YYYY') as \"year\",\n" +
                "    to_char(from_tz(created_on, 'GMT') at time zone 'America/New_York', 'DD') as \"dayOfMonth\",\n" +
                "    round(avg(gr.reading.airTemp), 2) as \"avgAirTemp\",\n" +
                "    round(avg(gr.reading.soilTemp), 2) as \"avgSoilTemp\",\n" +
                "    round(avg(gr.reading.humidity), 2) as \"avgHumidity\",\n" +
                "    round(avg(gr.reading.moisture), 2) as \"avgMoisture\",\n" +
                "    round(avg(gr.reading.light), 2) as \"avgLight\"\n" +
                "from greenthumb_readings gr\n" +
                "group by to_char(from_tz(created_on, 'GMT') at time zone 'America/New_York', 'YYYY'), to_char(from_tz(created_on, 'GMT') at time zone 'America/New_York', 'DD')\n" +
                "order by to_char(from_tz(created_on, 'GMT') at time zone 'America/New_York', 'YYYY'), to_char(from_tz(created_on, 'GMT') at time zone 'America/New_York', 'DD')";
        return entityManager.createNativeQuery(sql)
                .unwrap(org.hibernate.query.NativeQuery.class)
                .addScalar("year", IntegerType.INSTANCE)
                .addScalar("dayOfMonth", IntegerType.INSTANCE)
                .addScalar("avgAirTemp", FloatType.INSTANCE)
                .addScalar("avgSoilTemp", FloatType.INSTANCE)
                .addScalar("avgMoisture", FloatType.INSTANCE)
                .addScalar("avgHumidity", FloatType.INSTANCE)
                .addScalar("avgLight", FloatType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(ReadingByDayOfMonthDTO.class))
                .getResultList();
    }

    @Transactional
    public List getAvgReadingsOverall() {
        String sql = "select \n" +
                "    to_char(from_tz(created_on, 'GMT') at time zone 'America/New_York', 'YYYY') as \"year\",\n" +
                "    round(avg(gr.reading.airTemp), 2) as \"avgAirTemp\",\n" +
                "    round(avg(gr.reading.soilTemp), 2) as \"avgSoilTemp\",\n" +
                "    round(avg(gr.reading.humidity), 2) as \"avgHumidity\",\n" +
                "    round(avg(gr.reading.moisture), 2) as \"avgMoisture\",\n" +
                "    round(avg(gr.reading.light), 2) as \"avgLight\"\n" +
                "from greenthumb_readings gr\n" +
                "group by to_char(from_tz(created_on, 'GMT') at time zone 'America/New_York', 'YYYY')";
        return entityManager.createNativeQuery(sql)
                .unwrap(org.hibernate.query.NativeQuery.class)
                .addScalar("year", IntegerType.INSTANCE)
                .addScalar("avgAirTemp", FloatType.INSTANCE)
                .addScalar("avgSoilTemp", FloatType.INSTANCE)
                .addScalar("avgMoisture", FloatType.INSTANCE)
                .addScalar("avgHumidity", FloatType.INSTANCE)
                .addScalar("avgLight", FloatType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(ReadingOverallAverageDTO.class))
                .getResultList();
    }

}
