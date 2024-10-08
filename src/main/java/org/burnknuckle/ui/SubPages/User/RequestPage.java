package org.burnknuckle.ui.SubPages.User;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

import static org.burnknuckle.utils.ThemeManager.*;

public class RequestPage {
    private JLabel resRequestLabel;
    private JLabel disasterAddReqMainLabel;
    private JLabel teamAssignmentLabel;

    private void updateThemeColors(String change){
        resRequestLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
        disasterAddReqMainLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
        teamAssignmentLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
    }
    public JPanel createRequestPage(CardLayout cardLayout, JPanel mainContent, JPanel dashSpace) {
        JPanel disasterPanel = new JPanel();
        disasterPanel.setLayout(new GridBagLayout());
        GridBagConstraints bGbc = new GridBagConstraints();
        bGbc.gridx = 0;
        bGbc.gridy = 0;
        bGbc.fill = GridBagConstraints.HORIZONTAL;
        bGbc.weighty = 1.0;
        bGbc.weightx = 1.0;
        bGbc.insets = new Insets(10,10,10,10);
        JPanel ResRequestPanel = new JPanel();
        resRequestLabel = new JLabel(new FlatSVGIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("UserDashboardPanel/resRequest.svg"))));
        resRequestLabel.setPreferredSize(new Dimension(250,250));
        resRequestLabel.setOpaque(true);
        resRequestLabel.setText("Resource Request");
        resRequestLabel.setFont( new Font("Fira Code Retina",Font.PLAIN,20));
        resRequestLabel.setVerticalTextPosition(JLabel.BOTTOM);
        resRequestLabel.setHorizontalTextPosition(JLabel.CENTER);

        resRequestLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
        ResRequestPanel.add(resRequestLabel, BorderLayout.CENTER);

        JLabel resReqText = new JLabel();
        resReqText.setText("Resource Request");
        ResRequestPanel.add(resRequestLabel, BorderLayout.CENTER);
        ResRequestPanel.add(resReqText, BorderLayout.SOUTH);

        disasterAddReqMainLabel = new JLabel(new FlatSVGIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("UserDashboardPanel/disasterMain.svg"))));
        disasterAddReqMainLabel.setPreferredSize(new Dimension(250,250));
        disasterAddReqMainLabel.setOpaque(true);
        disasterAddReqMainLabel.setText("Disaster Request");
        disasterAddReqMainLabel.setFont( new Font("Fira Code Retina",Font.PLAIN,20));
        disasterAddReqMainLabel.setVerticalTextPosition(JLabel.BOTTOM);
        disasterAddReqMainLabel.setHorizontalTextPosition(JLabel.CENTER);
        disasterAddReqMainLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));

        teamAssignmentLabel = new JLabel(new FlatSVGIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("UserDashboardPanel/tasks.svg"))));
        teamAssignmentLabel.setPreferredSize(new Dimension(250,250));
        teamAssignmentLabel.setOpaque(true);
        teamAssignmentLabel.setText("Task Request");
        teamAssignmentLabel.setFont( new Font("Fira Code Retina",Font.PLAIN,20));
        teamAssignmentLabel.setVerticalTextPosition(JLabel.BOTTOM);
        teamAssignmentLabel.setHorizontalTextPosition(JLabel.CENTER);
        teamAssignmentLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));

        disasterPanel.add(resRequestLabel,bGbc);
        bGbc.gridx = 1;
        bGbc.gridy = 0;
        disasterPanel.add(disasterAddReqMainLabel,bGbc);
        bGbc.gridx = 2;
        bGbc.gridy = 0;
        disasterPanel.add(teamAssignmentLabel,bGbc);

        resRequestLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainContent, "Request");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                resRequestLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                resRequestLabel.setBackground(getColorFromHex(ADPThemeData.get("hover-menu-button")));
            }
        });

        disasterAddReqMainLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainContent, "Disaster");
            }
            @Override
            public void mouseExited(MouseEvent e) {
                disasterAddReqMainLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                disasterAddReqMainLabel.setBackground(getColorFromHex(ADPThemeData.get("hover-menu-button")));
            }
        });

        teamAssignmentLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainContent, "Team Assignments");
            }
            @Override
            public void mouseExited(MouseEvent e) {
                teamAssignmentLabel.setBackground(getColorFromHex(ADPThemeData.get("default-menu-button")));
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                teamAssignmentLabel.setBackground(getColorFromHex(ADPThemeData.get("hover-menu-button")));
            }
        });

        dashSpace.setLayout(new BorderLayout());
        dashSpace.add(disasterPanel, BorderLayout.CENTER);
        addThemeChangeListener(this::updateThemeColors);
        return disasterPanel;
    }

}
