package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.sql.SQLException;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/** Pequenas ajudas usadas pelas telas. */
public class Util {

    public static final Color AZUL = new Color(11, 37, 69);

    /** Fonte e cor do título de cada tela. */
    public static void titulo(JLabel rotulo) {
        rotulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        rotulo.setForeground(AZUL);
    }

    /** Adiciona "rótulo + campo" numa linha de um painel com GridBagLayout. */
    public static void linha(JPanel painel, int y, String texto, JComponent campo) {
        GridBagConstraints g = new GridBagConstraints();
        g.gridy = y;
        g.insets = new Insets(3, 3, 3, 3);
        g.anchor = GridBagConstraints.NORTHWEST;
        g.gridx = 0;
        painel.add(new JLabel(texto), g);
        g.gridx = 1;
        g.weightx = 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        painel.add(campo, g);
    }

    public static void erro(Component pai, Exception e) {
        String msg = e instanceof SQLException ? "Erro no banco de dados: " + e.getMessage() : e.getMessage();
        JOptionPane.showMessageDialog(pai, msg, "Atenção", JOptionPane.WARNING_MESSAGE);
    }

    public static boolean confirmar(Component pai, String pergunta) {
        return JOptionPane.showConfirmDialog(pai, pergunta, "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    /** Modelo de tabela cujas células não podem ser editadas. */
    public static DefaultTableModel modelo(String... colunas) {
        return new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };
    }
}
