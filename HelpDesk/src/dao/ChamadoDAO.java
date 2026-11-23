package dao;

import database.Conexao;
import model.Chamado;
import model.Prioridade;
import model.Status;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChamadoDAO {

    // JOIN traz o nome de quem abriu e do técnico (LEFT JOIN: o técnico pode não existir ainda)
    private static final String SELECT =
            "SELECT c.id, c.titulo, c.descricao, c.prioridade, c.status, c.solucao, c.usuario_id, "
            + "u.nome AS usuario_nome, c.tecnico_id, t.nome AS tecnico_nome, c.data_abertura "
            + "FROM chamados c JOIN usuarios u ON u.id = c.usuario_id "
            + "LEFT JOIN usuarios t ON t.id = c.tecnico_id ";

    public List<Chamado> listarTodos() throws SQLException {
        return listar(SELECT + "ORDER BY c.id DESC", null);
    }

    public List<Chamado> listarPorUsuario(int usuarioId) throws SQLException {
        return listar(SELECT + "WHERE c.usuario_id = ? ORDER BY c.id DESC", usuarioId);
    }

    public Chamado buscarPorId(int id) throws SQLException {
        List<Chamado> lista = listar(SELECT + "WHERE c.id = ?", id);
        return lista.isEmpty() ? null : lista.get(0);
    }

    public void inserir(Chamado c) throws SQLException {
        Conexao.executar("INSERT INTO chamados (titulo, descricao, prioridade, status, usuario_id, data_abertura) "
                + "VALUES (?, ?, ?, ?, ?, ?)",
                c.getTitulo(), c.getDescricao(), c.getPrioridade().name(), c.getStatus().name(),
                c.getUsuarioId(), c.getDataAbertura());
    }

    /** Dados que quem abriu o chamado pode mudar. */
    public void atualizarDados(Chamado c) throws SQLException {
        Conexao.executar("UPDATE chamados SET titulo = ?, descricao = ?, prioridade = ? WHERE id = ?",
                c.getTitulo(), c.getDescricao(), c.getPrioridade().name(), c.getId());
    }

    /** Dados do atendimento (técnico/administrador). */
    public void atualizarAtendimento(Chamado c) throws SQLException {
        Conexao.executar("UPDATE chamados SET status = ?, solucao = ?, tecnico_id = ? WHERE id = ?",
                c.getStatus().name(), c.getSolucao(), c.getTecnicoId(), c.getId());
    }

    public void excluir(int id) throws SQLException {
        Conexao.executar("DELETE FROM chamados WHERE id = ?", id);
    }

    private List<Chamado> listar(String sql, Integer parametro) throws SQLException {
        List<Chamado> lista = new ArrayList<>();
        try (Connection conn = Conexao.getConexao(); PreparedStatement ps = conn.prepareStatement(sql)) {
            if (parametro != null) {
                ps.setInt(1, parametro);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    private Chamado mapear(ResultSet rs) throws SQLException {
        Chamado c = new Chamado();
        c.setId(rs.getInt("id"));
        c.setTitulo(rs.getString("titulo"));
        c.setDescricao(rs.getString("descricao"));
        c.setPrioridade(Prioridade.valueOf(rs.getString("prioridade")));
        c.setStatus(Status.valueOf(rs.getString("status")));
        c.setSolucao(rs.getString("solucao"));
        c.setUsuarioId(rs.getInt("usuario_id"));
        c.setUsuarioNome(rs.getString("usuario_nome"));
        c.setTecnicoId(rs.getObject("tecnico_id", Integer.class));
        c.setTecnicoNome(rs.getString("tecnico_nome"));
        c.setDataAbertura(rs.getObject("data_abertura", LocalDateTime.class));
        return c;
    }
}
