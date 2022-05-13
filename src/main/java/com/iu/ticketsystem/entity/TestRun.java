package com.iu.ticketsystem.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "testrun")
public class TestRun {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "name", nullable = false)
	private String name;

	@ManyToOne
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User assignee;

	@ManyToOne
	@JoinColumn(name = "requirement_id", nullable = false)
	private Requirement requirement;

	@OneToMany(mappedBy = "testrun", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TestRunTestCase> testcaseAssoc = new ArrayList<>();

	public TestRun() {
		// TODO Auto-generated constructor stub
	}

	public TestRun(String name, Status status, User assignee, Requirement requirement,
			List<TestRunTestCase> testcaseAssoc) {
		super();
		this.name = name;
		this.status = status;
		this.assignee = assignee;
		this.requirement = requirement;
		this.testcaseAssoc = testcaseAssoc;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public User getAssignee() {
		return assignee;
	}

	public void setAssignee(User assignee) {
		this.assignee = assignee;
	}

	public Requirement getRequirement() {
		return requirement;
	}

	public void setRequirement(Requirement requirement) {
		this.requirement = requirement;
	}

	public List<TestRunTestCase> getTestcaseAssoc() {
		return testcaseAssoc;
	}

	public void setTestcaseAssoc(List<TestRunTestCase> testcaseAssoc) {
		this.testcaseAssoc = testcaseAssoc;
	}

	public void addTestcaseAssoc(TestRunTestCase trtc) {
		this.testcaseAssoc.add(trtc);
		trtc.setTestrun(this);
	}

	public void removeTestcaseAssoc(TestRunTestCase trtc) {
		this.testcaseAssoc.remove(trtc);
		trtc.setTestrun(null);
	}

	@Override
	public int hashCode() {
		return Objects.hash(assignee, id, name, requirement, status, testcaseAssoc);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestRun other = (TestRun) obj;
		return Objects.equals(assignee, other.assignee) && id == other.id && Objects.equals(name, other.name)
				&& Objects.equals(requirement, other.requirement) && Objects.equals(status, other.status)
				&& Objects.equals(testcaseAssoc, other.testcaseAssoc);
	}

}
