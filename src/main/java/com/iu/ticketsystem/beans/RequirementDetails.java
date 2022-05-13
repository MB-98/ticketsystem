package com.iu.ticketsystem.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;

import com.iu.ticketsystem.controller.NavigationController;
import com.iu.ticketsystem.dao.RequirementDao;
import com.iu.ticketsystem.dao.StatusDao;
import com.iu.ticketsystem.dao.TestCaseDao;
import com.iu.ticketsystem.dao.TestRunDao;
import com.iu.ticketsystem.dao.TestRunTestCaseDao;
import com.iu.ticketsystem.dao.UserDao;
import com.iu.ticketsystem.entity.Requirement;
import com.iu.ticketsystem.entity.Status;
import com.iu.ticketsystem.entity.TestCase;
import com.iu.ticketsystem.entity.TestRun;
import com.iu.ticketsystem.entity.TestRunTestCase;
import com.iu.ticketsystem.entity.User;

@ManagedBean
@ViewScoped
public class RequirementDetails implements Serializable{

	private static final long serialVersionUID = -3099100634095146173L;
	
	private NavigationController navigationController = new NavigationController();
	
	private RequirementDao reqDao = new RequirementDao();
	private TestRunDao testRunDao = new TestRunDao();
	private TestCaseDao testCaseDao = new TestCaseDao();
	private TestRunTestCaseDao testRunTestCaseDao = new TestRunTestCaseDao();
	private StatusDao statusDto = new StatusDao();
	private UserDao userDao = new UserDao();
	
	//Requirement attributes
	private int  id;
	private int  status;
	private Requirement requirement = new Requirement();
	
	//Lists
	private List<User> userList = new ArrayList<User>();
	private List<Status> statusList = new ArrayList<Status>();
	private List<Status> runStatusList = new ArrayList<Status>();
	private List<Status> caseStatusList = new ArrayList<Status>();
	
	
	//Case attributes 
	private String newCaseName = new String();
	private String newCaseDescription = new String();
	private TestCase selectedTestCase = new TestCase();
	
