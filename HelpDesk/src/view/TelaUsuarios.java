package view;

import controller.UsuarioController;
import model.Perfil;
import model.Usuario;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/** Área do administrador: cadastrar usuários e escolher o perfil (acesso) de cada um. */
public class TelaUsuarios extends JPanel {

    private final UsuarioController controller = new UsuarioController();
    private List<Usuario> usuarios = new ArrayList<>();
    private Usuario selecionado;                       // nulo = formulário em modo "novo"
    private boolean carregando;

    private final JLabel lblTitulo = new JLabel("Usuários");
    private final DefaultTableModel modelo = Util.modelo("ID", "Nome", "Perfil");
    private final JTable tblUsuarios = new JTable(modelo);
    private final JScrollPane scrUsuarios = new JScrollPane(tblUsuarios);
    private final JTextField txtNome = new JTextField();
    private final JTextField txtSenha = new JTextField();
    private final JComboBox<Perfil> cbPerfil = new JComboBox<>(Perfil.values());
    private final JButton btnSalvar = new JButton("Salvar");
    private final JButton btnAtualizar = new JButton("Atualizar");
    private final JButton btnExcluir = new JButton("Excluir");
    private final JButton btnLimpar = new JButton("Limpar");

    public TelaUsuarios() {
        montarTela();
        tblUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblUsuarios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !carregando) aoSelecionarLinha();
        });
        btnSalvar.addActionListener(e -> salvar());
        btnAtualizar.addActionListener(e -> atualizar());
        btnExcluir.addActionListener(e -> excluir());
        btnLimpar.addActionListener(e -> limparFormulario());
        tblUsuarios.getColumnModel().getColumn(0).setMaxWidth(50);     // coluna ID estreita
        carregarTabela();
        limparFormulario();
    }

    private void montarTela() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        Util.titulo(lblTitulo);

        JPanel campos = new JPanel(new GridBagLayout());
        Util.linha(campos, 0, "Nome (usado para entrar):", txtNome);
        Util.linha(campos, 1, "Senha:", txtSenha);
        Util.linha(campos, 2, "Perfil (acesso):", cbPerfil);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoes.add(btnSalvar);
        botoes.add(btnAtualizar);
        botoes.add(btnExcluir);
        botoes.add(btnLimpar);

        JPanel formulario = new JPanel(new BorderLayout());
        formulario.add(campos, BorderLayout.CENTER);
        formulario.add(botoes, BorderLayout.SOUTH);

        add(lblTitulo, BorderLayout.NORTH);
        add(scrUsuarios, BorderLayout.CENTER);
        add(formulario, BorderLayout.SOUTH);
    }

    private void carregarTabela() {
        carregando = true;
        try {
            usuarios = controller.listar();
            modelo.setRowCount(0);
            for (Usuario u : usuarios) {
                modelo.addRow(new Object[]{u.getId(), u.getNome(), u.getPerfil()});
            }
        } catch (Exception e) {
            Util.erro(this, e);
        } finally {
            carregando = false;
        }
    }

    private void aoSelecionarLinha() {
        int linha = tblUsuarios.getSelectedRow();
        if (linha < 0) return;
        selecionado = usuarios.get(linha);
        txtNome.setText(selecionado.getNome());
        txtSenha.setText(selecionado.getSenha());
        cbPerfil.setSelectedItem(selecionado.getPerfil());
        atualizarEstado();
    }

    private void limparFormulario() {
        carregando = true;
        tblUsuarios.clearSelection();
        carregando = false;
        selecionado = null;
        txtNome.setText("");
        txtSenha.setText("");
        cbPerfil.setSelectedItem(Perfil.USUARIO);
        atualizarEstado();
    }

    private void atualizarEstado() {
        boolean temSelecao = selecionado != null;
        btnSalvar.setEnabled(!temSelecao);
        btnAtualizar.setEnabled(temSelecao);
        btnExcluir.setEnabled(temSelecao);
    }

    private Usuario lerFormulario() {
        Usuario u = new Usuario();
        if (selecionado != null) u.setId(selecionado.getId());
        u.setNome(txtNome.getText());
        u.setSenha(txtSenha.getText());
        u.setPerfil((Perfil) cbPerfil.getSelectedItem());
        return u;
    }

    private void salvar() {
        try {
            controller.salvar(lerFormulario());
            carregarTabela();
            limparFormulario();
        } catch (Exception e) {
            Util.erro(this, e);
        }
    }

    private void atualizar() {
        salvar();   // o controller decide: id diferente de 0 = UPDATE
    }

    private void excluir() {
        if (!Util.confirmar(this, "Excluir o usuário \"" + selecionado.getNome() + "\"?")) return;
        try {
            controller.excluir(selecionado.getId());
            carregarTabela();
            limparFormulario();
        } catch (Exception e) {
            Util.erro(this, e);
        }
    }
}
