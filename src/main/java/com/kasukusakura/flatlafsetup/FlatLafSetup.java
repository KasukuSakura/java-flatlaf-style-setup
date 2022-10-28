package com.kasukusakura.flatlafsetup;

import com.formdev.flatlaf.FlatLaf;

import javax.swing.*;

public class FlatLafSetup {
    public static void setupLafClasspath() {
        UIManager.getDefaults().put("ClassLoader", FlatLaf.class.getClassLoader());
    }
}
