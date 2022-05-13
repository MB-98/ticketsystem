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
@Table(name = "testcase")
public class TestCase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description", nullable = false)
	private String description;

	@ManyToOne
	@JoinColumn(name = "requirement_id", nullable = false)
	private Requirement requirement;

	@OneToMany(mappedBy = "testcase", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TestRunTestCase> testrunAssoc = new ArrayList<>();

	public TestCase() {
		// TODO Auto-generated constructor stub
	}
	
	public TestCase(String name, String description, Requirement requirement, List<TestRunTestCase> testrunAssoc) {
		super();
		this.name = name;
		this.description = description;
		this.requirement = requirement;
		this.testrunAssoc = testrunAssoc;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Requirement getRequirement() {
		return requirement;
	}

	public void setRequirement(Requirement requirement) {
		this.requirement = requirement;
	}

	public List<TestRunTestCase> getTestrunAssoc() {
		return testrunAssoc;
	}

	public void setTestrunAssoc(List<TestRunTestCase> testrunAssoc) {
		this.testrunAssoc = testrunAssoc;
	}

	public void addTestcaseAssoc(TestRunTestCase trtc) {
		this.testrunAssoc.add(trtc);
		trtc.setTestcase(this);
	}

	public void removeTestcaseAssoc(TestRunTestCase trtc) {
		this.testrunAssoc.remove(trtc);
		trtc.setTestcase(null);
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, id, name, requirement, testrunAssoc);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestCase other = (TestCase) obj;
		return Objects.equals(description, other.description) && id == other.id && Objects.equals(name, other.name)
				&& Objects.equals(requirement, other.requirement) && Objects.equals(testrunAssoc, other.testrunAssoc);
	}

	@Override
	public String toString() {
		return "TestCase [id=" + id + ", name=" + name + ", description=" + description + "]";
	}

}
