import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public abstract class BuildingForm extends JFrame {
    protected JTextField capacityField;

    protected JTextField nameField;

    protected JTextField addressField;
    protected Selector typeSelector;

    protected TheatreParamsPanel theatreParamsPanel;

    protected CinemaParamsPanel cinemaParamsPanel;

    protected EstradeParamsPanel estradeParamsPanel;
    protected JPanel typeParamsPanel;
    protected JPanel mainPanel;
    protected JPanel mainButtonsPanel;
    protected JPanel paramsButtonsPanel;

    public BuildingForm() {
        initMainPanel();
        initParamsButtonPanel();
        initMainButtonPanel();
    }

    protected void initMainPanel() {
        mainPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 0;

        JLabel nameLabel = new JLabel("Название");
        nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        mainPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        nameField = new JTextField();
        mainPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel placeLabel = new JLabel("Адрес");
        placeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        mainPanel.add(placeLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        addressField = new JTextField();
        mainPanel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1;
        gbc.gridwidth = 2;
        try {
            typeSelector = new Selector("Тип", GetUtilities.getNamesMap("building_type", "type_id", "type_label"));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "При выполнении запроса произошла ошибка" + e);
        }
        mainPanel.add(typeSelector.getPanel(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        JLabel capacityLabel = new JLabel("Вместимость");
        capacityLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        mainPanel.add(capacityLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        capacityField = new JTextField();
        mainPanel.add(capacityField, gbc);

    }

    protected void showMainPanel() {
        this.getContentPane().removeAll();
        this.getContentPane().add(mainPanel, BorderLayout.CENTER);
        this.getContentPane().add(mainButtonsPanel, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    protected void initParamsButtonPanel() {
        paramsButtonsPanel = new JPanel();
        paramsButtonsPanel.add(Box.createHorizontalGlue());
        paramsButtonsPanel.setLayout(new BoxLayout(paramsButtonsPanel, BoxLayout.X_AXIS));
        JButton backButton = new JButton("назад");
        backButton.addActionListener(e -> showMainPanel());
        JButton confirmButton = new JButton("добавить");
        confirmButton.addActionListener(e -> applyChanges());
        paramsButtonsPanel.add(backButton);
        paramsButtonsPanel.add(confirmButton);
    }

    abstract void initMainButtonPanel();

    protected void showUniqueParamsPanel(String selectedType) {
        switch (selectedType) {
            case "Театр" -> {
                if (theatreParamsPanel == null) {
                    theatreParamsPanel = new TheatreParamsPanel();
                }

                typeParamsPanel = theatreParamsPanel;
            }
            case "Кинотеатр" -> {
                if (cinemaParamsPanel == null) {
                    cinemaParamsPanel = new CinemaParamsPanel();
                }

                typeParamsPanel = cinemaParamsPanel;
            }
            case "Эстрада" -> {
                if (estradeParamsPanel == null) {
                    estradeParamsPanel = new EstradeParamsPanel();
                }

                typeParamsPanel = estradeParamsPanel;
            }
        }

        this.getContentPane().removeAll();
        this.getContentPane().add(typeParamsPanel, BorderLayout.CENTER);
        this.getContentPane().add(paramsButtonsPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }


    abstract void applyChanges();
}
