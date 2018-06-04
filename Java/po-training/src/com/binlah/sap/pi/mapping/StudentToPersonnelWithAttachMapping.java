/**
 * 
 */
package com.binlah.sap.pi.mapping;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
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
import com.sap.aii.mapping.api.Attachment;
import com.sap.aii.mapping.api.DynamicConfiguration;
import com.sap.aii.mapping.api.DynamicConfigurationKey;
import com.sap.aii.mapping.api.InputAttachments;
import com.sap.aii.mapping.api.StreamTransformationException;
import com.sap.aii.mapping.api.TransformationInput;
import com.sap.aii.mapping.api.TransformationOutput;

/**
 * @author BinlaT
 * 
 */
public class StudentToPersonnelWithAttachMapping extends AbstractTransformation {

	private final Student student = new Student();
	private final Personnel personnel = new Personnel();

	// private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS");

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.aii.mapping.api.AbstractTransformation#transform(com.sap.aii.mapping.api.TransformationInput, com.sap.aii.mapping.api.TransformationOutput)
	 */
	@Override
	public void transform(TransformationInput input, TransformationOutput output) throws StreamTransformationException {
		InputStream mainInputStream = null;
		InputStream attachInputStream = null;
		String attachmentID = null;
		InputAttachments inputAttachments = input.getInputAttachments();
		// String str = null;

		try {
			// is = new ByteArrayInputStream(content);
			DynamicConfiguration conf = input.getDynamicConfiguration();
			DynamicConfigurationKey confKeyFileName = DynamicConfigurationKey.create("http://sap.com/xi/XI/System/File", "FileName");
			// conf.put(confKeyFileName, "personnel_" + sdf.format(new Date()) + ".xml");
			conf.put(confKeyFileName, "personnel_.xml");

			mainInputStream = input.getInputPayload().getInputStream();

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbFactory.newDocumentBuilder();

			Document mainSrc = builder.parse(mainInputStream);
			Document attSrc = null;
			Document dest = builder.newDocument();

			if (inputAttachments.areAttachmentsAvailable()) {
				// gets the attachmentIds and store it in an Object array
				Collection<String> collectionIDs = input.getInputAttachments().getAllContentIds(true);
				Object[] arrayObj = collectionIDs.toArray();

				// Loops at the input attachments to get the content of the attachment
				for (int i = 0; i < arrayObj.length; i++) {
					attachmentID = (String) arrayObj[i];
					getTrace().addInfo((i + 1) + ") Attachment Name: " + attachmentID);
					Attachment attachment = inputAttachments.getAttachment(attachmentID);
					String contentType = attachment.getContentType();

					// To retain attachment names in mail adapter (receiver). Updated on 3:35 PM 09/30/2013 IST.
					// Use charset only in case of textual attachments e.g., XML, flatfiles
					contentType = contentType + ";charset = \"UTF-8\";" + "name=\"" + attachmentID + "\"";
					byte[] content = attachment.getContent();

					attachInputStream = new ByteArrayInputStream(content);
					attSrc = builder.parse(attachInputStream);

					// str = new String(content);
				}
			}

			// traversingXML(src);
			// traversingXML(src, dest);

			getTrace().addInfo("Read main payload");
			readMainXML(mainSrc);

			getTrace().addInfo("Read attachment payload (from Adapter Module)");
			readAttachXML(attSrc);

			getTrace().addInfo("Map to target structure");
			mapStudentToPersonnel();

			getTrace().addInfo("Parse to DOM");
			parsePersonelToDOM(dest);

			getTrace().addInfo(dest.getTextContent());

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(dest);
			StreamResult result = new StreamResult(output.getOutputPayload().getOutputStream());
			transformer.transform(source, result);
			mainInputStream.close();

		} catch (Exception e) {
			getTrace().addInfo(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				mainInputStream.close();
			} catch (IOException e) {
				getTrace().addInfo(e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/* DOM Parser */
	public void readMainXML(Node src) {
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

			}
		}

	}

	/* DOM Parser */
	public void readAttachXML(Node src) {
		// logger.debug("------------ Recursive -----------");
		NodeList children = src.getChildNodes();

		// get ns:MT_Student
		Node MT_Student = children.item(0);

		// get MT_Student.element
		NodeList student_element = MT_Student.getChildNodes();

		for (int i = 0; i < student_element.getLength(); i++) {
			Node lineNode = student_element.item(i);

			if (lineNode.getNodeName().equalsIgnoreCase("course")) {

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

}
