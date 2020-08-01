package com.tdp.ms.autogestion.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tdp.ms.autogestion.repository.LogDataRepository;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblLogData;


@Service
public class FunctionsUtil {
	
	@Autowired
	LogDataRepository logDataRepository;
	
	@Async
	public void saveLogData(int idTicketTriaje, String documentNumber, String documentType, String channel, String typeLog,
			String request, String response, String actionLog) {
		LocalDateTime dateNow = LocalDateTime.now(ZoneOffset.of("-05:00"));
		TblLogData tableLogData = new TblLogData();
		tableLogData.setIdTicketTriaje(idTicketTriaje);
		tableLogData.setDocumentNumber(documentNumber);
		tableLogData.setDocumentType(documentType);
		tableLogData.setChannel(channel);
		tableLogData.setTypeLog(typeLog);
		tableLogData.setRequest(request);
		tableLogData.setResponse(response);
		tableLogData.setActionLog(actionLog);
		tableLogData.setDateCreated(dateNow);
		tableLogData.setActionLog(actionLog);		
		logDataRepository.saveAndFlush(tableLogData);		
	}


}
