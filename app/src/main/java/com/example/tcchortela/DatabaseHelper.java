package com.example.tcchortela;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHelper {
    private static final String URL = "jdbc:mysql://177.31.70.50:1415/dbHorta";
    private static final String USER = "dbHorta";
    private static final String PASSWORD = "dbHorta";

    private Connection connection;

    public Connection connectToDatabase() {
        if (connection == null) {
            try {
                // Carregar o driver JDBC do MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Estabelecer uma conexão com o banco de dados
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexão bem-sucedida");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println("Driver JDBC do MySQL não encontrado.");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Falha na conexão.");
            }
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexão fechada");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Falha ao fechar a conexão.");
            }
        }
    }

    public void checkUser(String name, String password, String email, UserCheckCallback callback) {
        Connection conn = connectToDatabase();
        if (conn != null) {
            new Thread(() -> {
                try {
                    String query = "SELECT access FROM users WHERE name = ? AND password = ? AND email = ?";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setString(1, name);
                    statement.setString(2, password);
                    statement.setString(3, email);
                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        int access = resultSet.getInt("access");
                        callback.onUserCheck(true, access);
                    } else {
                        callback.onUserCheck(false, 0); // Retorna 0 se usuário não encontrado
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    callback.onUserCheck(false, 0); // Retorna 0 em caso de erro
                } finally {
                    closeConnection();
                }
            }).start();
        } else {
            callback.onUserCheck(false, 0); // Retorna 0 se não conseguir conectar
        }
    }

    public interface UserCheckCallback {
        void onUserCheck(boolean userExists, int access);
    }
}
