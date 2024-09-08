package com.example.tcchortela;

import android.app.Activity;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseHelper {

    private static final String URL = "jdbc:mysql://sql10.freemysqlhosting.net:3306/sql10730179";
    private static final String USER = "sql10730179";
    private static final String PASSWORD = "6zXaSLY79d";

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private MainActivity context;

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
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            String query = "SELECT * FROM users WHERE nome = ? AND pass = ? AND email = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            resultSet = preparedStatement.executeQuery();

            boolean userFound = resultSet.next();  // Verifica se existe algum usuário com esses dados

            return userFound;

        } catch (SQLException e) {
            e.printStackTrace();
            runOnUiThread(() -> Toast.makeText(context, "Erro ao verificar usuário", Toast.LENGTH_SHORT).show());
            return false;

        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void runOnUiThread(Runnable action) {
        context.runOnUiThread(action);
    }
                }
