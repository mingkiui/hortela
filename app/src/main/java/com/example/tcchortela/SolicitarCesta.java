package com.example.tcchortela;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SolicitarCesta extends AppCompatActivity {

    private RadioGroup radioGroup1, radioGroup2, radioGroup3;
    private Button btnSend;
    private ImageButton btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.socilitarcesta);

        // Inicialização dos elementos da interface
        radioGroup1 = findViewById(R.id.radioGroup1Right);
        radioGroup2 = findViewById(R.id.radioGroup2);
        radioGroup3 = findViewById(R.id.radioGroup3Left); // Ajuste se necessário
        btnSend = findViewById(R.id.btnSend);
        btnClose = findViewById(R.id.btnClose);

        // Configuração do listener para o botão de enviar
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarRespostas();
            }
        });

        // Configuração do listener para o botão de fechar
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Fecha a atividade atual
            }
        });
    }

    private void enviarRespostas() {
        // Captura as respostas selecionadas
        int selectedId1 = radioGroup1.getCheckedRadioButtonId();
        int selectedId2 = radioGroup2.getCheckedRadioButtonId();

        // Verifica as respostas
        boolean temUmaPessoa = (selectedId1 == R.id.rb1); // Se "a) 1" está selecionado
        boolean rendaAlta = (selectedId2 == R.id.rb7 || selectedId2 == R.id.rb8); // "c" ou "d" estão selecionados

        // Condição para impedir o redirecionamento
        if (temUmaPessoa && rendaAlta) {
            Toast.makeText(this, "Você não pode se inscrever.", Toast.LENGTH_LONG).show();
            return; // Não redireciona
        }

        // Se a condição não for atendida, segue para a InscricaoBen.java
        Intent intent = new Intent(SolicitarCesta.this, InscricaoBen.class);
        startActivity(intent);
    }
}
