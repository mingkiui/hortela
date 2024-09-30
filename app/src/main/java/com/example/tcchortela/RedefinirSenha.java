package com.example.tcchortela;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import com.google.android.material.snackbar.Snackbar;

public class RedefinirSenha extends AppCompatActivity {

    private EditText etNovaSenha, etRepetirNovaSenha;
    private Button btnRedefinirSenha;
    private ImageButton btnClose;
    private String[] mensagens = {"Preencha todos os campos", "Senha redefinida com sucesso! Volte para a tela de Login", "Erro ao redefinir senha", "Erro: Email não encontrado", "As senhas não coincidem"};
    private String email; // Adicione esta variável

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redefinir_main);

        IniciarComponentes();

        // Receber o email da Intent
        email = getIntent().getStringExtra("EMAIL");

        // Lógica para redefinir senha
        btnRedefinirSenha.setOnClickListener(v -> {
            String novaSenha = etNovaSenha.getText().toString();
            String repetirNovaSenha = etRepetirNovaSenha.getText().toString();
            validarEAtualizarSenha(novaSenha, repetirNovaSenha, v);
        });

        // Botão para fechar e voltar à tela de login
        btnClose.setOnClickListener(v -> {
            Intent intent = new Intent(RedefinirSenha.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void IniciarComponentes() {
        etNovaSenha = findViewById(R.id.etNovaSenha);
        etRepetirNovaSenha = findViewById(R.id.etRepetirNovaSenha);
        btnRedefinirSenha = findViewById(R.id.btnRedefinirSenha);
        btnClose = findViewById(R.id.btnClose);
    }

    private void validarEAtualizarSenha(String novaSenha, String repetirNovaSenha, View v) {
        // Verificar se os campos estão vazios
        if (novaSenha.isEmpty() || repetirNovaSenha.isEmpty()) {
            mostrarSnackbar(v, mensagens[0]);
            return;
        }

        // Verificar se as duas senhas são iguais
        if (!novaSenha.equals(repetirNovaSenha)) {
            mostrarSnackbar(v, mensagens[4]);
            return;
        }

        // Atualizar a senha no banco de dados
        if (email != null) {
            boolean sucesso = DatabaseHelper.redefinirSenha(email, novaSenha);

            if (sucesso) {
                mostrarSnackbar(v, mensagens[1]);
            } else {
                mostrarSnackbar(v, mensagens[2]);
            }
        } else {
            mostrarSnackbar(v, mensagens[3]);
        }
    }

    private void mostrarSnackbar(View v, String mensagem) {
        Snackbar snackbar = Snackbar.make(v, mensagem, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(Color.WHITE);
        snackbar.setTextColor(Color.BLACK);
        snackbar.show();
    }
}