package com.tire.workshop.collector.service;

import com.tire.workshop.collector.AvailableTime;
import com.tire.workshop.collector.Domain;
import com.tire.workshop.collector.WorkshopCollectorInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectorService {

    private final List<WorkshopCollectorInterface> workshops;

    public Domain getAllAvailableTimes(String from, String until, String workshopName) {
        Domain domain = new Domain();

        List<AvailableTime> availableTimesList = workshops.stream()
                //.filter(workshop -> Objects.equals(workshop.getWorkshops().get(0).getName(), workshopName))
                .map(workshop -> workshop.getTireChangeTimes(from, until))
                .flatMap(Collection::stream)
                .toList();

        domain.setAvailableTimes(availableTimesList);

        return domain;
    }
}
