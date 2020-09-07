package com.tdp.ms.autogestion.autogestion.expose;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.tdp.ms.autogestion.business.CreateTicketUseCase;
import com.tdp.ms.autogestion.business.RetrieveTicketStatusUseCase;
import com.tdp.ms.autogestion.business.RetrieveTicketsUseCase;
import com.tdp.ms.autogestion.business.UpdateTicketStatusUseCase;
import com.tdp.ms.autogestion.expose.TicketController;
import com.tdp.ms.autogestion.expose.entities.TicketCreateRequest;
import com.tdp.ms.autogestion.expose.entities.TicketStatusResponse;
import com.tdp.ms.autogestion.expose.entities.TicketCreateRequest.AdditionalData;
import com.tdp.ms.autogestion.expose.entities.TicketCreateRequest.Channel;
import com.tdp.ms.autogestion.expose.entities.TicketCreateRequest.RelatedObject;

@AutoConfigureMockMvc
@ContextConfiguration(classes = { TicketController.class })
@SpringBootTest
public class RetrieveTicketStatusTestController {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CreateTicketUseCase createTicketUseCase;

	@MockBean
	private RetrieveTicketsUseCase retrieveTicketsUseCase;

	@MockBean
	private RetrieveTicketStatusUseCase retrieveTicketStatusUseCase;

	@MockBean
	private UpdateTicketStatusUseCase updateTicketStatusUseCase;

	private static Gson gson = new Gson();

	private static Map<String, TicketStatusResponse> ticketResponseMap = new HashMap<>();
	private static Map<String, String> ticketRequestMap = new HashMap<>();

	private static AdditionalData nationalTypeAdditional = new AdditionalData("nationalIdType", "DNI");
	private static AdditionalData nationalIdAdditional = new AdditionalData("nationalId", "70981983");
	private static AdditionalData productIdAdditional = new AdditionalData("productIdentifier", "serviceCode");

	private static List<AdditionalData> additionalRequest = new ArrayList<>();
	private static List<AdditionalData> emptyAdditionalRequest = new ArrayList<>();

	@BeforeAll
	public static void setup() throws JsonProcessingException {
		additionalRequest.add(nationalTypeAdditional);
		additionalRequest.add(nationalIdAdditional);
		additionalRequest.add(productIdAdditional);

		// RESPONSE POST
		ticketResponseMap.put("get", new TicketStatusResponse());

		// REQUEST POST
		ticketRequestMap.put("POST_COMPLETE", gson.toJson(new TicketCreateRequest("averia", "minor", "TroubleTicket", 1,
				new Channel("AppConvergente", "3"), new RelatedObject("broadband", "10368606"), additionalRequest)));

		ticketRequestMap.put("POST_EMPTY",
				gson.toJson(new TicketCreateRequest("averia", "minor", "TroubleTicket", 1,
						new Channel("AppConvergente", "3"), new RelatedObject("broadband", "10368606"),
						emptyAdditionalRequest)));
	}

	@Test
	public void retrieveTicketStatus_complete() throws Exception {
		testController("19406198", status().isOk());
	}

	@Test
	public void retrieveTicketStatus_empty() throws Exception {
		testController("", status().isNotFound());
	}
	
	@Test
	public void retrieveTicketStatus_notexist() throws Exception {
		testController("194061982", status().isNotFound());
	}

	@Test
	public void retrieveTicketStatus_invalid() throws Exception {
		testController("1940DDDD", status().isBadRequest());
	}
	
	private MvcResult testController(String id, ResultMatcher resultMatcher) throws Exception {
		return mockMvc
				.perform(MockMvcRequestBuilders.get("/trazabilidad/v1/tickets/{id}/status", id)				
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("utf-8"))
				.andExpect(resultMatcher)
				.andReturn();
	}
}