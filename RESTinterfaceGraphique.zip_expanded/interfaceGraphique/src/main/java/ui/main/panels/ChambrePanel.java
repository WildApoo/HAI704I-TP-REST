// IMPORTANT : code généré à 90% par chat gpt
package ui.main.panels;

import web.service.client.ChambreDTO;
import ui.main.panels.SearchPanel.SearchCriteria;
import manager.WebServiceManager;

import javax.swing.*;
import java.awt.*;

public class ChambrePanel extends JPanel {

    private final ChambreDTO dto;
    private final SearchCriteria criteria;
    private final String agenceName;
    private final WebServiceManager manager;

    public ChambrePanel(ChambreDTO dto, SearchCriteria criteria, WebServiceManager manager, String agenceName) {
        this.dto = dto;
        this.criteria = criteria;
        this.manager = manager;
        this.agenceName = agenceName;

        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initUI();
    }

    private void initUI() {
        // ----- PANEL GAUCHE : Infos -----
        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JLabel lblHotel = new JLabel(dto.nomHotel());
        lblHotel.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel lblVille = new JLabel("Ville : " + dto.ville());
        JLabel lblId = new JLabel("ID chambre : " + dto.ID());
        JLabel lblLits = new JLabel("Nombre de lits : " + dto.nbLit());
        JLabel lblPrix = new JLabel("Prix : " + dto.prix() + " € / nuit");
        lblPrix.setFont(new Font("Arial", Font.BOLD, 14));

        leftPanel.add(lblHotel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(lblVille);
        leftPanel.add(lblId);
        leftPanel.add(lblLits);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(lblPrix);

        // ----- PANEL DROITE : Image -----
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(150, 120));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        if (dto.imageUri() != null) {
        	
        	ImageIcon icon = manager.loadChambreImage(agenceName, dto.imageUri());

        	if (icon != null) {
        	    imageLabel.setIcon(icon);
        	} else {
        	    imageLabel.setText("Image indisponible");
        	}

        } else {
            imageLabel.setText("Aucune image");
        }

        // ----- BAS : Bouton réserver -----
        JButton btnReserver = new JButton("Réserver");
        btnReserver.setFocusPainted(false);
        btnReserver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReserver.setFont(new Font("Arial", Font.BOLD, 14));
        btnReserver.setForeground(Color.WHITE);
        btnReserver.setBackground(new Color(30, 144, 255));
        btnReserver.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        btnReserver.addActionListener(e -> handleReservation());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.add(btnReserver);

        add(leftPanel, BorderLayout.CENTER);
        add(imageLabel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int arc = 20;

        g2.setColor(new Color(0, 0, 0, 40));
        g2.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, arc, arc);

        g2.setColor(new Color(255, 255, 255));
        g2.fillRoundRect(0, 0, getWidth() - 10, getHeight() - 10, arc, arc);

        g2.dispose();
        super.paintComponent(g);
    }

    private void handleReservation() {
        try {
            String pseudo = manager.getPseudo();
            String mdp = manager.getMdp();

            ChambreDTO ok = manager.reserverChambre(
                    agenceName,
                    dto.nomHotel(),
                    dto.ID(),
                    criteria,
                    pseudo,
                    mdp
            );

            if (ok != null) {
                JOptionPane.showMessageDialog(this,
                        "Réservation effectuée avec succès !",
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "La réservation a échoué.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de la réservation.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
