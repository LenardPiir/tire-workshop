package com.tire.workshop.collector;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Domain {
    private List<AvailableTime> availableTimes;
}

