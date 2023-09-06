package com.barisbalcikoca.otobusapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class GuzergahActivity extends AppCompatActivity {

    private TextView txtGuzergah;
    Intent gelenIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guzergah);

        initComponents();
        registerEventHandlers();

        gelenIntent= getIntent();
        if (gelenIntent != null) {
            String selectedValue = gelenIntent.getStringExtra("selectedValue");

            // Alınan veriyi txtHatNo TextView'ine yerleştirin
            if (selectedValue != null) {
                txtGuzergah.setText(selectedValue);
            }
        }

    }

    private void initComponents() {
        txtGuzergah = findViewById(R.id.txtGuzergah);
    }

    private void registerEventHandlers() {

    }
}