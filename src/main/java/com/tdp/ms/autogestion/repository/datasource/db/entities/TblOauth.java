package com.tdp.ms.autogestion.repository.datasource.db.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.tdp.ms.autogestion.model.OAuth;

@Entity
@Table(name="tbl_oauth")
@NamedQuery(name="TblOauth.findAll", query="SELECT t FROM TblOauth t")
public class TblOauth implements Serializable {

	private static final long serialVersionUID = 1L;

	// @GeneratedValue(strategy=GenerationType.SEQUENCE, generator =
	// "tbl_oauth_Sequence")
	// @SequenceGenerator(name = "tbl_oauth_Sequence", sequenceName =
	// "esqfcrautogestion.\"SEQ_TBL_OAUTH\"", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "id_oauth")
	private Integer idOauth;

	@Column(name = "token_key")
	private String tokenKey;

	@Column(name = "token_type")
	private String tokenType;

	@Column(name = "access_token")
	private String accessToken;

	@Column(name = "expires_in")
	private String expiresIn;

	@Column(name = "consented_on")
	private String consentedOn;

	@Column(name = "token_scope")
	private String tokenScope;

	@Column(name = "refresh_token")
	private String refreshToken;

	@Column(name = "refresh_token_expires_in")
	private String refreshTokenExpiresIn;

	public TblOauth() {
	}

	public Integer getIdOauth() {
		return idOauth;
	}

	public void setIdOauth(Integer idOauth) {
		this.idOauth = idOauth;
	}

	public String getTokenKey() {
		return tokenKey;
	}

	public void setTokenKey(String tokenKey) {
		this.tokenKey = tokenKey;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getConsentedOn() {
		return consentedOn;
	}

	public void setConsentedOn(String consentedOn) {
		this.consentedOn = consentedOn;
	}

	public String getTokenScope() {
		return tokenScope;
	}

	public void setTokenScope(String tokenScope) {
		this.tokenScope = tokenScope;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getRefreshTokenExpiresIn() {
		return refreshTokenExpiresIn;
	}

	public void setRefreshTokenExpiresIn(String refreshTokenExpiresIn) {
		this.refreshTokenExpiresIn = refreshTokenExpiresIn;
	}

	public static TblOauth from(OAuth oAuth) {
		TblOauth tableOauth = new TblOauth();
		tableOauth.setIdOauth(1);
		tableOauth.setAccessToken(oAuth.getAccessToken());
		tableOauth.setConsentedOn(oAuth.getConsentedOn());
		tableOauth.setExpiresIn(oAuth.getExpiresIn());
		tableOauth.setRefreshToken(oAuth.getRefreshToken());
		tableOauth.setRefreshTokenExpiresIn(oAuth.getRefreshTokenExpiresIn());
		tableOauth.setTokenKey("TokenKey");
		tableOauth.setTokenScope(oAuth.getScope());
		tableOauth.setTokenType(oAuth.getTokenType());
		return tableOauth;
	}
	
	public OAuth fromThis() {
		return new OAuth(tokenKey, tokenType, accessToken, expiresIn, consentedOn, tokenScope, refreshToken,
				refreshTokenExpiresIn);
	}

}
