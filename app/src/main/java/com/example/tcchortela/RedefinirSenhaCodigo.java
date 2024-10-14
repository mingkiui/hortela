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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.Random;

public class RedefinirSenhaCodigo extends AppCompatActivity {

    private EditText etDigit1, etDigit2, etDigit3, etDigit4, editEmail;
    private Button btnResendCode, btnResetPassword;
    private ImageButton btnClose;
    private TextView tvCodeSent, tvAccountNotFound;

    private String generatedCode;  // Código gerado
    private String userEmail;  // Email do usuário
    String [] mensagem = {"Por favor, insira um e-mail", "Código incorreto, tente novamente", "Conta não cadastrada nos nossos servidores", "Por favor, insira um código"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redefinircod_main);

        // Ligando os elementos da interface aos IDs
        etDigit1 = findViewById(R.id.etDigit1);
        etDigit2 = findViewById(R.id.etDigit2);
        etDigit3 = findViewById(R.id.etDigit3);
        etDigit4 = findViewById(R.id.etDigit4);
        editEmail = findViewById(R.id.editEmail);
        btnResendCode = findViewById(R.id.btnResendCode);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        tvCodeSent = findViewById(R.id.tvCodeSent);
        tvAccountNotFound = findViewById(R.id.tvAccountNotFound);
        btnClose = findViewById(R.id.btnClose);

        // Gerar e enviar o código ao clicar em "Enviar código"
        btnResendCode.setOnClickListener(v -> {
            userEmail = editEmail.getText().toString().trim();
            if (!userEmail.isEmpty()) {

                // Verificar se o email está registrado usando isEmailRegistered
                if (DatabaseHelper.isEmailRegistered(userEmail)) {
                    // Gerar um código de 4 dígitos
                    generatedCode = generateCode();
                    // Enviar o e-mail com o código usando Sendinblue
                    SendinblueEmailSender.sendEmail(userEmail, generatedCode);

                    // Atualizar visibilidade e informar o envio
                    tvCodeSent.setText("*Foi enviado um código de confirmação para o e-mail " + userEmail);
                    tvCodeSent.setVisibility(View.VISIBLE);
                    tvAccountNotFound.setVisibility(View.GONE);
                    Toast.makeText(RedefinirSenhaCodigo.this, "Código enviado para " + userEmail, Toast.LENGTH_SHORT).show();
                } else {
                    // Email não encontrado, exibir mensagem de erro
                    hideKeyboard(v);
                    tvCodeSent.setVisibility(View.GONE);
                    tvAccountNotFound.setText("Conta não cadastrada nos nossos servidores");
                    tvAccountNotFound.setVisibility(View.VISIBLE);
                    Snackbar snackbar = Snackbar.make(v, mensagem[2], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            } else {
                hideKeyboard(v);
                Snackbar snackbar = Snackbar.make(v, mensagem[0], Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }
        });


        // Verificar o código ao clicar em "Redefinir Senha"
        btnResetPassword.setOnClickListener(v -> {
            String enteredCode = etDigit1.getText().toString() + etDigit2.getText().toString() +
                    etDigit3.getText().toString() + etDigit4.getText().toString();
            userEmail = editEmail.getText().toString().trim();

            if (enteredCode.isEmpty()) {
                // Se o código estiver vazio, exibe a mensagem de "insira um código"
                Snackbar snackbar = Snackbar.make(v, "Por favor, insira um código", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            } else {
                // Verifica se o código inserido está correto
                if (enteredCode.equals(generatedCode)) {
                    hideKeyboard(v);
                    Intent intent = new Intent(RedefinirSenhaCodigo.this, RedefinirSenha.class);
                    intent.putExtra("EMAIL", userEmail);
                    startActivity(intent);
                } else {
                    // Se o código estiver incorreto, exibe a mensagem apropriada
                    Snackbar snackbar = Snackbar.make(v, mensagem[1], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });
        

        btnClose.setOnClickListener(v -> {
            finish(); // Fecha a atividade atual
        });
    }

    // Função para gerar um código de 4 dígitos
    private String generateCode() {
        Random random = new Random();
        int code = random.nextInt(9000) + 1000;  // Gera um número entre 1000 e 9999
        return String.valueOf(code);
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
