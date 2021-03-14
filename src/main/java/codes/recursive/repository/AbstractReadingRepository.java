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
    public List getAvgReadingsByHour(Boolean today, int year) {
        String sql = "select \"year\", \"hour\", \"avgAirTemp\", \"avgSoilTemp\", \"avgMoisture\", \"avgHumidity\", \"avgLight\"\n";
        if(today) {
            sql += "from vw_avg_by_hour_today\n";
        }
        else {
            sql += "from vw_avg_by_hour\n";
        }
        sql += "where \"year\" = :year\n";
        return entityManager.createNativeQuery(sql)
                .unwrap(org.hibernate.query.NativeQuery.class)
                .setParameter("year", year)
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
    public List getAvgReadingsByDayNight(int year) {
        String sql = "select \"year\", \"timePeriod\", \"avgAirTemp\", \"avgSoilTemp\", \"avgMoisture\", \"avgHumidity\", \"avgLight\"\n" +
                "from vw_avg_by_day_night\n" +
                "where \"year\" = :year";
        return entityManager.createNativeQuery(sql)
                .setParameter("year", year)
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
    public List getAvgReadingsByDayOfMonth(int year) {
        String sql = "select \"year\", \"dayOfMonth\", \"avgAirTemp\", \"avgSoilTemp\", \"avgMoisture\", \"avgHumidity\", \"avgLight\"\n" +
                "from vw_avg_by_day\n" +
                "where \"year\" = :year";
        return entityManager.createNativeQuery(sql)
                .setParameter("year", year)
                .unwrap(org.hibernate.query.NativeQuery.class)
                .addScalar("year", IntegerType.INSTANCE)
                .addScalar("dayOfMonth", StringType.INSTANCE)
                .addScalar("avgAirTemp", FloatType.INSTANCE)
                .addScalar("avgSoilTemp", FloatType.INSTANCE)
                .addScalar("avgMoisture", FloatType.INSTANCE)
                .addScalar("avgHumidity", FloatType.INSTANCE)
                .addScalar("avgLight", FloatType.INSTANCE)
                .setResultTransformer(Transformers.aliasToBean(ReadingByDayOfMonthDTO.class))
                .getResultList();
    }

    @Transactional
    public List getAvgReadingsOverall(int year) {
        String sql = "select \"year\", \"avgAirTemp\", \"avgSoilTemp\", \"avgMoisture\", \"avgHumidity\", \"avgLight\"\n" +
                "from vw_avg_by_year\n" +
                "where \"year\" = :year";
        return entityManager.createNativeQuery(sql)
                .setParameter("year", year)
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
