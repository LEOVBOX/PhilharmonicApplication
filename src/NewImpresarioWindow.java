import javax.swing.*;
import java.sql.*;

public class NewImpresarioWindow extends NewPersonWindow {
    public NewImpresarioWindow() {
        super("Новый импресарио");
        okButton.addActionListener(e -> applyChanges());
    }

    private String createInsertSQLQuery() {
        return "INSERT INTO impresario (last_name, first_name, surname) VALUES"
                + "('"
                + lastName.getText() +
                "', '"
                + firstName.getText() +
                " ', '"
                + surname.getText() +
                "')";
    }

    private void addImpresario() {
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password)){
            Statement statement = connection.createStatement();
            statement.executeUpdate(createInsertSQLQuery());
            JOptionPane.showMessageDialog(this, "Импресарио успешно добавлен");
            dispose();
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }
    }

    // 1. Создаем новую строчку в таблице импресарио
    // 2. Создаем связи последнего добавленного импресарио с жанрами в таблице impresario_genre
    private void applyChanges() {
        addImpresario();

    }
}
