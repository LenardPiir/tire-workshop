package com.tire.workshop.manchester.service;

import com.tire.workshop.Domain;
import com.tire.workshop.manchester.ManchesterServiceInterface;
import com.tire.workshop.manchester.data.TireChangeTime;
import com.tire.workshop.manchester.data.TireChangeTimeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service("ManchesterService")
@RequiredArgsConstructor
public class TireChangeService implements ManchesterServiceInterface {

    public Domain getTireChangeTimes(int amount, int page, String from) {
        WebClient webClient = WebClient.create("http://localhost:9004/api/v2/tire-change-times");

         Flux<TireChangeTime> tireChangeTime = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("amount", amount)
                        .queryParam("page", page)
                        .queryParam("from", from)
                        .build())
                .retrieve()
                .bodyToFlux(TireChangeTime.class);

        List<TireChangeTime> tireChangeTimeList = Objects.requireNonNull(tireChangeTime
                .collectList()
                .block())
                .stream()
                .filter(TireChangeTime::isAvailable)
                .toList();

        TireChangeTimeResponse tireChangeTimeResponse = new TireChangeTimeResponse();
        tireChangeTimeResponse.setTireChangeTimeList(tireChangeTimeList);

        return mapToDomain(tireChangeTimeResponse);
    }

    private static Domain mapToDomain(TireChangeTimeResponse tireChangeTimeResponse) {
        Domain domain = new Domain();
        domain.setManchesterAvailableTimeList(tireChangeTimeResponse.getTireChangeTimeList());
        return domain;
    }

    public Mono<TireChangeTime> bookTireChangeTime(int id, String contactInformation) {
        WebClient webClient = WebClient.create("http://localhost:9004/api/v2/tire-change-times/" + id + "/booking");

        return webClient.post()
                .uri(UriBuilder::build)
                .body(BodyInserters.fromValue(contactInformation))
                .retrieve()
                .onStatus(
                        HttpStatus.UNPROCESSABLE_ENTITY::equals,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .bodyToMono(TireChangeTime.class);
    }
}
