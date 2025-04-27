package com.example.notesapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private CardView bottomNavCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Periksa apakah pengguna sudah login
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        // Inisialisasi CardView
        bottomNavCard = findViewById(R.id.bottom_nav_card);
        Log.d(TAG, "BottomNavCard initialized: " + (bottomNavCard != null));

        // Inisialisasi Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int backgroundColor;

            // Log ID item yang dipilih
            Log.d(TAG, "Selected item ID: " + item.getItemId());

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
                backgroundColor = Color.parseColor("#FFFFFF"); // Putih untuk HomeFragment
                Log.d(TAG, "Navigating to HomeFragment, CardView color: #FFFFFF");
            } else if (item.getItemId() == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
                backgroundColor = Color.parseColor("#FFFFFF"); // Putih untuk ProfileFragment
                Log.d(TAG, "Navigating to ProfileFragment, CardView color: #FFFFFF");
            } else if (item.getItemId() == R.id.nav_add_note) {
                selectedFragment = new AddNoteFragment();
                backgroundColor = Color.parseColor("#121212"); // Hitam untuk AddNoteFragment
                Log.d(TAG, "Navigating to AddNoteFragment, CardView color: #121212");
            } else {
                Log.d(TAG, "Unknown item ID: " + item.getItemId());
                return false;
            }

            if (selectedFragment != null) {
                // Ubah latar belakang CardView
                bottomNavCard.setCardBackgroundColor(backgroundColor);

                // Ganti fragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }

            return true;
        });

        // Set Fragment default saat aplikasi dibuka
        bottomNavCard.setCardBackgroundColor(Color.parseColor("#FFFFFF")); // Default putih
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();
        Log.d(TAG, "Default fragment set to HomeFragment, CardView color: #FFFFFF");
    }
}