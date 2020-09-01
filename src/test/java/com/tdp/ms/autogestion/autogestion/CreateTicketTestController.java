package com.tdp.ms.autogestion.autogestion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import com.tdp.ms.autogestion.expose.entities.TicketCreateRequest;
import com.tdp.ms.autogestion.expose.entities.TicketCreateRequest.Channel;
import com.tdp.ms.autogestion.expose.entities.TicketCreateRequest.RelatedObject;
import com.tdp.ms.autogestion.expose.entities.TicketCreateResponse;

@SpringBootTest
@AutoConfigureWebTestClient(timeout = "20000")
public class CreateTicketTestController {

	@Autowired
	private WebTestClient webClient;

	private static Map<String, TicketCreateResponse> ticketResponseMap = new HashMap<>();
	private static Map<String, TicketCreateRequest> ticketRequestMap = new HashMap<>();

	@BeforeAll
	public static void setup() {
		ticketResponseMap.put("post", new TicketCreateResponse());
		
		ticketRequestMap.put("post", new TicketCreateRequest("averia", "minor", "TroubleTicket", 25, 
				new Channel("AppConvergente", "3"), new RelatedObject("landline", "10368606"), new ArrayList<>()));
//		ticketRequestMap.put("post", new TicketCreateRequest("20000032", "13010108", "99", "DNI", "70981983", "1234567"));
//		ticketRequestMap.put("empty", new TicketCreateRequest("20000032", "13010108", "99", "", "70981983", "1234567"));
	}
	
	@Test
	public void indexPostTest() {
		webClient.post().uri("/trazabilidad/v1/tickets")
				.accept(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(ticketRequestMap.get("post")))
				.exchange().expectStatus()
				.isEqualTo(HttpStatus.CREATED)
				.expectBody(TicketCreateResponse.class)
				.isEqualTo(ticketResponseMap.get("post"));
	}
}
