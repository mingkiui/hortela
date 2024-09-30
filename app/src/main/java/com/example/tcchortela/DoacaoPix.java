package com.example.tcchortela;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.google.android.material.snackbar.Snackbar;

public class DoacaoPix extends AppCompatActivity {

    private ImageButton btnClose;
    private Button btnCopyPix;
    private TextView tvDuvidas;
    String[] mensagens = {"Chave Pix copiada para a área de transferência!"};
    private final String chavePix = "(11) 92345-6789";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doacaopix_main);

        btnClose = findViewById(R.id.btnClose);
        btnCopyPix = findViewById(R.id.btnCopyPix);
        tvDuvidas = findViewById(R.id.tvDuvidas);

        btnClose.setOnClickListener(v -> finish());

        btnCopyPix.setOnClickListener(v -> {
            // Copiando a chave Pix para o clipboard
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Chave Pix", chavePix);
            clipboard.setPrimaryClip(clip);

            // Exibir uma mensagem para o usuário
            Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();
        });

        tvDuvidas.setOnClickListener(v -> {
            Intent intent = new Intent(DoacaoPix.this, FaleConosco.class);
            startActivity(intent);
        });
    }
}
