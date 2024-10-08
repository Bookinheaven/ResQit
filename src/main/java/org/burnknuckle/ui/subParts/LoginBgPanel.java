package org.burnknuckle.ui.subParts;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class LoginBgPanel extends JPanel {
    private BufferedImage backgroundImage;

    public LoginBgPanel(String imagePath) {
        loadImage(imagePath);
    }

    private void loadImage(String imagePath) {
        SwingUtilities.invokeLater(() -> {
            try {
                backgroundImage = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource(imagePath)));
                backgroundImage = scaleImage(backgroundImage, getWidth(), getHeight());
                repaint();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private BufferedImage scaleImage(BufferedImage originalImage, int width, int height) {
        Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();
        return bufferedImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    @Override
    public void setSize(Dimension d) {
        super.setSize(d);
        if (backgroundImage != null) {
            backgroundImage = scaleImage(backgroundImage, d.width, d.height);
        }
    }
}
