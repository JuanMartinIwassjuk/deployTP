package ar.edu.utn.frba.dds.models.entidades.notificaciones;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaMailSender implements MailSender{
  private static Message prepareMessage(Session session, String myAccountEmail, String mail,Notificacion notificacion) {
    try {
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(myAccountEmail));

      message.setRecipient(Message.RecipientType.TO, new InternetAddress(mail));
      message.setSubject(notificacion.getTitulo());
      message.setText(notificacion.getDescripcion());
      return message;
    } catch (Exception e) {
      Logger.getLogger(JavaMailSender.class.getName()).log(Level.SEVERE, null, e);
    }
    return null;
  }

  @Override
  public void enviarNotificacion(String mail, Notificacion notificacion) {
    Properties props = new Properties();
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.port", "465");
    props.put("mail.smtp.socketFactory.port", "465");
    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    props.put("mail.smtp.ssl.protocols", "TLSv1.2.png");

    String myAccountEmail = "ACA VA LA CUENTA DE GMAIL";
    String password = "CLAVE GENERADA EN GMAIL (NO ES LA PASS, ES EL AUTENTICADOR)";

    Session session = Session.getInstance(props, new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(myAccountEmail, password);
      }
    });

    Message message = prepareMessage(session, myAccountEmail, mail,notificacion);
    assert message != null;
    try {
      Transport.send(message);
    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }
}