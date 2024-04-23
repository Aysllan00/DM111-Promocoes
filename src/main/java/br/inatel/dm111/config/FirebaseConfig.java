package br.inatel.dm111.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class FirebaseConfig {

    private static final String APP_NAME = "dm111-ayslan-promo";

    @Value("classpath:service-accounts.json")
    Resource resource;

    @Bean
    public FirebaseOptions firebaseOptions(GoogleCredentials googleCredentials) throws IOException {
        return FirebaseOptions.builder()
                .setCredentials(googleCredentials)
                .build();
    }

    @Bean
    public GoogleCredentials googleCredentials() throws IOException {
        return GoogleCredentials.fromStream(resource.getInputStream());
    }

    @Bean
    public FirebaseApp firebaseApp(FirebaseOptions firebaseOptions) {
        return FirebaseApp.initializeApp(firebaseOptions, APP_NAME);
    }

    @Bean
    public Firestore firestore(FirebaseApp firebaseApp) {
        return FirestoreClient.getFirestore(firebaseApp);
    }

    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}
