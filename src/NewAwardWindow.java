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


    public NewAwardWindow() {
        super("New event");
        try {
            setPreferredSize(new Dimension(400, 200));
            setResizable(false);
            setLocation(0, 0);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLayout(new BorderLayout());
            JPanel mainPanel = new JPanel(new GridBagLayout());

            try {
                artists = GetUtilities.getNames(false);
                events = GetUtilities.getEvents();
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

            gbc.gridx = 1;
            gbc.gridy = 4;
            gbc.gridwidth = 1;
            JButton cancelButton = new JButton("cancel");
            cancelButton.addActionListener(e -> dispose());
            mainPanel.add(cancelButton, gbc);

            gbc.gridx = 2;
            JButton okButton = new JButton("ok");
            mainPanel.add(okButton, gbc);

            add(mainPanel, BorderLayout.CENTER);

            pack();
            setVisible(true);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}

