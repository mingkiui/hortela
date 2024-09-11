package com.example.tcchortela;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class FaleConosco extends AppCompatActivity {

    private EditText inputEmail;
    private EditText inputSubject;
    private Button btnSend;
    private ImageView btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faleconosco);

        // Inicializa os componentes
        inputEmail = findViewById(R.id.input_email);
        inputSubject = findViewById(R.id.input_subject);
        btnSend = findViewById(R.id.btnSend);
        btnClose = findViewById(R.id.btnClose);

        // Define o comportamento do botão de envio
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendContactRequest();
            }
        });

        // Define o comportamento do botão de fechar
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Fecha a atividade atual
            }
        });
    }

    private void sendContactRequest() {
        String email = inputEmail.getText().toString().trim();
        String subject = inputSubject.getText().toString().trim();

        if (email.isEmpty() || subject.isEmpty()) {
            Toast.makeText(FaleConosco.this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
        } else {
            // Aqui você pode implementar a lógica para enviar os dados para o servidor ou salvar localmente
            Toast.makeText(FaleConosco.this, "Mensagem enviada com sucesso.", Toast.LENGTH_SHORT).show();
            // Limpa os campos após o envio
            inputEmail.setText("");
            inputSubject.setText("");
        }
    }
}
