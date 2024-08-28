package com.example.tcchortela;

import android.app.Activity;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseHelper {

    private static final String URL = "jdbc:mysql://sql10728282:3306/dbHort";
    private static final String USER = "sql10728282";
    private static final String PASSWORD = "UjvQEttD6J";

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private Activity context; // Adicionar uma referência à Activity

    public DatabaseHelper(Activity context) {
        this.context = context;
    }

    public void connectToDatabase(Runnable onConnected) {
        executor.submit(() -> {
            Connection connection = null;
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                // Sucesso na conexão
                runOnUiThread(() -> onConnected.run());
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(context, "Erro ao conectar com o banco de dados", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void runOnUiThread(Runnable action) {
        context.runOnUiThread(action);
    }
}
