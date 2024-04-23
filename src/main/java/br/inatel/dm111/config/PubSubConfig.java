package br.inatel.dm111.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.TopicName;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class PubSubConfig {

    @Value("${dm111.project-id}")
    private String projectId;

    @Value("${dm111.pubsub.supermarketlist.topic-name}")
    private String topicName;

    @Value("${dm111.pubsub.supermarketlist.subscription-name}")
    private String subscriptionName;

    @Bean
    //@Qualifier("supermarketlist")
    public TopicName topicName() {
        return TopicName.of(projectId, topicName);
    }

    // Need to set the GOOGLE_APPLICATION_CREDENTIALS pointing out
    // to the path of the credentials file
    @Bean
    public Publisher publisher(TopicName topicName) throws IOException {
        return Publisher.newBuilder(topicName).build();
    }

    @Bean
    public ProjectSubscriptionName subscriptionName() {
        return ProjectSubscriptionName.of(projectId, subscriptionName);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
