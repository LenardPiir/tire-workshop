package com.tire.workshop.manchester.service;

import com.tire.workshop.collector.AvailableTime;
import com.tire.workshop.collector.VehicleType;
import com.tire.workshop.collector.Workshop;
import com.tire.workshop.manchester.ManchesterServiceWorkshopInterface;
import com.tire.workshop.manchester.data.TireChangeTime;
import com.tire.workshop.manchester.data.TireChangeTimeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service("ManchesterService")
@RequiredArgsConstructor
public class TireChangeService implements ManchesterServiceWorkshopInterface {

    @Value("${manchester-service-url}")
    private String url;

    public List<AvailableTime> getTireChangeTimes(String from, String until) {
        // TODO: refactor
        int amount = 10;
        int page = 2;

        WebClient webClient = WebClient.create(url);

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

        return tireChangeTimeResponse.getTireChangeTimeList()
                .stream()
                .map(availableTime -> AvailableTime
                        .builder()
                        .availableTimeId(String.valueOf(availableTime.getId()))
                        .time(availableTime.getTime().toString())
                        .workshop(getWorkshop())
                        .build())
                .toList();
    }

    private static Workshop getWorkshop() {
        // TODO: on application start read data from a file
        Workshop workshop = new Workshop();
        workshop.setName("Manchester");
        workshop.setAddress("14 Bury New Rd, Manchester");

        List<VehicleType> vehicleTypes = new ArrayList<>();
        vehicleTypes.add(VehicleType.SOIDUAUTO);
        vehicleTypes.add(VehicleType.VEOAUTO);

        workshop.setVehicleType(vehicleTypes);

        return workshop;
    }

    /*
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

     */
}
