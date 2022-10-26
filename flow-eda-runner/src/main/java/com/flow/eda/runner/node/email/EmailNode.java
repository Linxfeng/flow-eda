package com.flow.eda.runner.node.email;

import com.flow.eda.common.exception.FlowException;
import com.flow.eda.runner.node.AbstractNode;
import com.flow.eda.runner.node.NodeFunction;
import com.flow.eda.runner.node.NodeVerify;
import org.bson.Document;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

public class EmailNode extends AbstractNode {
    private String fromEmail;
    private String[] toEmail;
    private String[] ccEmail;
    private String[] bccEmail;
    private String subject;
    private String text;
    private boolean isHtml;
    private JavaMailSender javaMailSender;

    public EmailNode(Document params) {
        super(params);
    }

    @Override
    public void run(NodeFunction callback) {
        // 发送邮件
        try {
            if (this.isHtml) {
                this.sendHtmlMail();
            } else {
                this.sendSimpleMail();
            }
        } catch (Exception e) {
            throw new FlowException("Failed to send email. " + e.getMessage());
        }

        setStatus(Status.FINISHED);
        callback.callback(output());
    }

    @Override
    protected void verify(Document params) {
        // 邮件服务器
        String host = params.getString("host");
        NodeVerify.notBlank(host, "host");

        // 发件人邮箱
        String username = params.getString("fromEmail");
        NodeVerify.notBlank(username, "fromEmail");
        this.fromEmail = username;

        // 邮箱授权码
        String password = params.getString("authCode");
        NodeVerify.notBlank(password, "authCode");

        // 收件人邮箱
        String toEmail = params.getString("toEmail");
        NodeVerify.notBlank(toEmail, "toEmail");
        this.toEmail = toEmail.split(",");

        // 是否发送html格式
        String isHtml = params.getString("isHtml");
        if ("HTML格式".equals(isHtml)) {
            this.isHtml = true;
        }

        // 邮件主题
        this.subject = params.getString("subject");
        NodeVerify.notBlank(subject, "subject");

        // 邮件内容
        this.text = params.getString("text");
        NodeVerify.notBlank(text, "text");
        if (this.isHtml) {
            NodeVerify.isTrue(this.text.startsWith("<"), "text");
        }

        // 抄送邮箱
        String ccEmail = params.getString("ccEmail");
        if (StringUtils.hasText(ccEmail)) {
            this.ccEmail = ccEmail.split(",");
        }

        // 密送邮箱
        String bccEmail = params.getString("bccEmail");
        if (StringUtils.hasText(bccEmail)) {
            this.bccEmail = bccEmail.split(",");
        }

        // 初始化 JavaMailSender
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);
        this.javaMailSender = javaMailSender;
    }

    /** 发送普通文本格式邮件 */
    private void sendSimpleMail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        if (ccEmail != null) {
            message.setCc(ccEmail);
        }
        if (bccEmail != null) {
            message.setBcc(bccEmail);
        }
        message.setSubject(subject);
        message.setText(text);
        message.setSentDate(new Date());
        javaMailSender.send(message);
    }

    /** 发送HTML格式邮件 */
    private void sendHtmlMail() throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        if (ccEmail != null) {
            helper.setCc(ccEmail);
        }
        if (bccEmail != null) {
            helper.setBcc(bccEmail);
        }
        helper.setSubject(subject);
        helper.setText(text, true);
        helper.setSentDate(new Date());
        javaMailSender.send(message);
    }
}
