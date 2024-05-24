import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class NewGenreRelationWindow extends JFrame {
    JPanel mainPanel;
    Selector names;
    Selector genres;
    boolean isImpresario;

    public NewGenreRelationWindow(String windowName, boolean isImpresario) {
        super(windowName);
        try {
            this.isImpresario = isImpresario;
            setPreferredSize(new Dimension(500, 300));
            setResizable(true);
            setLocation(0, 0);
            setDefaultCloseOperation(HIDE_ON_CLOSE);
            setLayout(new BorderLayout());
            setLayout(new BorderLayout());
            try {
                genres = new Selector("Выберите жанр", GetUtilities.getGenres());
                if (isImpresario) {
                    names = new Selector("Выберете импресарио", GetUtilities.getNames(true));
                }
                else {
                    names = new Selector("Выберете артиста", GetUtilities.getNames(false));
                }


            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                        + e.getMessage());
            }
            mainPanel = new JPanel(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1;
            gbc.weighty = 1;


            mainPanel.add(names.getPanel(), gbc);

            gbc.gridy++;
            mainPanel.add(genres.getPanel(), gbc);


            gbc.gridy++;
            DialogButtonsPanel dialogButtonsPanel = new DialogButtonsPanel();
            dialogButtonsPanel.cancelButton.addActionListener(e->dispose());
            dialogButtonsPanel.okButton.addActionListener(e->applyChanges());


            add(mainPanel, BorderLayout.CENTER);
            add(dialogButtonsPanel, BorderLayout.SOUTH);

            pack();
            setVisible(true);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void applyChanges() {
        // Создаем новую запись в таблице impresario_genre
        // Получаем id impresario
        String insertSQL;
        if (isImpresario) {
            insertSQL = "INSERT INTO impresario_genre (impresario_id, genre_id) VALUES (?, ?)";
        } else {
            insertSQL = "INSERT INTO artist_genre (artist_id, genre_id) VALUES (?, ?)";
        }

        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password);
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {

            int personID = names.getSelectedID();
            statement.setInt(1, personID);

            int genreID = genres.getSelectedID();
            statement.setInt(2, genreID);

            statement.executeUpdate();

            //NotificationWindow.showNotificationWindow(true, "Жанр успешно добавлен");
            JOptionPane.showMessageDialog(this, "Жанр успешно добавлен к артисту");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }
    }
}
