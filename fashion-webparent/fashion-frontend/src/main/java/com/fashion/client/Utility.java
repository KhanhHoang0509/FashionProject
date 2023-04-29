package com.fashion.client;


import com.fashion.client.setting.EmailSettingBag;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

public class Utility {

	//phương thức này sẽ lấy ra customer đang đăng nhập, sau đó trả về email của customer đó
	public static String getEmailOfAuthenticatedCustomer(HttpServletRequest request) {
		Object principal = request.getUserPrincipal();
		if (principal == null) return null;
		
		String customerEmail = null;
		
		if (principal instanceof UsernamePasswordAuthenticationToken 
				|| principal instanceof RememberMeAuthenticationToken) {
			customerEmail = request.getUserPrincipal().getName();
		}
		
		return customerEmail;
	}

	public static JavaMailSenderImpl prepareJavaMailSender(EmailSettingBag emailSettings) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();//tạo đối tượng JavaMailSenderImpl và gán các thông tin để gửi mail

		mailSender.setHost(emailSettings.getHost());//smtp.gmail.com
		mailSender.setPort(emailSettings.getPort());//587
		mailSender.setUsername(emailSettings.getUsername());//nhbtuyen2702@gmail.com
		mailSender.setPassword(emailSettings.getPassword());//gpctiolgpwrabzxn

		Properties mailProperties = new Properties();
		mailProperties.setProperty("mail.smtp.auth", emailSettings.getSmtpAuth());//true
		mailProperties.setProperty("mail.smtp.starttls.enable", emailSettings.getSmtpSecured());//true

		mailSender.setJavaMailProperties(mailProperties);

		return mailSender;
	}

	public static String getSiteUrl(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();//http://localhost:8083/ShoppingCartClient/create_customer	http://localhost:8083/ShoppingCartClient/forgot_password

		return siteURL.replace(request.getServletPath(), "");
	}

}
