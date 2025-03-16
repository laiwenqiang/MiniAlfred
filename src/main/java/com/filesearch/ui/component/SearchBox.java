package com.filesearch.ui.component;

import com.filesearch.core.searcher.SearchManager;
import com.filesearch.event.SearchEvent;
import com.filesearch.event.EventBus;
import com.filesearch.core.model.SearchResult;
import com.filesearch.system.FileLauncher;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicTextFieldUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * @author Lion
 * @date 2025/2/2 16:05
 */
public class SearchBox extends JTextField{
    private Timer searchTimer;
    private ResultList resultList = ResultList.getInstance();

    public SearchBox() {
        // 设置搜索框高度
        setPreferredSize(new Dimension(getWidth(), 60));
        setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        setFont(new Font("微软雅黑", Font.PLAIN, 30));

        // 添加自定义背景绘制
        setUI(new BasicTextFieldUI() {
            @Override
            protected void paintBackground(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(216, 216, 215, 255));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
            }
        });

        setupSearchTrigger();
        setupKeyBindings();
    }

    private void setupSearchTrigger() {
        getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { triggerSearch(); }
            public void removeUpdate(DocumentEvent e) { triggerSearch(); }
            public void changedUpdate(DocumentEvent e) { triggerSearch(); }
        });

        searchTimer = new Timer(10, e -> {
            EventBus.getInstance().publish(new SearchEvent(getText().trim()));
            SearchManager.getInstance().handleSearch(new SearchEvent(getText().trim()));
        });
        searchTimer.setRepeats(false);
    }

    private void triggerSearch() {
        searchTimer.restart();
    }

    private void setupKeyBindings() {
        // 获取输入映射和动作映射
        InputMap inputMap = getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap actionMap = getActionMap();

        // 上键处理
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "selectPrevious");
        actionMap.put("selectPrevious", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = resultList.getSelectedIndex();
                if (index == -1) {
                    index = resultList.getModel().getSize();
                }
                if (index > 0) {
                    resultList.setSelectedIndex(index - 1);
                    resultList.ensureIndexIsVisible(index - 1);
                }
            }
        });

        // 下键处理
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "selectNext");
        actionMap.put("selectNext", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = resultList.getSelectedIndex();
                if (index == -1) {
                    index = -1;
                }
                if (index < resultList.getModel().getSize() - 1) {
                    resultList.setSelectedIndex(index + 1);
                    resultList.ensureIndexIsVisible(index + 1);
                }
            }
        });

        // 回车键处理
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "openFile");
        actionMap.put("openFile", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SearchResult selected = resultList.getSelectedValue();
                if (selected != null) {
                    FileLauncher.open(selected.getFilePath());
                }
            }
        });
    }
}