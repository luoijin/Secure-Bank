package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Style {
    // Modern Color Palette - Dark Blue Accent
    public static final Color PRIMARY = new Color(30, 58, 138);        // Deep blue
    public static final Color PRIMARY_HOVER = new Color(29, 78, 216);  // Brighter blue on hover
    public static final Color PRIMARY_LIGHT = new Color(219, 234, 254); // Light blue background
    
    public static final Color BACKGROUND = new Color(248, 250, 252);    // Soft gray background
    public static final Color CARD_BG = Color.WHITE;
    public static final Color CARD_HOVER = new Color(248, 250, 252);
    
    public static final Color TEXT_PRIMARY = new Color(15, 23, 42);     // Almost black
    public static final Color TEXT_SECONDARY = new Color(100, 116, 139); // Medium gray
    public static final Color TEXT_MUTED = new Color(148, 163, 184);     // Light gray
    
    public static final Color SUCCESS = new Color(34, 197, 94);          // Green
    public static final Color SUCCESS_LIGHT = new Color(220, 252, 231);  // Light green
    public static final Color DANGER = new Color(239, 68, 68);           // Red
    public static final Color DANGER_LIGHT = new Color(254, 226, 226);   // Light red
    public static final Color WARNING = new Color(251, 146, 60);         // Orange
    
    public static final Color BORDER = new Color(226, 232, 240);         // Subtle border
    public static final Color SHADOW = new Color(0, 0, 0, 8);           // Soft shadow
    
    // Typography
    private static final Font FONT_DISPLAY = new Font("Segoe UI", Font.BOLD, 32);
    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 18);
    @SuppressWarnings("unused")
	private static final Font FONT_SUBHEADING = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);

    // Create Primary Button with modern styling
    public static JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(PRIMARY);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(200, 45));
        
        // Add subtle shadow and rounded corners feel
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 1),
            BorderFactory.createEmptyBorder(12, 24, 12, 24)
        ));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(PRIMARY_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(PRIMARY);
            }
        });

        return btn;
    }

    // Create Secondary Button
    public static JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(Color.WHITE);
        btn.setForeground(TEXT_PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(BACKGROUND);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(Color.WHITE);
            }
        });

        return btn;
    }
    
    // Create Success Button
    public static JButton createSuccessButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(SUCCESS);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(22, 163, 74));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(SUCCESS);
            }
        });

        return btn;
    }

    // Create Danger Button
    public static JButton createDangerButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(DANGER);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(220, 38, 38));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(DANGER);
            }
        });

        return btn;
    }

    // Modern Text Field with focus effects
    public static JTextField createModernTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(FONT_BODY);
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 2),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        
        // Add focus listener for better UX
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY, 2),
                    BorderFactory.createEmptyBorder(12, 16, 12, 16)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER, 2),
                    BorderFactory.createEmptyBorder(12, 16, 12, 16)
                ));
            }
        });
        
        return field;
    }

    // Modern Password Field
    public static JPasswordField createModernPasswordField(int columns) {
        JPasswordField field = new JPasswordField(columns);
        field.setFont(FONT_BODY);
        field.setBackground(Color.WHITE);
        field.setForeground(TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 2),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        
        // Add focus listener
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY, 2),
                    BorderFactory.createEmptyBorder(12, 16, 12, 16)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER, 2),
                    BorderFactory.createEmptyBorder(12, 16, 12, 16)
                ));
            }
        });
        
        return field;
    }

    // Modern Card with subtle shadow effect
    public static JPanel createCard() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        return panel;
    }
    
    // Create elevated card (with visual depth)
    public static JPanel createElevatedCard() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createMatteBorder(4, 0, 0, 0, PRIMARY_LIGHT)
            ),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        return panel;
    }

    // Display Title (largest)
    public static JLabel createDisplayTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_DISPLAY);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    // Title
    public static JLabel createTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_TITLE);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }
    
    // Heading
    public static JLabel createHeading(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_HEADING);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    // Subtitle
    public static JLabel createSubtitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_BODY);
        label.setForeground(TEXT_SECONDARY);
        return label;
    }
    
    // Muted Text
    public static JLabel createMutedText(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_SMALL);
        label.setForeground(TEXT_MUTED);
        return label;
    }
    
    // Badge component
    public static JLabel createBadge(String text, Color bgColor, Color fgColor) {
        JLabel badge = new JLabel(text);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge.setForeground(fgColor);
        badge.setBackground(bgColor);
        badge.setOpaque(true);
        badge.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));
        return badge;
    }
    
    // Info Panel (colored background panel)
    public static JPanel createInfoPanel(Color bgColor) {
        JPanel panel = new JPanel();
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        return panel;
    }
    
    // Stat Card (for dashboard metrics)
    public static JPanel createStatCard(String label, String value, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(0, 8));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createMatteBorder(3, 0, 0, 0, accentColor)
            ),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(FONT_SMALL);
        labelComp.setForeground(TEXT_SECONDARY);
        
        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueComp.setForeground(TEXT_PRIMARY);
        
        card.add(labelComp, BorderLayout.NORTH);
        card.add(valueComp, BorderLayout.CENTER);
        
        return card;
    }

    // Helper method to style existing components
    public static void styleTextField(JTextField field) {
        field.setFont(FONT_BODY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 2),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));
        field.setBackground(Color.WHITE);
    }

    public static JLabel createLabel(String text, boolean bold) {
        JLabel label = new JLabel(text);
        label.setFont(bold ? FONT_HEADING : FONT_BODY);
        label.setForeground(bold ? TEXT_PRIMARY : TEXT_SECONDARY);
        return label;
    }

    public static JPanel createCardPanel() {
        return createCard();
    }

    public static JLabel createDialogTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 22));
        label.setForeground(TEXT_PRIMARY);
        label.setBorder(new EmptyBorder(0, 0, 20, 0));
        return label;
    }
    
    // Create section divider
    public static JSeparator createDivider() {
        JSeparator separator = new JSeparator();
        separator.setForeground(BORDER);
        separator.setBackground(BORDER);
        return separator;
    }
}