import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.HashMap;

public class NewEventWindow extends JFrame {
    JTextField nameField;
    JComboBox<String> organizerSelector;
    JComboBox<String> placeSelector;
    JComboBox<String> typeSelector;
    HashMap<String, Integer> buildings;
    HashMap<String, Integer> impresario;
    HashMap<String, Integer> eventTypes;
    JPanel buttonsPanel;
    JTextField dateField;

    public NewEventWindow() {
        super("New event");
        try {
            setPreferredSize(new Dimension(600, 300));
            setLocation(0, 0);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLayout(new BorderLayout());
            JPanel mainPanel = new JPanel(new GridBagLayout());

            buildings = GetUtilities.getBuildingNames();
            impresario = GetUtilities.getNames(true);
            eventTypes = GetUtilities.getEventTypes();

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1;
            gbc.weighty = 1;


            JLabel nameLabel = new JLabel("Название мероприятия");
            nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            mainPanel.add(nameLabel, gbc);

            gbc.gridx = 1;
            gbc.weightx = 1;
            gbc.gridwidth = 2;
            nameField = new JTextField();
            mainPanel.add(nameField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 1;

            JLabel placeLabel = new JLabel("Место проведения");
            placeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            mainPanel.add(placeLabel, gbc);

            gbc.gridx = 1;
            gbc.gridwidth = 2;
            placeSelector = new JComboBox<>(buildings.keySet().toArray(new String[0]));
            mainPanel.add(placeSelector, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 1;
            JLabel organizerLabel = new JLabel("Организатор");
            organizerLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            mainPanel.add(organizerLabel, gbc);

            gbc.gridx = 1;
            gbc.gridwidth = 2;
            organizerSelector = new JComboBox<>(impresario.keySet().toArray(new String[0]));
            mainPanel.add(organizerSelector, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            JLabel typeLabel = new JLabel("Тип мероприятия");
            typeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            mainPanel.add(typeLabel, gbc);

            gbc.gridx = 1;
            typeSelector = new JComboBox<>(eventTypes.keySet().toArray(new String[0]));
            mainPanel.add(typeSelector, gbc);

            gbc.gridy = 4;
            gbc.gridx = 0;
            gbc.gridwidth = 1;
            JLabel dateLabel = new JLabel("Дата проведения (дд.мм.гггг)");
            dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            mainPanel.add(dateLabel, gbc);

            gbc.gridx = 1;
            gbc.gridwidth = 2;
            dateField = new JTextField();
            mainPanel.add(dateField, gbc);

            buttonsPanel = new JPanel();
            buttonsPanel.add(Box.createHorizontalGlue());
            buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
            JButton cancelButton = new JButton("отмена");
            cancelButton.addActionListener(e -> dispose());
            buttonsPanel.add(cancelButton);
            JButton okButton = new JButton("добавить");
            okButton.addActionListener(e -> applyChanges());
            buttonsPanel.add(okButton);

            add(mainPanel, BorderLayout.CENTER);
            add(buttonsPanel, BorderLayout.SOUTH);

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
            int type_id = eventTypes.get((String)typeSelector.getSelectedItem());
            statement.setInt(3, type_id);
            int organizer_id = impresario.get((String)organizerSelector.getSelectedItem());
            statement.setInt(4, organizer_id);
            int place_id = buildings.get((String)placeSelector.getSelectedItem());
            statement.setInt(5, place_id);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Мероприятие успешно добавлено");
            dispose();
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }
    }
}
