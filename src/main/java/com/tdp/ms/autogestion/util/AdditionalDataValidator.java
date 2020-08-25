package com.tdp.ms.autogestion.util;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import com.tdp.ms.autogestion.expose.entities.TicketCreateRequest.AdditionalData;

public class AdditionalDataValidator implements ConstraintValidator<AdditionalValid, List<AdditionalData>> {

	List<String> mandatoryfields = Arrays.asList("nationalIdType", "nationalId", "productIdentifier");

	@Override
	public boolean isValid(List<AdditionalData> value, ConstraintValidatorContext context) {
		for (String field : mandatoryfields) {
			if (validateRequestAdditionalData(value, field).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	private String validateRequestAdditionalData(List<AdditionalData> data, String value) {
		AdditionalData field = data.stream().filter(item -> value.equals(item.getKey())).findFirst().orElse(null);
		if (field.getKey().equals("productIdentifier")) {
			if (!field.getValue().equals(Constants.SERVICE_CODE) && !field.getValue().equals(Constants.PHONE)) {
				return "";
			}
		}
		return (field != null && field.getValue() != null) ? field.getValue() : "";
	}
}
