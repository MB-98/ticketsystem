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

import com.iu.ticketsystem.dao.StatusDao;
import com.iu.ticketsystem.dao.TestRunDao;
import com.iu.ticketsystem.dao.TestRunTestCaseDao;
import com.iu.ticketsystem.dao.UserDao;
import com.iu.ticketsystem.entity.Status;
import com.iu.ticketsystem.entity.TestRun;
import com.iu.ticketsystem.entity.TestRunTestCase;
import com.iu.ticketsystem.entity.User;
import com.iu.ticketsystem.util.SessionUtils;

@ManagedBean
@ViewScoped
public class MyTestRuns implements Serializable {

	private static final long serialVersionUID = -7063630359808985282L;

	private TestRunDao testRunDao = new TestRunDao();
	private TestRunTestCaseDao testRunTestCaseDao = new TestRunTestCaseDao();
	private UserDao userDao = new UserDao();
	private StatusDao statusDto = new StatusDao();

	private List<TestRun> testRunList = new ArrayList<>();

	private List<User> userList = new ArrayList<User>();
	private List<Status> statusList = new ArrayList<Status>();
	private List<Status> runStatusList = new ArrayList<Status>();
	private List<Status> caseStatusList = new ArrayList<Status>();

	User user;

	public MyTestRuns() {
		super();
		user = SessionUtils.getUser();
		testRunList = testRunDao.getAllByUserId(user.getId());
		userList = userDao.getAll();
		statusList = statusDto.getByType("req");
		runStatusList = statusDto.getByType("run");
		caseStatusList = statusDto.getByType("case");
	}

	public void updateTestRunStatus(SelectEvent<Integer> event) {
		System.out.println("update Testrun Status");
		Integer testrunId = (Integer) event.getComponent().getAttributes().get("testrunId");
		Integer testrunStatusId = (Integer) event.getComponent().getAttributes().get("testrunStatusId");
		Optional<TestRun> oTestRun = testRunList.stream().filter(e -> e.getId() == testrunId).findFirst();
		if (oTestRun.isPresent()) {
			TestRun testrun = oTestRun.get();
			testrun.setStatus(runStatusList.stream().filter(e -> e.getId() == testrunStatusId).findFirst().get());
			testRunDao.update(testrun);
			addMessage(FacesMessage.SEVERITY_INFO, "Success", "Test Run updated");
		}
	}

	public void updateTestRunAssignee(SelectEvent<Integer> event) {
		System.out.println("update Testrun Assignee");
		Integer testrunId = (Integer) event.getComponent().getAttributes().get("testrunId");
		Integer testrunAssigneeId = (Integer) event.getComponent().getAttributes().get("testrunAssigneeId");
		Optional<TestRun> oTestRun = testRunList.stream().filter(e -> e.getId() == testrunId).findFirst();
		if (oTestRun.isPresent()) {
			TestRun testrun = oTestRun.get();
			testrun.setAssignee(userList.stream().filter(e -> e.getId() == testrunAssigneeId).findFirst().get());
			testRunDao.update(testrun);
			addMessage(FacesMessage.SEVERITY_INFO, "Success", "Test Run updated");
		}
	}

	public void updateTestCaseStatus(SelectEvent<Integer> event) {
		System.out.println("update Testcase Status");
		Integer testrunId = (Integer) event.getComponent().getAttributes().get("testrunId");
		Integer testcaseId = (Integer) event.getComponent().getAttributes().get("testcaseId");
		Integer testCaseStatusId = (Integer) event.getComponent().getAttributes().get("testCaseStatusId");
		Optional<TestRunTestCase> oTestRunTestCase = testRunTestCaseDao.get(testrunId, testcaseId);
		if (oTestRunTestCase.isPresent()) {
			TestRunTestCase trtc = oTestRunTestCase.get();
			trtc.setStatus(caseStatusList.stream().filter(e -> e.getId() == testCaseStatusId).findFirst().get());
			testRunTestCaseDao.update(trtc);
			addMessage(FacesMessage.SEVERITY_INFO, "Success", "Test Run updated");
		}

	}

	public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(severity, summary, detail));
		context.getExternalContext().getFlash().setKeepMessages(true); // sorgt dafür, das Growl auch nach redirect
																		// angezeigt wird
	}

	public List<TestRun> getTestRunList() {
		return testRunList;
	}

	public void setTestRunList(List<TestRun> testRunList) {
		this.testRunList = testRunList;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
