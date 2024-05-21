import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Objects;

public class NewBuildingWindow extends JFrame {
    HashMap<String, Integer> buildingTypes;

    JTextField capacityField;

    JTextField nameField;

    JTextField addressField;
    JComboBox<String> typeSelector;

    JPanel typeParamsPanel;
    JPanel mainPanel;
    JPanel mainButtonsPanel;
    JPanel paramsButtonsPanel;

    TheatreParamsPanel theatreParamsPanel;

    CinemaParamsPanel cinemaParamsPanel;

    EstradeParamsPanel estradeParamsPanel;


    public NewBuildingWindow() {
        super("New building");
        try {
            setPreferredSize(new Dimension(500, 300));
            setLocation(0, 0);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLayout(new BorderLayout());
            buildingTypes = GetUtilities.getBuildingTypes();
            mainPanel = new JPanel(new GridBagLayout());


            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1;
            gbc.weighty = 1;
            gbc.insets = new Insets(10, 10, 10, 10);


            JLabel nameLabel = new JLabel("Название");
            nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            mainPanel.add(nameLabel, gbc);

            gbc.gridx = 1;
            gbc.weightx = 1;
            nameField = new JTextField();
            mainPanel.add(nameField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;

            JLabel placeLabel = new JLabel("Адрес");
            placeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            mainPanel.add(placeLabel, gbc);

            gbc.gridx = 1;
            addressField = new JTextField();
            mainPanel.add(addressField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            JLabel organizerLabel = new JLabel("Тип");
            organizerLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            mainPanel.add(organizerLabel, gbc);

            gbc.gridx = 1;
            typeSelector = new JComboBox<>(buildingTypes.keySet().toArray(new String[0]));
            mainPanel.add(typeSelector, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            JLabel capacityLabel = new JLabel("Вместимость");
            capacityLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            mainPanel.add(capacityLabel, gbc);

            gbc.gridx = 1;
            capacityField = new JTextField();
            mainPanel.add(capacityField, gbc);

            gbc.weightx = 1.0;
            gbc.weighty = 1.0;

            initMainButtonPanel();
            initParamsButtonPanel();

            add(mainPanel, BorderLayout.CENTER);
            add(mainButtonsPanel, BorderLayout.SOUTH);

            pack();
            setVisible(true);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initMainButtonPanel() {
        mainButtonsPanel = new JPanel();
        mainButtonsPanel.add(Box.createHorizontalGlue());
        mainButtonsPanel.setLayout(new BoxLayout(mainButtonsPanel, BoxLayout.X_AXIS));
        JButton cancelButton = new JButton("отмена");
        cancelButton.addActionListener(e -> dispose());
        mainButtonsPanel.add(cancelButton);
        JButton okButton = new JButton("далее");
        okButton.addActionListener(e -> showUniqueParamsPanel());
        mainButtonsPanel.add(okButton);
    }

    private void insertTheatre(int building_id) {
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password)) {
            String insertSQL = "INSERT INTO theatre (building_id, parter_capacity, benoir_capacity, mezzanine_capacity," +
                    "first_tier_capacity, second_tier_capacity, amphitheatre_capacity, galerka_capacity)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertSQL);
            statement.setInt(1, building_id);
            statement.setInt(2, theatreParamsPanel.getParterCapacity());
            statement.setInt(3, theatreParamsPanel.getBenoirCapacity());
            statement.setInt(4, theatreParamsPanel.getMezzanineCapacity());
            statement.setInt(5, theatreParamsPanel.getFirstTierCapacity());
            statement.setInt(6, theatreParamsPanel.getSecondTierCapacity());
            statement.setInt(7, theatreParamsPanel.getAmphitheatreCapacity());
            statement.setInt(8, theatreParamsPanel.getGalerkaCapacity());

            statement.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }
    }

    private void insertCinema(int building_id) {
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password)) {
            String insertSQL = "INSERT INTO cinema (building_id, screen_width, screen_height) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertSQL);
            statement.setInt(1, building_id);
            statement.setInt(2, cinemaParamsPanel.getScreenWidth());
            statement.setInt(3, cinemaParamsPanel.getScreenHeight());

            statement.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }
    }

    private void insertEstrade(int building_id) {
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password)) {
            String insertSQL = "INSERT INTO estrade (building_id, stage_width, stage_depth) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertSQL);
            statement.setInt(1, building_id);
            statement.setInt(2, estradeParamsPanel.getStageWidth());
            statement.setInt(3, estradeParamsPanel.getStageHeight());

            statement.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }

    }

    private void applyChanges() {
        Integer building_id = null;
        // Добавляем новую запись в таблицу building
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password)) {
            //String query = createBuildingInsertQuery();
            String insertSQL = "INSERT INTO building (name, address, capacity, type_id) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            try {
                statement.setString(1, nameField.getText());
                statement.setString(2, addressField.getText());
                int capacity = Integer.parseInt(capacityField.getText());
                statement.setInt(3, capacity);
                statement.setInt(4, buildingTypes.get((String) typeSelector.getSelectedItem()));
            }
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Все строки формы должны быть заполнены");
                return;
            }



            System.out.println(insertSQL);
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                // Получение сгенерированных ключей
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    building_id = generatedKeys.getInt(1);
                    System.out.println("Новая строка вставлена с ID: " + building_id);
                }
            } else {
                System.out.println("Вставка не удалась, строк не затронуто.");
                return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }

        // Добавляем новую запись в таблицу параметров типа
        if (building_id != null) {
            if (Objects.equals(typeSelector.getSelectedItem(), "Театр")) {
                insertTheatre(building_id);
            } else if (Objects.equals(typeSelector.getSelectedItem(), "Кинотеатр")) {
                insertCinema(building_id);
            } else if (Objects.equals(typeSelector.getSelectedItem(), "Эстрада")) {
                insertEstrade(building_id);
            }
            dispose();
        }
    }

    private void initParamsButtonPanel() {
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

    private void showMainPanel() {
        remove(typeParamsPanel);
        remove(paramsButtonsPanel);
        add(mainPanel, BorderLayout.CENTER);
        add(mainButtonsPanel, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private void showUniqueParamsPanel() {
        String selectedType = (String) Objects.requireNonNull(typeSelector.getSelectedItem());
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

        remove(mainPanel);
        remove(mainButtonsPanel);
        add(typeParamsPanel, BorderLayout.CENTER);
        add(paramsButtonsPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }
}
