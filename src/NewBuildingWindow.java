import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Objects;

public class NewBuildingWindow extends BuildingForm {
    public NewBuildingWindow() {
        super();
        try {
            setPreferredSize(new Dimension(500, 300));
            setLocation(0, 0);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLayout(new BorderLayout());

            add(mainPanel, BorderLayout.CENTER);
            add(mainButtonsPanel, BorderLayout.SOUTH);

            pack();
            setVisible(true);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
            JOptionPane.showMessageDialog(this, "Театр успешно добавлен");
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
            JOptionPane.showMessageDialog(this, "Кинотеатр успешно добавлен");
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
            JOptionPane.showMessageDialog(this, "Эстрада успешно добавлена");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }

    }

    @Override
    void initMainButtonPanel() {
        mainButtonsPanel = new JPanel();
        mainButtonsPanel.add(Box.createHorizontalGlue());
        mainButtonsPanel.setLayout(new BoxLayout(mainButtonsPanel, BoxLayout.X_AXIS));
        JButton cancelButton = new JButton("отмена");
        cancelButton.addActionListener(e -> dispose());
        mainButtonsPanel.add(cancelButton);
        JButton okButton = new JButton("далее");
        okButton.addActionListener(e -> showUniqueParamsPanel(typeSelector.getSelectedName()));
        mainButtonsPanel.add(okButton);
    }

    @Override
    void applyChanges() {
        Integer building_id = null;
        // Добавляем новую запись в таблицу building
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password)) {
            String insertSQL = "INSERT INTO building (name, address, capacity, type_id) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            try {
                statement.setString(1, nameField.getText());
                statement.setString(2, addressField.getText());
                int capacity = Integer.parseInt(capacityField.getText());
                statement.setInt(3, capacity);
                statement.setInt(4, typeSelector.getSelectedID());
            }
            catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Все строки формы должны быть заполнены");
                return;
            }

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
            if (Objects.equals(typeSelector.getSelectedName(), "Театр")) {
                insertTheatre(building_id);
            } else if (Objects.equals(typeSelector.getSelectedName(), "Кинотеатр")) {
                insertCinema(building_id);
            } else if (Objects.equals(typeSelector.getSelectedName(), "Эстрада")) {
                insertEstrade(building_id);
            }
            dispose();
        }
    }
}
