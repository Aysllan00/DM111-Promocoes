package br.inatel.dm111.messaging.publisher;

import br.inatel.dm111.messaging.Event;
import br.inatel.dm111.messaging.EventType;
import br.inatel.dm111.messaging.Operation;
import br.inatel.dm111.messaging.SuperMarketListMessage;
import br.inatel.dm111.persistence.supermarketlist.SuperMarketList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.ExecutionException;

@Component
public class SuperMarketListPublisher {

    private static final Logger log = LoggerFactory.getLogger(SuperMarketListPublisher.class);

    private final Publisher publisher;
    private final ObjectMapper objectMapper;

    public SuperMarketListPublisher(Publisher publisher,
                                    ObjectMapper objectMapper) {
        this.publisher = publisher;
        this.objectMapper = objectMapper;
    }

    public boolean publishCreation(SuperMarketList spl) {
        var event = buildEvent(spl, Operation.ENTITY_ADDED);
        return publish(event);
    }

    public boolean publishUpdate(SuperMarketList spl) {
        var event = buildEvent(spl, Operation.ENTITY_UPDATED);
        return publish(event);
    }

    public boolean publishDelete(SuperMarketList spl) {
        var event = buildEvent(spl, Operation.ENTITY_DELETED);
        return publish(event);
    }

    private boolean publish(Event event) {
        try {
            var convertedSpl = stringifyEvent(event);
            var data = ByteString.copyFromUtf8(convertedSpl);

            var pubSubMessage = PubsubMessage.newBuilder().setData(data).build();
            ApiFuture<String> result = publisher.publish(pubSubMessage);
            var messageId = result.get();
            log.info("Message successfully published {}.", messageId);

            return true;
        } catch (JsonProcessingException e) {
            log.error("Failure to convert the SuperMarketList to the correct format");
        } catch (ExecutionException | InterruptedException e) {
            log.error("Failure to publish the message into the topic.", e);
        }

        return false;
    }

    private String stringifyEvent(Event event) throws JsonProcessingException {
        return objectMapper.writeValueAsString(event);
    }

    private Event buildEvent(SuperMarketList spl, Operation operation) {
        return new Event(EventType.SUPERMARKET_LIST, operation, buildMessage(spl));
    }

    private SuperMarketListMessage buildMessage(SuperMarketList spl) {
        return new SuperMarketListMessage(spl.getId(),
                spl.getName(),
                spl.getUserId(),
                spl.getProducts(),
                Instant.now().toEpochMilli());
    }

}
