package com.iu.ticketsystem.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class NavigationController implements Serializable {

	private static final long serialVersionUID = -4029508946793516918L;

	public String getLogin() {
		return "login";
	}

	public String getRequirementOverview() {
		return "requirementOverview?faces-redirect=true";
	}

	public String getMyTestRuns() {
		return "myTestRuns?faces-redirect=true";
	}

	public String getRequirementDetails(int id) {
		return "requirementDetails?id=" + id + "&faces-redirect=true";
	}
}
