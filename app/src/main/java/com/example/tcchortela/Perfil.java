package com.example.tcchortela;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class Perfil extends AppCompatActivity {

    private TextView nameValue, emailValue, passwordValue;
    private Button personalInformation, editExit;
    private ImageButton btnClose;
    private String userEmail;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_main);

        IniciarComponentes();

        // Recupera os dados passados pela tela anterior
        userEmail = getIntent().getStringExtra("email");
        userName = getIntent().getStringExtra("nome");

        // Define os valores dos campos na tela de perfil
        nameValue.setText(userName);
        emailValue.setText(userEmail);

        // Botão para fechar a tela e voltar para a tela principal
        btnClose.setOnClickListener(v -> {
            Intent intent = new Intent(Perfil.this, TelaPrincipal.class);
            intent.putExtra("email", userEmail);
            intent.putExtra("nome", userName);
            startActivity(intent);
        });

        // Botão de sair
        editExit.setOnClickListener(v -> {
            Intent intent = new Intent(Perfil.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        personalInformation.setOnClickListener(v -> {
            Intent intent = new Intent(Perfil.this, InformacoesP.class);
            startActivity(intent);
            finish();
        });
    }

    private void IniciarComponentes() {
        nameValue = findViewById(R.id.nameValue);
        emailValue = findViewById(R.id.emailValue);
        passwordValue = findViewById(R.id.passwordValue);
        personalInformation = findViewById(R.id.personalInformation);
        editExit = findViewById(R.id.editExit);
        btnClose = findViewById(R.id.btnClose);
    }
}
