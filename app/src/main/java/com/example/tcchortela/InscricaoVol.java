package com.example.tcchortela;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;

public class InscricaoVol extends AppCompatActivity {

    private ImageView btnClose;
    private EditText etNameVol, etNumberVol, etEmailVol;
    private Button btnSend;

    String[] mensagens = {
            "Preencha todos os campos",
            "Inscrição realizada com sucesso!",
            "Erro ao verificar informações. Tente novamente",
            "Conta não cadastrada nos nossos servidores"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inscricaovol_main);

        btnClose = findViewById(R.id.btnClose);
        etNameVol = findViewById(R.id.etNameVol);
        etNumberVol = findViewById(R.id.etNumberVol);
        etEmailVol = findViewById(R.id.etEmailVol);
        btnSend = findViewById(R.id.btnSend);

        btnClose.setOnClickListener(v -> finish());

        btnSend.setOnClickListener(v -> {
            String nome = etNameVol.getText().toString().trim();
            String telefone = etNumberVol.getText().toString().trim();
            String email = etEmailVol.getText().toString().trim();

            if (nome.isEmpty() || telefone.isEmpty() || email.isEmpty()) {
                Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            } else {
                // Atualizar telefone do usuário
                boolean telefoneAtualizado = DatabaseHelper.getInstance().atualizarTelefoneUsuario(email, telefone);

                // Adicionar usuário como voluntário
                boolean voluntarioAdicionado = DatabaseHelper.getInstance().adicionarVoluntario(email);

                if (telefoneAtualizado && voluntarioAdicionado) {
                    hideKeyboard(v);
                    Snackbar snackbar = Snackbar.make(v, mensagens[1], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();

                    Intent intent = new Intent(InscricaoVol.this, TelaPrincipalVol.class);
                    startActivity(intent);
                    finish();

                } else if (!telefoneAtualizado) {
                    hideKeyboard(v);
                    Snackbar snackbar = Snackbar.make(v, mensagens[3], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                } else {
                    hideKeyboard(v);
                    Snackbar snackbar = Snackbar.make(v, mensagens[2], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
