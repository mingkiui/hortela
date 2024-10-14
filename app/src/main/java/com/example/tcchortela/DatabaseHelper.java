package com.example.tcchortela;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import android.os.StrictMode;
import android.util.Log;

public class DatabaseHelper {

    private static final String DB_URL = "jdbc:jtds:sqlserver://172.19.1.107/db8071";
    private static final String USER = "sa";
    private static final String PASS = "@ITB123456";

    private static DatabaseHelper instance;

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

    public static Object[] loginUser(String email, String pass) {
        Connection conn = connect();
        if (conn == null) {
            return new Object[]{-1, -1}; // Erro ao conectar ao banco
        }

        try {
            String query = "SELECT user_id, access FROM users WHERE email = ? AND pass = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, pass);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("user_id");
                int accessLevel = rs.getInt("access");
                return new Object[]{userId, accessLevel}; // Retornando o userId e o nível de acesso
            } else {
                return new Object[]{0, -1}; // Usuário não encontrado ou senha incorreta
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Erro ao realizar login: " + e.getMessage());
            e.printStackTrace();
            return new Object[]{-1, -1};
        } finally {
            closeConnection(conn);
        }
    }

    public static Object[] getUserDataByEmail(String email) {
        Connection conn = connect();
        if (conn == null) {
            return null;
        }

        try {
            String query = "SELECT nome, email, user_id FROM users WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Retorna o nome, email e o user_id como int
                return new Object[]{rs.getString("nome"), rs.getString("email"), rs.getInt("user_id")};
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

    public static boolean deleteUserAccount(String email) {
        Connection conn = connect();
        if (conn == null) {
            return false;
        }

        try {
            conn.setAutoCommit(false);

            String deleteVolunteerQuery = "DELETE FROM voluntario WHERE user_id = (SELECT user_id FROM users WHERE email = ?)";
            PreparedStatement stmtVolunteer = conn.prepareStatement(deleteVolunteerQuery);
            stmtVolunteer.setString(1, email);
            stmtVolunteer.executeUpdate();

            String deleteUserQuery = "DELETE FROM users WHERE email = ?";
            PreparedStatement stmtUser = conn.prepareStatement(deleteUserQuery);
            stmtUser.setString(1, email);
            int rowsAffected = stmtUser.executeUpdate();
            if (rowsAffected > 0) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Erro ao apagar conta: " + e.getMessage());
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException se) {
                Log.e("DatabaseHelper", "Erro ao reverter a transação: " + se.getMessage());
            }
            return false;
        } finally {
            closeConnection(conn);
        }
    }

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

    public String[] getUserInfo(int userId) throws SQLException {
        String[] userInfo = new String[4];
        Connection connection = connect();
        String query = "SELECT nome, telefone, endereco, cpf FROM users WHERE user_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userInfo[0] = resultSet.getString("nome");
                userInfo[1] = resultSet.getString("telefone");
                userInfo[2] = resultSet.getString("endereco");
                userInfo[3] = resultSet.getString("cpf");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erro ao buscar informações do usuário.");
        } finally {
            if (connection != null) {
                connection.close(); // Fecha a conexão
            }
        }
        return userInfo;
    }

    public void updateUserInfo(int userId, String nome, String telefone, String endereco, String cpf) throws SQLException {
        Connection connection = connect(); // Estabelece conexão com o banco de dados
        String updateQuery = "UPDATE users SET nome = ?, telefone = ?, endereco = ?, cpf = ? WHERE user_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, nome);
            preparedStatement.setString(2, telefone);
            preparedStatement.setString(3, endereco);
            preparedStatement.setString(4, cpf);
            preparedStatement.setInt(5, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erro ao atualizar informações do usuário.");
        } finally {
            if (connection != null) {
                connection.close(); // Fecha a conexão
            }
        }
    }

    public static boolean adicionarVoluntario(String email) {
        Connection conn = connect();
        if (conn == null) {
            return false;
        }

        try {
            String updateAccessQuery = "UPDATE users SET access = 2 WHERE email = ?";
            PreparedStatement stmt1 = conn.prepareStatement(updateAccessQuery);
            stmt1.setString(1, email);
            stmt1.executeUpdate();

            String addVoluntarioQuery = "INSERT INTO voluntario (user_id) SELECT user_id FROM users WHERE email = ?";
            PreparedStatement stmt2 = conn.prepareStatement(addVoluntarioQuery);
            stmt2.setString(1, email);
            int rowsAffected = stmt2.executeUpdate();
            return rowsAffected > 0;
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
            return rowsAffected > 0;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Erro ao redefinir senha: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(conn);
        }
    }

    public boolean atualizarDataDisponibilidade(int userId, String selectedDate) throws SQLException {
        Connection conn = connect();
        if (conn == null) {
            return false;
        }

        try {
            // Verificar se o voluntário já existe
            String checkDateQuery = "SELECT * FROM voluntario WHERE user_id = ?";
            PreparedStatement stmt1 = conn.prepareStatement(checkDateQuery);
            stmt1.setInt(1, userId);
            ResultSet rs = stmt1.executeQuery();

            if (rs.next()) {
                // Atualizar a data se o voluntário já existir
                String updateDateQuery = "UPDATE voluntario SET dataDisp = ? WHERE user_id = ?";
                PreparedStatement stmt2 = conn.prepareStatement(updateDateQuery);
                stmt2.setString(1, selectedDate);
                stmt2.setInt(2, userId);
                int rowsAffected = stmt2.executeUpdate();

                return rowsAffected > 0; // Retorna true se a atualização foi bem-sucedida
            } else {
                return false; // Voluntário não encontrado com o userId fornecido
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Erro ao processar a solicitação
        } finally {
            closeConnection(conn);
        }
    }

    public Object[] carregarAtividadeVoluntario(int userId) throws SQLException {
        Connection connection = connect();
        String query = "SELECT tipoAtividade, dataDisp FROM voluntario WHERE user_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            Object[] atividadeData = new Object[2];
            atividadeData[0] = resultSet.getString("tipoAtividade");
            atividadeData[1] = resultSet.getString("dataDisp"); // Caso queira exibir a data
            return atividadeData;
        } else {
            return null;
        }
    }

    public String getSavedDate(int userId) throws SQLException {
        String savedDate = null;
        Connection connection = connect(); // Método que estabelece a conexão com o banco de dados
        String query = "SELECT dataDisp FROM voluntario WHERE user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                savedDate = resultSet.getString("dataDisp"); // Supondo que a coluna no banco de dados se chama 'data'
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Lança a exceção para ser tratada na classe que chamar esse método
        } finally {
            if (connection != null) {
                connection.close(); // Fecha a conexão
            }
        }

        return savedDate; // Retorna a data ou null se não houver
    }


    public Object[] getCestaDistInfo(int userId) {
        Connection conn = connect();
        if (conn == null) {
            return null;
        }

        try {
            String query = "SELECT calendarioDist.local, calendarioDist.data " +
                    "FROM calendarioDist " +
                    "JOIN cesta ON calendarioDist.cesta_id = cesta.cesta_id " +
                    "WHERE cesta.user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Object[]{rs.getString("local"), rs.getDate("data")};
            } else {
                return null; // Nenhuma cesta encontrada para o usuário
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            closeConnection(conn);
        }
    }

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
