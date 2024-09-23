package com.example.tcchortela;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TelaPrincipalCarente extends AppCompatActivity {

    private ImageView icAcessarConta, icConsultarCesta;
    private TextView tvAcessarConta, tvConsultarCesta, tvDoacao, tvAjuda;
    private String userEmail;
    private String userNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telaprincipalcar);

        // Recupera os dados passados pela MainActivity
        userEmail = getIntent().getStringExtra("email");
        userNome = getIntent().getStringExtra("nome");

        icAcessarConta = findViewById(R.id.icAcessarConta);
        icConsultarCesta = findViewById(R.id.icConsultarCesta);
        tvAcessarConta = findViewById(R.id.tvAcessarConta);
        tvConsultarCesta = findViewById(R.id.tvConsultarCesta);
        tvDoacao = findViewById(R.id.tvDoacao);
        tvAjuda = findViewById(R.id.tvAjuda);

        View.OnClickListener acessarContaListener = v -> {
            Intent intent = new Intent(TelaPrincipalCarente.this, Perfil.class);
            intent.putExtra("email", userEmail); // Passa o e-mail do usuário para a tela de perfil
            intent.putExtra("nome", userNome);  // Passa o nome do usuário para a tela de perfil
            startActivity(intent);
        };

        icAcessarConta.setOnClickListener(acessarContaListener);
        tvAcessarConta.setOnClickListener(acessarContaListener);

        View.OnClickListener pedirCestaListener = v -> {
            Intent intent = new Intent(TelaPrincipalCarente.this, ConsultarCesta.class);
            startActivity(intent);
            finish();
        };
        icConsultarCesta.setOnClickListener(pedirCestaListener);
        tvConsultarCesta.setOnClickListener(pedirCestaListener);

        tvDoacao.setOnClickListener(v -> {
            Intent intent = new Intent(TelaPrincipalCarente.this, RealizarDoacao.class);
            startActivity(intent);
        });

        tvAjuda.setOnClickListener(v -> {
            Intent intent = new Intent(TelaPrincipalCarente.this, FaleConosco.class);
            startActivity(intent);
        });
    }
}
