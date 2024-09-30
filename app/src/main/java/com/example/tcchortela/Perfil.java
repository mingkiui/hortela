package com.example.tcchortela;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

public class Perfil extends AppCompatActivity {

    private TextView nameValue, emailValue;
    private Button personalInformation, editExit, editdrop;
    private ImageView btnClose;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_main);

        // Inicializa os componentes da interface
        IniciarComponentes();

        // Recupera os dados armazenados no SharedPreferences
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", "");
        String userName = sharedPreferences.getString("nome", "Usuário");

        // Define os valores dos campos de nome e email
        nameValue.setText(userName);
        emailValue.setText(userEmail);

        btnClose.setOnClickListener(v -> {
            finish(); // Fecha a atividade atual
        });


        // Botão para deslogar e ir para a tela de login
        editExit.setOnClickListener(v -> {
            // Limpar os dados do SharedPreferences para efetuar logout
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear(); // Limpa todos os dados
            editor.apply();

            // Voltar para a tela de login
            Intent intent = new Intent(Perfil.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Botão para ir para a tela de informações pessoais
        personalInformation.setOnClickListener(v -> {
            Intent intent = new Intent(Perfil.this, InformacoesP.class);
            startActivity(intent);
            finish();
        });

        // Botão para apagar a conta
        editdrop.setOnClickListener(v -> {
            // Utilize a variável userEmail já definida no início
            confirmarExclusaoConta(sharedPreferences.getString("email", ""));
        });
    }

    // Método para confirmar a exclusão da conta
    private void confirmarExclusaoConta(String email) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar Exclusão")
                .setMessage("Tem certeza que deseja excluir sua conta?")
                .setPositiveButton("Sim", (dialog, which) -> apagarConta(email))
                .setNegativeButton("Não", (dialog, which) -> dialog.dismiss())
                .show();
    }

    // Método para apagar a conta
    private void apagarConta(String email) {
        // Chama o método do DatabaseHelper para apagar a conta
        if (DatabaseHelper.deleteUserAccount(email)) {
            // Limpa os dados do SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            // Volta para a tela de login
            Intent intent = new Intent(Perfil.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Erro ao apagar conta. Tente novamente.", Toast.LENGTH_SHORT).show();
        }
    }

    // Inicializa os componentes da interface
    private void IniciarComponentes() {
        nameValue = findViewById(R.id.nameValue);
        emailValue = findViewById(R.id.emailValue);
        personalInformation = findViewById(R.id.personalInformation);
        editExit = findViewById(R.id.editExit);
        btnClose = findViewById(R.id.btnClose);
        editdrop = findViewById(R.id.editdrop);
    }
}
