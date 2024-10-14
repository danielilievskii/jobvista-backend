package mk.ukim.finki.predmeti.internettehnologii.jobvistabackend.service.intef;

public interface EmailSenderService {
    void sendEmail(String to, String subject, String text);
}
