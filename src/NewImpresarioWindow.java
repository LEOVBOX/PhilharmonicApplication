import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class NewImpresarioWindow extends NewPersonWindow {

    Connection connection;
    public NewImpresarioWindow(Connection connection) {
        super("Новый импресарио", connection);
        okButton.addActionListener(e -> applyChanges());
        this.connection = connection;
    }

    private String createSQLQuery() {
        return "insert into impresario (last_name, first_name, surname) values"
                + "('"
                + lastName.getText() +
                "', '"
                + firstName.getText() +
                " ', '"
                + surname.getText() +
                "')";
    }

    private void applyChanges() {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(createSQLQuery());
            JOptionPane.showMessageDialog(this, "Импресарио успешно добавлен");
            dispose();
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }
        finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
