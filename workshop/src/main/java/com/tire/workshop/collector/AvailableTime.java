package com.tire.workshop.collector;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AvailableTime {
    private String availableTimeId;
    private String time;
    private Workshop workshop;
}
