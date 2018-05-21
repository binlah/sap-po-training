package com.binlah.springboot.soap.web.services.example;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public ServletRegistrationBean messageDispatcherServlet(ApplicationContext context) {
		MessageDispatcherServlet messageDispatcherServlet = new MessageDispatcherServlet();
		messageDispatcherServlet.setApplicationContext(context);
		messageDispatcherServlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean(messageDispatcherServlet, "/ws/*");
	}

	@Bean(name = "students")
	public DefaultWsdl11Definition studentWsdl11Definition(XsdSchema studentsSchema) {
		DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
		definition.setPortTypeName("StudentPort");
		definition.setTargetNamespace("http://binlah.com/students");
		definition.setLocationUri("/ws");
		definition.setSchema(studentsSchema);
		return definition;
	}

	@Bean
	public XsdSchema studentsSchema() {
		return new SimpleXsdSchema(new ClassPathResource("student.xsd"));
	}

	@Bean(name = "teachers")
	public DefaultWsdl11Definition teacherWsdl11Definition(XsdSchema teachersSchema) {
		DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
		definition.setPortTypeName("TeacherPort");
		definition.setTargetNamespace("http://binlah.com/teachers");
		definition.setLocationUri("/ws");
		definition.setSchema(teachersSchema);
		return definition;
	}

	@Bean
	public XsdSchema teachersSchema() {
		return new SimpleXsdSchema(new ClassPathResource("teacher.xsd"));
	}
}