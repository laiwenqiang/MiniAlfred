package com.filesearch;

import com.filesearch.ui.MainWindow;

import javax.swing.*;

/**
 * @author Lion
 * @date 2025/2/2 14:23
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}