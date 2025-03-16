package com.filesearch.ui;

import com.filesearch.core.model.SearchResult;
import com.filesearch.event.EventBus;
import com.filesearch.event.SearchResultsEvent;
import com.filesearch.system.FileLauncher;
import com.filesearch.ui.component.ResultList;
import com.filesearch.ui.component.SearchBox;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;


/**
 * @author Lion
 * @date 2025/2/5 16:05
 */
public class MainWindow extends JFrame {
    private final SearchBox searchBox = new SearchBox();
    private final ResultList resultList = ResultList.getInstance();
    private final JScrollPane scrollPane;
    // 窗口尺寸相关常量
    private static final int WINDOW_WIDTH = 700;
    private static final int MAX_HEIGHT = 500;
    // 背景颜色
    private static final Color BACKGROUND_COLOR = new Color(239, 240, 239, 255);
    // 新增布局相关常量
    private static final int PANEL_GAP = 10;
    // 与SearchBox的HEIGHT保持一致
    private static final int SEARCH_HEIGHT = 60;
    // 新增圆角常量
    private static final int CORNER_RADIUS = 12;
    // 添加用于记录鼠标拖动的变量
    private Point mouseOffset;

    public MainWindow() {
        configureWindow();
        scrollPane = new JScrollPane(resultList);
        styleScrollPane();
        buildUI();
        registerEvents();
        setupWindowDragging();
        setupKeyBindings();
        adjustWindowSize(false);
        centerWindowAtTwoFifth();
    }

    private void configureWindow() {
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        // 设置窗口背景透明
        setBackground(new Color(0, 0, 0, 0));
    }

    private void buildUI() {
        JPanel content = createContentPanel();
        JPanel innerPanel = createInnerPanel();

        innerPanel.add(searchBox, BorderLayout.NORTH);
        innerPanel.add(scrollPane, BorderLayout.CENTER);

        content.add(innerPanel, BorderLayout.CENTER);
        setContentPane(content);
    }

    private JPanel createContentPanel() {
        JPanel content = new JPanel(new BorderLayout(0, PANEL_GAP)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                try {
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    // 先清除整个区域
                    g2.setColor(new Color(0, 0, 0, 0));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    // 再绘制圆角背景
                    g2.setColor(BACKGROUND_COLOR);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(),
                                   CORNER_RADIUS, CORNER_RADIUS);
                } finally {
                    g2.dispose();
                }
            }
        };

        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(
            PANEL_GAP, PANEL_GAP, PANEL_GAP, PANEL_GAP
        ));
        return content;
    }

    private JPanel createInnerPanel() {
        JPanel innerPanel = new JPanel(new BorderLayout(0, PANEL_GAP));
        innerPanel.setOpaque(false);
        return innerPanel;
    }

    private void styleScrollPane() {
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        // 设置滚动条宽度为2像素
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(2, 0));

        // 自定义滚动条样式
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(83, 31, 115);
                this.trackColor = BACKGROUND_COLOR;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }

            // 重写绘制滑块的方法，确保宽度为2像素
            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                if(thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                    return;
                }

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                  RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(thumbColor);
                g2.fillRect(thumbBounds.x, thumbBounds.y,
                           2, thumbBounds.height); // 固定宽度为2像素
                g2.dispose();
            }
        });
    }

    private void registerEvents() {
        EventBus.getInstance().subscribe(SearchResultsEvent.class, event -> {
            resultList.updateResults(event.results());
            boolean hasResults = !event.results().isEmpty();
            scrollPane.setVisible(hasResults);
            adjustWindowSize(hasResults);
            // 确保搜索框保持焦点
            searchBox.requestFocusInWindow();
        });

        // 修改选择监听器，只在鼠标点击时打开文件
        resultList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    SearchResult result = resultList.getSelectedValue();
                    if (result != null) {
                        FileLauncher.open(result.getFilePath());
                    }
                }
            }
        });
    }

    private void adjustWindowSize(boolean showResults) {
        int height = showResults
            ? Math.min(resultList.getPreferredSize().height + SEARCH_HEIGHT + PANEL_GAP * 2, MAX_HEIGHT)
            : SEARCH_HEIGHT + PANEL_GAP * 2;

        setSize(WINDOW_WIDTH, height);
        revalidate();
        repaint();
    }

    private void centerWindowAtTwoFifth() {
        // 获取屏幕尺寸
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // 计算窗口位置
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height / 5) - (getHeight() / 2);

        // 确保窗口不会太靠近屏幕顶部
        y = Math.max(20, y);

        setLocation(x, y);
    }

    private void setupWindowDragging() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseOffset = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mouseOffset = null;
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (mouseOffset != null) {
                    Point currentLocation = getLocation();
                    // 计算新位置：当前窗口位置 + (当前鼠标位置 - 初始鼠标偏移)
                    setLocation(
                            currentLocation.x + e.getX() - mouseOffset.x,
                            currentLocation.y + e.getY() - mouseOffset.y
                    );
                }
            }
        });
    }

    private void setupKeyBindings() {
        // 获取根面板的InputMap和ActionMap
        JRootPane rootPane = getRootPane();
        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = rootPane.getActionMap();

        // 注册ESC键
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "exitAction");
        actionMap.put("exitAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setState(Frame.ICONIFIED);
            }
        });
    }
}