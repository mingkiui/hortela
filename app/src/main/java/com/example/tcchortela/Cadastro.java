package com.example.tcchortela;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Cadastro extends AppCompatActivity {

    private EditText etUsername1, etPassword1, etConfirmPass1, etEmail1;
    private Button btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_main);

        etUsername1 = findViewById(R.id.etUsername1);
        etPassword1 = findViewById(R.id.etPassword1);
        etConfirmPass1 = findViewById(R.id.etConfirmPass1);
        etEmail1 = findViewById(R.id.etEmail1);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeUsuario = etUsername1.getText().toString().trim();
                String senha = etPassword1.getText().toString().trim();
                String repetirSenha = etConfirmPass1.getText().toString().trim();
                String email = etEmail1.getText().toString().trim();

                // Verificação básica para validar os campos
                if (nomeUsuario.isEmpty() || senha.isEmpty() || repetirSenha.isEmpty() || email.isEmpty()) {
                    Toast.makeText(Cadastro.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                } else if (!senha.equals(repetirSenha)) {
                    Toast.makeText(Cadastro.this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
                } else {
                    // Se todos os campos estiverem válidos, podemos prosseguir com o cadastro
                    // Aqui você pode adicionar a lógica para enviar os dados ao backend
                    realizarCadastro(nomeUsuario, senha, email);
                }
            }
        });
    }

    private void realizarCadastro(String nomeUsuario, String senha, String email) {
        // Implemente aqui a lógica de comunicação com o backend para o cadastro

        // Exemplo de sucesso:
        Toast.makeText(Cadastro.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();

        // Após o cadastro, você pode redirecionar para a tela de login ou tela principal
        Intent intent = new Intent(Cadastro.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}