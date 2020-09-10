package com.tdp.ms.autogestion.autogestion.util;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tdp.ms.autogestion.model.LogData;
import com.tdp.ms.autogestion.repository.LogDataRepository;
import com.tdp.ms.autogestion.repository.datasource.db.entities.TblLogData;
import com.tdp.ms.autogestion.util.FunctionsUtil;

@ExtendWith(MockitoExtension.class)
public class FunctionsUtilsTest {
	
	@InjectMocks
	private FunctionsUtil functionUtil;
	
	@Mock
	private LogDataRepository logDataRepository;
	
	private static LogData logData;
	
	@BeforeAll
	public static void setup() {
		logData = new LogData(102525, "70945683", "DNI", "Convergencia", "ERROR", "", "", "");
	}
	
	@Test
	void saveLogData() {
		when(logDataRepository.saveAndFlush(any(TblLogData.class))).thenReturn(new TblLogData());
		
		functionUtil.saveLogData(logData);
	}

}
