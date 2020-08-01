package com.tdp.ms.autogestion.model;

import javax.validation.constraints.NotEmpty;

import com.tdp.ms.autogestion.util.Constants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TicketRetrieveRequest {

	@NotEmpty(message = Constants.MSG_NOT_EMPTY)
	private String type;
	
	@NotEmpty(message = Constants.MSG_NOT_EMPTY)
	private String involvement;
	
	@NotEmpty(message = Constants.MSG_NOT_EMPTY)
	private String reference;
	
	@NotEmpty(message = Constants.MSG_NOT_EMPTY)
	private String nationalIdType;

	@NotEmpty(message = Constants.MSG_NOT_EMPTY)
	private String nationalId;

	
}
