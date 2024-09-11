package com.example.tcchortela;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Cadastro extends AppCompatActivity {

    private EditText etName, etPassword, etConfirmPass, etEmail;
    private Button btnRegister;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_main);  // Certifique-se de que o layout esteja correto

        etName = findViewById(R.id.etName1);
        etPassword = findViewById(R.id.etPassword1);
        etConfirmPass = findViewById(R.id.etConfirmPass1);
        etEmail = findViewById(R.id.etEmail1);
        btnRegister = findViewById(R.id.btnRegister);

        //dbHelper = new DatabaseHelper(this);

        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPass.getText().toString().trim();
            String email = etEmail.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email)) {
                Toast.makeText(Cadastro.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(Cadastro.this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
            } else {/*/
                // Chamando o método de registro da DatabaseHelper
                dbHelper.registerUser(name, password, email,
                        () -> {
                            // Sucesso no cadastro
                            Toast.makeText(Cadastro.this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Cadastro.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // Fecha a activity de cadastro
                        },
                        () -> {
                            // Falha no cadastro
                            Toast.makeText(Cadastro.this, "Erro ao cadastrar o usuário", Toast.LENGTH_SHORT).show();
                        }
                );
                /*/
            }
        });
    }
}
