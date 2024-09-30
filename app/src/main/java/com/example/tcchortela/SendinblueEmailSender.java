package com.example.tcchortela;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

// Modelo de dados para o envio do e-mail
class EmailData {
    public Sender sender;
    public Recipient[] to;
    public String subject;
    public String htmlContent;

    public EmailData(Sender sender, Recipient[] to, String subject, String htmlContent) {
        this.sender = sender;
        this.to = to;
        this.subject = subject;
        this.htmlContent = htmlContent;
    }
}

class Sender {
    public String name;
    public String email;

    public Sender(String name, String email) {
        this.name = name;
        this.email = email;
    }
}

class Recipient {
    public String email;

    public Recipient(String email) {
        this.email = email;
    }
}

// Interface da API do Sendinblue
interface SendinblueService {
    @Headers({
            "accept: application/json",
            "api-key: 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",
            "content-type: application/json"
    })
    @POST("smtp/email")
    Call<Void> sendEmail(@Body EmailData emailData);
}

public class SendinblueEmailSender {

    private static final String BASE_URL = "https://api.brevo.com/v3/";

    // Método para enviar e-mail de redefinição de senha
    public static void sendEmail(String userEmail, String code) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SendinblueService service = retrofit.create(SendinblueService.class);

        // Configura o e-mail para redefinição de senha
        Sender sender = new Sender("Hortelã", "hortelanetn@gmail.com");
        Recipient[] recipients = { new Recipient(userEmail) };
        String subject = "Código de Redefinição de Senha";
        String htmlContent = "<h3>Bem vindo de volta! Esse é seu código de redefinição de senha: " + code + ". Não compartilhe com ninguém essa sequência de números.</h3>";

        EmailData emailData = new EmailData(sender, recipients, subject, htmlContent);

        // Envia o e-mail
        Call<Void> call = service.sendEmail(emailData);
        call.enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    System.out.println("E-mail de redefinição de senha enviado com sucesso!");
                } else {
                    System.err.println("Erro ao enviar e-mail: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    // Novo método para enviar e-mail do "Fale Conosco"
    public static void sendContactEmail(String userEmail, String message) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SendinblueService service = retrofit.create(SendinblueService.class);

        // Configura o e-mail do "Fale Conosco"
        Sender sender = new Sender("Hortelã", "hortelanetn@gmail.com");
        Recipient[] recipients = { new Recipient("hortelanetn@gmail.com") }; // O email destino é o da Hortelã
        String subject = "Mensagem do Fale Conosco";
        String htmlContent = "<h3>Mensagem recebida de: " + userEmail + "</h3><p>" + message + "</p>";

        EmailData emailData = new EmailData(sender, recipients, subject, htmlContent);

        // Envia o e-mail
        Call<Void> call = service.sendEmail(emailData);
        call.enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    System.out.println("E-mail do Fale Conosco enviado com sucesso!");
                } else {
                    System.err.println("Erro ao enviar e-mail: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
