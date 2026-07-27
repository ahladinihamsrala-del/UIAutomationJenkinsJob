package utils;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.File;
import java.util.Properties;

/**
 * Sends an HTML email with the Extent Report (or any file) attached.
 *
 * Uses SendGrid's SMTP relay (smtp.sendgrid.net) rather than a personal
 * Gmail/Outlook account - built for automated/transactional sending, so it
 * doesn't hit the "suspicious sign-in" friction that personal mail providers
 * apply to CI/automation traffic.
 *
 * All sensitive values are read from environment variables so nothing
 * sensitive is committed to GitHub. Set these as Jenkins credentials /
 * environment variables:
 *   MAIL_FROM     - your verified Sender Identity email (SendGrid Settings > Sender Authentication)
 *   MAIL_PASSWORD - your SendGrid API key (starts with "SG.") - NOT an account password
 *   MAIL_TO, SMTP_HOST, SMTP_PORT
 *
 * Note: SendGrid's SMTP username is always the literal string "apikey" -
 * it is NOT your SendGrid account email or MAIL_FROM. Only the password
 * (the API key itself) is the real secret.
 */
public class EmailUtility {

    private static final String SMTP_HOST = System.getenv().getOrDefault("SMTP_HOST", "smtp.sendgrid.net");
    private static final String SMTP_PORT = System.getenv().getOrDefault("SMTP_PORT", "587");
    private static final String SMTP_USERNAME = "apikey"; // SendGrid's fixed SMTP username - always this literal string
    private static final String FROM_EMAIL = System.getenv("MAIL_FROM");
    private static final String MAIL_PASSWORD = System.getenv("MAIL_PASSWORD");
    private static final String TO_EMAIL = System.getenv().getOrDefault("MAIL_TO", "testuser.selenium67@gmail.com");

    public static void sendReportEmail(String subject, String bodyHtml, String attachmentPath) {

        if (FROM_EMAIL == null || MAIL_PASSWORD == null) {
            System.err.println("EmailUtility: MAIL_FROM / MAIL_PASSWORD env vars are not set. Skipping email.");
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        // Without these, a blocked/dropped connection (e.g. corporate firewall
        // silently blocking outbound SMTP) can hang indefinitely instead of
        // failing with a clear error. 10s is generous but bounded.
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.writetimeout", "10000");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USERNAME, MAIL_PASSWORD);
            }
        });

        try {
            System.out.println("EmailUtility: building session and connecting to " + SMTP_HOST + ":" + SMTP_PORT + " ...");
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(TO_EMAIL));
            message.setSubject(subject);

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(bodyHtml, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);

            if (attachmentPath != null) {
                File file = new File(attachmentPath);
                if (file.exists()) {
                    MimeBodyPart attachPart = new MimeBodyPart();
                    attachPart.attachFile(file);
                    attachPart.setFileName(file.getName());
                    multipart.addBodyPart(attachPart);
                    System.out.println("EmailUtility: attached " + file.getName() + " (" + file.length() + " bytes)");
                } else {
                    System.out.println("EmailUtility: report file not found at " + attachmentPath + " (sending without attachment)");
                }
            }

            message.setContent(multipart);
            System.out.println("EmailUtility: message built, calling Transport.send() now...");
            Transport.send(message);
            System.out.println("EmailUtility: report email sent to " + TO_EMAIL);

        } catch (Exception e) {
            System.err.println("EmailUtility: failed to send email - " + e.getMessage());
            e.printStackTrace();
        }
    }

    /** Optional standalone entry point, useful for testing SMTP config in isolation. */
    public static void main(String[] args) {
        sendReportEmail(
                "Test Email - Automation Framework",
                "<h3>This is a test email from EmailUtility</h3>",
                "test-output/ExtentReport/index.html"
        );
    }
}