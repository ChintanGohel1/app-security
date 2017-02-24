package com.controller;

import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import com.mail.SmtpMailSender;
import com.util.TokenUtils;

@RestController
public class MailSenderController {

	private final static Logger log = Logger.getLogger(MailSenderController.class);

	//	@Autowired
	//	private SmtpMailSender smtpMailSender;
	//
	//	@RequestMapping("/send-mail")
	//	public void sendMail() {
	//
	//		try {
	//			smtpMailSender.send("vinit.solanki@indianic.com", "Test mail from Spring", "Howdy");
	//		} catch (Exception e) {
	//			log.error("Exception while sending mail : " + e);
	//		}
	//
	//	}

	private SpringTemplateEngine templateEngine;
	private SmtpMailSender smtpMailSender;
	
	public MailSenderController() {
	    super();
    }

	@Autowired
	public MailSenderController(SpringTemplateEngine templateEngine, SmtpMailSender smtpMailSender) {
	    super();
	    this.templateEngine = templateEngine;
	    this.smtpMailSender = smtpMailSender;
    }



	@RequestMapping("/send-mail")
	public void sendMail(Locale locale) {

		try {
			final Context ctx = new Context(locale);
			ctx.setVariable("name", "Vinit Solanki");
			ctx.setVariable("email", "vinit.solanki@indianic.com");
			ctx.setVariable("actionUrl", "vinit.solanki@indianic.com");
			ctx.setVariable("expiration_time", TokenUtils.RESET_PASSWORD_TOKEN_EXPIRATION_TIME);

			System.out.println("TokenUtils.RESET_PASSWORD_TOKEN_EXPIRATION_TIME = " + TokenUtils.RESET_PASSWORD_TOKEN_EXPIRATION_TIME);

			final String htmlContent = this.templateEngine.process("test", ctx);

			System.out.println("htmlContent = " + htmlContent);

			smtpMailSender.send("vinit.solanki@indianic.com", "Test mail from Spring", htmlContent);

		} catch (Exception e) {
			log.error("Exception while sending mail : " + e);
		}

	}

}