package com.tdp.ms.autogestion.repository.datasource.db.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.tdp.ms.autogestion.model.EquivalenceNotification;

/**
 * The persistent class for the tbl_equivalence_notification database table.
 * 
 */
@Entity
@Table(name = "tbl_equivalence_notification")
@NamedQuery(name = "TblEquivalenceNotification.findAll", query = "SELECT t FROM TblEquivalenceNotification t")
public class TblEquivalenceNotification implements Serializable {
	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "id_equivalence_notification")
	private Integer idEquivalenceNotification;

	private String code;

	private String description;

	private String action;

	private String title;

	private String body;

	private String footer;

	private String icon;

	private String button;

	private String image;

	private String actionbutton;
	
	private String windows;
	
	private String usecase;
	
	// Nuevos campos
	private String subtitle;
	
	private String icon2;
	
	private String subtitle2;
	
	private String windows2;
	
	private String button2;
	
	private String actionbutton2;
	
	public TblEquivalenceNotification() {
		// Constructor without fields
	}

	public Integer getIdEquivalenceNotification() {
		return this.idEquivalenceNotification;
	}

	public void setIdEquivalenceNotification(Integer idEquivalenceNotification) {
		this.idEquivalenceNotification = idEquivalenceNotification;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public String getBody() {
		return body;
	}

	public String getFooter() {
		return footer;
	}

	public String getIcon() {
		return icon;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getButton() {
		return button;
	}

	public String getImage() {
		return image;
	}

	public String getActionbutton() {
		return actionbutton;
	}

	public void setButton(String button) {
		this.button = button;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setActionbutton(String actionbutton) {
		this.actionbutton = actionbutton;
	}

	public void setWindows(String windows) {
		this.windows = windows;
	}
	
	public String getWindows() {
		return windows;
	}
	
	public EquivalenceNotification fromThis() {
		return new EquivalenceNotification(idEquivalenceNotification, code, description, action, title, body, footer,
				icon, button, image, actionbutton, windows, usecase, subtitle, subtitle2, icon2, windows2, button2, actionbutton2);
	}

	public static List<EquivalenceNotification> listFromThis(List<TblEquivalenceNotification> equiNotList) {
		List<EquivalenceNotification> equivalence = new ArrayList<>();

		for (TblEquivalenceNotification tblEquiNotification : equiNotList) {
			equivalence.add(tblEquiNotification.fromThis());
		}

		return equivalence;
	}

	public String getUsecase() {
		return usecase;
	}

	public void setUsecase(String usecase) {
		this.usecase = usecase;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public String getIcon2() {
		return icon2;
	}

	public String getSubtitle2() {
		return subtitle2;
	}

	public String getWindows2() {
		return windows2;
	}

	public String getButton2() {
		return button2;
	}

	public String getActionbutton2() {
		return actionbutton2;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public void setIcon2(String icon2) {
		this.icon2 = icon2;
	}

	public void setSubtitle2(String subtitle2) {
		this.subtitle2 = subtitle2;
	}

	public void setWindows2(String windows2) {
		this.windows2 = windows2;
	}
	
	public void setButton2(String button2) {
		this.button2 = button2;
	}

	public void setActionbutton2(String actionbutton2) {
		this.actionbutton2 = actionbutton2;
	}
}