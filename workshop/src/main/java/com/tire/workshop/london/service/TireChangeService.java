package com.tire.workshop.london.service;

import com.tire.workshop.Domain;
import com.tire.workshop.london.LondonServiceInterface;
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

import java.util.UUID;

@Service("LondonService")
@RequiredArgsConstructor
public class TireChangeService implements LondonServiceInterface {

    private final WebClient webClient;

    public Domain getTireChangeTimes(String from, String until) {
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

        assert tireChangeTimesResponse != null;
        return mapToDomain(tireChangeTimesResponse);
    }

    private static Domain mapToDomain(TireChangeTimesResponse tireChangeTimesResponse) {
        Domain domain = new Domain();
        domain.setLondonAvailableTimeList(tireChangeTimesResponse.getAvailableTime());
        return domain;
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
