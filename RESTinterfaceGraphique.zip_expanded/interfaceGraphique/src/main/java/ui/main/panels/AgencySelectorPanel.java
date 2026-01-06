// IMPORTANT : code généré à 90% par chat gpt
package ui.main.panels;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import manager.WebServiceManager;

public class AgencySelectorPanel extends JPanel {

    private final WebServiceManager manager;
    private final List<JButton> buttons = new ArrayList<>();

    private String selectedAgency;

    public AgencySelectorPanel(WebServiceManager manager) {
        this.manager = manager;
        setLayout(new GridLayout(1, 0, 10, 0));
        initComponents();
    }

    private void initComponents() {

        for (String agenceName : manager.getAgences().keySet()) {
            JButton btn = new JButton(agenceName);
            btn.addActionListener(e -> selectAgency(agenceName, btn));
            buttons.add(btn);
            add(btn);

            // première agence sélectionnée par défaut
            if (selectedAgency == null) {
                selectedAgency = agenceName;
                highlightSelected(btn);
            }
        }
    }

    private void selectAgency(String agenceName, JButton btn) {
        selectedAgency = agenceName;
        highlightSelected(btn);
    }

    private void highlightSelected(JButton selectedBtn) {
        for (JButton b : buttons) {
            b.setBackground(null);
            b.setForeground(Color.BLACK);
        }

        selectedBtn.setBackground(new Color(70, 130, 180));
        selectedBtn.setForeground(Color.WHITE);
    }

    public String getSelectedAgency() {
        return selectedAgency;
    }
}
