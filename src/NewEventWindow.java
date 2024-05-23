import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

public class NewEventWindow extends JFrame {
    JTextField nameField;
    Selector organizerSelector;
    Selector placeSelector;
    Selector typeSelector;
    JTextField dateField;

    public NewEventWindow() {
        super("New event");
        try {
            setPreferredSize(new Dimension(600, 300));
            setLocation(0, 0);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLayout(new BorderLayout());
            JPanel mainPanel = new JPanel(new GridBagLayout());

            placeSelector = new Selector("Место проведения", GetUtilities.getBuildingNames());
            organizerSelector = new Selector("Организатор", GetUtilities.getNames(true));
            typeSelector = new Selector("Тип мероприятия", GetUtilities.getEventTypes());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weighty = 1;


            JLabel nameLabel = new JLabel("Название мероприятия");
            nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            mainPanel.add(nameLabel, gbc);

            gbc.gridx = 1;
            gbc.gridwidth = 1;
            nameField = new JTextField();
            mainPanel.add(nameField, gbc);

            gbc.gridwidth = 2;
            gbc.gridx = 0;
            gbc.gridy = 1;
            mainPanel.add(placeSelector.getPanel(), gbc);

            gbc.gridy++;
            mainPanel.add(organizerSelector.getPanel(), gbc);

            gbc.gridy++;
            mainPanel.add(typeSelector.getPanel(), gbc);

            gbc.gridy++;
            gbc.gridwidth = 1;
            JLabel dateLabel = new JLabel("Дата проведения (дд.мм.гггг)");
            dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            mainPanel.add(dateLabel, gbc);

            gbc.gridx = 1;
            dateField = new JTextField();
            mainPanel.add(dateField, gbc);

            DialogButtonsPanel dialogButtonsPanel = new DialogButtonsPanel();
            dialogButtonsPanel.cancelButton.addActionListener(e -> dispose());
            dialogButtonsPanel.okButton.addActionListener(e -> applyChanges());

            add(mainPanel, BorderLayout.CENTER);
            add(dialogButtonsPanel, BorderLayout.SOUTH);

            pack();
            setVisible(true);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void applyChanges() {
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password)) {
            String insertSQL = "INSERT INTO event (name, date, type_id, organizer_id, place_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertSQL);
            statement.setString(1, nameField.getText());
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
            statement.setDate(2, sqlDate);
            int type_id = typeSelector.getSelectedID();
            statement.setInt(3, type_id);
            int organizer_id = organizerSelector.getSelectedID();
            statement.setInt(4, organizer_id);
            int place_id = placeSelector.getSelectedID();
            statement.setInt(5, place_id);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Мероприятие успешно добавлено");
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }
    }
}
