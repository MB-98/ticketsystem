package com.iu.ticketsystem.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "requirement")
public class Requirement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description", nullable = false)
	private String description;

	@ManyToOne
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;

	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "requirement", cascade = CascadeType.ALL)
	private List<TestRun> testruns = new ArrayList<>();

	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "requirement", cascade = CascadeType.ALL)
	private List<TestCase> testcases = new ArrayList<>();

	public Requirement() {
		// TODO Auto-generated constructor stub
	}

	public Requirement(String name, String description, Status status) {
		super();
		this.name = name;
		this.description = description;
		this.status = status;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<TestRun> getTestruns() {
		return testruns;
	}

	public void setTestruns(List<TestRun> testruns) {
		this.testruns = testruns;
	}

	public List<TestCase> getTestcases() {
		return testcases;
	}

	public void setTestcases(List<TestCase> testcases) {
		this.testcases = testcases;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, id, name, status, testcases, testruns);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Requirement other = (Requirement) obj;
		return Objects.equals(description, other.description) && id == other.id && Objects.equals(name, other.name)
				&& Objects.equals(status, other.status) && Objects.equals(testcases, other.testcases)
				&& Objects.equals(testruns, other.testruns);
	}

	@Override
	public String toString() {
		return "Requirement [id=" + id + ", name=" + name + ", description=" + description + ", status=" + status
				+ ", testruns=" + testruns + ", testcases=" + testcases + "]";
	}

}
