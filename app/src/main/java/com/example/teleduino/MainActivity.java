package com.example.teleduino;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private BottomNavigationView bnv;
    String title = "";
    private static final int REQUEST_BLUETOOTH_SCAN = 1;

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
                selectedFragment = new BluetoothCommunications();
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
                    .replace(R.id.main_frame, new BluetoothCommunications())
                    .commit();
            getSupportActionBar().setTitle("Komunikasi Bluetooth");
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("device_address")){
            String message = intent.getStringExtra("device_address");

            Bundle bundle = new Bundle();
            bundle.putString("device_address", message);

            BluetoothCommunications fragmentBluetooth = new BluetoothCommunications();
            fragmentBluetooth.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new BluetoothCommunications()).commit();
        }
    }

}