// IMPORTANT : code généré à 90% par chat gpt
package ui.main;

import javax.swing.*;
import java.awt.*;

import manager.WebServiceManager;
import ui.main.panels.AgencySelectorPanel;
import ui.main.panels.SearchPanel;
import ui.main.panels.ResultsPanel;

public class MainFrame extends JFrame {

    private final WebServiceManager manager;

    private AgencySelectorPanel agencySelectorPanel;
    private SearchPanel searchPanel;
    private ResultsPanel resultsPanel;

    public MainFrame(WebServiceManager manager) {
        this.manager = manager;

        setTitle("Recherche de Chambres d'Hôtel");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {

        setLayout(new BorderLayout());

        // -------------------- TOP PANEL --------------------
        JPanel topPanel = new JPanel(new BorderLayout());

        agencySelectorPanel = new AgencySelectorPanel(manager);
        topPanel.add(agencySelectorPanel, BorderLayout.NORTH);

        searchPanel = new SearchPanel();
        topPanel.add(searchPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // -------------------- RESULTS PANEL --------------------
        resultsPanel = new ResultsPanel(manager);
        add(resultsPanel, BorderLayout.CENTER);

        // -------------------- INTERACTIONS --------------------
        searchPanel.setSearchListener(criteria -> {
            String agenceName = agencySelectorPanel.getSelectedAgency();
            resultsPanel.performSearch(agenceName, criteria);
        });
    }
}
