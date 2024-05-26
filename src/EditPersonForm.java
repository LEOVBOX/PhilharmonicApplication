import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class EditPersonForm extends JFrame {
    JTextField firstName;
    JTextField lastName;
    JTextField surname;
    Selector entitySelector;
    JPanel editPanel;
    JPanel bottomButtonsPanel;
    JButton deleteButton;
    JButton editButton;
    JButton cancelButton;

    JButton okButton;
    JButton goBackButton;

    JPanel selectPanel;

    JPanel confirmButtonPanel;

    String tableName;

    private void initSelectPanel() {
        selectPanel = new JPanel(new BorderLayout());
        selectPanel.add(entitySelector.getPanel(), BorderLayout.CENTER);
        selectPanel.add(bottomButtonsPanel, BorderLayout.SOUTH);
    }

    private void initEditPanel() {
        editPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 1;


        JLabel nameLabel = new JLabel("Имя");
        nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        editPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.gridwidth = 2;
        firstName = new JTextField();
        editPanel.add(firstName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        JLabel lastNameLabel = new JLabel("Фамилия");
        lastNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        editPanel.add(lastNameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        lastName = new JTextField();
        editPanel.add(lastName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        JLabel surnameLabel = new JLabel("Отчество");
        surnameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        editPanel.add(surnameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        surname = new JTextField();
        editPanel.add(surname, gbc);

    }

    public EditPersonForm(String tableName) {
        super("Edit " + tableName);
        this.tableName = tableName;
        try {
            setLayout(new BorderLayout());

            setSize(new Dimension(400, 200));
            setResizable(true);
            setLocation(0, 0);
            setDefaultCloseOperation(HIDE_ON_CLOSE);


            initBottomPanel();
            initConfirmButtonPanel();
            entitySelector = new Selector("Выбор персоны", GetUtilities.getNames(tableName));

            initSelectPanel();
            initEditPanel();

            add(selectPanel, BorderLayout.CENTER);

            pack();
            setVisible(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }
    }

    public void openEditPanel() {
        String preparedSQL = "SELECT first_name, last_name, surname FROM %s WHERE id = ?";
        String sql = String.format(preparedSQL, tableName);
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, entitySelector.getSelectedID());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    firstName.setText(resultSet.getString("first_name"));
                    lastName.setText(resultSet.getString("last_name"));
                    surname.setText(resultSet.getString("surname"));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }


        this.setSize(new Dimension(500, 300));
        this.getContentPane().removeAll();
        this.getContentPane().add(editPanel, BorderLayout.CENTER);
        this.getContentPane().add(confirmButtonPanel, BorderLayout.SOUTH);
        revalidate();
        repaint();

    }

    public void openSelectPanel() {
        this.setSize(new Dimension(400, 200));
        this.getContentPane().removeAll();
        this.getContentPane().add(selectPanel, BorderLayout.CENTER);
        this.getContentPane().add(bottomButtonsPanel, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private void applyChanges() {
        String preparedSQL = "UPDATE %s SET first_name = ?, last_name = ?, surname = ? WHERE id = ?";
        String sql = String.format(preparedSQL, tableName);
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, firstName.getText());
            statement.setString(2, lastName.getText());
            statement.setString(3, surname.getText());
            statement.setInt(4, entitySelector.getSelectedID());

            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Запись успешно изменена");
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }
    }

    private void delete() {

        String preparedSQL = "DELETE FROM %s WHERE id = ?";
        String sql = String.format(preparedSQL, tableName);
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
            // Показ диалогового окна
            int result = JOptionPane.showConfirmDialog(null, "Нажмите OK, чтобы продолжить", "Диалоговое окно", JOptionPane.OK_CANCEL_OPTION);

            // Проверка, была ли нажата кнопка OK
            if (result == JOptionPane.OK_OPTION) {
                // Выполнение события после нажатия OK
                delete();
                // Ваш код здесь
            }
        });

    }

    private void initConfirmButtonPanel() {
        confirmButtonPanel = new JPanel();
        confirmButtonPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridy = 0;
        gbc.gridx = 0;

        goBackButton = new JButton("назад");
        goBackButton.addActionListener(e -> openSelectPanel());
        confirmButtonPanel.add(goBackButton, gbc);

        gbc.gridx++;
        okButton = new JButton("применить изменения");
        okButton.addActionListener(e -> applyChanges());
        confirmButtonPanel.add(okButton, gbc);

        gbc.gridx++;
        JButton cancelButton = new JButton("отмена");
        cancelButton.addActionListener(e -> dispose());
        confirmButtonPanel.add(cancelButton);
    }
}
