package com.tire.workshop.london.data;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "tireChangeTimesResponse")
public class TireChangeTimesResponse {
    List<AvailableTime> availableTime;
}
