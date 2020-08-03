package com.tdp.ms.autogestion.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
//	
//	@NotEmpty(message = Constants.MSG_NOT_EMPTY)
//	private String involvement;
//	
//	@NotEmpty(message = Constants.MSG_NOT_EMPTY)
//	private String reference;
//	
	@NotNull(message = Constants.MSG_NOT_EMPTY)
	private RelatedObject relatedObject;
	
	@NotEmpty(message = Constants.MSG_NOT_EMPTY)
	private String nationalIdType;

	@NotEmpty(message = Constants.MSG_NOT_EMPTY)
	private String nationalId;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@EqualsAndHashCode(callSuper = false)
	public static class RelatedObject {
		@NotEmpty(message = Constants.MSG_NOT_EMPTY)
		private String involvement;

		@NotEmpty(message = Constants.MSG_NOT_EMPTY)
		private String reference;
	}
}
