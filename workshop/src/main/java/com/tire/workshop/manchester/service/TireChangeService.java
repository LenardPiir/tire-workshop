package com.tire.workshop.manchester.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tire.workshop.collector.domain.AvailableTime;
import com.tire.workshop.collector.domain.VehicleType;
import com.tire.workshop.collector.domain.Workshop;
import com.tire.workshop.manchester.ManchesterServiceWorkshopInterface;
import com.tire.workshop.manchester.data.TireChangeTime;
import com.tire.workshop.manchester.data.TireChangeTimeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
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
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, response -> response
                        .bodyToMono(String.class)
                        .map(Exception::new))
                .bodyToFlux(TireChangeTime.class);

        // TODO: Add filtering based on until date
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

    public List<Workshop> getWorkshops() {
        List<Workshop> workshopList = new ArrayList<>();
        workshopList.add(getWorkshop());
        return workshopList;
    }

    private static Workshop getWorkshop() {
        // TODO: on application start read data from a file
        Workshop workshop = new Workshop();
        workshop.setName("Manchester");
        workshop.setAddress("14 Bury New Rd, Manchester");

        List<VehicleType> vehicleTypes = new ArrayList<>();

        VehicleType vehicleType = new VehicleType();
        vehicleType.setType("Sõiduauto");
        vehicleType.setType("Veoauto");

        vehicleTypes.add(vehicleType);

        workshop.setVehicleType(vehicleTypes);

        return workshop;
    }

    public AvailableTime bookTireChangeTime(AvailableTime availableTime) {
        ObjectNode contactInformation = getJsonNode(availableTime);

        WebClient webClient = WebClient.create(url + "/" + availableTime.getAvailableTimeId() + "/booking");

        TireChangeTime tireChangeTime = webClient.post()
                .uri(UriBuilder::build)
                .body(BodyInserters.fromValue(contactInformation))
                .retrieve()
                .onStatus(
                        HttpStatus.UNPROCESSABLE_ENTITY::equals,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .bodyToMono(TireChangeTime.class).block();

        // TODO: fix nullpointer cause
        return new AvailableTime(String.valueOf(tireChangeTime.getId()),
                String.valueOf(tireChangeTime.getTime()),
                availableTime.getWorkshop(),
                availableTime.getContactInformation());
    }

    private static ObjectNode getJsonNode(AvailableTime availableTime) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode contactInformation = mapper.createObjectNode();
        contactInformation.put("contactInformation", availableTime.getContactInformation());
        return contactInformation;
    }
}
