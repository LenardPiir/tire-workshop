package com.tire.workshop.london.service;

import com.tire.workshop.collector.domain.AvailableTime;
import com.tire.workshop.collector.domain.VehicleType;
import com.tire.workshop.collector.domain.Workshop;
import com.tire.workshop.london.LondonServiceWorkshopInterface;
import com.tire.workshop.london.data.TireChangeBookingResponse;
import com.tire.workshop.london.data.TireChangeTimesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service("LondonService")
@RequiredArgsConstructor
public class TireChangeService implements LondonServiceWorkshopInterface {

    private final WebClient webClient;

    public List<AvailableTime> getTireChangeTimes(String from, String until) {
        TireChangeTimesResponse tireChangeTimesResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("available")
                        .queryParam("from", from)
                        .queryParam("until", until)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML)
                .retrieve()
                .bodyToMono(TireChangeTimesResponse.class).block();

        return tireChangeTimesResponse.getAvailableTime()
                .stream()
                .map(availableTime -> AvailableTime
                        .builder()
                        .availableTimeId(availableTime.getUuid().toString())
                        .time(availableTime.getTime())
                        .workshop(getWorkshop())
                        .build())
                .toList();
    }

    private static Workshop getWorkshop() {
        // TODO: on application start read data from a file
        Workshop workshop = new Workshop();
        workshop.setName("London");
        workshop.setAddress("1A Gunton Rd, London");

        List<VehicleType> vehicleTypes = new ArrayList<>();

        VehicleType vehicleType = new VehicleType();
        vehicleType.setType("Sõiduauto");

        vehicleTypes.add(vehicleType);

        workshop.setVehicleType(vehicleTypes);

        return workshop;
    }

    public Mono<TireChangeBookingResponse> bookTireChangeTime(UUID uuid, String contactInformation) {
        return webClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path(String.valueOf(uuid))
                        .path("/booking")
                        .build())
                .body(BodyInserters.fromValue(contactInformation))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML)
                .retrieve()
                .onStatus(
                        HttpStatus.UNPROCESSABLE_ENTITY::equals,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .bodyToMono(TireChangeBookingResponse.class);
    }
}
