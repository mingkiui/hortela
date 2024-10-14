package com.example.tcchortela;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class InscricaoBen extends AppCompatActivity {

    private EditText etNameBen, etNumberBen, etEmailBen, etCpf;
    private Button btnNext;
    private ImageView btnClose;
    String[] mensagens  = {"Preencha todos os campos",
            "Inscrição realizada com sucesso!",
            "Erro ao verificar informações. Tente novamente",
            "Conta não cadastrada nos nossos servidores" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inscricaoben_main);

        // Inicializa os componentes
        etNameBen = findViewById(R.id.etNameben);
        etNumberBen = findViewById(R.id.etNumberBen);
        etEmailBen = findViewById(R.id.etEmailBen);
        etCpf = findViewById(R.id.etCpf);
        btnNext = findViewById(R.id.btnNext);
        btnClose = findViewById(R.id.btnClose);

        // Botão de fechar
        btnClose.setOnClickListener(v -> finish());

        // Botão Próximo
        btnNext.setOnClickListener(v -> {
            String nome = etNameBen.getText().toString().trim();
            String telefone = etNumberBen.getText().toString().trim();
            String email = etEmailBen.getText().toString().trim();
            String cpf = etCpf.getText().toString().trim();

            if ( nome.isEmpty() || telefone.isEmpty() ||email.isEmpty() || cpf.isEmpty()) {
                Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            } else {
                verificarEmailEAtualizar(v); // Chame a função aqui ou autentique, conforme a lógica desejada.
            }
        });
    }

    private void verificarEmailEAtualizar(View v) {
        String nome = etNameBen.getText().toString().trim();
        String telefone = etNumberBen.getText().toString().trim();
        String email = etEmailBen.getText().toString().trim();
        String cpf = etCpf.getText().toString().trim();

        // Verifica se o e-mail está cadastrado
        if (DatabaseHelper.isEmailRegistered(email)) {
            // Atualiza as informações do beneficiário
            if (DatabaseHelper.updateBeneficiaryInfo(email, nome, telefone, cpf)) {
                // Atualiza o nível de acesso para 1
                if (DatabaseHelper.updateAccessLevel(email, 1)) {
                    hideKeyboard(v);
                    Snackbar snackbar = Snackbar.make(v, mensagens[1], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    Intent intent = new Intent(InscricaoBen.this, TelaPrincipalCarente.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                hideKeyboard(v);
                Snackbar snackbar = Snackbar.make(v, mensagens[2], Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }
        } else {
            hideKeyboard(v);
            Snackbar snackbar = Snackbar.make(v, mensagens[3], Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();
        }
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
