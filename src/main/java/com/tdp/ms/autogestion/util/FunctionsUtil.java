package com.tdp.ms.autogestion.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tdp.ms.autogestion.model.LogData;
import com.tdp.ms.autogestion.repository.LogDataRepository;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblLogData;


@Service
public class FunctionsUtil {
	
	@Autowired
	LogDataRepository logDataRepository;
	
	@Async
	public void saveLogData(LogData logData) {
		LocalDateTime dateNow = LocalDateTime.now(ZoneOffset.of("-05:00"));
		TblLogData tableLogData = new TblLogData();
		tableLogData.setIdTicketTriaje(logData.getIdTicketTriaje());
		tableLogData.setDocumentNumber(logData.getDocumentNumber());
		tableLogData.setDocumentType(logData.getDocumentType());
		tableLogData.setChannel(logData.getChannel());
		tableLogData.setTypeLog(logData.getTypeLog());
		tableLogData.setRequest(logData.getRequest());
		tableLogData.setResponse(logData.getResponse());
		tableLogData.setActionLog(logData.getActionLog());
		tableLogData.setDateCreated(dateNow);	
		logDataRepository.saveAndFlush(tableLogData);		
	}
}
