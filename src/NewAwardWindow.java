import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class NewAwardWindow extends AwardForm{
    public NewAwardWindow() {
        super();
        try {
            setPreferredSize(new Dimension(500, 300));
            setLocation(0, 0);
            setDefaultCloseOperation(HIDE_ON_CLOSE);
            setLayout(new BorderLayout());

            DialogButtonsPanel dialogButtonsPanel = new DialogButtonsPanel();
            dialogButtonsPanel.cancelButton.addActionListener(e->dispose());
            dialogButtonsPanel.okButton.addActionListener(e->applyChanges());

            add(dialogButtonsPanel, BorderLayout.SOUTH);
            add(mainPanel, BorderLayout.CENTER);

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

