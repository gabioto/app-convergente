package com.tdp.ms.autogestion.autogestion.expose;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.tdp.ms.autogestion.business.CreateTicketUseCase;
import com.tdp.ms.autogestion.business.RetrieveTicketStatusUseCase;
import com.tdp.ms.autogestion.business.RetrieveTicketsUseCase;
import com.tdp.ms.autogestion.business.UpdateTicketStatusUseCase;
import com.tdp.ms.autogestion.expose.TicketController;

@WebMvcTest(TicketController.class)
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
		testController("194061982", status().isOk());
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