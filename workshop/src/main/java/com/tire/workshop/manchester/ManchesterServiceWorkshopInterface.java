package com.tire.workshop.manchester;

import com.tire.workshop.collector.domain.AvailableTime;
import com.tire.workshop.collector.WorkshopCollectorInterface;
import com.tire.workshop.collector.domain.Workshop;

import java.util.List;

public interface ManchesterServiceWorkshopInterface extends WorkshopCollectorInterface {
    List<AvailableTime> getTireChangeTimes(String from, String until);

    List<Workshop> getWorkshops();

    AvailableTime bookTireChangeTime(AvailableTime availableTime);
}
