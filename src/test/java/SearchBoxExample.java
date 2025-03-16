/**
 * @author Lion
 * @date 2025/2/18 22:55
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;

public class SearchBoxExample extends JFrame {
    private JTextField searchField;

    public SearchBoxExample() {
        setTitle("Search Box Example");
        setSize(400, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setLocationRelativeTo(null);

        // Create a panel with a custom background
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(240, 240, 240));
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
            }
        };
        panel.setLayout(new BorderLayout());

        // Create the search field
        searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        searchField.setBackground(new Color(240, 240, 240));
        searchField.setForeground(Color.BLACK);
        searchField.setCaretColor(Color.BLACK);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performSearch();
                }
            }
        });

        // Add the search field to the panel
        panel.add(searchField, BorderLayout.CENTER);

        // Add the panel to the frame
        add(panel);

        // Set the frame's shape to be rounded
        setShape(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
    }

    private void performSearch() {
        String query = searchField.getText().trim();
        if (!query.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Searching for: " + query);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SearchBoxExample example = new SearchBoxExample();
            example.setVisible(true);
        });
    }
}

