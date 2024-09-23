package com.example.tcchortela;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import android.os.StrictMode;
import android.util.Log;

public class DatabaseHelper {

    private static final String DB_URL = "jdbc:jtds:sqlserver://172.19.1.173/dbHort";
    private static final String USER = "sa";
    private static final String PASS = "@ITB123456";

    // Conectar ao banco de dados SQL Server
    public static Connection connect() {
        Connection conn = null;
        try {
            // Permitir execução na thread principal (para desenvolvimento, não recomendado para produção)
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            // Carregar o driver SQL Server
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
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
            String query = "INSERT INTO users (nome, email, pass, access) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nome);
            stmt.setString(2, email);
            stmt.setString(3, pass);
            stmt.setInt(4, 0); // Valor de acesso padrão (0 = usuário comum)
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Erro ao registrar usuário: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(conn);
        }
    }

    // Método para verificar login
    public static int loginUser(String email, String pass) {
        Connection conn = connect();
        if (conn == null) {
            return -1; // Erro ao conectar ao banco
        }

        try {
            String query = "SELECT access FROM users WHERE email = ? AND pass = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, pass);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("access"); // Retornando o nível de acesso
            } else {
                return -1; // Usuário não encontrado ou senha incorreta
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Erro ao realizar login: " + e.getMessage());
            e.printStackTrace();
            return -1;
        } finally {
            closeConnection(conn);
        }
    }

    // Método para recuperar nome e e-mail do usuário com base no e-mail
    public static String[] getUserDataByEmail(String email) {
        Connection conn = connect();
        if (conn == null) {
            return null;
        }

        try {
            String query = "SELECT nome, email FROM users WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new String[]{rs.getString("nome"), rs.getString("email")}; // Retorna o nome e o e-mail do usuário
            } else {
                return null; // Usuário não encontrado
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Erro ao buscar dados do usuário: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            closeConnection(conn);
        }
    }

    // Método para verificar se o e-mail já está cadastrado
    public static boolean isEmailRegistered(String email) {
        Connection conn = connect();
        if (conn == null) {
            return false;
        }

        try {
            String query = "SELECT COUNT(*) FROM users WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            return rs.next() && rs.getInt(1) > 0; // Retorna true se o e-mail estiver cadastrado
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Erro ao verificar e-mail: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(conn);
        }
    }

    // Método para atualizar as informações do beneficiário
    public static boolean updateBeneficiaryInfo(String email, String nome, String telefone, String cpf) {
        Connection conn = connect();
        if (conn == null) {
            return false;
        }

        try {
            String query = "UPDATE users SET nome = ?, telefone = ?, cpf = ? WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nome);
            stmt.setString(2, telefone);
            stmt.setString(3, cpf);
            stmt.setString(4, email);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Erro ao atualizar informações do beneficiário: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(conn);
        }
    }

    // Método para atualizar o nível de acesso do usuário
    public static boolean updateAccessLevel(String email, int accessLevel) {
        Connection conn = connect();
        if (conn == null) {
            return false;
        }

        try {
            String query = "UPDATE users SET access = ? WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, accessLevel);
            stmt.setString(2, email);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Retorna verdadeiro se a atualização foi bem-sucedida
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Erro ao atualizar nível de acesso: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(conn);
        }
    }

    // Método para apagar a conta do usuário
    public static boolean deleteUserAccount(String email) {
        Connection conn = connect();
        if (conn == null) {
            return false;
        }

        try {
            String query = "DELETE FROM users WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Retorna true se a conta foi apagada
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Erro ao apagar conta: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.close(); // Fechar conexão
                }
            } catch (Exception e) {
                Log.e("DatabaseHelper", "Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    // Método para fechar a conexão
    private static void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close(); // Fechar conexão
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Erro ao fechar conexão: " + e.getMessage());
        }
    }
}
