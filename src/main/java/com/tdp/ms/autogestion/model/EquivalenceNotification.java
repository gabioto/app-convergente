package com.tdp.ms.autogestion.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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

@Getter
@Setter
@AllArgsConstructor
public class EquivalenceNotification {

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
	
	private String subtitle;
	
	private String subtitle2;
	
	private String icon2;
	
	private String windows2;
	
	private String button2;
	
	private String actionbutton2;	
}