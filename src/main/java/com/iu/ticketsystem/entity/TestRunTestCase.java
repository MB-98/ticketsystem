package com.iu.ticketsystem.entity;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "testrun_testcase")
public class TestRunTestCase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testrun_id", referencedColumnName = "id")
	private TestRun testrun;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testcase_id", referencedColumnName = "id")
	private TestCase testcase;

	@ManyToOne
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;

	public TestRunTestCase() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TestRunTestCase(TestRun testrun, TestCase testcase, Status status) {
		super();
		this.testrun = testrun;
		this.testcase = testcase;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public TestRun getTestrun() {
		return testrun;
	}

	public void setTestrun(TestRun testrun) {
		this.testrun = testrun;
	}

	public TestCase getTestcase() {
		return testcase;
	}

	public void setTestcase(TestCase testcase) {
		this.testcase = testcase;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, status, testcase, testrun);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestRunTestCase other = (TestRunTestCase) obj;
		return id == other.id && Objects.equals(status, other.status) && Objects.equals(testcase, other.testcase)
				&& Objects.equals(testrun, other.testrun);
	}

	@Override
	public String toString() {
		return "TestRunTestCase [id=" + id + ", testrun=" + testrun + ", testcase=" + testcase + ", status=" + status
				+ "]";
	}

}