	//Run attributes 
	private String newRunName = new String();
	private int newRunAssigneeId = 0;
	private List<Integer> testCaseIds = new ArrayList<Integer>();
	private List<String> selectedTestCasesIds;
	private TestRun selectedTestRun = new TestRun();
	
	
	public RequirementDetails() {
		super();
		id = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));    
		requirement = reqDao.get(id).get();
        userList = userDao.getAll();
        status = requirement.getStatus().getId();
        statusList = statusDto.getByType("req");
        runStatusList = statusDto.getByType("run");
        caseStatusList = statusDto.getByType("case");
	        
	}

	public void updateRequirement() {
		reqDao.update(requirement);
		addMessage(FacesMessage.SEVERITY_INFO, "Success", "Requirement updated");
	}
	
	public void updateStatus() {
		System.out.println("update Status");
		Status selectedStatus = statusList.stream().filter(e -> e.getId() == status).findFirst().get();
		requirement.setStatus(selectedStatus);
		reqDao.update(requirement);
		addMessage(FacesMessage.SEVERITY_INFO, "Success", "Requirement updated");
	}
	
	public void updateTestRunStatus(SelectEvent<Integer> event) {
		System.out.println("update Testrun Status");
		Integer testrunId =  (Integer) event.getComponent().getAttributes().get("testrunId");
		Integer testrunStatusId =  (Integer) event.getComponent().getAttributes().get("testrunStatusId");
		Optional<TestRun> oTestRun = requirement.getTestruns().stream().filter(e -> e.getId() == testrunId).findFirst();
		if(oTestRun.isPresent()) {
			TestRun testrun = oTestRun.get();
			testrun.setStatus(runStatusList.stream().filter(e -> e.getId() == testrunStatusId).findFirst().get());
			testRunDao.update(testrun);
			addMessage(FacesMessage.SEVERITY_INFO, "Success", "Test Run updated");
		}
	}
	
	public void updateTestRunAssignee(SelectEvent<Integer> event) {
		System.out.println("update Testrun Assignee");
		Integer testrunId =  (Integer) event.getComponent().getAttributes().get("testrunId");
		Integer testrunAssigneeId =  (Integer) event.getComponent().getAttributes().get("testrunAssigneeId");
		Optional<TestRun> oTestRun = requirement.getTestruns().stream().filter(e -> e.getId() == testrunId).findFirst();
		if(oTestRun.isPresent()) {
			TestRun testrun = oTestRun.get();
			testrun.setAssignee(userList.stream().filter(e -> e.getId() == testrunAssigneeId).findFirst().get());
			testRunDao.update(testrun);
			addMessage(FacesMessage.SEVERITY_INFO, "Success", "Test Run updated");
		}
	}
	
	public void updateTestCaseStatus(SelectEvent<Integer> event) {
		System.out.println("update Testcase Status");
		Integer testrunId =  (Integer) event.getComponent().getAttributes().get("testrunId");
		Integer testcaseId =  (Integer) event.getComponent().getAttributes().get("testcaseId");
		Integer testCaseStatusId =  (Integer) event.getComponent().getAttributes().get("testCaseStatusId");
		Optional<TestRunTestCase> oTestRunTestCase = testRunTestCaseDao.get(testrunId, testcaseId);
		if(oTestRunTestCase.isPresent()) {
			TestRunTestCase trtc = oTestRunTestCase.get();
			trtc.setStatus(caseStatusList.stream().filter(e -> e.getId() == testCaseStatusId).findFirst().get());
			testRunTestCaseDao.update(trtc);
			addMessage(FacesMessage.SEVERITY_INFO, "Success", "Test Run updated");
		}
		
	}
	
	public String createTestRun() {
		if(!newRunName.isEmpty()  && newRunAssigneeId != 0) {
			Optional<User> oNewRunAssignee = userList.stream().filter(e -> e.getId() == newRunAssigneeId).findAny();
			Optional<Status> oNewRunStatus = runStatusList.stream().filter(e -> e.getId() == 5).findAny();
			Optional<Status> oNewCaseStatus = caseStatusList.stream().filter(e -> e.getId() == 12).findAny();
			if(oNewRunStatus.isPresent() && oNewRunAssignee.isPresent() && requirement != null) {
				TestRun newTestRun = new TestRun();
				newTestRun.setName(newRunName);
				newTestRun.setRequirement(requirement);
				newTestRun.setAssignee(oNewRunAssignee.get());
				newTestRun.setStatus(oNewRunStatus.get());
				
				List<TestCase> testCaseList = new ArrayList<TestCase>();
				
				for (int i = 0; i < selectedTestCasesIds.size(); i++) {
					Integer id = Integer.valueOf(selectedTestCasesIds.get(i));
					Optional<TestCase> tCase = testCaseDao.get(id);
					if(tCase.isPresent()) {
						TestCase c = tCase.get();
						testCaseList.add(c);
					}
				}
				
				testCaseList.forEach(c -> newTestRun.addTestcaseAssoc(new TestRunTestCase(newTestRun, c, oNewCaseStatus.get())));
				testRunDao.save(newTestRun);

				newRunName = "";
				newRunAssigneeId = 0;
				selectedTestCasesIds.clear();
				addMessage(FacesMessage.SEVERITY_INFO, "Success", "Test Run saved");
				return navigationController.getRequirementDetails(requirement.getId());
			}
			else {
				 addMessage(FacesMessage.SEVERITY_ERROR, "Error", "Test Run could not be saved");
				 return navigationController.getRequirementDetails(requirement.getId());
			}
		}
		else {
			 addMessage(FacesMessage.SEVERITY_ERROR, "Error", "The Name, Description and Assignee fields cannot be empty");
			 return navigationController.getRequirementDetails(requirement.getId());
		}
		
	}
	
	public String createTestCase() {
		if(!newCaseName.isEmpty() && !newCaseDescription.isEmpty() ) {
				testCaseDao.save(new TestCase(newCaseName,newCaseDescription,requirement, null));
				newCaseName = "";
				newCaseDescription = "";
				addMessage(FacesMessage.SEVERITY_INFO, "Success", "Test Case saved");
				return navigationController.getRequirementDetails(requirement.getId());
		}
		else {
			addMessage(FacesMessage.SEVERITY_ERROR, "Error", "The Name and Description fields cannot be empty");
			return navigationController.getRequirementDetails(requirement.getId());
		}
		
	}

	public String updateTestRun() {
		if(!selectedTestRun.getName().isEmpty()  && selectedTestRun.getAssignee() != null ) {
			Optional<Status> oNewCaseStatus = caseStatusList.stream().filter(e -> e.getId() == 12).findAny();
			Optional<User> oNewRunAssignee = userList.stream().filter(e -> e.getId() == newRunAssigneeId).findAny();
			selectedTestRun.setAssignee(oNewRunAssignee.get());
			List<TestCase> testCaseList = new ArrayList<TestCase>();
			for (int i = 0; i < selectedTestCasesIds.size(); i++) {
				Integer id = Integer.valueOf(selectedTestCasesIds.get(i));
				Optional<TestCase> tCase = testCaseDao.get(id);
				if(tCase.isPresent()) {
					TestCase c = tCase.get();
					testCaseList.add(c);
				}
			}
			List<TestRunTestCase> runAssocs = selectedTestRun.getTestcaseAssoc();
			List<TestRunTestCase> runAssocsToRemove = new ArrayList<>();
			List<Integer> runAssocsIds =new ArrayList<>(); 
			
			runAssocs.forEach(e -> {
				runAssocsIds.add(e.getTestcase().getId());
				if(!selectedTestCasesIds.contains(String.valueOf(e.getTestcase().getId()))){
					runAssocsToRemove.add(e);
				}
			});
			
			runAssocsToRemove.forEach(e -> {
				testRunTestCaseDao.delete(e);
			});
			
			
			TestRun tr = testRunDao.get(selectedTestRun.getId()).get();
			tr.setName(selectedTestRun.getName());
			tr.setAssignee(oNewRunAssignee.get());
			
			System.err.println(selectedTestCasesIds);
			System.err.println(runAssocsIds);
			
			selectedTestCasesIds.forEach(e -> {
				if(!runAssocsIds.contains(Integer.valueOf(e))) {
					TestCase tc = testCaseList.stream().filter(f -> f.getId() == Integer.valueOf(e)).findFirst().get();
					tr.addTestcaseAssoc(new TestRunTestCase(tr, tc, oNewCaseStatus.get()));
				}
			});
		
			testRunDao.update(tr);

			newRunName = "";
			newRunAssigneeId = 0;
			selectedTestCasesIds.clear();
			addMessage(FacesMessage.SEVERITY_INFO, "Success", "Test Run saved");
			return navigationController.getRequirementDetails(requirement.getId());
		}
	
		else {
			 addMessage(FacesMessage.SEVERITY_ERROR, "Error", "The Name, Description and Assignee fields cannot be empty");
			 return navigationController.getRequirementDetails(requirement.getId());
		}
	}
	
	public String updateTestCase() {
		if(!selectedTestCase.getName().isEmpty() && !selectedTestCase.getDescription().isEmpty() ) {
			testCaseDao.update(selectedTestCase);
			addMessage(FacesMessage.SEVERITY_INFO, "Success", "Test Case updated");
			return navigationController.getRequirementDetails(requirement.getId());
		}
		else {
			addMessage(FacesMessage.SEVERITY_ERROR, "Error", "The Name and Description fields cannot be empty");
			return navigationController.getRequirementDetails(requirement.getId());
		}
		
	}
	
	public String deleteRun(int id) {
		System.out.println("TestRunId: "+ id);
		if(id != 0){
			System.out.println("TestRunId: "+ id);
			testRunDao.delete(requirement.getTestruns().stream().filter(e -> e.getId() == id).findFirst().get());
			addMessage(FacesMessage.SEVERITY_INFO, "Success", "Test Run deleted");
			return navigationController.getRequirementDetails(requirement.getId());
		}
		else {
			addMessage(FacesMessage.SEVERITY_ERROR, "Error", "Test Run could not be deleted");
			return navigationController.getRequirementDetails(requirement.getId());
		}
	}
	
	public String deleteCase(int id) {
		System.out.println("TestCaseId: "+ id);
		if(id != 0){
			System.out.println("TestCaseId: "+ id);
			testCaseDao.delete(requirement.getTestcases().stream().filter(e -> e.getId() == id).findFirst().get());
			addMessage(FacesMessage.SEVERITY_INFO, "Success", "Test Case deleted");
			return navigationController.getRequirementDetails(requirement.getId());
		}
		else {
			addMessage(FacesMessage.SEVERITY_ERROR, "Error", "Test Case could not be deleted");
			return navigationController.getRequirementDetails(requirement.getId());
		}
	}
	
	
	public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(severity, summary, detail));
		context.getExternalContext().getFlash().setKeepMessages(true); //sorgt dafür, das Growl auch nach redirect angezeigt wird
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public List<Status> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<Status> statusList) {
		this.statusList = statusList;
	}

	public List<Status> getRunStatusList() {
		return runStatusList;
	}

	public void setRunStatusList(List<Status> runStatusList) {
		this.runStatusList = runStatusList;
	}

	public List<Status> getCaseStatusList() {
		return caseStatusList;
	}

	public void setCaseStatusList(List<Status> caseStatusList) {
		this.caseStatusList = caseStatusList;
	}

	public Requirement getRequirement() {
		return requirement;
	}

	public void setRequirement(Requirement requirement) {
		this.requirement = requirement;
	}

	public String getNewCaseName() {
		return newCaseName;
	}

	public void setNewCaseName(String newCaseName) {
		this.newCaseName = newCaseName;
	}

	public String getNewCaseDescription() {
		return newCaseDescription;
	}

	public void setNewCaseDescription(String newCaseDescription) {
		this.newCaseDescription = newCaseDescription;
	}

	public String getNewRunName() {
		return newRunName;
	}

	public void setNewRunName(String newRunName) {
		this.newRunName = newRunName;
	}

	public int getNewRunAssigneeId() {
		return newRunAssigneeId;
	}

	public void setNewRunAssigneeId(int newRunAssigneeId) {
		this.newRunAssigneeId = newRunAssigneeId;
	}

	public List<Integer> getTestCaseIds() {
		return testCaseIds;
	}

	public void setTestCaseIds(List<Integer> testCaseIds) {
		this.testCaseIds = testCaseIds;
	}

	public List<String> getSelectedTestCasesIds() {
		return selectedTestCasesIds;
	}

	public void setSelectedTestCasesIds(List<String> selectedTestCasesIds) {
		this.selectedTestCasesIds = selectedTestCasesIds;
	}

	public TestCase getSelectedTestCase() {
		return selectedTestCase;
	}

	public void setSelectedTestCase(TestCase selectedTestCase) {
		this.selectedTestCase = selectedTestCase;
	}

	public TestRun getSelectedTestRun() {
		return selectedTestRun;
	}
	
	public void setSelectedTestRun(TestRun selectedTestRun) {
		selectedTestCasesIds = new ArrayList<String>();
		this.selectedTestRun = selectedTestRun;
		newRunAssigneeId = selectedTestRun.getAssignee().getId();
		selectedTestRun.getTestcaseAssoc().forEach(e -> selectedTestCasesIds.add(String.valueOf(e.getTestcase().getId())));
		
	}

	
	
	
	
}
