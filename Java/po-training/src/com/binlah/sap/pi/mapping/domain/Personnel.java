package com.binlah.sap.pi.mapping.domain;

import java.util.ArrayList;
import java.util.List;

public class Personnel {
	private String id;
	private String name;
	private String gender;
	private String dateOfBirth;
	private String age;
	private String email;
	private List<NameAndValue> additional;

	public Personnel() {
		// TODO Auto-generated constructor stub
	}

	public String convertToXML() {
		StringBuilder sb = new StringBuilder();

		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<ns:MT_Personnel xmlns:ns=\"urn:binlah.com:ERP:HR:Personnel\" type=\"Student via Manual\" place=\"University\">");
		sb.append(("<id>" + id + "</id>"));
		sb.append(("<name>" + name + "</name>"));
		sb.append(("<gender>" + gender + "</gender>"));
		sb.append(("<dateOfBirth>" + dateOfBirth + "</dateOfBirth>"));
		sb.append(("<age>" + age + "</age>"));
		sb.append(("<email>" + email + "</email>"));
		for (NameAndValue ad : additional) {
			sb.append(ad.convertToXML());
		}
		sb.append("</ns:MT_Personnel>");
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id:");
		sb.append(id);
		sb.append("\n");
		sb.append("name:");
		sb.append(name);
		sb.append("\n");
		return sb.toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<NameAndValue> getAdditional() {
		if (additional == null) {
			additional = new ArrayList<NameAndValue>();
		}
		return additional;
	}

	public void setAdditional(List<NameAndValue> additional) {
		this.additional = additional;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
