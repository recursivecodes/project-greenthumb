package codes.recursive.controller;

import codes.recursive.domain.ReadingByHourDTO;
import codes.recursive.repository.AbstractReadingRepository;
import codes.recursive.repository.ReadingRepository;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.views.ModelAndView;
import io.micronaut.websocket.WebSocketBroadcaster;

import java.util.List;
import java.util.Map;

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

    @Get("/reports")
    ModelAndView reports() {
        List readingsByHour = abstractReadingRepository.getAvgReadingsByHour();
        List readingsByDayOfMonth = abstractReadingRepository.getAvgReadingsByDayOfMonth();
        List readingsByDayNight = abstractReadingRepository.getAvgReadingsByDayNight();
        List readingsOverall = abstractReadingRepository.getAvgReadingsOverall();
        return new ModelAndView("reports", CollectionUtils.mapOf(
                "currentView", "reports",
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