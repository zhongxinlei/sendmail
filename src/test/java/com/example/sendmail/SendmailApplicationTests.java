package com.example.sendmail;

import com.example.sendmail.entity.User;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
class SendmailApplicationTests {

    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Test
    void contextLoads() {
    }

    @Test
    public void sendSimpleMail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("这是一封测试邮件");
        message.setFrom(emailFrom);
        message.setTo("ocean.deep2003@163.com");
//        message.setCc("37xxxxx37@qq.com");
//        message.setBcc("14xxxxx098@qq.com");
        message.setSentDate(new Date());
        message.setText("这是测试邮件的正文");
        javaMailSender.send(message);
    }

    @Test
    public void sendAttachFileMail() throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject("这是有附件的一封测试邮件");
        helper.setFrom(emailFrom);
        helper.setTo("543368712@qq.com");
        helper.setCc("xinlei.zhong@indegene.com");
//        helper.setBcc("14xxxxx098@qq.com");
        helper.setSentDate(new Date());
        helper.setText("这是测试邮件的正文");
        helper.addAttachment("测试图片.jpg", new File("G:\\java\\POP3-SMTP.jpg"));
        javaMailSender.send(mimeMessage);
    }

    @Test
    public void sendImgResMail() throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject("这是一封测试邮件");
        helper.setFrom(emailFrom);
        helper.setTo("543368712@qq.com");
//        helper.setCc("37xxxxx37@qq.com");
//        helper.setBcc("14xxxxx098@qq.com");
        helper.setSentDate(new Date());
        helper.setText("<p>hello 大家好，这是一封测试邮件，这封邮件包含两种图片，分别如下</p><p>第一张图片：</p><img src='cid:p01'/><p>第二张图片：</p><img src='cid:p02'/>", true);
        helper.addInline("p01", new FileSystemResource(new File("G:\\java\\POP3-SMTP.jpg")));
        helper.addInline("p02", new FileSystemResource(new File("G:\\java\\POP3-SMTP.jpg")));
        javaMailSender.send(mimeMessage);
    }

    //    Freemarker 作邮件模板
    @Test
    public void sendFreemarkerMail() throws MessagingException, IOException, TemplateException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject("这是一封测试邮件");
        helper.setFrom(emailFrom);
        helper.setTo("543368712@qq.com");
//        helper.setCc("37xxxxx37@qq.com");
//        helper.setBcc("14xxxxx098@qq.com");
        helper.setSentDate(new Date());
        //构建 Freemarker 的基本配置
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);
        // 配置模板位置，启动类的位置JhjmailApplication
        ClassLoader loader = SendmailApplication.class.getClassLoader();
        configuration.setClassLoaderForTemplateLoading(loader, "templates");
        //加载模板
        Template template = configuration.getTemplate("freemarker/mail.ftl");
        User user = new User();
        user.setUsername("蒋皓洁");
        user.setNum(1);
        user.setSalary(99999);
        StringWriter out = new StringWriter();
        //模板渲染，渲染的结果将被保存到 out 中 ，将out 中的 html 字符串发送即可
        template.process(user, out);
        helper.setText(out.toString(), true);
        javaMailSender.send(mimeMessage);
    }


    @Autowired
    TemplateEngine templateEngine;

    @Test
    public void sendThymeleafMail() throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject("这是一封测试邮件");
        helper.setFrom(emailFrom);
        helper.setTo("543368712@qq.com");
//        helper.setCc("37xxxxx37@qq.com");
//        helper.setBcc("14xxxxx098@qq.com");
        helper.setSentDate(new Date());
        Context context = new Context();
        context.setVariable("username", "javaboy");
        context.setVariable("num", "000001");
        context.setVariable("salary", "99999");
        String process = templateEngine.process("thy/mail.html", context);
        helper.setText(process, true);
        javaMailSender.send(mimeMessage);
    }


}
