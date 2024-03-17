package com.tire.workshop.collector.controller;

import com.tire.workshop.collector.domain.AvailableTime;
import com.tire.workshop.collector.domain.Domain;
import com.tire.workshop.collector.service.CollectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workshop-api")
@RequiredArgsConstructor
public class CollectorController {

    private final CollectorService collectorService;

    @GetMapping("/available-times")
    public Domain getAvailableTimes(@RequestParam String from,
                                       @RequestParam String until,
                                       @RequestParam List<String> workshopNames,
                                       @RequestParam List<String> vehicleTypes) {
        return collectorService.getAvailableTimes(from, until, workshopNames, vehicleTypes);
    }

    @PutMapping("/book-time")
    public @ResponseBody Domain bookTime(@RequestBody AvailableTime availableTime) {
        return collectorService.bookTireChangeTime(availableTime);
    }
}
