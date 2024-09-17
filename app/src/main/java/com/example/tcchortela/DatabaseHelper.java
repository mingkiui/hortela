package com.example.tcchortela;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import android.os.StrictMode;
import android.util.Log;

public class DatabaseHelper {

    // Informações do banco de dados
    private static final String DB_URL = "jdbc:sqlserver://db8071.public.databaseasp.net:1433;databaseName=db8071;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "db8071";
    private static final String PASS = "20anehort";

    // Conectar ao banco de dados SQL Server
    public static Connection connect() {
        Connection conn = null;
        try {
            // Permitir execução na thread principal (para desenvolvimento, não recomendado para produção)
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            // Carregar o driver SQL Server
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            Log.d("DatabaseHelper", "Conexão bem-sucedida");
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Erro de conexão: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

    // Método para registrar novo usuário
    public static boolean registerUser(String nome, String email, String pass) {
        Connection conn = connect();
        if (conn == null) {
            return false;
        }

        try {
            String query = "INSERT INTO users (nome, email, pass, acess) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nome);
            stmt.setString(2, email);
            stmt.setString(3, pass);
            stmt.setInt(4, 0); // Valor de acesso padrão (0 = usuário comum)
            stmt.executeUpdate();
            conn.close();
            return true;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Erro ao registrar usuário: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Método para verificar login
    public static boolean loginUser(String email, String pass) {
        Connection conn = connect();
        if (conn == null) {
            return false;
        }

        try {
            String query = "SELECT * FROM users WHERE email = ? AND pass = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, pass);
            ResultSet rs = stmt.executeQuery();

            boolean userExists = rs.next();
            conn.close();
            return userExists;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Erro ao realizar login: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
