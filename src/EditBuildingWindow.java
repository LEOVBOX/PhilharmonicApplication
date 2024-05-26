import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Objects;

public class EditBuildingWindow extends BuildingForm{
    Selector entitySelector;
    JPanel bottomButtonsPanel;
    JButton deleteButton;
    JButton editButton;
    JButton cancelButton;
    JButton okButton;
    JButton goBackButton;
    JPanel selectPanel;

    private void initSelectPanel() {
        selectPanel = new JPanel(new BorderLayout());
        selectPanel.add(entitySelector.getPanel(), BorderLayout.CENTER);
        selectPanel.add(bottomButtonsPanel, BorderLayout.SOUTH);
    }

    public EditBuildingWindow() {
        super();
        try {
            setLayout(new BorderLayout());

            setSize(new Dimension(400, 200));
            setResizable(true);
            setLocation(0, 0);
            setDefaultCloseOperation(HIDE_ON_CLOSE);

            initBottomPanel();
            entitySelector = new Selector("Сооружение", GetUtilities.getNamesMap("building", "id", "name", "address"));

            initSelectPanel();

            add(selectPanel, BorderLayout.CENTER);

            pack();
            setVisible(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }
    }

    public void openEditPanel() {
        String sql = "SELECT name, address, capacity, type_id FROM building WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, entitySelector.getSelectedID());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    nameField.setText(resultSet.getString("name"));
                    addressField.setText(resultSet.getString("address"));
                    capacityField.setText(resultSet.getString("capacity"));
                    typeSelector.setSelectedID(resultSet.getInt("type_id"));
                }

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }

        this.setSize(new Dimension(600, 300));
        showMainPanel();
    }

    public void openSelectPanel() {
        this.setSize(new Dimension(400, 200));
        this.getContentPane().removeAll();
        this.getContentPane().add(selectPanel, BorderLayout.CENTER);
        this.getContentPane().add(bottomButtonsPanel, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    @Override
    void applyChanges() {
        String sql = "UPDATE building SET name = ?, address = ?, capacity = ?, type_id = ? WHERE id = ?";
        // Добавляем новую запись в таблицу building
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password)) {
            PreparedStatement statement = connection.prepareStatement(sql);
            try {
                statement.setString(1, nameField.getText());
                statement.setString(2, addressField.getText());
                int capacity = Integer.parseInt(capacityField.getText());
                statement.setInt(3, capacity);
                statement.setInt(4, typeSelector.getSelectedID());
                statement.setInt(5, entitySelector.getSelectedID());

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Все строки формы должны быть заполнены");
                return;
            }


            // Добавляем новую запись в таблицу параметров типа
            if (Objects.equals(typeSelector.getSelectedName(), "Театр")) {
                updateTheatre(entitySelector.getSelectedID());
            } else if (Objects.equals(typeSelector.getSelectedName(), "Кинотеатр")) {
                updateCinema(entitySelector.getSelectedID());
            } else if (Objects.equals(typeSelector.getSelectedName(), "Эстрада")) {
                updateEstrade(entitySelector.getSelectedID());
            }
            dispose();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }
    }

    private void delete() {
        String sql = "DELETE FROM building WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, entitySelector.getSelectedID());
            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Удаление усепешно");
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }
    }

    private void initBottomPanel() {
        bottomButtonsPanel = new JPanel();
        bottomButtonsPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;

        deleteButton = new JButton("удалить");
        bottomButtonsPanel.add(deleteButton, gbc);

        gbc.gridx++;
        editButton = new JButton("изменить");
        bottomButtonsPanel.add(editButton, gbc);

        gbc.gridx++;
        cancelButton = new JButton("отмена");
        bottomButtonsPanel.add(cancelButton, gbc);

        editButton.addActionListener(e -> openEditPanel());
        cancelButton.addActionListener(e -> dispose());
        deleteButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null, "Нажмите OK, чтобы продолжить", "Диалоговое окно", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                delete();
            }
        });

    }

    @Override
    void initMainButtonPanel() {
        mainButtonsPanel = new JPanel();
        mainButtonsPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridy = 0;
        gbc.gridx = 0;

        goBackButton = new JButton("назад");
        goBackButton.addActionListener(e -> openSelectPanel());
        mainButtonsPanel.add(goBackButton, gbc);

        gbc.gridx++;
        okButton = new JButton("далее");
        okButton.addActionListener(e -> {
            try {
                showUniqueParamsPanel(typeSelector.getSelectedName(), entitySelector.getSelectedID());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        mainButtonsPanel.add(okButton, gbc);

        gbc.gridx++;
        JButton cancelButton = new JButton("отмена");
        cancelButton.addActionListener(e -> dispose());
        mainPanel.add(cancelButton);
    }

    private void updateTheatre(int building_id) {
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password)) {
            String insertSQL = "UPDATE theatre SET parter_capacity = ?, benoir_capacity = ?, mezzanine_capacity = ?, first_tier_capacity = ?," +
                    "second_tier_capacity = ?, amphitheatre_capacity = ?, galerka_capacity = ? WHERE building_id = ?";
            PreparedStatement statement = connection.prepareStatement(insertSQL);

            statement.setInt(1, theatreParamsPanel.getParterCapacity());
            statement.setInt(2, theatreParamsPanel.getBenoirCapacity());
            statement.setInt(3, theatreParamsPanel.getMezzanineCapacity());
            statement.setInt(4, theatreParamsPanel.getFirstTierCapacity());
            statement.setInt(5, theatreParamsPanel.getSecondTierCapacity());
            statement.setInt(6, theatreParamsPanel.getAmphitheatreCapacity());
            statement.setInt(7, theatreParamsPanel.getGalerkaCapacity());
            statement.setInt(8, building_id);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Театр успешно обновлен");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }
    }

    private void updateEstrade(int building_id) {
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password)) {
            String insertSQL = "UPDATE estrade SET stage_width = ?, stage_depth = ? WHERE building_id = ?";
            PreparedStatement statement = connection.prepareStatement(insertSQL);
            statement.setInt(1, estradeParamsPanel.getStageWidth());
            statement.setInt(2, estradeParamsPanel.getStageDepth());
            statement.setInt(3, building_id);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Эстрада успешно обновлена");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }
    }

    private void updateCinema(int building_id) {
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password)) {
            String insertSQL = "UPDATE cinema SET screen_width = ?, screen_height = ? WHERE building_id = ?";
            PreparedStatement statement = connection.prepareStatement(insertSQL);
            statement.setInt(1, cinemaParamsPanel.getScreenWidth());
            statement.setInt(2, cinemaParamsPanel.getScreenHeight());
            statement.setInt(3, building_id);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Кинотеатр успешно добавлен");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }
    }
}
