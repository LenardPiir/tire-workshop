package com.tire.workshop.collector.service;

import com.tire.workshop.Domain;
import com.tire.workshop.london.LondonServiceInterface;
import com.tire.workshop.manchester.ManchesterServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CollectorService {

    private final LondonServiceInterface londonServiceInterface;
    private final ManchesterServiceInterface manchesterServiceInterface;


    private final int amount = 20;
    private final int page = 2;

    public Domain getAllAvailableTimes(String from, String until) {
        Domain domain = new Domain();

        domain.setLondonAvailableTimeList(londonServiceInterface.getTireChangeTimes(from, until).getLondonAvailableTimeList());
        domain.setManchesterAvailableTimeList(manchesterServiceInterface.getTireChangeTimes(amount, page, from).getManchesterAvailableTimeList());

        return domain;
    }
}
