/**
 * 
 */
package com.binlah.springboot.soap.web.services.example.ws;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.binlah.teachers.NameAndValue;
import com.binlah.teachers.Teacher;

/**
 * @author BinlaT
 *
 */
@Component
public class TeacherRepository {

	private static Map<Integer, Teacher> db = new HashMap<Integer, Teacher>();

	@PostConstruct
	void initData() {
		NameAndValue course1 = new NameAndValue();
		course1.setName("CR1");
		course1.setValue("Math");

		NameAndValue course2 = new NameAndValue();
		course2.setName("CR2");
		course2.setValue("Science");

		NameAndValue course3 = new NameAndValue();
		course3.setName("C3");
		course3.setValue("English");

		NameAndValue room1 = new NameAndValue();
		room1.setName("RM1");
		room1.setValue("Room 1 Building 1");

		NameAndValue room2 = new NameAndValue();
		room2.setName("RM2");
		room2.setValue("Room 2 Building 1");

		NameAndValue room3 = new NameAndValue();
		room3.setName("RM3");
		room3.setValue("Room 3 Building 2");

		NameAndValue room4 = new NameAndValue();
		room4.setName("RM4");
		room4.setValue("Room 4 Office Space");

		Teacher s1 = new Teacher();
		s1.setId(1);
		s1.setName("Pong+");
		s1.getCourses().add(course1);
		s1.getCourses().add(course2);
		s1.getRooms().add(room1);
		s1.getRooms().add(room2);
		db.put(s1.getId(), s1);

		Teacher s2 = new Teacher();
		s2.setId(2);
		s2.setName("Koy");
		s2.getCourses().add(course3);
		s2.getRooms().add(room2);
		s2.getRooms().add(room3);
		s2.getRooms().add(room4);
		db.put(s2.getId(), s2);

		Teacher s3 = new Teacher();
		s3.setId(3);
		s3.setName("Chai");
		s3.getCourses().add(course1);
		s3.getCourses().add(course3);
		db.put(s3.getId(), s3);
	}

	public Teacher getById(int id) {
		Assert.notNull(id, "The country's name must not be null");
		return db.get(id);
	}
}
