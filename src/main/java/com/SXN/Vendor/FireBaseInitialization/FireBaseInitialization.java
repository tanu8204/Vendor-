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
    // public void initialization() throws IOException {
    //     FileInputStream serviceAccount =
    //             new FileInputStream("secrets.RENDER_TOKEN");

    //     FirebaseOptions options = new FirebaseOptions.Builder()
    //             .setCredentials(GoogleCredentials.fromStream(serviceAccount))
    //             .build();

    //     FirebaseApp.initializeApp(options);
    // }
     try {
            // Read service account key from environment variable
            String renderToken = System.getenv("RENDER_TOKEN");

            // Convert the service account key string to InputStream
            ByteArrayInputStream serviceAccountStream = new ByteArrayInputStream(renderToken.getBytes(StandardCharsets.UTF_8));

            // Initialize Firebase with the service account key
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            System.err.println("Error initializing Firebase: " + e.getMessage());
        }


}
