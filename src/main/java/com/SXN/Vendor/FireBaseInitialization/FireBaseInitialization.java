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
                FileInputStream serviceAccount = new FileInputStream("./serviceAccountKey.json");

                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                // Check if FirebaseApp with name "DEFAULT" exists
                if (FirebaseApp.getApps().isEmpty()) {
                    FirebaseApp.initializeApp(options);
                } else {
                    // If FirebaseApp already exists, re-initialize it with the new options
                    FirebaseApp.getInstance().delete(); // Delete the existing app instance
                    FirebaseApp.initializeApp(options); // Initialize with the new options
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
}
