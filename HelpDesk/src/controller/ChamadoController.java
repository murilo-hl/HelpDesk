package controller;

import dao.ChamadoDAO;
import model.Chamado;
import model.Perfil;
import model.Status;
import model.Usuario;
import java.time.LocalDateTime;
import java.util.List;

/** Regras dos chamados: USUARIO mexe só nos seus; TECNICO atende todos; ADMIN faz tudo. */
public class ChamadoController {

    private final ChamadoDAO dao = new ChamadoDAO();

    /** Usuário comum vê só os seus chamados; técnico e administrador veem todos. */
    public List<Chamado> listar() throws Exception {
        Usuario u = Sessao.usuario;
        return u.getPerfil() == Perfil.USUARIO ? dao.listarPorUsuario(u.getId()) : dao.listarTodos();
    }

    public void salvar(Chamado c) throws Exception {
        Usuario u = Sessao.usuario;
        if (u.getPerfil() == Perfil.TECNICO) throw new Exception("Técnicos não abrem chamados.");
        validar(c);
        c.setUsuarioId(u.getId());
        c.setStatus(Status.ABERTO);
        c.setDataAbertura(LocalDateTime.now());
        dao.inserir(c);
    }

    public void atualizar(Chamado c) throws Exception {
        Usuario u = Sessao.usuario;
        Chamado atual = buscar(c.getId());
        if (u.getPerfil() == Perfil.USUARIO) {
            if (!podeMexerComoDono(atual, u)) throw new Exception("Você só pode editar os seus chamados que ainda estão abertos.");
            validar(c);
            dao.atualizarDados(c);
        } else {
            if (u.getPerfil() == Perfil.ADMIN) {
                validar(c);
                dao.atualizarDados(c);
            }
            if (c.getStatus() == Status.RESOLVIDO && (c.getSolucao() == null || c.getSolucao().isBlank())) {
                throw new Exception("Descreva a solução para marcar o chamado como resolvido.");
            }
            // quem começa a atender sem técnico vira o técnico do chamado
            Integer tecnico = atual.getTecnicoId();
            if (tecnico == null && c.getStatus() != Status.ABERTO) tecnico = u.getId();
            c.setTecnicoId(tecnico);
            dao.atualizarAtendimento(c);
        }
    }

    /** O técnico (ou administrador) assume o chamado: vira o responsável e o status passa a "Em andamento". */
    public void assumir(int id) throws Exception {
        Usuario u = Sessao.usuario;
        if (u.getPerfil() == Perfil.USUARIO) throw new Exception("Apenas técnicos e administradores assumem chamados.");
        Chamado c = buscar(id);
        if (c.getTecnicoId() != null) throw new Exception("Este chamado já tem um técnico.");
        c.setTecnicoId(u.getId());
        c.setStatus(Status.EM_ANDAMENTO);
        dao.atualizarAtendimento(c);
    }

    public void excluir(int id) throws Exception {
        Usuario u = Sessao.usuario;
        Chamado atual = buscar(id);
        if (u.getPerfil() == Perfil.TECNICO) throw new Exception("Técnicos não excluem chamados.");
        if (u.getPerfil() == Perfil.USUARIO && !podeMexerComoDono(atual, u)) {
            throw new Exception("Você só pode excluir os seus chamados que ainda estão abertos.");
        }
        dao.excluir(id);
    }

    private Chamado buscar(int id) throws Exception {
        Chamado c = dao.buscarPorId(id);
        if (c == null) throw new Exception("Chamado não encontrado. Atualize a lista.");
        return c;
    }

    private boolean podeMexerComoDono(Chamado c, Usuario u) {
        return c.getUsuarioId() == u.getId() && c.getStatus() == Status.ABERTO;
    }

    private void validar(Chamado c) throws Exception {
        if (c.getTitulo() == null || c.getTitulo().isBlank()) throw new Exception("Informe o título.");
        if (c.getDescricao() == null || c.getDescricao().isBlank()) throw new Exception("Informe a descrição.");
    }
}
