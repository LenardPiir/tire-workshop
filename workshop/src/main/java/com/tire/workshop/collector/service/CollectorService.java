package com.tire.workshop.collector.service;

import com.tire.workshop.collector.WorkshopCollectorInterface;
import com.tire.workshop.collector.domain.AvailableTime;
import com.tire.workshop.collector.domain.Domain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CollectorService {

    private final List<WorkshopCollectorInterface> workshops;

    public Domain getAvailableTimes(String from, String until, List<String> workshopNames, List<String> vehicleTypes) {
        Domain domain = new Domain();

        List<AvailableTime> availableTimesList = workshops.stream()
                .filter(workshop -> !workshop.getTireChangeTimes(from, until)
                        .stream()
                        .filter(workshopChild -> workshopNames.contains(workshopChild.getWorkshop().getName())).toList().isEmpty())
                .filter(workshop -> !workshop.getTireChangeTimes(from, until)
                        .stream()
                        .filter(workshopChild -> !workshopChild.getWorkshop().getVehicleType()
                                .stream()
                                .filter(vehicleType -> vehicleTypes.contains(vehicleType.getType()))
                                .toList().isEmpty())
                        .toList().isEmpty())
                .map(workshop -> workshop.getTireChangeTimes(from, until))
                .flatMap(Collection::stream)
                .toList();

        domain.setAvailableTimes(availableTimesList);

        return domain;
    }

    public Domain bookTireChangeTime(AvailableTime availableTime) {
        List<AvailableTime> availableTimeList = workshops
                .stream()
                .filter(workshop -> !workshop.getWorkshops()
                        .stream()
                        .filter(workshopChild -> Objects.equals(availableTime.getWorkshop().getName(), workshopChild.getName()))
                        .toList().isEmpty())
                .toList()
                .stream().map(workshopChild -> workshopChild.bookTireChangeTime(availableTime))
                .toList();

        Domain domain = new Domain();
        domain.setAvailableTime(availableTimeList.get(0));

        return domain;
    }
}
