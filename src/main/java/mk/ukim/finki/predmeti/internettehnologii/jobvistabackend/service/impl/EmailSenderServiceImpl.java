package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.service.impl;

import mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.service.intef.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${custom.mail.sender.email}")
    private String senderEmail;

    @Value("${custom.mail.sender.name}")
    private String senderName;

    @Override
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(String.format("%s <%s>", senderName, senderEmail));
        mailSender.send(message);
    }
}
