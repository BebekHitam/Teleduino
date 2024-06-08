package com.example.teleduino;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private BottomNavigationView bnv;
    String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bnv = findViewById(R.id.bottom_navigation);
        bnv.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            String title = null;  // Initialize title to avoid potential null warnings

            int itemId = item.getItemId();

            if (itemId == R.id.bluetooth_navigation){
                selectedFragment = new bluetoothCommunications();
                getSupportActionBar().setTitle("Komunikasi Bluetooth");
                    // Handle potential default case (optional)
            }
            if (itemId == R.id.wifi_navigation){
                selectedFragment = new wifiCommunications();
                getSupportActionBar().setTitle("Komunikasi Wifi");
                // Handle potential default case (optional)
            }

            if (selectedFragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                transaction.replace(R.id.main_frame, selectedFragment);
                transaction.commit();
            }

            if (title != null) {  // Check if title is set before updating
                getSupportActionBar().setTitle(title);
            }

            return true;
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_frame, new bluetoothCommunications())
                    .commit();
            getSupportActionBar().setTitle("Komunikasi Bluetooth");
        }
    }

}