import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class NewArtistWindow extends NewPersonWindow {

    public NewArtistWindow() {
        super("Новый артист");
        okButton.addActionListener(e -> applyChanges());
        this.connection = connection;
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
        try {
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
