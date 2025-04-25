package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView registerTextView;
    private FirebaseAuth mAuth;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inisialisasi komponen
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerTextView = findViewById(R.id.registerTextView);

        // Inisialisasi Firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("users");

        // Aksi tombol login
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Harap isi semua field", Toast.LENGTH_SHORT).show();
                return;
            }

            // Autentikasi dengan Firebase Authentication
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Dapatkan UID pengguna
                            String userId = mAuth.getCurrentUser().getUid();
                            Log.d("LoginActivity", "User ID: " + userId);

                            // Am專家 pengguna dari database
                            database.child(userId).get().addOnCompleteListener(dbTask -> {
                                if (dbTask.isSuccessful() && dbTask.getResult().exists()) {
                                    String username = dbTask.getResult().child("username").getValue(String.class);
                                    Toast.makeText(LoginActivity.this, "Login berhasil! Selamat datang, " + username, Toast.LENGTH_SHORT).show();

                                    // Intent ke MainActivity
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("username", username); // Opsional: Kirim username ke MainActivity
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Log.e("LoginActivity", "Database error: Data pengguna tidak ditemukan");
                                    Toast.makeText(LoginActivity.this, "Gagal mengambil data pengguna", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            Log.e("LoginActivity", "Auth error: " + task.getException().getMessage());
                            Toast.makeText(LoginActivity.this, "Gagal login: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // Aksi teks registrasi
        registerTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            Toast.makeText(LoginActivity.this, "Menuju halaman registrasi", Toast.LENGTH_SHORT).show();
        });
    }
}