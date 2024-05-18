import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class NewGenreWindow extends JFrame {
    JTextField genreField;

    public NewGenreWindow() {
        super("New genre");
        try {
            setPreferredSize(new Dimension(400, 200));
            setResizable(false);
            setLocation(0, 0);
            setDefaultCloseOperation(HIDE_ON_CLOSE);
            setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1;
            gbc.weighty = 1;

            JLabel genreLabel = new JLabel("Название жанра");
            genreLabel.setHorizontalAlignment(SwingConstants.CENTER);
            add(genreLabel, gbc);

            gbc.gridy = 1;
            genreField = new JTextField();
            add(genreField, gbc);

            gbc.gridwidth = 1;
            gbc.gridy = 2;
            JButton cancelButton = new JButton("cancel");
            cancelButton.addActionListener(e -> dispose());
            add(cancelButton, gbc);

            gbc.gridx = 1;
            JButton okButton = new JButton("ok");
            okButton.addActionListener(e -> applyChanges());
            add(okButton, gbc);
            pack();
            setVisible(true);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String createSQLQuery() {
        return "insert into genre (name) values"
                + "('" +
                genreField.getText() +
                "')";
    }

    private void applyChanges() {
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password)){
            Statement statement = connection.createStatement();
            statement.executeUpdate(createSQLQuery());
            JOptionPane.showMessageDialog(this, "Жанр успешно добавлен");
            dispose();
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }
    }
}
