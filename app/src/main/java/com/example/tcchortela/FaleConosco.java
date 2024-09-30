package com.example.tcchortela;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class FaleConosco extends AppCompatActivity {

    private EditText inputEmail;
    private EditText inputSubject;
    private Button btnSend;
    private ImageView btnClose;

    String[] mensagens = {"Por favor, preencha todos os campos.", "Mensagem enviada com sucesso."};

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
        btnSend.setOnClickListener(v -> sendContactRequest(v));

        // Define o comportamento do botão de fechar
        btnClose.setOnClickListener(v -> {
            finish(); // Fecha a atividade atual
        });
    }

    private void sendContactRequest(View v) {
        String email = inputEmail.getText().toString().trim();
        String subject = inputSubject.getText().toString().trim();

        if (email.isEmpty() || subject.isEmpty()) {
            Snackbar snackbar = Snackbar.make(v, mensagens[0], Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();
        } else {
            // Envia a mensagem usando a classe SendinblueEmailSender
            SendinblueEmailSender.sendContactEmail(email, subject);

            Snackbar snackbar = Snackbar.make(v, mensagens[1], Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();

            // Limpa os campos após o envio
            inputEmail.setText("");
            inputSubject.setText("");
        }
    }
}
