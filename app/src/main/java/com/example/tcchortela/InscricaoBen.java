package com.example.tcchortela;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class InscricaoBen extends AppCompatActivity {

    private EditText etNameBen, etNumberBen, etEmailBen, etCpf;
    private Button btnNext;
    private ImageView btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inscricaoben_main);

        // Inicializa os componentes
        etNameBen = findViewById(R.id.etNameben);
        etNumberBen = findViewById(R.id.etNumberBen);
        etEmailBen = findViewById(R.id.etEmailBen);
        etCpf = findViewById(R.id.etCpf);
        btnNext = findViewById(R.id.btnNext);
        btnClose = findViewById(R.id.btnClose);

        // Botão de fechar
        btnClose.setOnClickListener(v -> finish());

        // Botão Próximo
        btnNext.setOnClickListener(v -> verificarEmailEAtualizar());
    }

    private void verificarEmailEAtualizar() {
        String nome = etNameBen.getText().toString().trim();
        String telefone = etNumberBen.getText().toString().trim();
        String email = etEmailBen.getText().toString().trim();
        String cpf = etCpf.getText().toString().trim();

        // Verifica se o e-mail está cadastrado
        if (DatabaseHelper.isEmailRegistered(email)) {
            // Atualiza as informações do beneficiário
            if (DatabaseHelper.updateBeneficiaryInfo(email, nome, telefone, cpf)) {
                // Atualiza o nível de acesso para 1
                if (DatabaseHelper.updateAccessLevel(email, 1)) {
                    Toast.makeText(this, "Inscrição realizada com sucesso!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(InscricaoBen.this, TelaPrincipalCarente.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                Toast.makeText(this, "Erro ao verificar informações. Tente novamente.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Conta não cadastrada nos nossos servidores.", Toast.LENGTH_SHORT).show();
        }
    }
}
