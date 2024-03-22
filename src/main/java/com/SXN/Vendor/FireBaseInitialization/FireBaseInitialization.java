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
    public void initialization() {
        try {
            // Check if FirebaseApp with name "DEFAULT" exists
            if (FirebaseApp.getApps().isEmpty()) {
                // Initialize Firebase only if it hasn't been initialized yet
                FileInputStream serviceAccount = new FileInputStream("serviceAccountKey.json");
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();
                FirebaseApp.initializeApp(options);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
