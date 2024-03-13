package com.tire.workshop.london;

import com.tire.workshop.collector.AvailableTime;
import com.tire.workshop.collector.WorkshopCollectorInterface;

import java.util.List;

public interface LondonServiceWorkshopInterface extends WorkshopCollectorInterface {
    List<AvailableTime> getTireChangeTimes(String from, String until);

    // TODO: remove?
    //List<Workshop> getWorkshops();
}
