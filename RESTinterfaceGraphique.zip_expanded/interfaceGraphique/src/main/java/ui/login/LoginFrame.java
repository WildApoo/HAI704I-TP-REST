// IMPORTANT : code généré à 90% par chat gpt
package ui.login;

import javax.swing.*;

import manager.WebServiceManager;
import ui.main.MainFrame;

import java.awt.*;

public class LoginFrame extends JFrame {

    private final WebServiceManager manager;

    private JTextField pseudoField;
    private JPasswordField mdpField;
    private JButton btnLogin;
    private JButton btnRegister;

    public LoginFrame(WebServiceManager manager) {
        this.manager = manager;

        setTitle("Connexion");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel lblPseudo = new JLabel("Pseudo :");
        pseudoField = new JTextField(20);

        JLabel lblMdp = new JLabel("Mot de passe :");
        mdpField = new JPasswordField(20);

        JPanel pseudoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pseudoPanel.add(lblPseudo);
        pseudoPanel.add(pseudoField);

        JPanel mdpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mdpPanel.add(lblMdp);
        mdpPanel.add(mdpField);

        panel.add(pseudoPanel);
        panel.add(mdpPanel);

        btnLogin = new JButton("Connexion");
        btnRegister = new JButton("Créer un compte");

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(btnLogin);
        buttonsPanel.add(btnRegister);

        panel.add(Box.createVerticalStrut(15));
        panel.add(buttonsPanel);

        add(panel);

        btnLogin.addActionListener(e -> onLogin());
        btnRegister.addActionListener(e -> onRegister());
    }

    // -------------------------- ACTIONS --------------------------

    private void onLogin() {
        String pseudo = pseudoField.getText().trim();
        String mdp = new String(mdpField.getPassword()).trim();

        if (pseudo.isEmpty() || mdp.isEmpty()) {
            showError("Veuillez saisir un pseudo et un mot de passe.");
            return;
        }

        boolean ok = manager.connexion(pseudo, mdp);

        if (!ok) {
            showError("Identifiants incorrects.");
            return;
        }

        this.dispose();
        SwingUtilities.invokeLater(() ->
                new MainFrame(manager).setVisible(true)
        );
    }

    private void onRegister() {
        String pseudo = pseudoField.getText().trim();
        String mdp = new String(mdpField.getPassword()).trim();

        if (pseudo.isEmpty() || mdp.isEmpty()) {
            showError("Saisissez un pseudo et un mot de passe.");
            return;
        }

        boolean ok = manager.inscrireSurToutes(pseudo, mdp);

        if (ok) {
            JOptionPane.showMessageDialog(this,
                    "Compte créé avec succès sur les agences.",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            showError("Erreur lors de la création du compte.");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
    }
}
