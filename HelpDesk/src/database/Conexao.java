package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/** Conexão com o MySQL. Ajuste USUARIO e SENHA se o seu MySQL for diferente. */
public class Conexao {

    private static final String URL = "jdbc:mysql://localhost:3306/helpdesk";
    private static final String USUARIO = "root";
    private static final String SENHA = "";

    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }

    /** Executa INSERT, UPDATE ou DELETE com PreparedStatement (os valores entram nos "?"). */
    public static void executar(String sql, Object... valores) throws SQLException {
        try (Connection conn = getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < valores.length; i++) {
                ps.setObject(i + 1, valores[i]);
            }
            ps.executeUpdate();
        }
    }
}
