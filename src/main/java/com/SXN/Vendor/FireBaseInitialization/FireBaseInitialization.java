package com.SXN.Vendor.FireBaseInitialization;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;


@Configuration

public class FireBaseInitialization {

    @PostConstruct
     try {
            // Read service account key from environment variable
            String renderToken = System.getenv("RENDER_TOKEN");

            // Convert the service account key string to InputStream
            ByteArrayInputStream serviceAccountStream = new ByteArrayInputStream(renderToken.getBytes(StandardCharsets.UTF_8));
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(serviceAccountStream.getBytes(StandardCharsets.UTF_8))))
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            System.err.println("Error initializing Firebase: " + e.getMessage());
        }


}
