import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;

public class DeleteArtistFromEventWindow extends JFrame {
    JLabel impresarioName;
    Selector eventSelector;
    Selector artistSelector;
    JPanel artistSelectPanel;
    JPanel bottomButtonsPanel;
    JButton deleteButton;
    JButton nextButton;
    JButton cancelButton;
    JButton goBackButton;
    JPanel impresarioSelectPanel;
    JPanel confirmButtonPanel;

    private void initImpresarioSelectPanel() {
        impresarioSelectPanel = new JPanel(new BorderLayout());
        impresarioSelectPanel.add(eventSelector.getPanel(), BorderLayout.CENTER);
        impresarioSelectPanel.add(bottomButtonsPanel, BorderLayout.SOUTH);
    }

    private void initArtistSelectPanel() {
        artistSelectPanel = new JPanel(new BorderLayout());
        impresarioName = new JLabel();
        artistSelectPanel.add(impresarioName, BorderLayout.NORTH);
        artistSelectPanel.add(confirmButtonPanel, BorderLayout.SOUTH);
    }

    // entity1ID and entity2ID - id's labels in relationship table
    public DeleteArtistFromEventWindow() {
        try {
            setLayout(new BorderLayout());

            setSize(new Dimension(400, 200));
            setResizable(true);
            setLocation(0, 0);
            setDefaultCloseOperation(HIDE_ON_CLOSE);

            initBottomPanel();
            initConfirmButtonPanel();
            eventSelector = new Selector("Мероприятие", GetUtilities.getNamesMap("event", "id", "name"));

            initImpresarioSelectPanel();
            initArtistSelectPanel();

            add(impresarioSelectPanel, BorderLayout.CENTER);

            pack();
            setVisible(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }
    }

    public void openArtistSelectPanel() {
        HashMap<String, Integer> artists = new HashMap<>();
        String sql = "SELECT id, last_name, first_name, surname FROM artist JOIN artist_event ON artist.id = artist_event.artist_id WHERE event_id = ?";
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, eventSelector.getSelectedID());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String name = resultSet.getString("last_name") + " " +
                            resultSet.getString("first_name") + " " +
                            resultSet.getString("surname");

                    artists.put(name, resultSet.getInt("id"));
                }
            }
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Произошла ошибка при выполнении запроса " + e);
        }
        impresarioName.setText(eventSelector.getSelectedName());

        this.setSize(new Dimension(500, 300));
        this.getContentPane().removeAll();
        artistSelector = new Selector("Артист", artists);
        artistSelectPanel.add(artistSelector.getPanel(), BorderLayout.CENTER);
        this.getContentPane().add(artistSelectPanel, BorderLayout.CENTER);
        this.getContentPane().add(confirmButtonPanel, BorderLayout.SOUTH);
        revalidate();
        repaint();

    }

    public void openSelectPanel() {
        impresarioName.setText(eventSelector.getSelectedName());
        this.setSize(new Dimension(400, 200));
        this.getContentPane().removeAll();
        this.getContentPane().add(impresarioSelectPanel, BorderLayout.CENTER);
        this.getContentPane().add(bottomButtonsPanel, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private void delete() {
        String preparedSQL = "DELETE FROM artist_event WHERE event_id = ? AND artist_id = ?";
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password);
             PreparedStatement statement = connection.prepareStatement(preparedSQL)) {

            statement.setInt(1, eventSelector.getSelectedID());
            statement.setInt(2, artistSelector.getSelectedID());
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

        nextButton.addActionListener(e -> openArtistSelectPanel());
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
