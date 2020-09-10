package com.tdp.ms.autogestion.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Customer {

	private String nationalId;

	private String nationalType;

	private String serviceCode;
}
