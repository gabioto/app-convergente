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
public class UpdateTicketStatusTest {

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
	void updateTicketStatus_complete() throws Exception {
		testController("194022", "WA_SOLVED", status().isOk());
	}

	@Test
	void updateTicketStatus_invalid() throws Exception {
		testController("1940DDDD", "WA_SOLVED", status().isBadRequest());
	}

	private MvcResult testController(String id, String status, ResultMatcher resultMatcher) throws Exception {
		return mockMvc.perform(MockMvcRequestBuilders.patch("/trazabilidad/v1/tickets/{id}/{status}", id, status)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
				.andExpect(resultMatcher).andReturn();
	}
}
