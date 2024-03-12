package com.tire.workshop.london.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableTime {
    private UUID uuid;
    private String time;
}
