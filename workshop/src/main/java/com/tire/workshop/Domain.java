package com.tire.workshop;

import com.tire.workshop.london.data.AvailableTime;
import com.tire.workshop.manchester.data.TireChangeTime;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Domain {
    private List<AvailableTime> londonAvailableTimeList;
    private List<TireChangeTime> manchesterAvailableTimeList;
}

