/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

/**
 *
 * @author sttsenov
 */
import java.io.IOException;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import static javafxapplication1.GUIImplementation.getCurrentTime;
import static javafxapplication1.Launcher.storeError;

public class Email {
    /**
     * Logs into a Gmail account and send an email through there 
     * @param toUser the receiver of the email
     * @param subject the subject of the email
     * @param messageUser the message in the email
     * @throws java.io.IOException
     */
    public static void sendGmail(String toUser, String subject, String messageUser) throws IOException {
        
        //username and password to log in a google mail account
        final String username = ""; //email that you are going to send message through
        final String password = ""; //password for the email

        //specifies the port, host, authentication and protocol
        Properties prop = new Properties();
	prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        
        //logs into the email and sends a message
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            //writes the message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("")); //email address
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toUser)
            );
            message.setSubject(subject);
            message.setText(messageUser);

            //sends the message
            Transport.send(message);
        } catch (MessagingException e){
            storeError(e.getMessage(), getCurrentTime());
        }
    }
}
