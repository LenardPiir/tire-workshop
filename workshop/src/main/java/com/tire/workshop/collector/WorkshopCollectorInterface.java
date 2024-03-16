package com.tire.workshop.collector;

import com.tire.workshop.collector.domain.AvailableTime;
import com.tire.workshop.collector.domain.Workshop;

import java.util.Collection;
import java.util.List;

public interface WorkshopCollectorInterface {
    List<AvailableTime> getTireChangeTimes(String from, String until);

    List<Workshop> getWorkshops();

    AvailableTime bookTireChangeTime(AvailableTime availableTime);
}
