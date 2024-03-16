package com.tire.workshop.collector.service;

import com.tire.workshop.collector.WorkshopCollectorInterface;
import com.tire.workshop.collector.domain.AvailableTime;
import com.tire.workshop.collector.domain.Domain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectorService {

    private final List<WorkshopCollectorInterface> workshops;

    public Domain getAvailableTimes(String from, String until, List<String> workshopNames, List<String> vehicleTypes) {
        Domain domain = new Domain();

        List<AvailableTime> availableTimesList = workshops.stream()
                .filter(workshop -> !workshop.getTireChangeTimes(from, until)
                        .stream()
                        .filter(workshop1 -> workshopNames.contains(workshop1.getWorkshop().getName())).toList().isEmpty())
                .map(workshop -> workshop.getTireChangeTimes(from, until))
                .flatMap(Collection::stream)
                .toList();

        domain.setAvailableTimes(availableTimesList);

        return domain;
    }
}
