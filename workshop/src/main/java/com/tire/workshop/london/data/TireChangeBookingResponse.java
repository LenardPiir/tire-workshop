package com.tire.workshop.london.data;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "tireChangeBookingResponse")
public class TireChangeBookingResponse {
    private UUID uuid;
    private String time;
}
