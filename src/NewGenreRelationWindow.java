import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class NewGenreRelationWindow extends JFrame {
    ArrayList<JComboBox<String>> genresSelectors;

    ArrayList<String> availableGenres;
    JPanel mainPanel;
    JPanel genresPanel;
    JButton addGenreButton;
    JComboBox<String> nameSelector;
    JButton okButton;

    JLabel nameLabel;

    HashMap<String, Integer> names;

    HashMap<String, Integer> genres;

    boolean isImpresario;

    public NewGenreRelationWindow(String windowName, boolean isImpresario) {
        super(windowName);
        try {
            this.isImpresario = isImpresario;
            setPreferredSize(new Dimension(400, 200));
            setResizable(false);
            setLocation(0, 0);
            setDefaultCloseOperation(HIDE_ON_CLOSE);
            setLayout(new BorderLayout());
            names = new HashMap<>();
            genres = new HashMap<>();
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
            }
            else {
                nameLabel = new JLabel("Выберете артиста");
            }

            nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            mainPanel.add(nameLabel, gbc);

            gbc.gridy = 1;
            gbc.weightx = 1;
            gbc.gridwidth = 1;
            nameSelector = new JComboBox<>(getNames().toArray(new String[0]));
            mainPanel.add(nameSelector, gbc);

            gbc.gridy = 2;

            initGenresPanel();
            mainPanel.add(genresPanel, gbc);


            gbc.gridx = 1;
            gbc.gridy = 5;
            gbc.gridwidth = 1;
            JButton cancelButton = new JButton("cancel");
            cancelButton.addActionListener(e -> dispose());
            mainPanel.add(cancelButton, gbc);

            gbc.gridx = 2;
            okButton = new JButton("ok");
            okButton.addActionListener(e -> applyChanges());
            mainPanel.add(okButton, gbc);

            add(mainPanel, BorderLayout.CENTER);

            pack();
            setVisible(true);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initGenresPanel() {
        availableGenres = getGenres();
        String[] genres = availableGenres.toArray(new String[0]);
        JComboBox<String> genreSelector = new JComboBox<>(availableGenres.toArray(new String[0]));
        genresSelectors.add(genreSelector);
        genresPanel = new JPanel();
        genresPanel.add(genreSelector);
        addGenreButton = new JButton("Добавить жанр");
        addGenreButton.addActionListener(e -> {
            genresSelectors.add(new JComboBox<>(genres));
            genresPanel.add(genresSelectors.getLast());
            revalidate();
            repaint();
        });
        genresPanel.add(addGenreButton);
    }

    private ArrayList<String> getGenres() {
        ArrayList<String> genreNames = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password)) {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM genre");

            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                genreNames.add(name);

                genres.put(name, id);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }

        return genreNames;
    }

    private ArrayList<String> getNames() {
        ArrayList<String> impresarioNames = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet;
            if (isImpresario) {
                resultSet = statement.executeQuery("SELECT * FROM impresario");
            }
            else {
                resultSet = statement.executeQuery("SELECT * FROM artist");
            }

            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String name = resultSet.getString("last_name");
                name = name + " " + resultSet.getString("first_name");
                name = name + " " + resultSet.getString("surname");

                impresarioNames.add(name);
                names.put(name, id);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }
        return impresarioNames;
    }

    private String createSQLQuery(int person_id, int genre_id) {
        if (isImpresario) {
            return "insert into impresario_genre (impresario_id, genre_id) values"
                    + "('"
                    + person_id +
                    "', '"
                    + genre_id +
                    "')";
        }
        else {
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

        for (JComboBox<String> selector : genresSelectors) {
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
        }
        dispose();
    }
}
