package com.tire.workshop.london.data;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "tireChangeBookingRequest")
public class TireChangeBookingRequest {
    private String contactInformation;
}
