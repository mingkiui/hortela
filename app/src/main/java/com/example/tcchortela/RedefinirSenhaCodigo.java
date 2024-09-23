package com.example.tcchortela;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class RedefinirSenhaCodigo extends AppCompatActivity {

    /*/private EditText etDigit1, etDigit2, etDigit3, etDigit4, editEmail;
    private Button btnResendCode, btnResetPassword;
    private TextView tvCodeSent, tvAccountNotFound;

    private String generatedCode;  // Código gerado
    private String userEmail;  // Email do usuário

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

        // Gerar e enviar o código ao clicar em "Enviar código"
        btnResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail = editEmail.getText().toString().trim();
                if (!userEmail.isEmpty()) {
                    // Gerar um código de 4 dígitos
                    generatedCode = generateCode();
                    // Enviar o e-mail com o código
                    try {
                        MailjetEmailSender.sendEmail(userEmail, generatedCode);
                        // Atualizar visibilidade e informar o envio
                        tvCodeSent.setText("*Foi enviado um código de confirmação para o e-mail " + userEmail);
                        tvCodeSent.setVisibility(View.VISIBLE);
                        tvAccountNotFound.setVisibility(View.GONE);
                        Toast.makeText(RedefinirSenhaCodigo.this, "Código enviado para " + userEmail, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(RedefinirSenhaCodigo.this, "Erro ao enviar o e-mail.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RedefinirSenhaCodigo.this, "Por favor, insira um e-mail", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Verificar o código ao clicar em "Redefinir Senha"
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredCode = etDigit1.getText().toString() + etDigit2.getText().toString() +
                        etDigit3.getText().toString() + etDigit4.getText().toString();

                if (generatedCode != null && enteredCode.equals(generatedCode)) {
                    // Código correto, navegar para tela de redefinir senha
                    Intent intent = new Intent(RedefinirSenhaCodigo.this, RedefinirSenha.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(RedefinirSenhaCodigo.this, "Código incorreto, tente novamente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Função para gerar um código de 4 dígitos
    private String generateCode() {
        Random random = new Random();
        int code = random.nextInt(9000) + 1000;  // Gera um número entre 1000 e 9999
        return String.valueOf(code);
    }/*/
}
