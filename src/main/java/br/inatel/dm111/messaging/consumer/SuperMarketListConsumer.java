package br.inatel.dm111.messaging.consumer;

import br.inatel.dm111.messaging.Event;
import br.inatel.dm111.messaging.Operation;
import br.inatel.dm111.persistence.supermarketlist.SuperMarketList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class SuperMarketListConsumer {

    private final Logger log = LoggerFactory.getLogger(SuperMarketListConsumer.class);

    private final ObjectMapper objectMapper;
    private final ProjectSubscriptionName subscriptionName;

    public SuperMarketListConsumer(ObjectMapper objectMapper, ProjectSubscriptionName subscriptionName) {
        this.objectMapper = objectMapper;
        this.subscriptionName = subscriptionName;
    }

    //@PostConstruct
    public void run() {
        log.info("Starting consumer process...");
        MessageReceiver receiver = (PubsubMessage message, AckReplyConsumer consumer) -> {
            var messageId = message.getMessageId();
            var messageString = message.getData().toString(StandardCharsets.UTF_8);
            try {
                var event = objectMapper.readValue(messageString, Event.class);
                log.info("Converted Event: {}", event);
                if (Operation.ENTITY_ADDED == event.operation()) {
                    log.info("Entity added... {}", event.data());
                } else if (Operation.ENTITY_UPDATED == event.operation()) {
                    log.info("Entity updated... {}", event.data());
                } else {
                    log.info("Entity deleted... {}", event.data());
                }
            } catch (JsonProcessingException e) {
                log.error("Failure to process the supermarket list event.", e);
            }
            log.info("Received message: {} with attrs: {}", messageId, messageString);
            consumer.ack();
        };

        var subscriber = Subscriber.newBuilder(subscriptionName, receiver).build();

        // Start messaging consumer
        subscriber.startAsync().awaitRunning();
        log.info("Listening the messages from the supermarket list topic...");

//        try {
//            // Stop messaging consumption gracefully
//            subscriber.awaitTerminated(30, TimeUnit.SECONDS);
//        } catch (TimeoutException e) {
//            // Force stop messaging consumption
//            subscriber.stopAsync();
//        }
    }
}
