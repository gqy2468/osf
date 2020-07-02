package com.moefilm.web.service.Impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.moefilm.web.service.MailService;
import com.moefilm.web.util.Property;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service("mailService")
public class MailServiceImpl implements MailService {
	
	static{
		String classpath = MailServiceImpl.class.getClassLoader().getResource("").getPath();
		templateGroup = new StringTemplateGroup("mailTemplates", classpath + "/mailTemplates");
	}
	
	public static String IMG_BASE_URL;
	
	public static StringTemplateGroup templateGroup;
	
    @Autowired
    private JavaMailSenderImpl mailSender;
    
    private void sendMail(String to, String subject, String body) {
    	MimeMessage mail = mailSender.createMimeMessage();	
    	try {
    		MimeMessageHelper helper = new MimeMessageHelper(mail, true, "utf-8");
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body, true);
			mailSender.send(mail);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
    	
    }
    
    /**
     * send activation mail to
     * @param to
     */
    public void sendAccountActivationEmail(String to, String key){
    	//String body = "<a href='"+ACTIVATE_CONTEXT+key+"?email="+to+"'>激活链接</a>";
    	StringTemplate activation_temp = templateGroup.getInstanceOf("activation");
    	activation_temp.setAttribute("img_base_url", IMG_BASE_URL);
    	activation_temp.setAttribute("email", to);
    	activation_temp.setAttribute("href", Property.ACTIVATE_CONTEXT+key+"?email="+to);
    	activation_temp.setAttribute("link", Property.ACTIVATE_CONTEXT+key+"?email="+to);
    	sendMail(to, "OSF账户激活", activation_temp.toString());
    }
    
    /**
     * send change password link to
     * @param to
     */
    public void sendResetPwdEmail(String to, String key){
    	
    	StringTemplate activation_temp = templateGroup.getInstanceOf("resetpwd");
    	activation_temp.setAttribute("img_base_url", IMG_BASE_URL);
    	activation_temp.setAttribute("href", Property.RESETPWD_CONTEXT+"?key="+key+"&email="+to);
    	activation_temp.setAttribute("link", Property.RESETPWD_CONTEXT+"?key="+key+"&email="+to);
    	
    	sendMail(to, "OSF账户密码重置", activation_temp.toString());
    }
}
