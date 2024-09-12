package com.example.tcchortela;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tcchortela.FaleConosco;
import com.example.tcchortela.Perfil;
import com.example.tcchortela.RealizarDoacao;
import com.example.tcchortela.SolicitarCesta;

public class TelaPrincipal extends AppCompatActivity {

    private ImageView icAcessarConta, icPedirCesta;
    private TextView tvAcessarConta, tvPedirCesta, tvDoacao, tvAjuda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telaprincipal_main); // Referência ao layout XML

        // Inicialização dos componentes da tela
        icAcessarConta = findViewById(R.id.icAcessarConta);
        icPedirCesta = findViewById(R.id.icPedirCesta);
        tvAcessarConta = findViewById(R.id.tvAcessarConta);
        tvPedirCesta = findViewById(R.id.tvPedirCesta);
        tvDoacao = findViewById(R.id.tvDoacao);
        tvAjuda = findViewById(R.id.tvAjuda);

        // Configuração dos cliques para acessar a conta
        View.OnClickListener acessarContaListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaPrincipal.this, Perfil.class);
                startActivity(intent);
            }
        };
        icAcessarConta.setOnClickListener(acessarContaListener);
        tvAcessarConta.setOnClickListener(acessarContaListener);

        // Configuração dos cliques para pedir cesta
        View.OnClickListener pedirCestaListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaPrincipal.this, SolicitarCesta.class);
                startActivity(intent);
                finish();
            }
        };
        icPedirCesta.setOnClickListener(pedirCestaListener);
        tvPedirCesta.setOnClickListener(pedirCestaListener);

        // Clique para fazer doação
        tvDoacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Exemplo de ação para a doação
                Intent intent = new Intent(TelaPrincipal.this, RealizarDoacao.class);
                startActivity(intent);
            }
        });

        // Clique para ajuda
        tvAjuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Exemplo de ação para ajuda
                Intent intent = new Intent(TelaPrincipal.this, FaleConosco.class);
                startActivity(intent);
            }
        });
    }
}
