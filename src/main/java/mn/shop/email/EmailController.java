package mn.shop.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("mail")
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("send")
    public ResponseEntity<?> updateProduct(@RequestBody MailDto mailDto) {
        log.info("start to send email phone");
        emailService.send("tsotan.des@gmail.com", mailDto);
        return ResponseEntity.ok(Boolean.TRUE);
    }

}
