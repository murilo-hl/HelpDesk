package view;

import controller.Sessao;
import controller.UsuarioController;
import model.Perfil;
import model.Usuario;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/** Tela principal: aba de chamados para todos e, só para o administrador, a aba de usuários. */
public class TelaPrincipal extends JFrame {

    private final JLabel lblUsuario = new JLabel();
    private final JButton btnSair = new JButton("Sair");
    private final JTabbedPane abas = new JTabbedPane();

    public TelaPrincipal() {
        Usuario u = Sessao.usuario;
        setTitle("HelpDesk");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        lblUsuario.setText("Logado como: " + u.getNome() + " (" + u.getPerfil() + ")");
        JPanel topo = new JPanel(new BorderLayout());
        topo.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        topo.add(lblUsuario, BorderLayout.WEST);
        topo.add(btnSair, BorderLayout.EAST);

        abas.addTab(u.getPerfil() == Perfil.USUARIO ? "Meus chamados" : "Chamados", new TelaChamados());
        if (u.getPerfil() == Perfil.ADMIN) {
            abas.addTab("Usuários", new TelaUsuarios());
        }

        add(topo, BorderLayout.NORTH);
        add(abas, BorderLayout.CENTER);
        btnSair.addActionListener(e -> sair());
        setSize(1000, 680);
        setLocationRelativeTo(null);
    }

    private void sair() {
        new UsuarioController().logout();
        new TelaLogin().setVisible(true);
        dispose();
    }
}
