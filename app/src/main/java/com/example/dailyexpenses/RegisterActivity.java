package com.example.dailyexpenses;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, phoneEditText, passwordEditText, confirmPasswordEditText, roleEditText;
    private Button registerButton;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("Users");

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        roleEditText = findViewById(R.id.roleEditText);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(view -> registerUser());
    }

    private void registerUser() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String role = roleEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name)) { nameEditText.setError("Full Name is required"); return; }
        if (TextUtils.isEmpty(email)) { emailEditText.setError("Email is required"); return; }
        if (TextUtils.isEmpty(phone)) { phoneEditText.setError("Phone Number is required"); return; }
        if (TextUtils.isEmpty(password)) { passwordEditText.setError("Password is required"); return; }
        if (!password.equals(confirmPassword)) { confirmPasswordEditText.setError("Passwords do not match"); return; }
        if (TextUtils.isEmpty(role)) { roleEditText.setError("Role is required"); return; }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                if (firebaseUser != null) {
                    String userId = firebaseUser.getUid();
                    User user = new User(userId, name, email, phone, role);

                    userRef.child(userId).setValue(user).addOnCompleteListener(databaseTask -> {
                        if (databaseTask.isSuccessful()) {
                            Toast.makeText(this, "Registration successful.", Toast.LENGTH_LONG).show();
                            mAuth.signOut();
                            startActivity(new Intent(this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(this, "Failed to save details: " + databaseTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public static class User {
        public String userId, name, email, phone, role;

        public User() {} // Default constructor for Firebase

        public User(String userId, String name, String email, String phone, String role) {
            this.userId = userId;
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.role = role;
        }
    }
}
