package view;

import controller.ChamadoController;
import controller.Sessao;
import model.Chamado;
import model.Perfil;
import model.Prioridade;
import model.Status;
import model.Usuario;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/** Lista de chamados + formulário. O que aparece/habilita depende do perfil de quem está logado. */
public class TelaChamados extends JPanel {

    private final ChamadoController controller = new ChamadoController();
    private final Usuario logado = Sessao.usuario;
    private List<Chamado> chamados = new ArrayList<>();   // linhas da tabela, na mesma ordem
    private Chamado selecionado;                          // nulo = formulário em modo "novo"
    private boolean carregando;

    private final JLabel lblTitulo = new JLabel();
    private final DefaultTableModel modelo = Util.modelo("ID", "Título", "Solicitante", "Prioridade", "Status", "Técnico", "Abertura");
    private final JTable tblChamados = new JTable(modelo);
    private final JScrollPane scrChamados = new JScrollPane(tblChamados);
    private final JTextField txtTitulo = new JTextField();
    private final JTextArea txtDescricao = new JTextArea(3, 20);
    private final JScrollPane scrDescricao = new JScrollPane(txtDescricao);
    private final JComboBox<Prioridade> cbPrioridade = new JComboBox<>(Prioridade.values());
    private final JComboBox<Status> cbStatus = new JComboBox<>(Status.values());
    private final JTextArea txtSolucao = new JTextArea(2, 20);
    private final JScrollPane scrSolucao = new JScrollPane(txtSolucao);
    private final JButton btnSalvar = new JButton("Salvar");
    private final JButton btnAtualizar = new JButton("Atualizar");
    private final JButton btnExcluir = new JButton("Excluir");
    private final JButton btnAssumir = new JButton("Assumir chamado");
    private final JButton btnLimpar = new JButton("Limpar");

    public TelaChamados() {
        montarTela();
        tblChamados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblChamados.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !carregando) aoSelecionarLinha();
        });
        btnSalvar.addActionListener(e -> salvar());
        btnAtualizar.addActionListener(e -> atualizar());
        btnExcluir.addActionListener(e -> excluir());
        btnAssumir.addActionListener(e -> assumir());
        btnLimpar.addActionListener(e -> limparFormulario());
        tblChamados.getColumnModel().getColumn(0).setMaxWidth(50);     // coluna ID estreita
        carregarTabela();
        limparFormulario();
    }

    private void montarTela() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lblTitulo.setText(logado.getPerfil() == Perfil.USUARIO ? "Meus chamados" : "Todos os chamados");
        Util.titulo(lblTitulo);

        JPanel campos = new JPanel(new GridBagLayout());
        Util.linha(campos, 0, "Título:", txtTitulo);
        Util.linha(campos, 1, "Descrição:", scrDescricao);
        Util.linha(campos, 2, "Prioridade:", cbPrioridade);
        Util.linha(campos, 3, "Status:", cbStatus);
        Util.linha(campos, 4, "Solução:", scrSolucao);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoes.add(btnSalvar);
        botoes.add(btnAtualizar);
        botoes.add(btnExcluir);
        botoes.add(btnAssumir);
        botoes.add(btnLimpar);
        // o que o perfil nunca poderá usar nem aparece
        btnSalvar.setVisible(logado.getPerfil() != Perfil.TECNICO);
        btnExcluir.setVisible(logado.getPerfil() != Perfil.TECNICO);
        btnAssumir.setVisible(logado.getPerfil() != Perfil.USUARIO);

        JPanel formulario = new JPanel(new BorderLayout());
        formulario.add(campos, BorderLayout.CENTER);
        formulario.add(botoes, BorderLayout.SOUTH);

        add(lblTitulo, BorderLayout.NORTH);
        add(scrChamados, BorderLayout.CENTER);
        add(formulario, BorderLayout.SOUTH);
    }

    private void carregarTabela() {
        carregando = true;
        try {
            chamados = controller.listar();
            modelo.setRowCount(0);
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            for (Chamado c : chamados) {
                modelo.addRow(new Object[]{c.getId(), c.getTitulo(), c.getUsuarioNome(), c.getPrioridade(), c.getStatus(),
                    c.getTecnicoNome() == null ? "-" : c.getTecnicoNome(), formato.format(c.getDataAbertura())});
            }
        } catch (Exception e) {
            Util.erro(this, e);
        } finally {
            carregando = false;
        }
    }

    private void aoSelecionarLinha() {
        int linha = tblChamados.getSelectedRow();
        if (linha < 0) return;
        selecionado = chamados.get(linha);
        txtTitulo.setText(selecionado.getTitulo());
        txtDescricao.setText(selecionado.getDescricao());
        cbPrioridade.setSelectedItem(selecionado.getPrioridade());
        cbStatus.setSelectedItem(selecionado.getStatus());
        txtSolucao.setText(selecionado.getSolucao() == null ? "" : selecionado.getSolucao());
        atualizarEstado();
    }

    private void limparFormulario() {
        carregando = true;
        tblChamados.clearSelection();
        carregando = false;
        selecionado = null;
        txtTitulo.setText("");
        txtDescricao.setText("");
        txtSolucao.setText("");
        cbPrioridade.setSelectedItem(Prioridade.MEDIA);
        cbStatus.setSelectedItem(Status.ABERTO);
        atualizarEstado();
    }

    /** Liga/desliga campos e botões conforme o perfil e o chamado selecionado. */
    private void atualizarEstado() {
        Perfil perfil = logado.getPerfil();
        boolean temSelecao = selecionado != null;
        boolean donoAberto = temSelecao && selecionado.getUsuarioId() == logado.getId() && selecionado.getStatus() == Status.ABERTO;

        boolean editaDados = perfil == Perfil.ADMIN ? true : perfil == Perfil.USUARIO && (!temSelecao || donoAberto);
        boolean editaAtendimento = perfil != Perfil.USUARIO && temSelecao;

        txtTitulo.setEditable(editaDados);
        txtDescricao.setEditable(editaDados);
        cbPrioridade.setEnabled(editaDados);
        cbStatus.setEnabled(editaAtendimento);
        txtSolucao.setEditable(editaAtendimento);

        btnSalvar.setEnabled(!temSelecao);
        btnAtualizar.setEnabled(temSelecao && (perfil != Perfil.USUARIO || donoAberto));
        btnExcluir.setEnabled(temSelecao && (perfil == Perfil.ADMIN || donoAberto));
        btnAssumir.setEnabled(temSelecao && selecionado.getTecnicoId() == null);
    }

    private Chamado lerFormulario() {
        Chamado c = new Chamado();
        if (selecionado != null) c.setId(selecionado.getId());
        c.setTitulo(txtTitulo.getText());
        c.setDescricao(txtDescricao.getText());
        c.setPrioridade((Prioridade) cbPrioridade.getSelectedItem());
        c.setStatus((Status) cbStatus.getSelectedItem());
        c.setSolucao(txtSolucao.getText());
        return c;
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
        try {
            controller.atualizar(lerFormulario());
            carregarTabela();
            limparFormulario();
        } catch (Exception e) {
            Util.erro(this, e);
        }
    }

    private void excluir() {
        if (!Util.confirmar(this, "Excluir o chamado \"" + selecionado.getTitulo() + "\"?")) return;
        try {
            controller.excluir(selecionado.getId());
            carregarTabela();
            limparFormulario();
        } catch (Exception e) {
            Util.erro(this, e);
        }
    }

    private void assumir() {
        try {
            controller.assumir(selecionado.getId());
            carregarTabela();
            limparFormulario();
        } catch (Exception e) {
            Util.erro(this, e);
        }
    }
}
