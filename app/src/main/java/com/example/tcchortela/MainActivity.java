package com.example.tcchortela;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword, tvNoAccount;
    private SharedPreferences sharedPreferences;
    String[] mensagens = {"Preencha todos os campos", "E-mail ou senha inválidos"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verifica se o usuário já está logado
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            String email = sharedPreferences.getString("email", "");
            int accessLevel = sharedPreferences.getInt("accessLevel", -1);
            abrirTelaPrincipal(email, accessLevel);
            return; // Impede que a tela de login seja carregada novamente
        }

        setContentView(R.layout.activity_main);
        iniciarComponentes();

        tvNoAccount.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Cadastro.class);
            startActivity(intent);
        });

        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RedefinirSenhaCodigo.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                hideKeyboard(v);
                Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            } else {
                autenticarUsuario(v, email, password);
            }
        });

    }

    private void iniciarComponentes() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvNoAccount = findViewById(R.id.tvNoAccount);
    }

    private void autenticarUsuario(View v, String email, String pass) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            DatabaseHelper databaseHelper = new DatabaseHelper();
            Object[] loginResult = databaseHelper.loginUser(email, pass); // Modificado para retornar um array de objetos

            handler.post(() -> {
                int accessLevel = (int) loginResult[1]; // Acessa o accessLevel
                int userId = (int) loginResult[0]; // Acessa o userId

                if (accessLevel == -1) {
                    hideKeyboard(v);
                    Snackbar snackbar = Snackbar.make(v, mensagens[1], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                } else {
                    Object[] userData = databaseHelper.getUserDataByEmail(email);

                    if (userData != null && userData.length >= 3) {
                        String nome = (String) userData[0];
                        String userEmail = (String) userData[1];

                        // Salvar o estado de login e detalhes do usuário
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("email", userEmail);
                        editor.putString("nome", nome);
                        editor.putInt("accessLevel", accessLevel);
                        editor.putInt("userId", userId); // Armazena o userId
                        editor.apply();

                        abrirTelaPrincipal(userEmail, accessLevel);
                    } else {
                        Snackbar snackbar = Snackbar.make(v, "Erro ao obter dados do usuário", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    }
                }
            });
        });
    }


    private void abrirTelaPrincipal(String email, int accessLevel) {
        Intent intent = null;

        if (accessLevel == 0) {
            intent = new Intent(MainActivity.this, TelaPrincipal.class);
        } else if (accessLevel == 1) {
            intent = new Intent(MainActivity.this, TelaPrincipalCarente.class);
        } else if (accessLevel == 2) {
            intent = new Intent(MainActivity.this, TelaPrincipalVol.class);
        }

        if (intent != null) {
            intent.putExtra("email", email);  // Passa o e-mail do usuário
            startActivity(intent);
            finish();
        }
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
