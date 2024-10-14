package com.example.tcchortela;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.View;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.sql.SQLException;

public class InformacoesP extends AppCompatActivity {

    private Button editProfileButton, editSave, editCancel;
    private EditText txtNome, txtTelefone, txtEndereco, txtCPF;
    private ImageView btnClose;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    private int userId;
    private String nomeOriginal, telefoneOriginal, enderecoOriginal, cpfOriginal; // Para armazenar os valores originais
    private String[] mensagem = {"Informações atualizadas com sucesso!", "Erro ao atualizar informações."};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informacoesp_main);

        // Inicialização dos componentes
        editProfileButton = findViewById(R.id.editProfileButton);
        editSave = findViewById(R.id.editSave);
        editCancel = findViewById(R.id.editCancel);
        txtNome = findViewById(R.id.txtNome);
        txtTelefone = findViewById(R.id.txtTelefone);
        txtCPF = findViewById(R.id.txtCPF);
        txtEndereco = findViewById(R.id.txtEndereco);
        btnClose = findViewById(R.id.btnClose);

        // Inicializa o DatabaseHelper e SharedPreferences
        databaseHelper = new DatabaseHelper();
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);

        // Carrega as informações do usuário
        carregarInformacoesDoUsuario();

        // Esconde os botões de salvar e cancelar inicialmente
        editSave.setVisibility(View.GONE);
        editCancel.setVisibility(View.GONE);

        // Configura o botão de edição
        editProfileButton.setOnClickListener(v -> {
            habilitarEdicao(true);
            editProfileButton.setVisibility(View.GONE); // Esconde o botão "Editar"
            editSave.setVisibility(View.VISIBLE); // Mostra o botão "Salvar"
            editCancel.setVisibility(View.VISIBLE); // Mostra o botão "Cancelar"
        });

        // Configura o botão de salvar
        editSave.setOnClickListener(v -> {
            salvarAlteracoes(v);
            editSave.setVisibility(View.GONE); // Esconde o botão "Salvar"
            editCancel.setVisibility(View.GONE); // Esconde o botão "Cancelar"
            editProfileButton.setVisibility(View.VISIBLE); // Mostra o botão "Editar" novamente
        });

        // Configura o botão de cancelar
        editCancel.setOnClickListener(v -> {
            cancelarEdicao(); // Desfaz as alterações e retorna os valores originais
            editSave.setVisibility(View.GONE); // Esconde o botão "Salvar"
            editCancel.setVisibility(View.GONE); // Esconde o botão "Cancelar"
            editProfileButton.setVisibility(View.VISIBLE); // Mostra o botão "Editar" novamente
        });

        // Configura o botão de fechar
        btnClose.setOnClickListener(v -> finish());
    }

    private void carregarInformacoesDoUsuario() {
        if (userId != -1) {
            try {
                String[] userInfo = databaseHelper.getUserInfo(userId);
                nomeOriginal = userInfo[0]; // Armazena os valores originais
                telefoneOriginal = userInfo[1];
                enderecoOriginal = userInfo[2];
                cpfOriginal = userInfo[3];

                txtNome.setText(nomeOriginal);
                txtTelefone.setText(telefoneOriginal);
                txtEndereco.setText(enderecoOriginal);
                txtCPF.setText(cpfOriginal);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void habilitarEdicao(boolean habilitar) {
        txtNome.setEnabled(habilitar);
        txtTelefone.setEnabled(habilitar);
        txtEndereco.setEnabled(habilitar);
        txtCPF.setEnabled(habilitar);
        editSave.setEnabled(habilitar);
        editCancel.setEnabled(habilitar);
    }

    private void salvarAlteracoes(View v) {
        Log.d("InformacoesP", "Entrou no método salvarAlteracoes()");

        if (userId != -1) {
            String nome = txtNome.getText().toString();
            String telefone = txtTelefone.getText().toString();
            String endereco = txtEndereco.getText().toString();
            String cpf = txtCPF.getText().toString();

            Log.d("InformacoesP", "Valores obtidos: Nome: " + nome + ", Telefone: " + telefone + ", Endereço: " + endereco + ", CPF: " + cpf);

            try {
                databaseHelper.updateUserInfo(userId, nome, telefone, endereco, cpf);
                Log.d("InformacoesP", "Dados atualizados com sucesso no banco de dados");
                Snackbar snackbar = Snackbar.make(v, mensagem[0], Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
                habilitarEdicao(false); // Desabilita edição após salvar
            } catch (SQLException e) {
                e.printStackTrace();
                Log.e("InformacoesP", "Erro ao atualizar informações: " + e.getMessage());
                Snackbar snackbar = Snackbar.make(v, mensagem[1], Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }
        } else {
            Log.e("InformacoesP", "userId não é válido!");
        }
    }

    private void cancelarEdicao() {
        // Restaura os valores originais
        txtNome.setText(nomeOriginal);
        txtTelefone.setText(telefoneOriginal);
        txtEndereco.setText(enderecoOriginal);
        txtCPF.setText(cpfOriginal);
        habilitarEdicao(false); // Desabilita a edição
    }
}
