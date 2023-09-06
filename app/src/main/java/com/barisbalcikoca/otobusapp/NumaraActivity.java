package com.barisbalcikoca.otobusapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class NumaraActivity extends AppCompatActivity {
    private TextView txtHatNo;
    Intent gelenIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numara);


        initComponents();
        registerEventHandlers();

        // Intent'ten gönderilen veriyi alın
        gelenIntent= getIntent();
        if (gelenIntent != null) {
            String selectedValue = gelenIntent.getStringExtra("selectedValue");

            // Alınan veriyi txtHatNo TextView'ine yerleştirin
            if (selectedValue != null) {
                txtHatNo.setText(selectedValue);
            }
        }
    }


    private void initComponents() {
        txtHatNo = findViewById(R.id.txtHatNo);
    }

    private void registerEventHandlers() {

    }
}