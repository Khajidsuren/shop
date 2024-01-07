package mn.shop.email;

import mn.shop.utils.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    private final static Logger LOGGER = LoggerFactory
            .getLogger(EmailService.class);

    @Autowired
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String fromMail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(String to, MailDto mailDto) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setText(fillHtml(mailDto), true);
            helper.setTo(to);
            helper.setSubject(mailDto.getPhoneNumber());
            helper.setFrom(fromMail);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            LOGGER.error("failed to send email", e);
            throw new IllegalStateException("failed to send email");
        }
    }

    public String fillHtml(MailDto mailDto) {
        Map<String, String> map = buildMap(mailDto);
        String template = """
                <body style="margin: 0; padding: 0; font-family: Arial, sans-serif; font-size: 14px; background-color: #f8f8f8;">
                	<table border="0" cellpadding="0" cellspacing="0" width="100%">
                		<tr>
                			<td align="center" style="background-color: #1abc9c; padding: 40px;">
                				<h1 style="color: #fff; font-size: 32px;">Таньд санал хүсэлт ирлээ</h1>
                			</td>
                		</tr>
                		<tr>
                			<td align="center" style="padding: 20px;">
                			</td>
                		</tr>
                		<tr>
                			<td align="center" style="padding: 40px;">
                				<h2 style="color: #1abc9c; font-size: 28px;">Санал хүсэлт</h2>
                				<p style="color: #333; line-height: 1.5; width: 50%;">{%comment%}</p>
                				<h2 style="color: #1abc9c; font-size: 28px;">Утасны дугаар</h2>
                				<p style="color: #333; line-height: 1.5; width: 50%;">{%phone%}</p>
                			</td>
                		</tr>
                	</table>
                </body>
                """;
        if (map.isEmpty())
            throw new ValidationException("Утасны дугаар, захилах бүтээгдэхүүн олдсонгүй.");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (StringUtils.hasText(entry.getValue()))
                template = template.replace(entry.getKey(), entry.getValue());
            else template = template.replace(entry.getKey(), "");
        }
        return template;
    }

    public Map<String, String> buildMap(MailDto mailDto) {
        Map<String, String> map = new HashMap<>();
        map.put("{%comment%}", mailDto.getSuggest());
        map.put("{%phone%}", mailDto.getPhoneNumber());
        return map;
    }
}
