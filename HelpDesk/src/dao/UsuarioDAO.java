package dao;

import database.Conexao;
import model.Perfil;
import model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    /** Login: devolve o usuário se o nome e a senha conferem com o banco; senão, null. */
    public Usuario autenticar(String nome, String senha) throws SQLException {
        String sql = "SELECT id, nome, senha, perfil FROM usuarios WHERE nome = ? AND senha = ?";
        try (Connection conn = Conexao.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setString(2, senha);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    public List<Usuario> listar() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement("SELECT id, nome, senha, perfil FROM usuarios ORDER BY nome");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public void inserir(Usuario u) throws SQLException {
        Conexao.executar("INSERT INTO usuarios (nome, senha, perfil) VALUES (?, ?, ?)",
                u.getNome(), u.getSenha(), u.getPerfil().name());
    }

    public void atualizar(Usuario u) throws SQLException {
        Conexao.executar("UPDATE usuarios SET nome = ?, senha = ?, perfil = ? WHERE id = ?",
                u.getNome(), u.getSenha(), u.getPerfil().name(), u.getId());
    }

    public void excluir(int id) throws SQLException {
        Conexao.executar("DELETE FROM usuarios WHERE id = ?", id);
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setNome(rs.getString("nome"));
        u.setSenha(rs.getString("senha"));
        u.setPerfil(Perfil.valueOf(rs.getString("perfil")));
        return u;
    }
}
