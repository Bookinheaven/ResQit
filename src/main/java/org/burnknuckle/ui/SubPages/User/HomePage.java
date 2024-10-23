package org.burnknuckle.ui.SubPages.User;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import org.burnknuckle.utils.Database;
import org.burnknuckle.utils.EnglishTileFactoryInfo;
import org.burnknuckle.utils.LocationService;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.viewer.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.*;

import static org.burnknuckle.Main.logger;
import static org.burnknuckle.utils.Resources.*;

public class HomePage {
    private List<Waypoint> waypoints;
    private JXMapViewer mapViewer;
    private HashMap<Integer, String> disasterPoints;
    private JPanel homePage;
    private JPanel rightPanel;
    public JPanel createHomePage(JPanel mainContent) {
        homePage = new JPanel(new BorderLayout());
        JLayeredPane wrapper = new JLayeredPane();
        JPanel Layer1 = new JPanel(new BorderLayout());
        JPanel leftPanel = createLeftPanel();
        rightPanel = createRightPanel();

        mapViewer = new JXMapViewer();
        initializeMapViewer();

        waypoints = new ArrayList<>();
        disasterPoints = new HashMap<>();
        loadWaypoints();
        Layer1.add(mapViewer);
        wrapper.add(Layer1, Integer.valueOf(0));
//        wrapper.add(mapViewer, Integer.valueOf(1));
        wrapper.add(leftPanel, Integer.valueOf(1));
        wrapper.add(rightPanel, Integer.valueOf(2));

        homePage.add(wrapper, BorderLayout.CENTER);
        setupComponentResizeListener(mainContent, wrapper, Layer1);

        return homePage;
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new MigLayout("", "push[]push"));
        JLabel welcomeLabel = new JLabel("ResQit");
        titlePanel.setBackground(Color.BLUE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titlePanel.add(welcomeLabel);
        return titlePanel;
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new MigLayout("", "[grow]", "[]"));
        leftPanel.setSize(new Dimension(500, 300));

        JPanel zoomControls = createZoomControls();
        JPanel searchPanel = createSearchPanel();

        leftPanel.setOpaque(false);
        leftPanel.setBackground(new Color(0, 0, 0, 0));
        leftPanel.add(searchPanel, "wrap");
        leftPanel.add(zoomControls, "wrap");

        return leftPanel;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new MigLayout("", "[grow]", "[]"));
        rightPanel.setSize(new Dimension(500, 300));
//        rightPanel.setBounds();

        // should keep disaster name and its details
        JLabel disasterDetailsLabel = new JLabel("Disaster Details");
        disasterDetailsLabel.setFont(new Font("Arial", Font.BOLD, 20));
        rightPanel.add(disasterDetailsLabel, "wrap");

