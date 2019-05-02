package com.mm.gemini.helper.utils;

import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Properties;

/**
 * 邮箱发送工具
 */
@Component
public class SendEmailUtils {
    @Value("${sys_email_password}")
    private  String SYS_EMAIL_PWD;
    @Value("${sys_email_username}")
    private  String SYS_EMAIL_USERNAME;
    @Value("${sys_email_type}")
    private  String SYS_EAML_TYPE;

	public void sendVerificationCode(String email,String content,String title) throws GeneralSecurityException{
		  // 
        String to = email;

        // 
        String from = SYS_EMAIL_USERNAME;

        //
        String host = SYS_EAML_TYPE;  //

        // 
        Properties properties = System.getProperties();

        // 
        properties.setProperty("mail.smtp.host", host);

        properties.put("mail.smtp.auth", "true");
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);
        // 
        Session session = Session.getDefaultInstance(properties,new Authenticator(){
            public PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(SYS_EMAIL_USERNAME, SYS_EMAIL_PWD); //
            }
        });

        try{
            // 
            MimeMessage message = new MimeMessage(session);

            // 
            message.setFrom(new InternetAddress(from));

            // 
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            //设置邮件标题
            message.setSubject(title);

            //设置邮件内容
            message.setText(content);

            // 
            Transport.send(message);
            System.out.println("Sent message successfully");
        }catch (MessagingException mex) {
            mex.printStackTrace();
        }
	}
}
