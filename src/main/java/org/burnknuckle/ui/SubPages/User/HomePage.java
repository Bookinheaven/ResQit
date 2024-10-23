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
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.*;

import static org.burnknuckle.Main.logger;
import static org.burnknuckle.utils.MainUtils.getStackTraceAsString;
import static org.burnknuckle.utils.Resources.*;

public class HomePage {
    private List<Waypoint> waypoints;
    private JXMapViewer mapViewer;
    private HashMap<Integer, String> disasterPoints;
    private boolean isLoading = false;
    private JPanel homePage;
    private JPanel rightPanel;
    private JButton refreshButton;
    private JLabel disasterNameValue;
    private JLabel disasterTypeValue;
    private JLabel disasterLocationValue;
    private JLabel disasterStartDateValue;
    private JLabel disasterEndDateValue;
    private List<Map<String, Object>> data;
    public JPanel createHomePage(JPanel mainContent) {
        homePage = new JPanel(new BorderLayout());
        JLayeredPane wrapper = new JLayeredPane();
        JPanel Layer1 = new JPanel(new BorderLayout());
        rightPanel = createRightPanel();
//        rightPanel.setOpaque(false);
        mapViewer = new JXMapViewer();
        initializeMapViewer();
        waypoints = new ArrayList<>();
        disasterPoints = new HashMap<>();
        loadWaypoints();
        Layer1.add(mapViewer);
        wrapper.add(Layer1, Integer.valueOf(0));
        wrapper.add(rightPanel, Integer.valueOf(1));

        homePage.add(wrapper, BorderLayout.CENTER);
        setupComponentResizeListener(mainContent, wrapper, Layer1);

        return homePage;
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
        rightPanel.setLayout(new MigLayout("", "[grow]", "[grow][]"));
        rightPanel.setSize(new Dimension(350, 400));

        JPanel leftPanel = createLeftPanel();
        rightPanel.add(leftPanel, "wrap");

        JPanel disasterDetailsPanel = new JPanel(new MigLayout("wrap 1", "", ""));

        TitledBorder titledBorder = new TitledBorder("Disaster Details");
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 18));
        titledBorder.setTitleColor(Color.RED);
        disasterDetailsPanel.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), titledBorder));

        disasterDetailsPanel.setOpaque(false);
        disasterDetailsPanel.setBackground(new Color(0, 0, 0, 0));
        JScrollPane scroll = new JScrollPane();
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setPreferredSize(new Dimension(350, 200));

        JPanel innerPanel = new JPanel(new MigLayout("wrap 1", "[]", "[]"));

        JLabel disasterNameLabel = new JLabel("Disaster Name: ");
        disasterNameLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel disasterTypeLabel = new JLabel("Type: ");
        disasterTypeLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel disasterLocationLabel = new JLabel("Location: ");
        disasterLocationLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel disasterStartDateLabel = new JLabel("Start Date: ");
        disasterStartDateLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel disasterEndDateLabel = new JLabel("End Date: ");
        disasterEndDateLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        disasterNameValue = new JLabel("N/A");
        disasterTypeValue = new JLabel("N/A");
        disasterLocationValue = new JLabel("N/A");
        disasterStartDateValue = new JLabel("N/A");
        disasterEndDateValue = new JLabel("N/A");

        innerPanel.add(disasterNameLabel);
        innerPanel.add(disasterNameValue, "wrap");
        innerPanel.add(disasterTypeLabel);
        innerPanel.add(disasterTypeValue, "wrap");
        innerPanel.add(disasterLocationLabel);
        innerPanel.add(disasterLocationValue, "wrap");
        innerPanel.add(disasterStartDateLabel);
        innerPanel.add(disasterStartDateValue, "wrap");
        innerPanel.add(disasterEndDateLabel);
        innerPanel.add(disasterEndDateValue, "wrap");

        scroll.setViewportView(innerPanel);
        disasterDetailsPanel.add(scroll);

        rightPanel.add(disasterDetailsPanel, "wrap");

        rightPanel.setOpaque(false);
        rightPanel.setBackground(new Color(0, 0, 0, 0));

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
        refreshButton = new JButton();
        refreshButton.setIcon(refreshIcon);
        refreshButton.addActionListener(e -> {
            refreshButton.setEnabled(false);
            refreshData();
        });

        JPanel zoomControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        zoomControls.setOpaque(false);
        zoomControls.add(zoomInButton);
        zoomControls.add(zoomOutButton);
        zoomControls.add(refreshButton);

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
                isLoading = true;
                refreshButton.setEnabled(false);
                Database disasterBase = Database.getInstance();
                disasterBase.getConnection();
                data = disasterBase.getAllData(2, "disastername location startdate enddate location responsestatus");
                logger.info(data.toString());

                int i = 0;
                for (Map<String, Object> disaster : data) {
                    if(disaster.get("responsestatus").equals("ongoing")){
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
                isLoading = false;
                refreshButton.setEnabled(true);

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
//                rightPanel.setBackground(Color.red);
//                rightPanel.setSize(new Dimension(250, height));
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
                mapViewer.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        updateDisasterDetails(disasterName);
                    }
                });
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

    private void refreshData() {
        if (isLoading) {
            return;
        }
        isLoading = true;
        waypoints.clear();
        disasterPoints.clear();
        loadWaypoints();
        refreshButton.setEnabled(true);

    }

    private void updateDisasterDetails(String disasterName) {
        try {
            Database disasterBase = Database.getInstance();
            disasterBase.getConnection();
            List<Map<String, Object>> disasterData = disasterBase.getAllData(2, "disastername disastertype location startdate enddate");
            for (Map<String, Object> disaster : disasterData) {
                if (disaster.get("disastername").equals(disasterName)) {
                    String disasterType = disaster.get("disastertype") != null ? disaster.get("disastertype").toString() : "N/A";
                    String location = disaster.get("location") != null ? disaster.get("location").toString() : "N/A";
                    String startDate = disaster.get("startdate") != null ? disaster.get("startdate").toString() : "N/A";
                    String endDate = disaster.get("enddate") != null ? disaster.get("enddate").toString() : "N/A";
                    disasterNameValue.setText(disasterName);
                    disasterTypeValue.setText(disasterType);
                    disasterLocationValue.setText(location);
                    disasterStartDateValue.setText(startDate);
                    disasterEndDateValue.setText(endDate);
                    break;
                }
            }
        } catch (Exception e) {
            logger.error(String.format("Error in updateDisasterDetails: %s", getStackTraceAsString(e)));
        }
    }


}
