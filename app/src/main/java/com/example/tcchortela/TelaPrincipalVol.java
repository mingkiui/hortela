package com.example.tcchortela;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TelaPrincipalVol extends AppCompatActivity {

    private ImageView icAcessarConta, icCalendario;
    private TextView tvAcessarConta, tvCalendario, tvDoacao, tvAjuda;
    private String userEmail;
    private String userNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telaprincipalvol);

        // Recupera os dados passados pela MainActivity
        userEmail = getIntent().getStringExtra("email");
        userNome = getIntent().getStringExtra("nome");

        icAcessarConta = findViewById(R.id.icAcessarConta);
        icCalendario = findViewById(R.id.icCalendario);
        tvAcessarConta = findViewById(R.id.tvAcessarConta);
        tvCalendario = findViewById(R.id.tvCalendario);
        tvDoacao = findViewById(R.id.tvDoacao);
        tvAjuda = findViewById(R.id.tvAjuda);

        View.OnClickListener acessarContaListener = v -> {
            Intent intent = new Intent(TelaPrincipalVol.this, Perfil.class);
            intent.putExtra("email", userEmail); // Passa o e-mail do usuário para a tela de perfil
            intent.putExtra("nome", userNome);  // Passa o nome do usuário para a tela de perfil
            startActivity(intent);
        };

        icAcessarConta.setOnClickListener(acessarContaListener);
        tvAcessarConta.setOnClickListener(acessarContaListener);

        View.OnClickListener pedirCestaListener = v -> {
            Intent intent = new Intent(TelaPrincipalVol.this, CalendarioVol.class);
            startActivity(intent);
            finish();
        };
        icCalendario.setOnClickListener(pedirCestaListener);
        tvCalendario.setOnClickListener(pedirCestaListener);

        tvDoacao.setOnClickListener(v -> {
            Intent intent = new Intent(TelaPrincipalVol.this, DoacaoPix.class);
            startActivity(intent);
        });

        tvAjuda.setOnClickListener(v -> {
            Intent intent = new Intent(TelaPrincipalVol.this, FaleConosco.class);
            startActivity(intent);
        });
    }
}
