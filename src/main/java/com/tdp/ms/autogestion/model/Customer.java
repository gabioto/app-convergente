package com.tdp.ms.autogestion.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Customer {

	private String nationalId;

	private String nationalType;

	private String serviceCode;
}
