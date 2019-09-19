package com.baseproject.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Async
	public void simpleEmail(String email, String subject, String text) {

		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(email);

		msg.setSubject(subject);
		msg.setText(text);

		javaMailSender.send(msg);

	}

	@Async
	public void simpleEmail(List<String> emails, String subject, String text) {

		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(emails.toArray(new String[emails.size()]));

		msg.setSubject(subject);
		msg.setText(text);

		javaMailSender.send(msg);

	}

	@Async
	public void htmlEmail(String email, String subject, String html) throws MessagingException {

		MimeMessage msg = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg, true);

		helper.setTo(email);
		helper.setSubject(subject);
		helper.setText(html, true);

		javaMailSender.send(msg);

	}

	@Async
	public void htmlEmail(List<String> emails, String subject, String html) throws MessagingException {

		MimeMessage msg = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg, true);

		helper.setTo(emails.toArray(new String[emails.size()]));
		helper.setSubject(subject);
		helper.setText(html, true);

		javaMailSender.send(msg);

	}

	public String loadHtmlTemplate(String template, Map<String, String> parameters) throws IOException {

		Resource res = new ClassPathResource("email/"+template);
		InputStream inputStream = res.getInputStream();

		byte[] bdata = FileCopyUtils.copyToByteArray(inputStream);
		String templateStr = new String(bdata, StandardCharsets.UTF_8);

		for (Entry<String, String> entry : parameters.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			templateStr = templateStr.replace(key, value);
		}

		return templateStr.replaceAll("\\s+", " ");
	}
}
