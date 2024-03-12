package com.tire.workshop.london;

import com.tire.workshop.Domain;
import com.tire.workshop.london.data.TireChangeBookingResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface LondonServiceInterface {
    Domain getTireChangeTimes(String from, String until);
    Mono<TireChangeBookingResponse> bookTireChangeTime(UUID uuid, String contactInformation);
}
