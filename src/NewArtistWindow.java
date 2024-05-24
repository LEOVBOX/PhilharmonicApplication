import javax.swing.*;
import java.sql.*;

public class NewArtistWindow extends NewPersonWindow {

    public NewArtistWindow() {
        super("Новый артист");
        okButton.addActionListener(e -> applyChanges());
    }

    private void applyChanges() {
        String sql = "INSERT INTO artist (last_name, first_name, surname) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, lastName.getText());
            statement.setString(2, firstName.getText());
            statement.setString(3, surname.getText());

            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Артист успешно добавлен");
            lastName.setText("");
            firstName.setText("");
            surname.setText("");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }


    }

}
