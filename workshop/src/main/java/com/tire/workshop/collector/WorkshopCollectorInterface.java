package com.tire.workshop.collector;

import java.util.List;

public interface WorkshopCollectorInterface {
    List<AvailableTime> getTireChangeTimes(String from, String until);

    // TODO: remove?
    //List<Workshop> getWorkshops();
}
