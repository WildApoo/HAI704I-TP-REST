// IMPORTANT : code généré à 90% par chat gpt
package ui.main.panels;

import ui.model.Ville;


import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class SearchPanel extends JPanel {

    private JComboBox<Ville> comboVille;
    private JTextField txtDateDeb, txtDateFin, txtPrixMin, txtPrixMax, txtNbLits;
    private JButton btnRechercher;
    private Consumer<SearchCriteria> searchListener;

    public SearchPanel() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initComponents();
    }

    private void initComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        int y = 0;

        // Ville
        gbc.gridx = 0; gbc.gridy = y;
        add(new JLabel("Ville :"), gbc);
        comboVille = new JComboBox<>(Ville.values());
        gbc.gridx = 1;
        add(comboVille, gbc);

        // Date début
        gbc.gridx = 0; gbc.gridy = ++y;
        add(new JLabel("Date début (aaaammjj) :"), gbc);
        txtDateDeb = new JTextField(8);
        gbc.gridx = 1;
        add(txtDateDeb, gbc);

        // Date fin
        gbc.gridx = 0; gbc.gridy = ++y;
        add(new JLabel("Date fin (aaaammjj) :"), gbc);
        txtDateFin = new JTextField(8);
        gbc.gridx = 1;
        add(txtDateFin, gbc);

        // Prix min
        gbc.gridx = 0; gbc.gridy = ++y;
        add(new JLabel("Prix min :"), gbc);
        txtPrixMin = new JTextField("0", 6);
        gbc.gridx = 1;
        add(txtPrixMin, gbc);

        // Prix max
        gbc.gridx = 0; gbc.gridy = ++y;
        add(new JLabel("Prix max :"), gbc);
        txtPrixMax = new JTextField("100000", 6);
        gbc.gridx = 1;
        add(txtPrixMax, gbc);

        // Nb lits
        gbc.gridx = 0; gbc.gridy = ++y;
        add(new JLabel("Nb lits :"), gbc);
        txtNbLits = new JTextField(4);
        gbc.gridx = 1;
        add(txtNbLits, gbc);

        // Bouton Rechercher
        gbc.gridx = 0; gbc.gridy = ++y; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        btnRechercher = new JButton("Rechercher des disponibilités");
        add(btnRechercher, gbc);

        // Action bouton
        btnRechercher.addActionListener(e -> onSearch());
    }

    private void onSearch() {
        try {
            if (txtDateDeb.getText().trim().isEmpty() ||
                txtDateFin.getText().trim().isEmpty() ||
                txtNbLits.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Veuillez remplir Date début, Date fin et Nb lits.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int dateDeb = Integer.parseInt(txtDateDeb.getText().trim());
            int dateFin = Integer.parseInt(txtDateFin.getText().trim());
            double prixMin = Double.parseDouble(txtPrixMin.getText().trim());
            double prixMax = Double.parseDouble(txtPrixMax.getText().trim());
            int nbLits = Integer.parseInt(txtNbLits.getText().trim());
            Ville ville = (Ville) comboVille.getSelectedItem();

            if (prixMin < 0 || prixMax < 0 || nbLits < 0) {
                JOptionPane.showMessageDialog(this,
                        "Prix min, prix max et nb lits doivent être positifs.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (dateFin < dateDeb) {
                JOptionPane.showMessageDialog(this,
                        "La date de fin doit être postérieure à la date de début.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            SearchCriteria criteria = new SearchCriteria(dateDeb, dateFin, prixMin, prixMax, ville, nbLits);
            if (searchListener != null) searchListener.accept(criteria);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez vérifier les formats numériques saisis.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setSearchListener(Consumer<SearchCriteria> listener) {
        this.searchListener = listener;
    }

    public static class SearchCriteria {
        public final int dateDeb;
        public final int dateFin;
        public final double prixMin;
        public final double prixMax;
        public final Ville ville;
        public final int nbLits;

        public SearchCriteria(int dateDeb, int dateFin, double prixMin, double prixMax, Ville ville, int nbLits) {
            this.dateDeb = dateDeb;
            this.dateFin = dateFin;
            this.prixMin = prixMin;
            this.prixMax = prixMax;
            this.ville = ville;
            this.nbLits = nbLits;
        }
    }
}
