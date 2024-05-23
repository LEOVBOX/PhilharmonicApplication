import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;

public class NewArtistEventWindow extends JFrame {
    HashMap<String, Integer> artists;
    HashMap<String, Integer> events;

    JPanel mainPanel;
    JComboBox<String> eventSelector;
    JComboBox<String> artistSelector;

    JPanel buttonsPanel;
    JButton okButton;
    JButton cancelButton;

    public NewArtistEventWindow() {
        super("Add artist to event");
        try {
            setPreferredSize(new Dimension(500, 300));
            setResizable(true);
            setLocation(0, 0);
            setDefaultCloseOperation(HIDE_ON_CLOSE);
            setLayout(new BorderLayout());
            setLayout(new BorderLayout());
            artists = new HashMap<>();
            try {
                events = GetUtilities.getEvents();
                artists = GetUtilities.getNames(false);

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                        + e.getMessage());
            }
            mainPanel = new JPanel(new GridBagLayout());


            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1;
            gbc.weighty = 1;

            JLabel nameLabel = new JLabel("Выберете артиста");
            mainPanel.add(nameLabel, gbc);

            gbc.gridx = 1;
            gbc.weightx = 1;
            gbc.gridwidth = 1;
            artistSelector = new JComboBox<>(artists.keySet().toArray(new String[0]));
            mainPanel.add(artistSelector, gbc);

            gbc.gridy = 1;
            gbc.gridx = 0;
            JLabel eventLabel = new JLabel("Выберете мероприятие");
            mainPanel.add(eventLabel, gbc);

            gbc.gridx = 1;
            eventSelector = new JComboBox<>(events.keySet().toArray(new String[0]));
            mainPanel.add(eventSelector, gbc);

            buttonsPanel = new JPanel();
            buttonsPanel.add(Box.createHorizontalGlue());
            buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));

            cancelButton = new JButton("cancel");
            cancelButton.addActionListener(e -> dispose());
            buttonsPanel.add(cancelButton);

            okButton = new JButton("ok");
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
            String insertSQL = "INSERT INTO artist_event (artist_id, event_id) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertSQL);
            int artist_id = artists.get((String) artistSelector.getSelectedItem());
            statement.setInt(1, artist_id);
            int event_id = events.get((String) eventSelector.getSelectedItem());
            statement.setInt(2, event_id);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Артист успешно добавлен");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }
    }
}
