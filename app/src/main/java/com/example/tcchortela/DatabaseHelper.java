package com.example.tcchortela;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.util.Log;

public class DatabaseHelper {

    private static final String DB_URL = "jdbc:jtds:sqlserver://172.19.1.134/dbHort";
    private static final String USER = "sa";
    private static final String PASS = "@ITB123456";

    private static DatabaseHelper instance;

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

    // Método para atualizar detalhes do beneficiário (nome, telefone, cpf, endereco)
    public static boolean updateBeneficiaryDetails(String email, String nome, String telefone, String cpf, String endereco) {
        Connection conn = connect();
        if (conn == null) {
            return false;
        }

        try {
            String query = "UPDATE users SET nome = ?, telefone = ?, cpf = ?, endereco = ? WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nome);
            stmt.setString(2, telefone);
            stmt.setString(3, cpf);
            stmt.setString(4, endereco);
            stmt.setString(5, email);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Retorna true se a atualização foi bem-sucedida
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Erro ao atualizar detalhes do beneficiário: " + e.getMessage());
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
            // Começar uma transação
            conn.setAutoCommit(false);

            // Primeiro, exclua da tabela voluntario
            String deleteVolunteerQuery = "DELETE FROM voluntario WHERE user_id = (SELECT user_id FROM users WHERE email = ?)";
            PreparedStatement stmtVolunteer = conn.prepareStatement(deleteVolunteerQuery);
            stmtVolunteer.setString(1, email);
            stmtVolunteer.executeUpdate();

            // Agora, exclua da tabela users
            String deleteUserQuery = "DELETE FROM users WHERE email = ?";
            PreparedStatement stmtUser = conn.prepareStatement(deleteUserQuery);
            stmtUser.setString(1, email);
            int rowsAffected = stmtUser.executeUpdate();

            // Se a exclusão do usuário for bem-sucedida, confirmar a transação
            if (rowsAffected > 0) {
                conn.commit();
                return true; // Retorna true se a conta foi apagada
            } else {
                conn.rollback(); // Reverter em caso de falha na exclusão
                return false;
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Erro ao apagar conta: " + e.getMessage());
            e.printStackTrace();
            try {
                conn.rollback(); // Reverter em caso de erro
            } catch (SQLException se) {
                Log.e("DatabaseHelper", "Erro ao reverter a transação: " + se.getMessage());
            }
            return false;
        } finally {
            closeConnection(conn);
        }
    }


    // Método para atualizar o telefone do usuário na tabela users
    public static boolean atualizarTelefoneUsuario(String email, String telefone) {
        Connection conn = connect();
        if (conn == null) {
            return false;
        }

        try {
            String query = "UPDATE users SET telefone = ? WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, telefone);
            stmt.setString(2, email);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Retorna true se a atualização foi bem-sucedida
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Erro ao atualizar telefone do usuário: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(conn);
        }
    }

    // Método para adicionar o usuário na tabela voluntario
    public static boolean adicionarVoluntario(String email) {
        Connection conn = connect();
        if (conn == null) {
            return false;
        }

        try {
            // Atualizar o nível de acesso para 2 (voluntário)
            String updateAccessQuery = "UPDATE users SET access = 2 WHERE email = ?";
            PreparedStatement stmt1 = conn.prepareStatement(updateAccessQuery);
            stmt1.setString(1, email);
            stmt1.executeUpdate();

            // Adicionar na tabela voluntario (chave estrangeira para o usuário)
            String addVoluntarioQuery = "INSERT INTO voluntario (user_id) SELECT user_id FROM users WHERE email = ?";
            PreparedStatement stmt2 = conn.prepareStatement(addVoluntarioQuery);
            stmt2.setString(1, email);
            int rowsAffected = stmt2.executeUpdate();
            return rowsAffected > 0; // Retorna true se o voluntário foi adicionado
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Erro ao adicionar voluntário: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(conn);
        }
    }
    public static synchronized DatabaseHelper getInstance() {
        if (instance == null) {
            instance = new DatabaseHelper();
        }
        return instance;
    }

    // Método para redefinir a senha de um usuário
    public static boolean redefinirSenha(String email, String novaSenha) {
        Connection conn = connect();
        if (conn == null) {
            return false;
        }

        try {
            String query = "UPDATE users SET pass = ? WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, novaSenha);
            stmt.setString(2, email);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Retorna true se a senha foi atualizada com sucesso
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Erro ao redefinir senha: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(conn);
        }
    }

    // Método para atualizar a data de disponibilidade no banco de dados remoto
    public boolean updateDataDisponibilidade(String userId, String dataDisponibilidade) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = connect();
            if (conn != null) {
                String query = "UPDATE voluntario SET dataDisp = ? WHERE user_id = ?";
                stmt = conn.prepareStatement(query);
                stmt.setString(1, dataDisponibilidade);
                stmt.setString(2, userId);
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return false;
    }

    // Método para obter o tipo de atividade para um voluntário
    public String getTipoAtividade(String userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String tipoAtividade = null;

        try {
            conn = connect();
            if (conn != null) {
                String query = "SELECT tipoAtividade FROM voluntario WHERE user_id = ?";
                stmt = conn.prepareStatement(query);
                stmt.setString(1, userId);
                rs = stmt.executeQuery();

                if (rs.next()) {
                    tipoAtividade = rs.getString("tipoAtividade");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return tipoAtividade;
    }

    // Método para fechar a conexão
    private static void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close(); // Fechar conexão
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Erro ao fechar conexão: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
