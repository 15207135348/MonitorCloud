package com.whut.common.service.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.whut.common.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Map;

/**
 * @author yy
 * @describe
 * @time 18-11-22 下午7:07
 */
@Service
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender mailSender;

    //模板引擎对象
    private final TemplateEngine templateEngine;

    private static final String FROM = "1365733349@qq.com";

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }


    @Override
    public void sendTextEmail(String toAddress, String subject, String msgBody) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(FROM);
        simpleMailMessage.setTo(toAddress);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(msgBody);
        mailSender.send(simpleMailMessage);
    }


    @Override
    public boolean sendHtmlEmail(String toAddress, String subject, String htmlBody){
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        try {
            helper.setTo(toAddress);
            helper.setFrom(FROM);
            helper.setText(htmlBody, true);
            helper.setSubject(subject);
            mailSender.send(message);
        } catch (MessagingException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean sendHtmlEmail(String toAddress, String subject, String htmlBody, String filePath){

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toAddress);
            helper.setFrom(FROM);
            helper.setText(htmlBody, true);
            helper.setSubject(subject);
            File file = new File(filePath);
            if (!file.exists()) {
                return false;
            }
            helper.addAttachment(file.getName(), file);
            mailSender.send(message);
        } catch (MessagingException e) {
            return false;
        }
        return true;
    }

    public boolean sendHtmlEmail(String toAddress, String subject, String templatePath, Map<String,String> dataMap){
        Context context = new Context();
        context.setVariables(dataMap);
        return sendHtmlEmail(toAddress,subject,templateEngine.process(templatePath,context));
    }

}
