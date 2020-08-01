package com.tdp.ms.autogestion.repository.datasource.api.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tdp.ms.autogestion.model.OAuth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Class: TrazabilidadpruebaResponse. <br/>
 * <b>Copyright</b>: &copy; 2019 Telef&oacute;nica del Per&uacute;<br/>
 * <b>Company</b>: Telef&oacute;nica del Per&uacute;<br/>
 *
 * @author Telef&oacute;nica del Per&uacute; (TDP) <br/>
 *         <u>Service Provider</u>: Everis Per&uacute; SAC (EVE) <br/>
 *         <u>Developed by</u>: <br/>
 *         <ul>
 *         <li>Developer name</li>
 *         </ul>
 *         <u>Changes</u>:<br/>
 *         <ul>
 *         <li>YYYY-MM-DD Creaci&oacute;n del proyecto.</li>
 *         </ul>
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OAuthApiResponse {
	
	private String id;

	@JsonProperty("token_key")
	private String tokenKey;

	@JsonProperty("token_type")
	private String tokenType;

	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("expires_in")
	private String expiresIn;

	@JsonProperty("consented_on")
	private String consentedOn;

	private String scope;

	@JsonProperty("refresh_token")
	private String refreshToken;

	@JsonProperty("refresh_token_expires_in")
	private String refreshTokenExpiresIn;
	
	public OAuth fromThis() {
		return new OAuth(tokenKey, tokenType, accessToken, expiresIn, consentedOn, scope, refreshToken,
				refreshTokenExpiresIn);
	}
}
