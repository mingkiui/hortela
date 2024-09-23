package com.example.tcchortela;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.resource.Email;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MailjetEmailSender {

    /*/private static final String API_KEY = "14b4da72e1788adae5ebc50a0ee4474e";  // Sua chave pública
    private static final String API_SECRET = "e9a16fd43ecf6a8340a95b1d67053354";  // Sua chave privada

    public static void sendEmail(String userEmail, String code) throws JSONException {
        // Inicializa o cliente Mailjet
        MailjetClient client = new MailjetClient(API_KEY, API_SECRET, new ClientOptions.Builder().build());

        // Define o assunto e o corpo do e-mail
        String subject = "Código de Redefinição de Senha";
        String body = String.format("Bem vindo de volta! Esse é seu código de redefinição de senha: %s. Não compartilhe com ninguém essa sequência de números.", code);

        // Configura a mensagem
        JSONObject message = new JSONObject()
                .put("From", new JSONObject()
                        .put("Email", "Hortelan@gmail.com")  // E-mail do remetente
                        .put("Name", "Hortelã"))  // Nome do serviço
                .put("To", new JSONArray()
                        .put(new JSONObject()
                                .put("Email", userEmail)))  // E-mail do destinatário (usuário)
                .put("Subject", subject)
                .put("TextPart", body)
                .put("HTMLPart", "<h3>" + body + "</h3>");  // Corpo do e-mail em HTML

        // Cria a requisição
        MailjetRequest request = new MailjetRequest(Email.resource)
                .property("Messages", new JSONArray().put(message));

        try {
            // Envia o e-mail
            MailjetResponse response = client.post(request);
            System.out.println("E-mail enviado com sucesso!");
            System.out.println("Status: " + response.getStatus());
            System.out.println("Resposta: " + response.getData());
        } catch (MailjetException e) {
            e.printStackTrace();
            System.err.println("Erro ao enviar e-mail: " + e.getMessage());
        }
    }/*/
}
