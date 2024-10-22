package org.burnknuckle.utils;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.util.Objects;

public class Resources {
    public static ImageIcon editButtonIcon = new FlatSVGIcon(Objects.requireNonNull(Resources.class.getClassLoader().getResource("AdminDashboardPanel/edit.svg")));
    public static ImageIcon saveButtonIcon = new FlatSVGIcon(Objects.requireNonNull(Resources.class.getClassLoader().getResource("AdminDashboardPanel/save.svg")));
    public static ImageIcon deleteButtonIcon = new FlatSVGIcon(Objects.requireNonNull(Resources.class.getClassLoader().getResource("AdminDashboardPanel/delete.svg")));
    public static ImageIcon eyeOpenIcon = new FlatSVGIcon(Objects.requireNonNull(Resources.class.getClassLoader().getResource("Common/eyeOpen.svg")));
    public static ImageIcon eyeClosedIcon = new FlatSVGIcon(Objects.requireNonNull(Resources.class.getClassLoader().getResource("Common/eyeClosed.svg")));
    public static ImageIcon zoomInIcon = new FlatSVGIcon(Objects.requireNonNull(Resources.class.getClassLoader().getResource("Common/zoom-in.svg")));
    public static ImageIcon zoomOutIcon = new FlatSVGIcon(Objects.requireNonNull(Resources.class.getClassLoader().getResource("Common/zoom-out.svg")));
    public static ImageIcon searchIcon = new FlatSVGIcon(Objects.requireNonNull(Resources.class.getClassLoader().getResource("Common/search.svg")));

}
