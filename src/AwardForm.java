import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class AwardForm extends JFrame {

    JTextField nameField;
    Selector artists;
    Selector events;
    JPanel mainPanel;

    public AwardForm() {
        initMainPanel();
    }
    protected void initMainPanel() {
        mainPanel = new JPanel(new GridBagLayout());
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

        try {
            artists = new Selector("Артист", GetUtilities.getNamesMap("artist", "id", "last_name", "first_name", "surname"));
            events = new Selector("Мероприятие", GetUtilities.getNamesMap("event", "id", "name"));
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }

        gbc.gridwidth = 2;
        mainPanel.add(events.getPanel(), gbc);

        gbc.gridwidth = 2;
        gbc.gridy++;
        mainPanel.add(artists.getPanel(), gbc);

    }
}
