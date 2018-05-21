/**
 * 
 */
package com.binlah.sap.pi.mapping;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.binlah.sap.pi.mapping.domain.NameAndValue;
import com.binlah.sap.pi.mapping.domain.Personnel;
import com.binlah.sap.pi.mapping.domain.Student;
import com.sap.aii.mapping.api.AbstractTransformation;
import com.sap.aii.mapping.api.StreamTransformationException;
import com.sap.aii.mapping.api.TransformationInput;
import com.sap.aii.mapping.api.TransformationOutput;

/**
 * @author BinlaT
 *
 */
public class StudentToPersonnelMapping extends AbstractTransformation {

	private final Student student = new Student();
	private final Personnel personnel = new Personnel();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.aii.mapping.api.AbstractTransformation#transform(com.sap.aii.mapping.api.TransformationInput, com.sap.aii.mapping.api.TransformationOutput)
	 */
	@Override
	public void transform(TransformationInput input, TransformationOutput output) throws StreamTransformationException {
		InputStream is = null;

		try {
			// is = new ByteArrayInputStream(content);
			is = input.getInputPayload().getInputStream();

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbFactory.newDocumentBuilder();

			Document src = builder.parse(is);
			Document dest = builder.newDocument();

			// traversingXML(src);
			// traversingXML(src, dest);

			traversingXML(src);
			mapStudentToPersonnel();

			if (student.getGender().equalsIgnoreCase("M")) { // method 1: use when manual write xml
				getTrace().addInfo("method 1: Write XML manually!");
				getTrace().addInfo(personnel.convertToXML());
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(output.getOutputPayload().getOutputStream()));
				try {
					bw.write(personnel.convertToXML());
					bw.close();
				} catch (IOException e) {
					getTrace().addWarning(e.getMessage());
				}

			} else { // method 2: use when write by DOM
				getTrace().addInfo("method 2: Write XML via DOM!");
				parsePersonelToDOM(dest);
				getTrace().addInfo(dest.getTextContent());
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(dest);
				StreamResult result = new StreamResult(output.getOutputPayload().getOutputStream());
				transformer.transform(source, result);

			}
			is.close();

		} catch (Exception e) {
			getTrace().addInfo(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				getTrace().addInfo(e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public void parsePersonelToDOM(Document doc) {
		Element rootElement = doc.createElement("MT_Personnel");
		rootElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:ns", "urn:binlah.com:ERP:HR:Personnel");
		rootElement.setAttribute("type", "Student via DOM");
		rootElement.setAttribute("place", "University");
		doc.appendChild(rootElement);

		Element idElement = doc.createElement("id");
		idElement.appendChild(doc.createTextNode(personnel.getId()));
		rootElement.appendChild(idElement);

		Element nameElement = doc.createElement("name");
		nameElement.appendChild(doc.createTextNode(personnel.getName()));
		rootElement.appendChild(nameElement);

		Element genderElement = doc.createElement("gender");
		genderElement.appendChild(doc.createTextNode(personnel.getGender()));
		rootElement.appendChild(genderElement);

		Element dateOfBirthElement = doc.createElement("dateOfBirth");
		dateOfBirthElement.appendChild(doc.createTextNode(personnel.getDateOfBirth()));
		rootElement.appendChild(dateOfBirthElement);

		Element ageElement = doc.createElement("age");
		ageElement.appendChild(doc.createTextNode(personnel.getAge()));
		rootElement.appendChild(ageElement);

		Element emailElement = doc.createElement("email");
		emailElement.appendChild(doc.createTextNode(personnel.getEmail()));
		rootElement.appendChild(emailElement);

		for (NameAndValue nv : personnel.getAdditional()) {
			Element additionalElement = doc.createElement("additionl");
			rootElement.appendChild(additionalElement);

			Element nvNameElement = doc.createElement("name");
			nvNameElement.appendChild(doc.createTextNode(nv.getName()));
			additionalElement.appendChild(nvNameElement);

			Element nvValueElement = doc.createElement("value");
			nvValueElement.appendChild(doc.createTextNode(nv.getValue()));
			additionalElement.appendChild(nvValueElement);
		}

	}

	/* DOM Parser */
	public void traversingXML(Node src) {
		// logger.debug("------------ Recursive -----------");
		NodeList children = src.getChildNodes();

		// get ns:MT_Student
		Node MT_Student = children.item(0);

		// get MT_Student.element
		NodeList student_element = MT_Student.getChildNodes();

		for (int i = 0; i < student_element.getLength(); i++) {
			Node lineNode = student_element.item(i);

			if (lineNode.getNodeName().equalsIgnoreCase("id")) {
				student.setId(lineNode.getChildNodes().item(0).getNodeValue());

			} else if (lineNode.getNodeName().equalsIgnoreCase("firstName")) {
				student.setFirstName(lineNode.getChildNodes().item(0).getNodeValue());

			} else if (lineNode.getNodeName().equalsIgnoreCase("lastName")) {
				student.setLastName(lineNode.getChildNodes().item(0).getNodeValue());

			} else if (lineNode.getNodeName().equalsIgnoreCase("gender")) {
				student.setGender(lineNode.getChildNodes().item(0).getNodeValue());

			} else if (lineNode.getNodeName().equalsIgnoreCase("dateOfBirth")) {
				student.setDateOfBirth(lineNode.getChildNodes().item(0).getNodeValue());

			} else if (lineNode.getNodeName().equalsIgnoreCase("age")) {
				student.setAge(lineNode.getChildNodes().item(0).getNodeValue());

			} else if (lineNode.getNodeName().equalsIgnoreCase("email")) {
				student.setEmail(lineNode.getChildNodes().item(0).getNodeValue());

			} else if (lineNode.getNodeName().equalsIgnoreCase("course")) {

				NodeList nameAndValueNodeList = lineNode.getChildNodes();
				NameAndValue course = new NameAndValue();
				student.getCourses().add(course);
				for (int j = 0; j < nameAndValueNodeList.getLength(); j++) {
					Node dataNode = nameAndValueNodeList.item(j);
					if (dataNode.getNodeName().equals("name")) {
						course.setName(dataNode.getChildNodes().item(0).getNodeValue());

					} else if (dataNode.getNodeName().equals("value")) {
						course.setValue(dataNode.getChildNodes().item(0).getNodeValue());

					}
				}
			} else if (lineNode.getNodeName().equalsIgnoreCase("club")) {

				NodeList nameAndValueNodeList = lineNode.getChildNodes();
				NameAndValue club = new NameAndValue();
				student.getClubs().add(club);
				for (int j = 0; j < nameAndValueNodeList.getLength(); j++) {
					Node dataNode = nameAndValueNodeList.item(j);
					if (dataNode.getNodeName().equals("name")) {
						club.setName(dataNode.getChildNodes().item(0).getNodeValue());

					} else if (dataNode.getNodeName().equals("value")) {
						club.setValue(dataNode.getChildNodes().item(0).getNodeValue());

					}
				}
			}
		}

	}

	public void mapStudentToPersonnel() {
		int id = Integer.parseInt(student.getId());
		String gender = student.getGender();
		int age = Integer.parseInt(student.getAge());
		String email = student.getEmail();

		personnel.setId(new DecimalFormat("000000").format(id));
		personnel.setName(student.getFirstName() + " " + student.getLastName());
		personnel.setGender(gender);

		try {
			Date dateOfBirth = new SimpleDateFormat("dd/MM/yyyy").parse(student.getDateOfBirth());
			personnel.setDateOfBirth(new SimpleDateFormat("yyyy-MMM-dd").format(dateOfBirth));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (gender.equalsIgnoreCase("M")) {
		} else if (gender.equalsIgnoreCase("F")) {
			age += 5;
		} else {
			age -= 10;
		}

		personnel.setAge(String.valueOf(age));

		if (email.contains("@") && email.contains(".")) {
			personnel.setEmail(student.getEmail());
		} else {
			personnel.setEmail("Error");
		}

		for (NameAndValue course : student.getCourses()) {
			personnel.getAdditional().add(new NameAndValue(course.getName(), course.getValue()));
		}
		for (NameAndValue club : student.getClubs()) {
			personnel.getAdditional().add(new NameAndValue(club.getName(), club.getValue()));
		}
	}

}
