package com.example.tcchortela;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword, tvNoAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvNoAccount = findViewById(R.id.tvNoAccount);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String email = etEmail.getText().toString().trim();

                // Verificar se os campos estão preenchidos
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email)) {
                    Toast.makeText(MainActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                } else {
                    // Realizar a lógica de autenticação
                    // Aqui você pode implementar a verificação do login com seu backend ou banco de dados

                    // Exemplo de lógica simples (verificação fictícia)
                    if (username.equals("user") && password.equals("password") && email.equals("user@example.com")) {
                        // Se o login for bem-sucedido, redirecionar para outra atividade
                        Intent intent = new Intent(MainActivity.this, TelaPrincipal.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Mostrar uma mensagem de erro se o login falhar
                        Toast.makeText(MainActivity.this, "Credenciais inválidas", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        tvNoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redireciona para a tela de cadastro
                Intent intent = new Intent(MainActivity.this, Cadastro.class);
                startActivity(intent);
            }
        });
        // Configurar o listener para o texto de "Esqueceu a senha?"
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirecionar para a atividade de recuperação de senha
                Intent intent = new Intent(MainActivity.this, RedefinirSenhaCodigo.class);
                startActivity(intent);
            }
        });
    }
}