package org.burnknuckle.ui.SubPages.User;

import net.miginfocom.swing.MigLayout;
import org.burnknuckle.ui.subParts.LoginBgPanel;
import org.burnknuckle.utils.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.burnknuckle.Main.logger;
import static org.burnknuckle.utils.MainUtils.getStackTraceAsString;
import static org.burnknuckle.utils.Userdata.getUsername;

public class VolunteerRegistration {
    private final String username = getUsername();
    private CardLayout cardLayout;
    private JPanel mainContent;
    private void clickSubmit(){
        Map<String, Object> data = new HashMap<>();
        data.put("volunteer_reg_time", new Timestamp(System.currentTimeMillis()));
        data.put("privilege", "vol");
        try {
            Database db = Database.getInstance();
            db.getConnection();
            db.updateData12(0,username ,data);
            cardLayout.show(mainContent, "HomePage");
        } catch (Exception e){
            logger.error("Error in: %s".formatted(getStackTraceAsString(e)));
        }
        JOptionPane.showMessageDialog(null, "Form submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public JPanel createVolunteerRegistrationSubPage(CardLayout cardLayout, JPanel mainContent) {
        this.cardLayout = cardLayout;
        this.mainContent = mainContent;
        JPanel inner = new JPanel(new BorderLayout());
        LoginBgPanel OuterBgPanel = new LoginBgPanel("Common/formPagesBg.jpg");
        OuterBgPanel.setLayout(new BorderLayout());
        inner.setOpaque(false);
        JScrollPane scrollablePanel = new JScrollPane() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Shape roundedRectangle = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 5, 5);
                g2d.setColor(new Color(0, 0, 0, 94));
                g2d.fill(roundedRectangle);
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(0, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2d.draw(roundedRectangle);
            }
        };
        // Title Label
        JLabel title = new JLabel("Volunteer Registration Form");
        title.setFont(new Font("Inter", Font.BOLD, 32));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        // Top Bar Panel
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        topBar.add(title);

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        topBar.add(separator);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
        wrapper.setOpaque(false);
        JPanel centerForm = new JPanel(new MigLayout("wrap 2", "", ""));
        centerForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String pledgeText = "<html>" +
                "<body style='font-family: Verdana, sans-serif;'>" +
                "<h1 style='font-size: 20px;'>Disaster Volunteer Pledge</h1>" +
                "<p style='font-size: 16px; line-height: 1.6; text-align: justify;'>As a dedicated volunteer committed to disaster response and recovery, I pledge to:</p>" +
                "<ul style='font-size: 12px; line-height: 1.8; margin-left: 20px;'>" +
                "<li><b>Act with integrity and compassion:</b> Serving individuals and communities affected by disasters with empathy, respect, and a deep commitment to their well-being.</li>" +
                "<li><b>Ensure safety for all:</b> Prioritize my own safety and the safety of others by strictly adhering to guidelines, safety protocols, and proper procedures at all times.</li>" +
                "<li><b>Stay calm and resilient:</b> Remain composed and focused in high-pressure situations, maintaining a positive attitude in the face of adversity and challenges.</li>" +
                "<li><b>Be adaptable and solution-oriented:</b> Adjust to rapidly changing circumstances with flexibility and a problem-solving mindset, ensuring that the needs of those affected are met efficiently.</li>" +
                "<li><b>Respect cultural diversity and inclusion:</b> Value and embrace the diverse backgrounds of individuals affected by disasters, working with cultural sensitivity and inclusivity to foster trust and cooperation.</li>" +
                "<li><b>Communicate transparently and collaborate:</b> Share clear, concise, and honest information with my team, relief organizations, and community members, ensuring a unified and effective response effort.</li>" +
                "<li><b>Maintain confidentiality and privacy:</b> Protect the sensitive information of disaster survivors, upholding their dignity and trust at all times by keeping personal details confidential.</li>" +
                "<li><b>Strive for continuous learning and self-improvement:</b> Actively seek out training and development opportunities to enhance my disaster response skills and stay informed about best practices.</li>" +
                "<li><b>Be a reliable and committed volunteer:</b> Fulfill my duties with dedication, understanding the importance of showing up on time, completing assigned tasks, and being dependable throughout the relief operation.</li>" +
                "<li><b>Uplift and empower communities:</b> Support the empowerment and resilience of affected communities, encouraging recovery, rebuilding, and long-term sustainability in disaster response efforts.</li>" +
                "</ul>" +
                "<br>" +
                "<p style='font-size: 12px; line-height: 1.6; text-align: justify;'>" +
                "I acknowledge the importance of my role and the responsibilities it entails, and I commit to carrying out my duties to the best of my ability in support of disaster relief efforts.<br>"+
                "</p>" +
                "<div>" +
                "<br><b style='font-size: 14px;'>Volunteer: %s </b><br>".formatted(username) +
                "<b style='font-size: 14px;'>Date: %s</b>".formatted(LocalDateTime.now().toLocalDate().toString()) +
                "</div>" +
                "</body>" +
                "</html>";
        JEditorPane editorPane = new JEditorPane("text/html", pledgeText);
        editorPane.setPreferredSize(new Dimension(700, 600));
        editorPane.setEditable(false);
        centerForm.add(editorPane, "gaptop 20, span 2, grow, gapbottom 30");
        JSeparator separator1 = new JSeparator(JSeparator.HORIZONTAL);
        centerForm.add(separator1, "gaptop 40, span 2, grow, gapbottom 30");

        JButton submitButton = new JButton();
        submitButton.setText("Submit");
        submitButton.setPreferredSize(new Dimension(300,50));
        submitButton.setFont(new Font("Inter", Font.PLAIN, 16));
        submitButton.addActionListener(_ -> clickSubmit());
        centerForm.add(submitButton, "span 2, align center");

        inner.add(topBar, BorderLayout.NORTH);
        inner.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

        wrapper.add(centerForm);

        scrollablePanel.setViewportView(wrapper);
        scrollablePanel.setOpaque(false);

        OuterBgPanel.add(inner, BorderLayout.NORTH);
        scrollablePanel.getViewport().setOpaque(false);

        OuterBgPanel.add(scrollablePanel, BorderLayout.CENTER);
        return OuterBgPanel;
    }
}
