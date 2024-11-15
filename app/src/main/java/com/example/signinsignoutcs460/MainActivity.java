// MainActivity.java
package com.example.signinsignoutcs460;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * MainActivity handles user login functionality.
 * Users can sign in with email and password or navigate to the sign-up page.
 */
public class MainActivity extends AppCompatActivity {
    private EditText emailField, passwordField;
    private Button signInButton, registerButton;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    /**
     * Initializes the activity and sets up the sign-in and register button listeners.
     * @param savedInstanceState Saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        signInButton = findViewById(R.id.signInButton);
        registerButton = findViewById(R.id.registerButton);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        signInButton.setOnClickListener(view -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            runOnUiThread(() -> Toast.makeText(MainActivity.this, "Sign In Successful", Toast.LENGTH_SHORT).show());
                        } else {
                            runOnUiThread(() -> Toast.makeText(MainActivity.this, "Sign In Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show());
                        }
                    });
        });

        registerButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Retrieves user data from Firebase Realtime Database after successful sign-in.
     */
    private void retrieveUserData() {
        String userId = firebaseAuth.getCurrentUser().getUid();
        databaseReference.child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                String firstName = snapshot.child("firstName").getValue(String.class);
                String profileImageBase64 = snapshot.child("profileImage").getValue(String.class);

                // Decode Base64 image if available
                if (profileImageBase64 != null && !profileImageBase64.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Welcome, " + firstName, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Failed to retrieve user data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
