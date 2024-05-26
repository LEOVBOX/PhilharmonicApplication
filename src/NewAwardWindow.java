import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class NewAwardWindow extends JFrame {
    JTextField nameField;
    Selector artists;
    Selector events;

    public NewAwardWindow() {
        super("New event");
        try {
            setPreferredSize(new Dimension(500, 300));
            setLocation(0, 0);
            setDefaultCloseOperation(HIDE_ON_CLOSE);
            setLayout(new BorderLayout());
            JPanel mainPanel = new JPanel(new GridBagLayout());

            try {
                artists = new Selector("Артист", GetUtilities.getNamesMap("artist", "id", "last_name", "first_name", "surname"));
                events = new Selector("Мероприятие", GetUtilities.getNamesMap("event", "id", "name"));
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
            gbc.weighty = 1;


            JLabel nameLabel = new JLabel("Название награды");
            nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            mainPanel.add(nameLabel, gbc);

            gbc.gridx = 1;
            gbc.weightx = 1;
            nameField = new JTextField();
            mainPanel.add(nameField, gbc);

            gbc.gridx = 0;
            gbc.gridy++;


            gbc.gridwidth = 2;
            mainPanel.add(events.getPanel(), gbc);

            gbc.gridwidth = 2;

            gbc.gridy++;
            mainPanel.add(artists.getPanel(), gbc);

            DialogButtonsPanel dialogButtonsPanel = new DialogButtonsPanel();
            dialogButtonsPanel.cancelButton.addActionListener(e->dispose());
            dialogButtonsPanel.okButton.addActionListener(e->applyChanges());

            add(mainPanel, BorderLayout.CENTER);
            add(dialogButtonsPanel, BorderLayout.SOUTH);

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
            int artist_id = artists.getSelectedID();
            statement.setInt(1, artist_id);
            int event_id = events.getSelectedID();
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

