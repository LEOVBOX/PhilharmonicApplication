import javax.swing.*;
import java.awt.*;
import java.sql.*;

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
        okButton.addActionListener(e ->
            showUniqueParamsPanel(typeSelector.getSelectedName()));
        mainButtonsPanel.add(okButton, gbc);

        gbc.gridx++;
        JButton cancelButton = new JButton("отмена");
        cancelButton.addActionListener(e -> dispose());
        mainPanel.add(cancelButton);
    }
}
