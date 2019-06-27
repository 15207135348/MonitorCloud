package com.whut.common.service;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * @author yy
 * @describe
 * @time 18-11-22 下午7:06
 */
public interface EmailService {

    void sendTextEmail(String toAddress, String subject, String msgBody);

    boolean sendHtmlEmail(String toAddress, String subject, String htmlBody);

    boolean sendHtmlEmail(String toAddress, String subject, String htmlBody, String filePath);

    boolean sendHtmlEmail(String toAddress, String subject, String templatePath, Map<String,String> dataMap);

    

}
