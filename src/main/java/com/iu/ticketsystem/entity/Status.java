package com.iu.ticketsystem.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "status")
public class Status {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "type", nullable = false)
	private String type;

	@Column(name = "name", nullable = false)
	private String name;

	public Status() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Status(String type, String name) {
		super();
		this.type = type;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Status other = (Status) obj;
		return id == other.id && Objects.equals(name, other.name) && Objects.equals(type, other.type);
	}

	@Override
	public String toString() {
		return "Status [id=" + id + ", type=" + type + ", name=" + name + "]";
	}

}
