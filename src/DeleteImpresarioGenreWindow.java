import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;

public class DeleteImpresarioGenreWindow extends JFrame {
    JLabel impresarioName;
    Selector impresarioSelector;
    Selector genreSelector;
    JPanel genreSelectPanel;
    JPanel bottomButtonsPanel;
    JButton deleteButton;
    JButton nextButton;
    JButton cancelButton;
    JButton goBackButton;
    JPanel artistSelectPanel;
    JPanel confirmButtonPanel;

    private void initArtistSelectPanel() {
        artistSelectPanel = new JPanel(new BorderLayout());
        artistSelectPanel.add(impresarioSelector.getPanel(), BorderLayout.CENTER);
        artistSelectPanel.add(bottomButtonsPanel, BorderLayout.SOUTH);
    }

    private void initGenreSelectPanel() {
        genreSelectPanel = new JPanel(new BorderLayout());
        impresarioName = new JLabel();
        genreSelectPanel.add(impresarioName, BorderLayout.NORTH);
        genreSelectPanel.add(confirmButtonPanel, BorderLayout.SOUTH);
    }

    // entity1ID and entity2ID - id's labels in relationship table
    public DeleteImpresarioGenreWindow() {
        try {
            setLayout(new BorderLayout());

            setSize(new Dimension(400, 200));
            setResizable(true);
            setLocation(0, 0);
            setDefaultCloseOperation(HIDE_ON_CLOSE);

            initBottomPanel();
            initConfirmButtonPanel();
            impresarioSelector = new Selector("импресарио", GetUtilities.getNamesMap("impresario", "id", "last_name", "first_name", "surname"));

            initArtistSelectPanel();
            initGenreSelectPanel();

            add(artistSelectPanel, BorderLayout.CENTER);

            pack();
            setVisible(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }
    }

    public void openGenreSelectPanel() {
        HashMap<String, Integer> genres = new HashMap<>();
        String sql = "SELECT id, name FROM genre JOIN impresario_genre ON genre.id = impresario_genre.genre_id WHERE impresario_id = ?";
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, impresarioSelector.getSelectedID());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    genres.put(resultSet.getString("name"), resultSet.getInt("id"));
                }
            }
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Произошла ошибка при выполнении запроса " + e);
        }
        impresarioName.setText(impresarioSelector.getSelectedName());

        this.setSize(new Dimension(500, 300));
        this.getContentPane().removeAll();
        genreSelector = new Selector("Жанр", genres);
        genreSelectPanel.add(genreSelector.getPanel(), BorderLayout.CENTER);
        this.getContentPane().add(genreSelectPanel, BorderLayout.CENTER);
        this.getContentPane().add(confirmButtonPanel, BorderLayout.SOUTH);
        revalidate();
        repaint();

    }

    public void openSelectPanel() {
        impresarioName.setText(impresarioSelector.getSelectedName());
        this.setSize(new Dimension(400, 200));
        this.getContentPane().removeAll();
        this.getContentPane().add(artistSelectPanel, BorderLayout.CENTER);
        this.getContentPane().add(bottomButtonsPanel, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private void delete() {
        String preparedSQL = "DELETE FROM impresario_genre WHERE impresario_id = ? AND genre_id = ?";
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password);
             PreparedStatement statement = connection.prepareStatement(preparedSQL)) {

            statement.setInt(1, impresarioSelector.getSelectedID());
            statement.setInt(2, genreSelector.getSelectedID());
            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Удаление усепешно");
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }
    }

    private void initBottomPanel() {
        bottomButtonsPanel = new JPanel();
        bottomButtonsPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;

        gbc.gridx++;
        nextButton = new JButton("далее");
        bottomButtonsPanel.add(nextButton, gbc);

        gbc.gridx++;
        cancelButton = new JButton("отмена");
        bottomButtonsPanel.add(cancelButton, gbc);

        nextButton.addActionListener(e -> openGenreSelectPanel());
        cancelButton.addActionListener(e -> dispose());
    }

    private void initConfirmButtonPanel() {
        confirmButtonPanel = new JPanel();
        confirmButtonPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridy = 0;
        gbc.gridx = 0;

        goBackButton = new JButton("назад");
        goBackButton.addActionListener(e -> openSelectPanel());
        confirmButtonPanel.add(goBackButton, gbc);

        gbc.gridx++;
        deleteButton = new JButton("удалить");
        deleteButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null, "Нажмите OK, чтобы продолжить", "Диалоговое окно", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                delete();
            }
        });
        confirmButtonPanel.add(deleteButton, gbc);

        gbc.gridx++;
        JButton cancelButton = new JButton("отмена");
        cancelButton.addActionListener(e -> dispose());
        confirmButtonPanel.add(cancelButton);
    }
}
