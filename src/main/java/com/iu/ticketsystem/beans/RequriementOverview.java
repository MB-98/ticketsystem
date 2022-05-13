package com.iu.ticketsystem.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.iu.ticketsystem.controller.NavigationController;
import com.iu.ticketsystem.dao.RequirementDao;
import com.iu.ticketsystem.dao.StatusDao;
import com.iu.ticketsystem.entity.Requirement;
import com.iu.ticketsystem.entity.Status;

@ManagedBean
@ViewScoped
public class RequriementOverview implements Serializable {

	private static final long serialVersionUID = -7063630359808985282L;

	NavigationController navigationController = new NavigationController();

	private RequirementDao reqDto = new RequirementDao();
	private StatusDao statusDto = new StatusDao();
	
	private List<Requirement> requirements = new ArrayList<Requirement>();
	private List<Requirement> newRequirements = new ArrayList<Requirement>();
	private List<Requirement> readyRequirements = new ArrayList<Requirement>();
	private List<Requirement> inTestRequirements = new ArrayList<Requirement>();
	private List<Requirement> acceptedRequirements = new ArrayList<Requirement>();
	private List<Status> statusList = new ArrayList<Status>();

	String newName = new String();
	String newDescription = new String();

	public RequriementOverview() {
		super();
		requirements = reqDto.getAll();
		newRequirements = requirements.stream().filter(e -> e.getStatus().getId() == 1).collect(Collectors.toList());
		readyRequirements = requirements.stream().filter(e -> e.getStatus().getId() == 2).collect(Collectors.toList());
		inTestRequirements = requirements.stream().filter(e -> e.getStatus().getId() == 3).collect(Collectors.toList());
		acceptedRequirements = requirements.stream().filter(e -> e.getStatus().getId() == 4)
				.collect(Collectors.toList());
		statusList = statusDto.getByType("req");
	}

	public String createRequirement() {
		if (!newName.isEmpty() && !newDescription.isEmpty()) {
			Optional<Status> oNewStatus = statusList.stream().filter(e -> e.getId() == 1).findAny();
			if (oNewStatus.isPresent()) {
				reqDto.save(new Requirement(newName, newDescription, oNewStatus.get()));
				newName = "";
				newDescription = "";
				addMessage(FacesMessage.SEVERITY_INFO, "Success", "Requirement saved");
				return navigationController.getRequirementOverview();
			} else {
				addMessage(FacesMessage.SEVERITY_ERROR, "Error", "Requirement could not be saved");
				return navigationController.getRequirementOverview();
			}
		} else {
			addMessage(FacesMessage.SEVERITY_ERROR, "Error", "The Name and Description fields cannot be empty");
			return navigationController.getRequirementOverview();
		}

	}

	public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(severity, summary, detail));
		context.getExternalContext().getFlash().setKeepMessages(true); // sorgt dafür, das Growl auch nach redirect
																		// angezeigt wird
	}

	public List<Requirement> getRequirements() {
		return requirements;
	}

	public void setRequirements(List<Requirement> requirements) {
		this.requirements = requirements;
	}

	public List<Requirement> getNewRequirements() {
		return newRequirements;
	}

	public void setNewRequirements(List<Requirement> newRequirements) {
		this.newRequirements = newRequirements;
	}

	public List<Requirement> getReadyRequirements() {
		return readyRequirements;
	}

	public void setReadyRequirements(List<Requirement> readyRequirements) {
		this.readyRequirements = readyRequirements;
	}

	public List<Requirement> getInTestRequirements() {
		return inTestRequirements;
	}

	public void setInTestRequirements(List<Requirement> inTestRequirements) {
		this.inTestRequirements = inTestRequirements;
	}

	public List<Requirement> getAcceptedRequirements() {
		return acceptedRequirements;
	}

	public void setAcceptedRequirements(List<Requirement> acceptedRequirements) {
		this.acceptedRequirements = acceptedRequirements;
	}

	public List<Status> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<Status> statusList) {
		this.statusList = statusList;
	}

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

	public String getNewDescription() {
		return newDescription;
	}

	public void setNewDescription(String newDescription) {
		this.newDescription = newDescription;
	}

}
