package br.inatel.dm111.api.user.service;

import br.inatel.dm111.persistence.user.User;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FirebaseCloudMessagePublisher {

    private static final Logger log = LoggerFactory.getLogger(FirebaseCloudMessagePublisher.class);

    private final FirebaseMessaging firebaseMessaging;

    public FirebaseCloudMessagePublisher(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    public boolean publish(User user) {
        var notification = buildNotification(user);
        var message = buildMessage(user);
        try {
            var result = firebaseMessaging.send(message);
            log.info("Message successfully published to FCM. {}", result);
            return true;
        } catch (FirebaseMessagingException e) {
            log.error("Failure to publish the message to FCM. ", e);
        }

        return false;
    }

    private Map<String, String> buildNotification(User user) {
        var message = buildGreeting(user);
        return Map.of("body", message,
                "title", "Hi there!");
//        return Notification.builder()
//                .setBody(message)
//                .setTitle("Hi there!")
//                .build();
    }

    private String buildGreeting(User user) {
        var message = new StringBuilder("Hello ")
                .append(user.getName())
                .append(" welcome to the DM111 app")
                .toString();
        return message;
    }

    private Message buildMessage(User user) {
        return Message.builder()
                .setTopic("/topics/users")
                .putAllData(buildNotification(user))
                .build();
    }
}
