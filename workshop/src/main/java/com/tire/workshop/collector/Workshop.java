package com.tire.workshop.collector;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Workshop {
    private String name;
    private String address;
    private List<VehicleType> vehicleType;
}
