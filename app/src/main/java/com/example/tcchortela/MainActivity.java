package com.example.tcchortela;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword, tvNoAccount;
    String[] mensagens = {"Preencha todos os campos", "Login efetuado com sucesso"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IniciarComponentes();

        tvNoAccount.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Cadastro.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String pass = etPassword.getText().toString();

            if (email.isEmpty() || pass.isEmpty()) {
                Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            } else {
                // Iniciar a autenticação do usuário
                autenticarUsuario(v, email, pass);
            }
        });

        // Integração do tvForgotPassword
        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RedefinirSenhaCodigo.class);
            startActivity(intent);
        });
    }

    // Método para iniciar os componentes da tela
    private void IniciarComponentes() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvNoAccount = findViewById(R.id.tvNoAccount);
    }

    // Autenticação do usuário usando ExecutorService
    private void autenticarUsuario(View v, String email, String pass) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            boolean loginSucesso = DatabaseHelper.loginUser(email, pass);

            handler.post(() -> {
                if (loginSucesso) {
                    // Login bem-sucedido, vá para a tela principal
                    TelaPrincipal();
                } else {
                    // Login falhou, mostre uma mensagem de erro
                    Snackbar snackbar = Snackbar.make(v, "E-mail ou Senha inválidos", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            });
        });
    }

    // Método para navegar para a tela principal após login bem-sucedido
    private void TelaPrincipal() {
        Intent intent = new Intent(MainActivity.this, TelaPrincipal.class);
        startActivity(intent);
        finish();
    }
}
