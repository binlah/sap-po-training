package com.binlah.springboot.soap.web.services.example.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.binlah.teachers.GetTeacherRequest;
import com.binlah.teachers.GetTeacherResponse;

@Endpoint
public class TeacherEndpoint {

	private TeacherRepository teacherRepository;

	@Autowired
	public TeacherEndpoint(TeacherRepository teacherRepository) {
		super();
		this.teacherRepository = teacherRepository;
	}

	@PayloadRoot(namespace = "http://binlah.com/teachers", localPart = "getTeacherRequest")
	@ResponsePayload
	public GetTeacherResponse processCourseRequest(@RequestPayload GetTeacherRequest request) {
		GetTeacherResponse response = new GetTeacherResponse();

		// Student student = getById(request.getId());
		response.setTeacher(teacherRepository.getById(request.getId()));

		return response;
	}

}