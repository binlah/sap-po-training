/**
 * 
 */
package com.binlah.springboot.soap.web.services.example.ws;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.binlah.students.NameAndValue;
import com.binlah.students.Student;

/**
 * @author BinlaT
 *
 */
@Component
public class StudentRepository {

	private static Map<Integer, Student> db = new HashMap<Integer, Student>();

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
		
		NameAndValue club1 = new NameAndValue();
		club1.setName("CL1");
		club1.setValue("Ping Pong");
		
		NameAndValue club2 = new NameAndValue();
		club2.setName("CL2");
		club2.setValue("Go");
		
		NameAndValue club3 = new NameAndValue();
		club3.setName("CL3");
		club3.setValue("Tennis");
		
		NameAndValue club4 = new NameAndValue();
		club4.setName("CL4");
		club4.setValue("Foot Ball");

		Student s1 = new Student();
		s1.setId(1);
		s1.setName("Binla");
		s1.getCourses().add(course1);
		s1.getCourses().add(course2);
		s1.getClubs().add(club1);
		s1.getClubs().add(club2);
		db.put(s1.getId(), s1);

		Student s2 = new Student();
		s2.setId(2);
		s2.setName("Vang");
		s2.getCourses().add(course3);
		s2.getClubs().add(club2);
		s2.getClubs().add(club3);
		s2.getClubs().add(club4);
		db.put(s2.getId(), s2);

		Student s3 = new Student();
		s3.setId(3);
		s3.setName("Tao");
		s3.getCourses().add(course1);
		s3.getCourses().add(course3);
		db.put(s3.getId(), s3);
	}

	public Student getById(int id) {
		Assert.notNull(id, "The country's name must not be null");
		return db.get(id);
	}
}
