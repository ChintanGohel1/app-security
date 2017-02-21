package com.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Vinit Solanki
 *
 */
@Component
public class Constants {

	public static final String LOGOUT_URL = "/auth/logout";

	public static String clientHost;

	public static String clientPort;

	public static String clientAppName;

	public static String clientForgotPassUrl;

	@Value("${client.host}")
	public void setClientHost(String clientHost) {
		Constants.clientHost = clientHost;
	}

	@Value("${client.port}")
	public void setClientPort(String clientPort) {
		Constants.clientPort = clientPort;
	}

	@Value("${client.app.name}")
	public void setClientAppName(String clientAppName) {
		Constants.clientAppName = clientAppName;
	}

	@Value("${client.app.forgotPassUrl}")
	public void setClientForgotPassUrl(String clientForgotPassUrl) {
		Constants.clientForgotPassUrl = clientForgotPassUrl;
	}

	//public static String CLIENT_URL = clientHost + clientPort + clientAppName;

}
