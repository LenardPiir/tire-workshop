package com.tire.workshop.collector.controller;

import com.tire.workshop.collector.domain.AvailableTime;
import com.tire.workshop.collector.domain.Domain;
import com.tire.workshop.collector.domain.Workshop;
import com.tire.workshop.collector.service.WorkshopCollectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workshop-api")
@RequiredArgsConstructor
public class WorkshopCollectorController {

    private final WorkshopCollectorService workshopCollectorService;

    @GetMapping("/available-times")
    public Domain getAvailableTimes(@RequestParam String from,
                                       @RequestParam String until,
                                       @RequestParam List<String> workshopNames,
                                       @RequestParam List<String> vehicleTypes) {
        return workshopCollectorService.getAvailableTimes(from, until, workshopNames, vehicleTypes);
    }

    @PostMapping("/book-time")
    public @ResponseBody Domain bookTime(@RequestBody AvailableTime availableTime) {
        return workshopCollectorService.bookTireChangeTime(availableTime);
    }

    @GetMapping("/workshops")
    public List<Workshop> getWorkshops() {
        return workshopCollectorService.getWorkshops();
    }
}
