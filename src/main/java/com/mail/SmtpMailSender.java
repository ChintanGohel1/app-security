package com.mail;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class SmtpMailSender {

	private final static Logger log = Logger.getLogger(SmtpMailSender.class);

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.sendername}")
	private String senderName;

	@Async
	public void send(String to, String subject, String body) {

		if (!StringUtils.isEmpty(to)) {

			MimeMessage message = javaMailSender.createMimeMessage();

			try {

				MimeMessageHelper helper = new MimeMessageHelper(message, false);

				helper.setFrom("dummy@email.com", senderName);
				helper.setSubject(subject);
				helper.setTo(to.split(";"));
				helper.setText(body, true); // true indicates html
				// continue using helper object for more functionalities like adding attachments, etc.  

				javaMailSender.send(message);
				log.info("Mail sent to : " + to);

			} catch (UnsupportedEncodingException | MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		//			Thread thread = new Thread(task);
		//			thread.start();
	}
}
