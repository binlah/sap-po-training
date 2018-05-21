/**
 * 
 */
package com.binlah.sap.pi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.binlah.sap.pi.mapping.domain.NameAndValue;
import com.binlah.sap.pi.mapping.domain.Personnel;
import com.binlah.sap.pi.mapping.domain.Student;

/**
 * @author BinlaT
 *
 */
public class ReadXML {

	private static Log logger = LogFactory.getLog(ReadXML.class);

	private static String src_path = "D:\\PI\\Student.xml";
	private static String dest_path = "D:\\PI\\Personnel.xml";

	String result = "";

	private Element MT_Student;

	private final Student student = new Student();
	private final Personnel personnel = new Personnel();

	// private boolean isFirstItemToHeader = true;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ReadXML readFile = new ReadXML();
	}

	public ReadXML() {
		// readMT();
		readAndConvert();
	}

	public void readAndConvert() {
		byte[] content = {};
		InputStream is = null;

		StringBuilder sb = new StringBuilder();

		try {
			// is = new ByteArrayInputStream(content);
			is = new FileInputStream(new File(src_path));

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbFactory.newDocumentBuilder();

			Document src = builder.parse(is);
			Document dest = builder.newDocument();

			init(dest);

			// traversingXML(src);
			traversingXML(src);
			logger.debug("student:" + student);

			map();

			logger.debug("personnel:" + personnel);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(dest);

			StreamResult result = new StreamResult(new File(dest_path));

			transformer.transform(source, result);

			logger.debug("File saved!");

			is.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void init(Document doc) {

		// root elements
		Element rootElement = doc.createElement("MT_Personnel");
		doc.appendChild(rootElement);

	}

	/* DOM Parser */
	public void traversingXML(Node src) {
		// logger.debug("------------ Recursive -----------");
		NodeList children = src.getChildNodes();

		// get ns:MT_Student
		Node MT_Student = children.item(0);

		// get MT_Student.element
		NodeList student_element = MT_Student.getChildNodes();
		// logger.debug("line length:" + line.getLength());

		for (int i = 0; i < student_element.getLength(); i++) {
			Node lineNode = student_element.item(i);

			if (lineNode.getNodeName().equalsIgnoreCase("id")) {
				// logger.debug("id:" + lineNode.getChildNodes().item(0).getNodeValue());
				student.setId(lineNode.getChildNodes().item(0).getNodeValue());

			} else if (lineNode.getNodeName().equalsIgnoreCase("firstName")) {
				// logger.debug("firstName:" + lineNode.getChildNodes().item(0).getNodeValue());
				student.setFirstName(lineNode.getChildNodes().item(0).getNodeValue());

			} else if (lineNode.getNodeName().equalsIgnoreCase("lastName")) {
				// logger.debug("lastName:" + lineNode.getChildNodes().item(0).getNodeValue());
				student.setLastName(lineNode.getChildNodes().item(0).getNodeValue());

			} else if (lineNode.getNodeName().equalsIgnoreCase("gender")) {
				// logger.debug("gender:" + lineNode.getChildNodes().item(0).getNodeValue());
				student.setGender(lineNode.getChildNodes().item(0).getNodeValue());

			} else if (lineNode.getNodeName().equalsIgnoreCase("dateOfBirth")) {
				// logger.debug("dateOfBirth:" + lineNode.getChildNodes().item(0).getNodeValue());
				student.setDateOfBirth(lineNode.getChildNodes().item(0).getNodeValue());

			} else if (lineNode.getNodeName().equalsIgnoreCase("age")) {
				// logger.debug("age:" + lineNode.getChildNodes().item(0).getNodeValue());
				student.setAge(lineNode.getChildNodes().item(0).getNodeValue());

			} else if (lineNode.getNodeName().equalsIgnoreCase("email")) {
				// logger.debug("email:" + lineNode.getChildNodes().item(0).getNodeValue());
				student.setEmail(lineNode.getChildNodes().item(0).getNodeValue());

			} else if (lineNode.getNodeName().equalsIgnoreCase("course")) {

				NodeList nameAndValueNodeList = lineNode.getChildNodes();
				NameAndValue course = new NameAndValue();
				student.getCourses().add(course);
				for (int j = 0; j < nameAndValueNodeList.getLength(); j++) {
					Node dataNode = nameAndValueNodeList.item(j);
					if (dataNode.getNodeName().equals("name")) {
						// logger.debug("course name:" + dataNode.getChildNodes().item(0).getNodeValue());
						course.setName(dataNode.getChildNodes().item(0).getNodeValue());

					} else if (dataNode.getNodeName().equals("value")) {
						// logger.debug("course value:" + dataNode.getChildNodes().item(0).getNodeValue());
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
						// logger.debug("club name:" + dataNode.getChildNodes().item(0).getNodeValue());
						club.setName(dataNode.getChildNodes().item(0).getNodeValue());

					} else if (dataNode.getNodeName().equals("value")) {
						// logger.debug("club value:" + dataNode.getChildNodes().item(0).getNodeValue());
						club.setValue(dataNode.getChildNodes().item(0).getNodeValue());

					}
				}
			}
		}

	}

	public void map() {
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
