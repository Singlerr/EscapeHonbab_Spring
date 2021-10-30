package io.github.escapehonbab.spring;

import io.github.escapehonbab.jpa.objects.User;
import io.github.escapehonbab.lang.StaticMessage;
import io.github.escapehonbab.spring.objects.RequestBundle;
import io.github.escapehonbab.spring.objects.ResponseBundle;
import io.github.escapehonbab.spring.objects.VerificationBundle;
import io.github.escapehonbab.spring.service.VerificationService;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;


@EnableAsync
@RestController
@RequestMapping("/api/v1/auth")
public class VerificationController {

    @Value("${sns.api_secret}")
    private String apiSecret;

    @Value("${sns.api_key}")
    private String apiKey;

    @Value("${sns.phone_number}")
    private String phoneNumber;

    @PostMapping(value = "/msg")
    public ResponseBundle sendVerificationMessage(@RequestBody RequestBundle bundle) throws IOException {
        Message msg = new Message(apiKey, apiSecret);
        HashMap<String, String> params = new HashMap<>();
        params.put("to", bundle.getMessage(User.class).getPhoneNumber());
        params.put("from", phoneNumber);
        params.put("type", "SMS");
        params.put("text", String.format("인증 코드는 [%s]입니다.", VerificationService.addNewVerification(bundle.getMessage(User.class).getPhoneNumber())));
        params.put("app_version", "eh 1.0");
        int responseCode;
        String response;
        try {
            JSONObject result = msg.send(params);
            if (result.get("success_count").toString().equals("1")) {
                responseCode = StaticMessage.RESP_SUCCESS;
                response = StaticMessage.SUCCESS_TRANSACTION;
            } else {
                responseCode = StaticMessage.RESP_FAILED;
                response = result.get("result_message").toString().toUpperCase();
            }
        } catch (CoolsmsException ex) {
            responseCode = StaticMessage.RESP_FAILED;
            response = ex.getMessage().toUpperCase();
        }

        return ResponseBundle.builder().response(response).responseCode(responseCode).build();
    }

    @PostMapping(value = "/verify")
    public ResponseBundle verify(@RequestBody VerificationBundle bundle) {
        String response = VerificationService.getVerificationCheckMessage(bundle.getPhoneNumber(), bundle.getCode());
        int responseCode = StaticMessage.RESP_FAILED;
        if (response.equals(StaticMessage.Verification.SUCCESS_VERIFICATION)) {
            responseCode = StaticMessage.RESP_SUCCESS;
        }
        return ResponseBundle.builder().response(response).responseCode(responseCode).build();
    }
}
