import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class NewArtistWindow extends NewPersonWindow {

    public NewArtistWindow() {
        super("Новый артист");
        okButton.addActionListener(e -> applyChanges());
    }

    private String createSQLQuery() {
        return "insert into artist (last_name, first_name, surname) values"
                + "('"
                + lastName.getText() +
                "', '"
                + firstName.getText() +
                " ', '"
                + surname.getText() +
                "')";
    }

    private void applyChanges() {
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password)){
            Statement statement = connection.createStatement();
            statement.executeUpdate(createSQLQuery());
            JOptionPane.showMessageDialog(this, "Артист успешно добавлен");
            dispose();
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }
    }

}
