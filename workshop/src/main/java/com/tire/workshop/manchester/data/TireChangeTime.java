package com.tire.workshop.manchester.data;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@RequiredArgsConstructor
public class TireChangeTime {
    private int id;
    private Date time;
    private boolean available;
}
