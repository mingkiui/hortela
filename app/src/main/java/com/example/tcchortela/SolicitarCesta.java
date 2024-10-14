package com.example.tcchortela;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.google.android.material.snackbar.Snackbar;

public class SolicitarCesta extends AppCompatActivity {

    private RadioGroup radioGroup1, radioGroup2;
    private Button btnSend;
    private ImageButton btnClose;

    String[] mensagens = {"Infelizmente, você não entrou nos critérios"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.socilitarcesta);

        // Inicialização dos elementos da interface
        radioGroup1 = findViewById(R.id.radioGroup1Right);
        radioGroup2 = findViewById(R.id.radioGroup2);
        btnSend = findViewById(R.id.btnSend);
        btnClose = findViewById(R.id.btnClose);

        // Configuração do listener para o botão de enviar
        btnSend.setOnClickListener(v -> enviarRespostas(v));

        // Configuração do listener para o botão de fechar
        btnClose.setOnClickListener(v -> {
            Intent intent = new Intent(SolicitarCesta.this, TelaPrincipal.class);
            startActivity(intent);
            finish();
        });
    }

    private void enviarRespostas(View v) {
        // Captura as respostas selecionadas
        int selectedId1 = radioGroup1.getCheckedRadioButtonId();
        int selectedId2 = radioGroup2.getCheckedRadioButtonId();

        // Verifica se nada foi selecionado
        if (selectedId1 == -1 || selectedId2 == -1) {
            Snackbar snackbar = Snackbar.make(v, "Por favor, responda as perguntas", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();
            return; // Não redireciona
        }

        // Verifica as respostas
        boolean temUmaPessoa = (selectedId1 == R.id.rb1);
        boolean rendaAlta = (selectedId2 == R.id.rb7 || selectedId2 == R.id.rb8);

        // Condição para impedir o redirecionamento
        if (temUmaPessoa && rendaAlta) {
            Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();
            return; // Não redireciona
        } else {
            // Se a condição não for atendida, segue para a InscricaoBen.java
            Intent intent = new Intent(SolicitarCesta.this, InscricaoBen.class);
            startActivity(intent);
        }
    }
}
