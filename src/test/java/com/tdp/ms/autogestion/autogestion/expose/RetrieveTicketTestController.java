package com.tdp.ms.autogestion.autogestion.expose;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tdp.ms.autogestion.business.CreateTicketUseCase;
import com.tdp.ms.autogestion.business.RetrieveTicketStatusUseCase;
import com.tdp.ms.autogestion.business.RetrieveTicketsUseCase;
import com.tdp.ms.autogestion.business.UpdateTicketStatusUseCase;
import com.tdp.ms.autogestion.expose.TicketController;

@WebMvcTest(TicketController.class)
public class RetrieveTicketTestController {
	
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
	
	private static Map<String, String> retrieveTicketRequestMap = new HashMap<>();
	
	@BeforeAll
	public static void setup() throws JsonProcessingException {
		retrieveTicketRequestMap.put("type","CMS");
		retrieveTicketRequestMap.put("involvement","landline");
		retrieveTicketRequestMap.put("reference","4640668");
		retrieveTicketRequestMap.put("nationalIdType","DNI");
		retrieveTicketRequestMap.put("nationalId","72010850");
	}
	
	@Test
	public void retrieveTicket_completeFields() throws Exception {
		testController(retrieveTicketRequestMap, status().isOk());
	}
	
	@Test
	public void retrieveTicket_emptyFields() throws Exception {
		testControllerEmpty(status().isBadRequest());
	}
	
	private MvcResult testController(Map<String, String> requestName, ResultMatcher resultMatcher) throws Exception {
		return mockMvc
				.perform(MockMvcRequestBuilders.get("/trazabilidad/v1/tickets/retrieveTickets")
				.param("type", requestName.get("type"))
				.param("involvement", requestName.get("involvement"))
				.param("reference", requestName.get("reference"))
				.param("nationalIdType", requestName.get("nationalIdType"))
				.param("nationalId", requestName.get("nationalId"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("utf-8")
				)
			    .andExpect(resultMatcher)
				.andReturn();
				
	}
	
	private MvcResult testControllerEmpty (ResultMatcher resultMatcher) throws Exception {

		return mockMvc
				.perform(MockMvcRequestBuilders.get("/trazabilidad/v1/tickets/retrieveTickets")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("utf-8")
				)
			    .andExpect(resultMatcher)
				.andReturn();
				
	}

}
