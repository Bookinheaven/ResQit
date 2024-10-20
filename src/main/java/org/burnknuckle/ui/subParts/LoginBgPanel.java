package org.burnknuckle.ui.subParts;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import static org.burnknuckle.Main.logger;
import static org.burnknuckle.utils.MainUtils.getStackTraceAsString;

public class LoginBgPanel extends JPanel {
    private BufferedImage backgroundImage;
    private BufferedImage scaledImage;
    private int lastWidth = -1;
    private int lastHeight = -1;

    public LoginBgPanel(String imagePath) {
        loadImage(imagePath);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int newWidth = getWidth();
                int newHeight = getHeight();
                if (backgroundImage != null && newWidth > 0 && newHeight > 0) {
                    if (newWidth != lastWidth || newHeight != lastHeight) {
                        scaledImage = scaleImage(backgroundImage, newWidth, newHeight);
                        lastWidth = newWidth;
                        lastHeight = newHeight;
                        repaint();
                    }
                }
            }
        });
    }

    private void loadImage(String imagePath) {
        try {
            backgroundImage = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource(imagePath)));
            if (getWidth() > 0 && getHeight() > 0) {
                scaledImage = scaleImage(backgroundImage, getWidth(), getHeight());
                lastWidth = getWidth();
                lastHeight = getHeight();
                repaint();
            }
        } catch (IOException e) {
            logger.error("Error in: %s".formatted(getStackTraceAsString(e)));
        }
    }

    private BufferedImage scaleImage(BufferedImage originalImage, int width, int height) {
        if (width <= 0 || height <= 0) {
            return originalImage;
        }
        Image scaled;
        if (Math.abs(width - lastWidth) > 20 || Math.abs(height - lastHeight) > 20) {
            scaled = originalImage.getScaledInstance(width, height, Image.SCALE_FAST);
        } else {
            scaled = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        }
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(scaled, 0, 0, null);
        g2d.dispose();
        return bufferedImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (scaledImage != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawImage(scaledImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    @Override
    public void setSize(Dimension d) {
        super.setSize(d);
        if (backgroundImage != null && d.width > 0 && d.height > 0 &&
                (d.width != lastWidth || d.height != lastHeight)) {
            scaledImage = scaleImage(backgroundImage, d.width, d.height);
            lastWidth = d.width;
            lastHeight = d.height;
            repaint();
        }
    }
}
