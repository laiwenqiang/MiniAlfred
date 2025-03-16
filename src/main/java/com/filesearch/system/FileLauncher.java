package com.filesearch.system;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Lion
 * @date 2025/2/2 15:24
 */
public class FileLauncher {
    public static void open(Path path) {
        try {
            Desktop.getDesktop().open(path.toFile());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,
                    "无法打开文件: " + ex.getMessage(),
                    "错误",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
