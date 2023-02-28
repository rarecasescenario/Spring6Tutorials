package com.programming.techie;

import com.iwm.config.AppConfig;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class EmailApplicaiton {

	public static void main(String[] args) {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
		//CourseService cs = ctx.getBean("courseService",CourseService.class);


	}

}
