import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class NewArtistEventWindow extends JFrame {
    Selector artistsSelector;
    Selector eventsSelector;
    JPanel mainPanel;

    public NewArtistEventWindow() {
        super("Add artist to event");
        try {
            setPreferredSize(new Dimension(500, 300));
            setResizable(true);
            setLocation(0, 0);
            setDefaultCloseOperation(HIDE_ON_CLOSE);
            setLayout(new BorderLayout());
            setLayout(new BorderLayout());
            try {
                eventsSelector = new Selector("Мероприятие", GetUtilities.getNamesMap("event", "id", "name"));
                artistsSelector = new Selector("Артист", GetUtilities.getNamesMap("artist", "id", "last_name", "first_name", "surname"));

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                        + e.getMessage());
            }
            mainPanel = new JPanel(new GridBagLayout());


            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1;
            gbc.weighty = 1;


            gbc.gridwidth = 1;
            mainPanel.add(artistsSelector.getPanel(), gbc);

            gbc.gridy++;
            mainPanel.add(eventsSelector.getPanel(), gbc);

            DialogButtonsPanel dialogButtonsPanel = new DialogButtonsPanel();
            dialogButtonsPanel.okButton.addActionListener(e -> applyChanges());
            dialogButtonsPanel.cancelButton.addActionListener(e -> dispose());

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
            String insertSQL = "INSERT INTO artist_event (artist_id, event_id) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertSQL);
            int artist_id = artistsSelector.getSelectedID();
            statement.setInt(1, artist_id);
            int event_id = eventsSelector.getSelectedID();
            statement.setInt(2, event_id);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Артист успешно добавлен");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }
    }
}
