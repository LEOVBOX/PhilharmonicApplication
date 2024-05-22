import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;

public class NewAwardWindow extends JFrame {
    JTextField nameField;
    JComboBox<String> eventSelector;

    JComboBox<String> artistSelector;

    HashMap<String, Integer> artists;
    HashMap<String, Integer> events;

    JPanel buttonsPanel;


    public NewAwardWindow() {
        super("New event");
        try {
            setPreferredSize(new Dimension(500, 300));
            setLocation(0, 0);
            setDefaultCloseOperation(HIDE_ON_CLOSE);
            setLayout(new BorderLayout());
            JPanel mainPanel = new JPanel(new GridBagLayout());

            try {
                artists = GetUtilities.getNames(false);
                events = GetUtilities.getCompetitions();
            }
            catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                        + e.getMessage());
            }

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1;
            gbc.weighty = 1;


            JLabel nameLabel = new JLabel("Название награды");
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

            JLabel placeLabel = new JLabel("Название мероприятия");
            placeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            mainPanel.add(placeLabel, gbc);

            gbc.gridx = 1;
            gbc.gridwidth = 2;
            eventSelector = new JComboBox<>(events.keySet().toArray(new String[0]));
            mainPanel.add(eventSelector, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 1;
            JLabel organizerLabel = new JLabel("ФИО артиста");
            organizerLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            mainPanel.add(organizerLabel, gbc);

            gbc.gridx = 1;
            gbc.gridwidth = 2;
            artistSelector = new JComboBox<>(artists.keySet().toArray(new String[0]));
            mainPanel.add(artistSelector, gbc);

            buttonsPanel = new JPanel();
            buttonsPanel.add(Box.createHorizontalGlue());
            buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
            JButton cancelButton = new JButton("отмена");
            cancelButton.addActionListener(e -> dispose());
            buttonsPanel.add(cancelButton);

            gbc.gridx = 2;
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

    public void applyChanges() {
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password)) {
            String insertSQL = "INSERT INTO awards (artist_id, event_id, name) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertSQL);
            int artist_id = artists.get((String)artistSelector.getSelectedItem());
            statement.setInt(1, artist_id);
            int event_id = events.get((String)eventSelector.getSelectedItem());
            statement.setInt(2, event_id);
            statement.setString(3, nameField.getText());

            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Награда успешно добавлена");
            dispose();
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }
    }
}

