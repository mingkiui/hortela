package com.example.tcchortela;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class ConsultarCesta extends AppCompatActivity {

    private ImageView btnClose;
    private TextView text_date, text_address_name;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences; // Para obter o userId

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultarcesta);

        btnClose = findViewById(R.id.btnClose);
        text_date = findViewById(R.id.text_date);
        text_address_name = findViewById(R.id.text_address_name);

        databaseHelper = new DatabaseHelper();
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1); // Recupera o userId armazenado

        if (userId != -1) {
            Object[] distInfo = databaseHelper.getCestaDistInfo(userId);

            if (distInfo != null) {
                String local = (String) distInfo[0];
                Date dataDist = (Date) distInfo[1];

                text_address_name.setText(local);
                text_date.setText(dataDist.toString()); // Formatar conforme necessário
            } else {
                text_date.setText("Data não encontrada");
                text_address_name.setText("Local não encontrado");
            }
        }

        btnClose.setOnClickListener(v -> {
            Intent intent = new Intent(ConsultarCesta.this, TelaPrincipalCarente.class);
            startActivity(intent);
        });
    }
}

