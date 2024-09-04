package com.example.tcchortela;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseHelper {

    private static final String URL = "jdbc:mysql://sql10728282:3306/dbHort";
    private static final String USER = "sql10728282";
    private static final String PASSWORD = "sql10728282";

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private MainActivity context; // Adicionar uma referência à Activity

    public DatabaseHelper(Activity context) {
        this.context = (MainActivity) context;
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
                runOnUiThread(() -> Toast.makeText(context, "Erro ao conectar ao banco de dados", Toast.LENGTH_SHORT).show());
            }
        });
    }

    public boolean checkUser(String name, String password, String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM users WHERE nome = ? AND pass = ? AND email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{name, password, email});

        if (cursor.moveToFirst()) {
            cursor.close();
            return true;  // Usuário encontrado
        } else {
            cursor.close();
            return false; // Usuário não encontrado
        }
    }

    private SQLiteDatabase getReadableDatabase() {
        return null;
    }

    private void runOnUiThread(Runnable action) {
        context.runOnUiThread(action);
    }

}