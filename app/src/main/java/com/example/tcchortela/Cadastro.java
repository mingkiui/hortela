package com.example.tcchortela;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;

public class Cadastro extends AppCompatActivity {

    private EditText etName1, etEmail1, etPassword1, etConfirmPass1;
    private Button btnRegister;
    private ImageButton btnClose;
    private String[] mensagens = {"Preencha todos os campos", "Cadastro realizado com sucesso", "Erro ao cadastrar usuário"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_main);

        IniciarComponentes();

        btnRegister.setOnClickListener(v -> {
            String nome = etName1.getText().toString();
            String email = etEmail1.getText().toString();
            String senha = etPassword1.getText().toString();
            String confirmSenha = etConfirmPass1.getText().toString();

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmSenha.isEmpty()) {
                hideKeyboard(v);
                Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            } else if (!senha.equals(confirmSenha)) {
                hideKeyboard(v);
                Snackbar snackbar = Snackbar.make(v, "As senhas não coincidem", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            } else {
                CadastrarUsuario(nome, email, senha, v);
            }
        });

        btnClose.setOnClickListener(v -> {
            Intent intent = new Intent(Cadastro.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void CadastrarUsuario(String nome, String email, String senha, View v) {
        boolean sucesso = DatabaseHelper.registerUser(nome, email, senha);

        if (sucesso) {
            hideKeyboard(v);
            Snackbar snackbar = Snackbar.make(v, mensagens[1], Snackbar.LENGTH_SHORT);
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

    private void IniciarComponentes() {
        etName1 = findViewById(R.id.etName1);
        etEmail1 = findViewById(R.id.etEmail1);
        etPassword1 = findViewById(R.id.etPassword1);
        etConfirmPass1 = findViewById(R.id.etConfirmPass1);
        btnRegister = findViewById(R.id.btnRegister);
        btnClose = findViewById(R.id.btnClose);
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

