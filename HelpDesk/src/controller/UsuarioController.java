package controller;

import dao.UsuarioDAO;
import model.Perfil;
import model.Usuario;
import java.sql.SQLException;
import java.util.List;

public class UsuarioController {

    private final UsuarioDAO dao = new UsuarioDAO();

    /** Confere nome e senha no banco. Se estiver certo, guarda o usuário na Sessao. */
    public Usuario login(String nome, String senha) throws Exception {
        if (nome == null || nome.isBlank() || senha == null || senha.isEmpty()) {
            throw new Exception("Informe o nome e a senha.");
        }
        Usuario u = dao.autenticar(nome.trim(), senha);
        if (u == null) {
            throw new Exception("Nome ou senha incorretos.");
        }
        Sessao.usuario = u;
        return u;
    }

    public void logout() {
        Sessao.usuario = null;
    }

    // ---- daqui para baixo: somente o administrador ----

    public List<Usuario> listar() throws Exception {
        exigirAdmin();
        return dao.listar();
    }

    public void salvar(Usuario u) throws Exception {
        exigirAdmin();
        if (u.getNome() == null || u.getNome().isBlank()) throw new Exception("Informe o nome.");
        if (u.getSenha() == null || u.getSenha().isBlank()) throw new Exception("Informe a senha.");
        if (u.getId() == Sessao.usuario.getId() && u.getPerfil() != Perfil.ADMIN) {
            throw new Exception("Você não pode tirar o seu próprio acesso de administrador.");
        }
        u.setNome(u.getNome().trim());
        try {
            if (u.getId() == 0) dao.inserir(u); else dao.atualizar(u);
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) throw new Exception("Já existe um usuário com esse nome.");
            throw e;
        }
    }

    public void excluir(int id) throws Exception {
        exigirAdmin();
        if (id == Sessao.usuario.getId()) throw new Exception("Você não pode excluir o seu próprio usuário.");
        try {
            dao.excluir(id);
        } catch (SQLException e) {
            if (e.getErrorCode() == 1451) throw new Exception("Este usuário tem chamados e não pode ser excluído.");
            throw e;
        }
    }

    private void exigirAdmin() throws Exception {
        if (Sessao.usuario == null || Sessao.usuario.getPerfil() != Perfil.ADMIN) {
            throw new Exception("Apenas o administrador pode fazer isto.");
        }
    }
}
