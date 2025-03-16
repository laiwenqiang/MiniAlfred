package com.filesearch.ui.component;

import com.filesearch.core.model.SearchResult;
import com.filesearch.system.FileLauncher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * @author Lion
 * @date 2025/2/2 22:37
 */
public class ResultList extends JList<SearchResult> {
    // 与滚动条颜色一致
    private static final Color SELECTED_COLOR = new Color(83, 31, 115);
    private static ResultList instance;

    private ResultList() {
        setOpaque(false);
        setCellRenderer(new CustomResultRenderer());
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setSelectionBackground(new Color(0, 0, 0, 0));
        setSelectionForeground(Color.WHITE);
        
        // 添加键盘监听
        setupKeyboardNavigation();
    }

    public static ResultList getInstance() {
        if (instance == null) {
            instance = new ResultList();
            instance.setupMouseListener();
        }
        return instance;
    }

    public void updateResults(List<SearchResult> results) {
        DefaultListModel<SearchResult> model = new DefaultListModel<>();
        results.forEach(model::addElement);
        setModel(model);
        
        // 如果有结果，选中第一项
        if (!results.isEmpty()) {
            setSelectedIndex(0);
            ensureIndexIsVisible(0);
        }
    }

    private class CustomResultRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(
                JList<?> list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {
            
            JLabel label = (JLabel) super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);
            
            SearchResult result = (SearchResult) value;
            label.setIcon(result.getFileIcon());
            label.setText(result.toString());
            // 设置不透明
            label.setOpaque(true);
            // 设置内边距
            label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

            // 根据选中状态设置背景色
            if (isSelected) {
                label.setBackground(SELECTED_COLOR);
                label.setForeground(Color.WHITE);
            } else {
                label.setBackground(Color.WHITE);
                label.setForeground(Color.BLACK);
            }

            return label;
        }
    }

    private void setupMouseListener() {
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int index = locationToIndex(e.getPoint());
                if (index != getSelectedIndex()) {
                    setSelectedIndex(index);
                }
            }
        });
    }

    private void setupKeyboardNavigation() {
        InputMap inputMap = getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap actionMap = getActionMap();
        
        // 上键
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "selectPrevious");
        actionMap.put("selectPrevious", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = getSelectedIndex();
                if (index == -1) { // 如果没有选中项
                    index = getModel().getSize(); // 从最后开始
                }
                if (index > 0) {
                    setSelectedIndex(index - 1);
                    ensureIndexIsVisible(index - 1);
                }
            }
        });
        
        // 下键
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "selectNext");
        actionMap.put("selectNext", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = getSelectedIndex();
                if (index == -1) { // 如果没有选中项
                    index = -1; // 从第一个开始
                }
                if (index < getModel().getSize() - 1) {
                    setSelectedIndex(index + 1);
                    ensureIndexIsVisible(index + 1);
                }
            }
        });
        
        // 回车键打开文件
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "openFile");
        actionMap.put("openFile", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SearchResult selected = getSelectedValue();
                if (selected != null) {
                    FileLauncher.open(selected.getFilePath());
                }
            }
        });
    }
}
