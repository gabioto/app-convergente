package com.tdp.ms.autogestion.autogestion.business.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.tdp.ms.autogestion.business.impl.UpdateTicketStatusUseCaseImpl;
import com.tdp.ms.autogestion.exception.ResourceNotFoundException;
import com.tdp.ms.autogestion.exception.ValidRequestException;
import com.tdp.ms.autogestion.expose.entities.TicketStatusResponse;
import com.tdp.ms.autogestion.model.Customer;
import com.tdp.ms.autogestion.model.Ticket;
import com.tdp.ms.autogestion.repository.TicketRepository;

@ExtendWith(MockitoExtension.class)
public class UpdateTicketUseCaseTest {

	@InjectMocks
	private UpdateTicketStatusUseCaseImpl updateTicketUseCase;

	@Mock
	private TicketRepository ticketRepository;

	private static Ticket ticketComplete;

	@BeforeAll
	public static void setup() {
		LocalDateTime actualDate = LocalDateTime.now(ZoneOffset.of("-05:00"));

		ticketComplete = new Ticket(0, 19406743, "/ticket/v2/tickets/19406743", "averia", actualDate, "minor",
				"TroubleTicket", "acknowledged", actualDate, "Ticket generado", 1, "", "20000032", "99", "serviceCode",
				"broadband", "3", null, new Customer("70981983", "DNI", "10368606"), null, actualDate,
				new ArrayList<>(), new ArrayList<>());
	}

	@Test
	void updateTicket_exist() {
		when(ticketRepository.updateTicketStatus(anyInt(), anyString())).thenReturn(ticketComplete);

		ResponseEntity<TicketStatusResponse> ticketStatusResponse = updateTicketUseCase.updateTicketStatus(194022,
				"WA_SOLVED");

		assertNotNull(ticketStatusResponse);
	}

	@Test
	void updateTicket_invalidTicket() {
		ValidRequestException exception = assertThrows(ValidRequestException.class, () -> {
			updateTicketUseCase.updateTicketStatus(0, "WA_SOLVED");
		});

		assertEquals(exception.getMessage(), "idTicket is empty or null");
	}

	@Test
	void updateTicket_invalidStatus() {
		ValidRequestException exception = assertThrows(ValidRequestException.class, () -> {
			updateTicketUseCase.updateTicketStatus(1, "WA_SOLVED_SOLVED");
		});

		assertEquals(exception.getMessage(), "invalid status param");
	}

	@Test
	void updateTicket_resourceException() {
		when(ticketRepository.updateTicketStatus(anyInt(), anyString())).thenThrow(ResourceNotFoundException.class);

		assertThrows(ResourceNotFoundException.class, () -> {
			updateTicketUseCase.updateTicketStatus(1, "WA_SOLVED");
		});
	}

}