//        rightPanel.setOpaque(false);
//        rightPanel.setBackground(new Color(0, 0, 0, 0));

        return rightPanel;
    }

    private JPanel createZoomControls() {
        JButton zoomInButton = new JButton();
        zoomInButton.setIcon(zoomInIcon);
        zoomInButton.addActionListener(e -> {
            int zoomLevel = mapViewer.getZoom() - 1;
            if(zoomLevel > 0 && zoomLevel < 18){
                mapViewer.setZoom(zoomLevel);

            }
        });

        JButton zoomOutButton = new JButton();
        zoomOutButton.setIcon(zoomOutIcon);
        zoomOutButton.addActionListener(e -> {
            int zoomLevel = mapViewer.getZoom() + 1;
            if(zoomLevel > 0 && zoomLevel < 18){
                mapViewer.setZoom(zoomLevel);

            }
        });

        JPanel zoomControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        zoomControls.setOpaque(false);
        zoomControls.add(zoomInButton);
        zoomControls.add(zoomOutButton);

        return zoomControls;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        searchPanel.setOpaque(false);
        JTextField searchField = new JTextField(20);
        searchField.setMargin(new Insets(5, 5, 5, 5));
        searchField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search");

        JButton searchButton = new JButton();
        searchButton.setIcon(searchIcon);
        searchButton.addActionListener(e -> searchLocation(searchField.getText()));

        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        return searchPanel;
    }

    private void searchLocation(String location) {
        GeoPosition coordinates = LocationService.getCoordinates(location);
        if (coordinates != null) {
            mapViewer.setAddressLocation(coordinates);
            mapViewer.setZoom(10);
        } else {
            JOptionPane.showMessageDialog(mapViewer, "Location not found!");
        }
    }

    private void initializeMapViewer() {
        TileFactoryInfo info = new EnglishTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);
        tileFactory.setThreadPoolSize(8);

        GeoPosition india = new GeoPosition(50.11, 8.68);
        mapViewer.setZoom(7);
        mapViewer.setAddressLocation(india);

        mapViewer.addMouseWheelListener(this::handleMouseWheel);
        mapViewer.addMouseMotionListener(new MouseMotionAdapter() {
            private Point lastPoint;

            @Override
            public void mouseDragged(MouseEvent e) {
                mapViewer.setCursor(new Cursor(Cursor.MOVE_CURSOR));
                if (lastPoint != null) {
                    Point currentPoint = e.getPoint();
                    int deltaX = lastPoint.x - currentPoint.x;
                    int deltaY = lastPoint.y - currentPoint.y;
                    mapViewer.setCenter(new Point((int) (mapViewer.getCenter().getX() + deltaX),
                            (int) (mapViewer.getCenter().getY() + deltaY)));
                }
                lastPoint = e.getPoint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                lastPoint = e.getPoint();
                mapViewer.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                updateToolTip(e.getPoint());
            }
        });
    }

    private void handleMouseWheel(MouseWheelEvent e) {
        double zoomFactor = 1;
        double zoomLevel = mapViewer.getZoom();
        zoomLevel += (e.getWheelRotation() < 0) ? -zoomFactor : zoomFactor;
        if (zoomLevel < 18 && zoomLevel > 0) {
            mapViewer.setZoom((int) Math.round(zoomLevel));
        }
    }

    private void loadWaypoints() {
        SwingWorker<Void, Waypoint> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                Database disasterBase = Database.getInstance();
                disasterBase.getConnection();
                List<Map<String, Object>> data = disasterBase.getAllData(2, "disastername location startdate enddate location");
                logger.info(data.toString());

                int i = 0;
                for (Map<String, Object> disaster : data) {
                    String location = disaster.get("location").toString();
                    if (location != null) {
                        GeoPosition coordinates = LocationService.getCoordinates(location);
                        if (coordinates != null) {
                            publish(new DefaultWaypoint(coordinates));
                            disasterPoints.put(i++, disaster.get("disastername").toString());
                        } else {
                            logger.warn("Coordinates not found for location: {}", location);
                        }
                    } else {
                        logger.warn("Invalid location for disaster: {}", disaster.get("disastername"));
                    }
                }
                return null;
            }

            @Override
            protected void process(List<Waypoint> chunks) {
                for (Waypoint waypoint : chunks) {
                    waypoints.add(waypoint);
                    updateMap();
                }
            }

            @Override
            protected void done() {
                if (!waypoints.isEmpty()) {
                    updateMap();
                }
            }
        };

        worker.execute();
    }

    private void setupComponentResizeListener(JPanel mainContent, JLayeredPane wrapper, JPanel layer1) {
        mainContent.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent evt) {
                int width = mainContent.getWidth();
                int height = mainContent.getHeight();
                Dimension newSize = new Dimension(width - 1, height - 1);
                wrapper.setSize(newSize);
                layer1.setSize(newSize);
                rightPanel.setBackground(Color.red);
                rightPanel.setSize(new Dimension(250, height));
                homePage.revalidate();
            }
        });
    }

    private void updateToolTip(Point mousePoint) {
        for (int i = 0; i < waypoints.size(); i++) {
            Waypoint wp = waypoints.get(i);
            GeoPosition waypointPosition = wp.getPosition();
            Point2D waypointPoint2D = mapViewer.convertGeoPositionToPoint(waypointPosition);
            Point waypointPoint = new Point((int) waypointPoint2D.getX(), (int) waypointPoint2D.getY());

            int proximityThreshold = 10;
            if (waypointPoint.distance(mousePoint) < proximityThreshold) {
                String disasterName = disasterPoints.get(i);
                mapViewer.setToolTipText(disasterName);
                return;
            }
        }
        mapViewer.setToolTipText(null);
    }

    private void updateMap() {
        if (!waypoints.isEmpty()) {
            WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<>();
            waypointPainter.setWaypoints(new HashSet<>(waypoints));
            CompoundPainter<JXMapViewer> compoundPainter = new CompoundPainter<>();
            compoundPainter.setPainters(waypointPainter);
            mapViewer.setOverlayPainter(compoundPainter);
            mapViewer.repaint();
        }
    }
}
