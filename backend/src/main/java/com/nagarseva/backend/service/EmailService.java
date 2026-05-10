package com.nagarseva.backend.service;

import com.nagarseva.backend.enums.IssueType;
import com.nagarseva.backend.enums.Role;
import com.nagarseva.backend.enums.Status;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Async
    public void sendRegistrationSuccessEmail(String name, String email) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try {
            helper.setTo(email);
            helper.setSubject("✅ Welcome to Nagarseva – Registration Successful!");

            ClassPathResource resource = new ClassPathResource("templates/register.html");

            String htmlContent = new String(
                    resource.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            htmlContent = htmlContent.replace("{{name}}", name);

            helper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
            System.out.println("done");
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void sendComplaintApprovedEmail(String name, String email, Integer complaintId, IssueType issueType, Integer wardId, Status status) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try {
            helper.setTo(email);
            helper.setSubject("✅ Your Complaint Has Been Approved – Nagarseva");

            ClassPathResource resource = new ClassPathResource("templates/approved.html");

            String htmlContent = new String(
                    resource.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            htmlContent = htmlContent.replace("{{name}}", name);
            htmlContent = htmlContent.replace("{{complaintId}}",complaintId+"");
            htmlContent = htmlContent.replace("{{issueType}}",issueType.name());
            htmlContent = htmlContent.replace("{{wardNo}}",wardId+"");
            htmlContent = htmlContent.replace("{{status}}",status.name());

            helper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void sendComplaintDisapprovedEmail(String name, String email, Integer complaintId, IssueType issueType, Integer wardId, Status status) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try {
            helper.setTo(email);
            helper.setSubject("❌ Your Complaint Has Been Rejected – Nagarseva");

            ClassPathResource resource = new ClassPathResource("templates/rejected.html");

            String htmlContent = new String(
                    resource.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            htmlContent = htmlContent.replace("{{name}}", name);
            htmlContent = htmlContent.replace("{{complaintId}}",complaintId+"");
            htmlContent = htmlContent.replace("{{issueType}}",issueType.name());
            htmlContent = htmlContent.replace("{{wardNo}}",wardId+"");
            htmlContent = htmlContent.replace("{{status}}",status.name());

            helper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void sendComplaintPendingVerificationEmail(String name, String email, Integer complaintId, IssueType issueType, Integer wardId, Status status) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try {
            helper.setTo(email);
            helper.setSubject("✅ Complaint Work Completed • Verification Pending – NagarSeva");

            ClassPathResource resource = new ClassPathResource("templates/pendingverification.html");

            String htmlContent = new String(
                    resource.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            htmlContent = htmlContent.replace("{{name}}", name);
            htmlContent = htmlContent.replace("{{complaintId}}",complaintId+"");
            htmlContent = htmlContent.replace("{{issueType}}",issueType.name());
            htmlContent = htmlContent.replace("{{wardNo}}",wardId+"");
            htmlContent = htmlContent.replace("{{status}}",status.name());

            helper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void sendComplaintAutoClosedEmail(String name, String email, Integer complaintId, IssueType issueType, Integer wardId, Status status) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try {
            helper.setTo(email);
            helper.setSubject("⏳ Complaint Automatically Closed – NagarSeva");

            ClassPathResource resource = new ClassPathResource("templates/autoclosed.html");

            String htmlContent = new String(
                    resource.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            htmlContent = htmlContent.replace("{{name}}", name);
            htmlContent = htmlContent.replace("{{complaintId}}",complaintId+"");
            htmlContent = htmlContent.replace("{{issueType}}",issueType.name());
            htmlContent = htmlContent.replace("{{wardNo}}",wardId+"");
            htmlContent = htmlContent.replace("{{status}}",status.name());

            helper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void sendComplaintReopenedEmail(String name, String email, Integer complaintId, IssueType issueType, Integer wardId, Status status) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try {
            helper.setTo(email);
            helper.setSubject("🔄 Complaint Reopened for Further Action – NagarSeva");

            ClassPathResource resource = new ClassPathResource("templates/reopened.html");

            String htmlContent = new String(
                    resource.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            htmlContent = htmlContent.replace("{{name}}", name);
            htmlContent = htmlContent.replace("{{complaintId}}",complaintId+"");
            htmlContent = htmlContent.replace("{{issueType}}",issueType.name());
            htmlContent = htmlContent.replace("{{wardNo}}",wardId+"");
            htmlContent = htmlContent.replace("{{status}}",status.name());

            helper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void sendOTPGenerationEmail(String resetOtp, String email) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try {
            helper.setTo(email);
            helper.setSubject("🔐 Password Reset Verification Code – NagarSeva");

            ClassPathResource resource = new ClassPathResource("templates/forgotpassword.html");

            String htmlContent = new String(
                    resource.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            htmlContent = htmlContent.replace("{{otp}}",resetOtp);

            helper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void sendPasswordChangedConfirmationEmail(String name, String email, LocalDateTime current) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try {
            helper.setTo(email);
            helper.setSubject("✅ Your Password Has Been Changed – NagarSeva\"");

            ClassPathResource resource = new ClassPathResource("templates/passwordchanged.html");

            String htmlContent = new String(
                    resource.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            htmlContent = htmlContent.replace("{{name}}",name);
            htmlContent = htmlContent.replace("{{changedDate}}",current.toLocalDate().toString());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            htmlContent = htmlContent.replace("{{changedTime}}",current.toLocalTime().format(formatter));

            helper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void sendOfficialRegistrationSuccessEmail(String name, String email, Role role, String password) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try {
            helper.setTo(email);
            helper.setSubject("✅ Welcome to NagarSeva – Your Account Is Ready");

            ClassPathResource resource = new ClassPathResource("templates/registerbyadmin.html");

            String htmlContent = new String(
                    resource.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            htmlContent = htmlContent.replace("{{name}}",name);

            String role1 = "";
            if (role.equals(Role.COUNCILLOR)) role1 = "Ward " + role.name();
            if (role.equals(Role.OFFICER)) role1 = "Municipal " + role.name();
            if (role.equals(Role.ADMIN)) role1 = "Municipal Administrator";

            htmlContent = htmlContent.replace("{{role}}", role1);
            htmlContent = htmlContent.replace("{{email}}", email);
            htmlContent = htmlContent.replace("{{tempPass}}", password);

            helper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
