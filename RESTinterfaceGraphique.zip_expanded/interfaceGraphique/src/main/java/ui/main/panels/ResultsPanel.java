package ui.main.panels;

import manager.WebServiceManager;
import web.service.client.ChambreDTO;
import ui.main.panels.SearchPanel.SearchCriteria;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ResultsPanel extends JPanel {

    private final WebServiceManager manager;

    private JPanel containerPanel;
    private JScrollPane scrollPane;

    public ResultsPanel(WebServiceManager manager) {
        this.manager = manager;

        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {

        containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        containerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        scrollPane = new JScrollPane(containerPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Appelée par MainFrame lors d'une recherche
     */
    public void performSearch(String agenceName, SearchCriteria criteria) {

        containerPanel.removeAll();

        try {
            List<ChambreDTO> chambres =
                    manager.rechercherChambres(agenceName, criteria);

            if (chambres == null || chambres.isEmpty()) {
                JLabel noResult = new JLabel("Aucun résultat pour votre recherche.");
                noResult.setFont(new Font("Arial", Font.ITALIC, 14));
                containerPanel.add(noResult);
            } else {

                for (ChambreDTO dto : chambres) {
                    ChambrePanel chambrePanel =
                            new ChambrePanel(dto, criteria, manager, agenceName);

                    chambrePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    chambrePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
                    chambrePanel.setPreferredSize(new Dimension(900, 180));
                    chambrePanel.setMinimumSize(new Dimension(900, 180));

                    JPanel wrapper = new JPanel(new BorderLayout());
                    wrapper.add(chambrePanel, BorderLayout.CENTER);
                    wrapper.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

                    containerPanel.add(wrapper);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de la recherche des chambres.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }

        containerPanel.revalidate();
        containerPanel.repaint();
    }
}
