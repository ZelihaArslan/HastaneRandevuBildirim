package org.example;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsSender {
    // Twilio hesap bilgilerinizi buraya ekleyin
    public static final String ACCOUNT_SID = "AC8d883#############";
    public static final String AUTH_TOKEN = "a5ec99#############";
    public static final String FROM_NUMBER = "+195########";

    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public static void sendSms(String to, String body) {
        Message message = Message.creator(
                        new PhoneNumber(to),
                        new PhoneNumber(FROM_NUMBER),
                        body)
                .create();
        System.out.println("SMS gönderildi: " + message.getSid());
    }
}
