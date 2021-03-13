package codes.recursive.controller;

import codes.recursive.repository.AbstractReadingRepository;
import codes.recursive.repository.ReadingRepository;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.views.ModelAndView;
import io.micronaut.websocket.WebSocketBroadcaster;

import java.time.LocalDateTime;
import java.util.List;

@Controller("/page")
public class PageController {

    private final WebSocketBroadcaster broadcaster;
    private final ReadingRepository readingRepository;
    private final AbstractReadingRepository abstractReadingRepository;

    public PageController(
            WebSocketBroadcaster broadcaster,
            ReadingRepository readingRepository,
            AbstractReadingRepository abstractReadingRepository) {
        this.broadcaster = broadcaster;
        this.readingRepository = readingRepository;
        this.abstractReadingRepository = abstractReadingRepository;
    }

    @Get()
    ModelAndView home() {
        return new ModelAndView("home", CollectionUtils.mapOf("currentView", "home"));
    }

    @Get("/reports{/year}")
    ModelAndView reports(@Nullable @PathVariable Integer year) {
        if( year == null ) {
            year = LocalDateTime.now().getYear();
        }
        List readingsByHourToday = abstractReadingRepository.getAvgReadingsByHour(true, year);
        List readingsByHour = abstractReadingRepository.getAvgReadingsByHour(false, year);
        List readingsByDayOfMonth = abstractReadingRepository.getAvgReadingsByDayOfMonth(year);
        List readingsByDayNight = abstractReadingRepository.getAvgReadingsByDayNight(year);
        List readingsOverall = abstractReadingRepository.getAvgReadingsOverall(year);
        return new ModelAndView("reports", CollectionUtils.mapOf(
                "year", year,
                "currentView", "reports",
                "readingsByHourToday", readingsByHourToday,
                "readingsByHour", readingsByHour,
                "readingsByDayOfMonth", readingsByDayOfMonth,
                "readingsByDayNight", readingsByDayNight,
                "readingsOverall", readingsOverall
        ));
    }

    @Get("/test")
    String test() {
        this.broadcaster.broadcastSync("test");
        return "test";
    }
}