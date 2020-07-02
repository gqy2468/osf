package com.moefilm.web.service;

public interface MailService {
    
    /**
     * send activation mail to
     * @param to
     */
    public void sendAccountActivationEmail(String to, String key);
    
    /**
     * send change password link to
     * @param to
     */
    public void sendResetPwdEmail(String to, String key);
}
