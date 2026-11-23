package view;

import controller.UsuarioController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class TelaLogin extends JFrame {

    private final UsuarioController controller = new UsuarioController();

    private final JLabel lblTitulo = new JLabel("HelpDesk - Entrar");
    private final JTextField txtNome = new JTextField(15);
    private final JPasswordField pwdSenha = new JPasswordField(15);
    private final JLabel lblErro = new JLabel(" ");
    private final JButton btnEntrar = new JButton("Entrar");

    public TelaLogin() {
        setTitle("HelpDesk");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel campos = new JPanel(new GridBagLayout());
        Util.linha(campos, 0, "Nome:", txtNome);
        Util.linha(campos, 1, "Senha:", pwdSenha);

        lblErro.setForeground(Color.RED);
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoes.add(btnEntrar);

        JPanel sul = new JPanel(new BorderLayout());
        sul.add(lblErro, BorderLayout.NORTH);
        sul.add(botoes, BorderLayout.SOUTH);

        Util.titulo(lblTitulo);
        JPanel conteudo = new JPanel(new BorderLayout(10, 10));
        conteudo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        conteudo.add(lblTitulo, BorderLayout.NORTH);
        conteudo.add(campos, BorderLayout.CENTER);
        conteudo.add(sul, BorderLayout.SOUTH);
        setContentPane(conteudo);

        btnEntrar.addActionListener(e -> entrar());
        getRootPane().setDefaultButton(btnEntrar);      // a tecla Enter aciona "Entrar"
        pack();
        setLocationRelativeTo(null);
    }

    private void entrar() {
        try {
            controller.login(txtNome.getText(), new String(pwdSenha.getPassword()));
            new TelaPrincipal().setVisible(true);
            dispose();
        } catch (Exception e) {
            lblErro.setText(e.getMessage());
            pwdSenha.setText("");
        }
    }
}
