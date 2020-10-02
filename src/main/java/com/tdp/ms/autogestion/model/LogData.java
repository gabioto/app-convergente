package com.tdp.ms.autogestion.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LogData{
	private int idTicketTriaje;
	private String documentNumber;
	private String documentType;
	private String channel;
	private String typeLog;
	private String request;
	private String response;
	private String actionLog;
}
