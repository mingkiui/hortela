package com.example.tcchortela;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TelaPrincipal extends AppCompatActivity {

    private ImageView icAcessarConta, icPedirCesta;
    private TextView tvAcessarConta, tvPedirCesta, tvDoacao, tvAjuda, tvInscVol;
    private String userEmail;
    private String userNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telaprincipal_main);

        // Recupera os dados passados pela MainActivity
        userEmail = getIntent().getStringExtra("email");
        userNome = getIntent().getStringExtra("nome");

        icAcessarConta = findViewById(R.id.icAcessarConta);
        icPedirCesta = findViewById(R.id.icPedirCesta);
        tvAcessarConta = findViewById(R.id.tvAcessarConta);
        tvPedirCesta = findViewById(R.id.tvPedirCesta);
        tvDoacao = findViewById(R.id.tvDoacao);
        tvAjuda = findViewById(R.id.tvAjuda);
        tvInscVol = findViewById(R.id.tvInscVol);

        View.OnClickListener acessarContaListener = v -> {
            Intent intent = new Intent(TelaPrincipal.this, Perfil.class);
            intent.putExtra("email", userEmail); // Passa o e-mail do usuário para a tela de perfil
            intent.putExtra("nome", userNome);  // Passa o nome do usuário para a tela de perfil
            startActivity(intent);
        };

        icAcessarConta.setOnClickListener(acessarContaListener);
        tvAcessarConta.setOnClickListener(acessarContaListener);

        View.OnClickListener pedirCestaListener = v -> {
            Intent intent = new Intent(TelaPrincipal.this, SolicitarCesta.class);
            startActivity(intent);
            finish();
        };
        icPedirCesta.setOnClickListener(pedirCestaListener);
        tvPedirCesta.setOnClickListener(pedirCestaListener);

        tvDoacao.setOnClickListener(v -> {
            Intent intent = new Intent(TelaPrincipal.this, DoacaoPix.class);
            startActivity(intent);
        });

        tvAjuda.setOnClickListener(v -> {
            Intent intent = new Intent(TelaPrincipal.this, FaleConosco.class);
            startActivity(intent);
        });

        tvInscVol.setOnClickListener(v -> {
            Intent intent = new Intent(TelaPrincipal.this, InscricaoVol.class);
            startActivity(intent);
        });
    }
}
