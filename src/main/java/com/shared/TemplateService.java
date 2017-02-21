package com.shared;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

/**
 * @author Vinit Solanki
 *
 **/
@Component
public class TemplateService {

	private static SpringTemplateEngine templateEngine;

	@Autowired
	public TemplateService(SpringTemplateEngine templateEngine) {
		super();
		this.templateEngine = templateEngine;
	}

	public static String getTemplate(String page, Context ctx) {

		return templateEngine.process(page, ctx);
	}

}
