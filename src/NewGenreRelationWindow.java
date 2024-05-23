import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class NewGenreRelationWindow extends JFrame {
    ArrayList<JComboBox<String>> genresSelectors;

    JPanel mainPanel;
    JPanel genresPanel;
    //JButton addGenreButton;
    JComboBox<String> nameSelector;
    JButton okButton;

    JLabel nameLabel;

    HashMap<String, Integer> names;

    HashMap<String, Integer> genres;

    JPanel buttonsPanel;

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
            names = new HashMap<>();
            try {
                genres = GetUtilities.getGenres();
                names = GetUtilities.getNames(isImpresario);

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                        + e.getMessage());
            }
            mainPanel = new JPanel(new GridBagLayout());
            genresSelectors = new ArrayList<>();

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1;
            gbc.weighty = 1;

            if (isImpresario) {
                nameLabel = new JLabel("Выберете импресарио");
            } else {
                nameLabel = new JLabel("Выберете артиста");
            }
            nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

            mainPanel.add(nameLabel, gbc);

            gbc.gridy = 1;
            gbc.weightx = 1;
            gbc.gridwidth = 1;
            nameSelector = new JComboBox<>(GetUtilities.getNames(isImpresario).keySet().toArray(new String[0]));
            mainPanel.add(nameSelector, gbc);

            gbc.gridy = 2;

            initGenresPanel();
            mainPanel.add(genresPanel, gbc);


            gbc.gridx = 1;
            gbc.gridy = 5;
            gbc.gridwidth = 1;

            buttonsPanel = new JPanel();
            buttonsPanel.add(Box.createHorizontalGlue());
            buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));

            JButton cancelButton = new JButton("cancel");
            cancelButton.addActionListener(e -> dispose());
            buttonsPanel.add(cancelButton);

            okButton = new JButton("ok");
            okButton.addActionListener(e -> applyChanges());
            buttonsPanel.add(okButton);

            add(mainPanel, BorderLayout.CENTER);
            add(buttonsPanel, BorderLayout.SOUTH);

            pack();
            setVisible(true);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initGenresPanel() {
        String[] genresNames = genres.keySet().toArray(new String[0]);
        JComboBox<String> genreSelector = new JComboBox<>(genresNames);
        genresSelectors.add(genreSelector);
        genresPanel = new JPanel();
        genresPanel.add(genreSelector);
        //addGenreButton = new JButton("Добавить жанр");
        /*addGenreButton.addActionListener(e -> {
            genresSelectors.add(new JComboBox<>(genresNames));
            genresPanel.add(genresSelectors.getLast());
            revalidate();
            repaint();
        });
        genresPanel.add(addGenreButton);*/
    }

    private String createSQLQuery(int person_id, int genre_id) {
        if (isImpresario) {
            return "insert into impresario_genre (impresario_id, genre_id) values"
                    + "('"
                    + person_id +
                    "', '"
                    + genre_id +
                    "')";
        } else {
            return "insert into artist_genre (artist_id, genre_id) values"
                    + "('"
                    + person_id +
                    "', '"
                    + genre_id +
                    "')";
        }

    }


    private void applyChanges() {
        // Создаем новую запись в таблице impresario_genre
        // Получаем id impresario
        String selectedName = (String) nameSelector.getSelectedItem();
        int impresarioID = names.get(selectedName);

        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password)) {
            String selectedGenre = (String) genresSelectors.getFirst().getSelectedItem();
            int genreID = genres.get(selectedGenre);
            Statement statement = connection.createStatement();
            statement.executeUpdate(createSQLQuery(impresarioID, genreID));
            JOptionPane.showMessageDialog(this, "Жанр успешно добавлен");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }


        /*for (JComboBox<String> selector : genresSelectors) {
            String selectedGenre = (String) selector.getSelectedItem();
            int genreID = genres.get(selectedGenre);
            try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password)) {
                Statement statement = connection.createStatement();
                statement.executeUpdate(createSQLQuery(impresarioID, genreID));
            }
            catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                        + e.getMessage());
            }
        }*/

    }
}
