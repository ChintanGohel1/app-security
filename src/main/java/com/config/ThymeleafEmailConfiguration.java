package com.config;//package com.config;
//
//import java.util.Properties;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
//
//@Configuration
//public class ThymeleafEmailConfiguration {
//	@Bean
//	public JavaMailSender getJavaMailSenderImpl() {
//		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
//
//		Properties props = new Properties();
//		/* some properties here */
//
//		javaMailSender.setJavaMailProperties(props);
//
//		return javaMailSender;
//	}
//
//	@Bean
//	public ClassLoaderTemplateResolver emailTemplateResolver() {
//		ClassLoaderTemplateResolver emailTemplateResolver = new ClassLoaderTemplateResolver();
//		emailTemplateResolver.setPrefix("mails/");
//		emailTemplateResolver.setSuffix(".html");
//		emailTemplateResolver.setTemplateMode("HTML5");
//		emailTemplateResolver.setCharacterEncoding("UTF-8");
//		emailTemplateResolver.setOrder(1);
//
//		return emailTemplateResolver;
//	}
//}