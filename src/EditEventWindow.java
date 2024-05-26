import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EditEventWindow extends JFrame {
    JTextField nameField;
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
    Selector placeSelector;
    Selector organizerSelector;
    Selector typeSelector;
    JTextField dateField;


    private void initSelectPanel() {
        selectPanel = new JPanel(new BorderLayout());
        selectPanel.add(entitySelector.getPanel(), BorderLayout.CENTER);
        selectPanel.add(bottomButtonsPanel, BorderLayout.SOUTH);
    }

    private void initEditPanel() {
        try {
            editPanel = new JPanel(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weighty = 1;


            JLabel nameLabel = new JLabel("Название мероприятия");
            nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            editPanel.add(nameLabel, gbc);

            gbc.gridx = 1;
            gbc.gridwidth = 1;
            nameField = new JTextField();
            editPanel.add(nameField, gbc);

            gbc.gridwidth = 2;
            gbc.gridx = 0;
            gbc.gridy = 1;
            placeSelector = new Selector("Место проведения",
                    GetUtilities.getNamesMap("building", "id", "name", "address"));


            editPanel.add(placeSelector.getPanel(), gbc);

            gbc.gridy++;
            organizerSelector = new Selector("Организатор",
                    GetUtilities.getNamesMap("impresario", "id", "last_name", "first_name", "surname"));
            editPanel.add(organizerSelector.getPanel(), gbc);

            gbc.gridy++;
            typeSelector = new Selector("Тип мероприятия",
                    GetUtilities.getNamesMap("event_type", "type_id", "name"));
            editPanel.add(typeSelector.getPanel(), gbc);

            gbc.gridy++;
            gbc.gridwidth = 1;
            JLabel dateLabel = new JLabel("Дата проведения (дд.мм.гггг)");
            dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            editPanel.add(dateLabel, gbc);

            gbc.gridx = 1;
            dateField = new JTextField();
            editPanel.add(dateField, gbc);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Ошибка при выполнении запроса" + e);

        }
    }

    public EditEventWindow() {
        super("Edit event");
        try {
            setLayout(new BorderLayout());

            setSize(new Dimension(400, 200));
            setResizable(true);
            setLocation(0, 0);
            setDefaultCloseOperation(HIDE_ON_CLOSE);

            initBottomPanel();
            initConfirmButtonPanel();
            entitySelector = new Selector("Мероприятие", GetUtilities.getNamesMap("event", "id", "name"));

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
        String sql = "SELECT name, date, place_id, organizer_id FROM event WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, entitySelector.getSelectedID());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    nameField.setText(resultSet.getString("name"));
                    dateField.setText(convertDateFormat(resultSet.getDate("date").toString()));
                    placeSelector.setSelectedID(resultSet.getInt("place_id"));
                    organizerSelector.setSelectedID(resultSet.getInt("organizer_id"));
                }

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }

        this.setSize(new Dimension(600, 300));
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
        String insertSQL = "UPDATE event SET name = ?, date = ?, type_id = ?, organizer_id = ?, place_id = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password)) {

            PreparedStatement statement = connection.prepareStatement(insertSQL);
            statement.setInt(6, entitySelector.getSelectedID());
            Date sqlDate = null;
            try {
                // Создание объекта SimpleDateFormat с нужным форматом
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                // Преобразование строки в java.util.Date
                java.util.Date parsedDate = dateFormat.parse(dateField.getText());
                // Преобразование java.util.Date в java.sql.Date
                sqlDate = new java.sql.Date(parsedDate.getTime());
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(this, "Неверный формат даты\n"
                        + e.getMessage());
            }
            statement.setString(1, nameField.getText());
            statement.setDate(2, sqlDate);
            int type_id = typeSelector.getSelectedID();
            statement.setInt(3, type_id);
            int organizer_id = organizerSelector.getSelectedID();
            statement.setInt(4, organizer_id);
            int place_id = placeSelector.getSelectedID();
            statement.setInt(5, place_id);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Мероприятие успешно изменено");
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }
    }

    private void delete() {
        String sql = "DELETE FROM event WHERE id = ?";
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

    public static String convertDateFormat(String date) {
        // Разделение строки по символу "-"
        String[] parts = date.split("-");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-mm-dd");
        }

        String year = parts[0];
        String month = parts[1];
        String day = parts[2];

        // Форматирование в "дд.мм.гггг"
        return day + "." + month + "." + year;
    }
}
