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

    private EditText etName, etEmail, etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword, tvNoAccount;
    private DatabaseHelper dbHelper; // Declare o DatabaseHelper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvNoAccount = findViewById(R.id.tvNoAccount);


        dbHelper = new DatabaseHelper(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String email = etEmail.getText().toString().trim();


                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email)) {
                    Toast.makeText(MainActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                } else {
                    // Verifique as credenciais no banco de dados
                    if (dbHelper.checkUser(name, password, email)) {
                        Intent intent = new Intent(MainActivity.this, TelaPrincipal.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Credenciais inválidas", Toast.LENGTH_SHORT).show();
                    }
                    };
            }
        });

        tvNoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Cadastro.class);
                startActivity(intent);
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RedefinirSenhaCodigo.class);
                startActivity(intent);
            }
        });
    }
}
