package com.tire.workshop.manchester.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TireChangeTimeResponse {
    List<TireChangeTime> tireChangeTimeList;
}
