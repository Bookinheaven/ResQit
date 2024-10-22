package org.burnknuckle.ui.SubPages.User;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import org.burnknuckle.utils.EnglishTileFactoryInfo;
import org.burnknuckle.utils.LocationService;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.viewer.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

import static org.burnknuckle.utils.Resources.*;

public class HomePage {
    public JPanel createHomePage(JPanel mainContent) {
        JPanel homePage = new JPanel(new BorderLayout());
        JLayeredPane wrapper = new JLayeredPane();

        JPanel Layer1 = new JPanel(new BorderLayout());
        JPanel titlePanel = new JPanel(new MigLayout("", "push[]push"));
        JLabel welcomeLabel = new JLabel("ResQit");
        titlePanel.setBackground(Color.BLUE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titlePanel.add(welcomeLabel);
        Layer1.add(titlePanel);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new MigLayout("", "[grow]", "[]"));
        rightPanel.setSize(new Dimension(500, 300));

        JXMapViewer mapViewer = new JXMapViewer();

        TileFactoryInfo info = new EnglishTileFactoryInfo();  // Use custom tile provider with English labels
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

        tileFactory.setThreadPoolSize(8);

        GeoPosition frankfurt = new GeoPosition(50.11, 8.68);
        mapViewer.setZoom(7);
        mapViewer.setAddressLocation(frankfurt);

        Set<Waypoint> waypoints = new HashSet<>();
        waypoints.add(new DefaultWaypoint(new GeoPosition(19.0760, 72.8777)));  // Mumbai
        waypoints.add(new DefaultWaypoint(new GeoPosition(28.6139, 77.2090)));  // Delhi
        waypoints.add(new DefaultWaypoint(new GeoPosition(12.9716, 77.5946)));  // Bengaluru
        waypoints.add(new DefaultWaypoint(new GeoPosition(13.0827, 80.2707)));  // Chennai
        waypoints.add(new DefaultWaypoint(new GeoPosition(22.5726, 88.3639)));  // Kolkata


        WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<>();
        waypointPainter.setWaypoints(waypoints);

        CompoundPainter<JXMapViewer> compoundPainter = new CompoundPainter<>();
        compoundPainter.setPainters(waypointPainter);
        mapViewer.setOverlayPainter(compoundPainter);

        JButton zoomInButton = new JButton();
        zoomInButton.setIcon(zoomInIcon);
        zoomInButton.addActionListener(_ -> mapViewer.setZoom(mapViewer.getZoom() - 1));

        JButton zoomOutButton = new JButton();
        zoomOutButton.setIcon(zoomOutIcon);
        zoomOutButton.addActionListener(_ -> mapViewer.setZoom(mapViewer.getZoom() + 1));

        JPanel zoomControls = new JPanel();
        zoomControls.add(zoomInButton);
        zoomControls.add(zoomOutButton);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10,10));
        JTextField searchField = new JTextField(20);
        searchField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search");
        JButton searchButton = new JButton();
        searchButton.setIcon(searchIcon);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String location = searchField.getText();
                GeoPosition coordinates = LocationService.getCoordinates(location);
                if (coordinates != null) {
                    mapViewer.setAddressLocation(coordinates);
                    mapViewer.setZoom(10);
                } else {
                    JOptionPane.showMessageDialog(homePage, "Location not found!");
                }
            }
        });
        mapViewer.addMouseWheelListener(new MouseWheelListener() {
            private double zoomLevel = mapViewer.getZoom();

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double zoomFactor = 0.4;
                if (e.getWheelRotation() < 0) {
                    zoomLevel = zoomLevel - zoomFactor;
                } else {
                    zoomLevel = zoomLevel + zoomFactor;
                }
                if(zoomLevel < 15 && zoomLevel > 0 ){
                    mapViewer.setZoom((int) Math.round(zoomLevel));
                }
            }
        });

        mapViewer.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            private Point lastPoint;

            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                if (lastPoint != null) {
                    Point currentPoint = e.getPoint();
                    int deltaX = lastPoint.x - currentPoint.x;
                    int deltaY = lastPoint.y - currentPoint.y;

                    mapViewer.setCenter(new Point((int) (mapViewer.getCenter().getX() + deltaX), (int) (mapViewer.getCenter().getY() + deltaY)));
                }
                lastPoint = e.getPoint();
            }

            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                lastPoint = e.getPoint();
            }
        });
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String location = searchField.getText();
                GeoPosition coordinates = LocationService.getCoordinates(location);
                if (coordinates != null) {
                    mapViewer.setAddressLocation(coordinates);
                    mapViewer.setZoom(10);
                } else {
                    JOptionPane.showMessageDialog(homePage, "Location not found!");
                }
            }
        });
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        rightPanel.setOpaque(false);
        rightPanel.setBackground(new Color(0,0,0,0));
        rightPanel.add(searchPanel, "wrap");
        rightPanel.add(zoomControls, "wrap");

        wrapper.add(Layer1, Integer.valueOf(0));
        wrapper.add(mapViewer, Integer.valueOf(1));
        wrapper.add(rightPanel, Integer.valueOf(2));

        homePage.add(wrapper, BorderLayout.CENTER);

        mainContent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent evt) {
                int width = mainContent.getWidth();
                int height = mainContent.getHeight();
                wrapper.setSize(new Dimension(width - 1, height - 1));
                Layer1.setSize(new Dimension(width - 1, height - 1));
                mapViewer.setSize(new Dimension(width - 1, height - 1));
                homePage.revalidate();
            }
        });
        return homePage;
    }
}
