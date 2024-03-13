package com.tire.workshop.collector.controller;

import com.tire.workshop.collector.Domain;
import com.tire.workshop.collector.service.CollectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workshops")
@RequiredArgsConstructor
public class CollectorController {

    private final CollectorService collectorService;

    @GetMapping("/available-times")
    public Domain getAllAvailableTimes(@RequestParam String from,
                                       @RequestParam String until,
                                        @RequestParam String workshopName) {
        return collectorService.getAllAvailableTimes(from, until, workshopName);
    }
}
