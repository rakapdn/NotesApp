package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment {

    private TextView usernameTextView, emailTextView;
    private MaterialButton logoutButton;
    private FirebaseAuth mAuth;
    private DatabaseReference database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Inisialisasi komponen
        usernameTextView = view.findViewById(R.id.username_text);
        emailTextView = view.findViewById(R.id.email_text);
        logoutButton = view.findViewById(R.id.logout_button);

        // Inisialisasi Firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("users");

        // Ambil data pengguna
        String userId = mAuth.getCurrentUser().getUid();
        database.child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                DataSnapshot snapshot = task.getResult();
                String username = snapshot.child("username").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);
                usernameTextView.setText("Username: " + (username != null ? username : "Tidak tersedia"));
                emailTextView.setText("Email: " + (email != null ? email : "Tidak tersedia"));
                Log.d("ProfileFragment", "Username: " + username + ", Email: " + email);
            } else {
                Toast.makeText(getContext(), "Gagal memuat data pengguna", Toast.LENGTH_LONG).show();
                Log.e("ProfileFragment", "Database error: " + (task.getException() != null ? task.getException().getMessage() : "Data tidak ditemukan"));
            }
        });

        // Aksi tombol logout
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(getContext(), "Berhasil keluar", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        return view;
    }
}