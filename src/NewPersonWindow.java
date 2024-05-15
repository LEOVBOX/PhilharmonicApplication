import org.checkerframework.checker.units.qual.A;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class NewPersonWindow extends JFrame {
    JTextField firstName;
    JTextField lastName;
    JTextField surname;

    JButton okButton;

    Connection connection;


    public NewPersonWindow(String windowName, Connection connection) {
        super(windowName);
        try {
            setPreferredSize(new Dimension(400, 200));
            setResizable(false);
            setLocation(0, 0);
            setDefaultCloseOperation(HIDE_ON_CLOSE);
            setLayout(new BorderLayout());
            JPanel mainPanel = new JPanel(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1;
            gbc.weighty = 1;


            JLabel nameLabel = new JLabel("Имя");
            nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            mainPanel.add(nameLabel, gbc);

            gbc.gridx = 1;
            gbc.weightx = 1;
            gbc.gridwidth = 2;
            firstName = new JTextField();
            mainPanel.add(firstName, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 1;

            JLabel lastNameLabel = new JLabel("Фамилия");
            lastNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            mainPanel.add(lastNameLabel, gbc);

            gbc.gridx = 1;
            gbc.gridwidth = 2;
            lastName = new JTextField();
            mainPanel.add(lastName, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 1;
            JLabel surnameLabel = new JLabel("Отчество");
            surnameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            mainPanel.add(surnameLabel, gbc);

            gbc.gridx = 1;
            gbc.gridwidth = 2;
            surname = new JTextField();
            mainPanel.add(surname, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 3;


            String[] genres = getGenres();
            JComboBox genre = new JComboBox(genres);
            mainPanel.add(genre, gbc);


            gbc.gridx = 1;
            gbc.gridy = 4;
            gbc.gridwidth = 1;
            JButton cancelButton = new JButton("cancel");
            cancelButton.addActionListener(e -> dispose());
            mainPanel.add(cancelButton, gbc);

            gbc.gridx = 2;
            okButton = new JButton("ok");
            mainPanel.add(okButton, gbc);

            add(mainPanel, BorderLayout.CENTER);

            pack();
            setVisible(true);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String[] getGenres() {
        ResultSet resultSet = null;
        Statement statement = null;
        String[] genreNamesArray = null;
        try {
            statement = connection.createStatement();
            ArrayList<String> genreNames = new ArrayList<>();
            resultSet = statement.executeQuery("SELECT name FROM genre");

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                genreNames.add(name);
            }

            genreNamesArray = genreNames.toArray(new String[0]);

        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }
        finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return genreNamesArray;
    }
}
