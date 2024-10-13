package org.burnknuckle.utils;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.util.Objects;

public class Resources {
    public static ImageIcon editButtonIcon = new FlatSVGIcon(Objects.requireNonNull(Resources.class.getClassLoader().getResource("AdminDashboardPanel/edit.svg")));
    public static ImageIcon saveButtonIcon = new FlatSVGIcon(Objects.requireNonNull(Resources.class.getClassLoader().getResource("AdminDashboardPanel/save.svg")));
    public static ImageIcon deleteButtonIcon = new FlatSVGIcon(Objects.requireNonNull(Resources.class.getClassLoader().getResource("AdminDashboardPanel/delete.svg")));

}
