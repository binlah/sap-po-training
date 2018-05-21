package com.binlah.springboot.soap.web.services.example.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.binlah.students.GetStudentRequest;
import com.binlah.students.GetStudentResponse;

@Endpoint
public class StudentEndpoint {

	private StudentRepository studentRepository;

	@Autowired
	public StudentEndpoint(StudentRepository studentRepository) {
		super();
		this.studentRepository = studentRepository;
	}

	@PayloadRoot(namespace = "http://binlah.com/students", localPart = "getStudentRequest")
	@ResponsePayload
	public GetStudentResponse processCourseRequest(@RequestPayload GetStudentRequest request) {
		GetStudentResponse response = new GetStudentResponse();

//		Student student = getById(request.getId());
		response.setStudent(studentRepository.getById(request.getId()));

		return response;
	}

}