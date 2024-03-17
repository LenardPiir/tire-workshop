package com.tire.workshop;

import com.tire.workshop.collector.domain.AvailableTime;
import com.tire.workshop.collector.domain.VehicleType;
import com.tire.workshop.collector.domain.Workshop;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WorkshopApplicationTests {

	@Autowired
	MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void givenAvailableTime_thenReturnBookedObject() throws Exception {
		Workshop workshop = new Workshop();
		workshop.setAddress("14 Bury New Rd, Manchester");
		workshop.setName("Manchester");

		List<VehicleType> vehicleTypeList = new ArrayList<>();
		VehicleType vehicleType = new VehicleType();
		vehicleType.setType("Veoauto");

		vehicleTypeList.add(vehicleType);

		workshop.setVehicleType(vehicleTypeList);

		AvailableTime availableTime = new AvailableTime("68", "2024-12-20", workshop, vehicleType.getType());

		mockMvc.perform(MockMvcRequestBuilders
						.post("/workshop-api/book-time")
						.content(asJsonString(availableTime))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.availableTimeId").value("68"));
	}

	public static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
