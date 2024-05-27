import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;

public class EditAwardWindow extends AwardForm {
    private JPanel selectPanel;
    private JPanel selectButtonsPanel;
    private JPanel mainButtonsPanel;
    private Selector entitySelector;

    public EditAwardWindow() {
        super();
        try {
            setPreferredSize(new Dimension(500, 300));
            setLocation(0, 0);
            setDefaultCloseOperation(HIDE_ON_CLOSE);
            setLayout(new BorderLayout());

            initMainButtonPanel();
            initSelectPanel();

            add(selectPanel, BorderLayout.CENTER);
            add(selectButtonsPanel, BorderLayout.SOUTH);

            pack();
            setVisible(true);


        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initSelectButtonPanel() {
        selectButtonsPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;

        JButton cancelButton = new JButton("отмена");
        cancelButton.addActionListener(e -> dispose());
        selectButtonsPanel.add(cancelButton, gbc);

        gbc.gridx++;
        JButton nextButton = new JButton("далее");
        nextButton.addActionListener(e->openMainPanel());
        selectButtonsPanel.add(nextButton, gbc);

    }

    private void initSelectPanel() {
        selectPanel = new JPanel(new BorderLayout());

        HashMap<String, Integer> awardsMap = new HashMap<>();
        String sql = "SELECT awards.id, \"event\".name as en, awards.name as an, artist.first_name, artist.last_name, artist.surname FROM awards JOIN event ON awards.event_id = \"event\".id JOIN " +
                "artist ON artist.id = awards.artist_id";
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    StringBuilder name = new StringBuilder();
                    name.append(resultSet.getString("en")).append(" ");
                    name.append(resultSet.getString("an")).append(" ");
                    name.append(resultSet.getString("first_name")).append(" ");
                    name.append(resultSet.getString("last_name")).append(" ");
                    name.append(resultSet.getString("surname")).append(" ");
                    awardsMap.put(name.toString(), resultSet.getInt("id"));
                }
            }
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполенении запроса произошла ошибка" + e);
        }

        entitySelector = new Selector("Награда", awardsMap);

        initSelectButtonPanel();
        selectPanel.add(entitySelector.getPanel(), BorderLayout.CENTER);
        selectPanel.add(selectButtonsPanel, BorderLayout.SOUTH);
    }

    void initMainButtonPanel() {
        mainButtonsPanel = new JPanel();
        mainButtonsPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridy = 0;
        gbc.gridx = 0;

        JButton goBackButton = new JButton("назад");
        goBackButton.addActionListener(e -> openSelectPanel());
        mainButtonsPanel.add(goBackButton, gbc);

        gbc.gridx++;
        JButton confirmButton = new JButton("применить изменения");
        confirmButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null, "Нажмите OK, чтобы применить изменения", "Диалоговое окно", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                applyChanges();
            }
        });

        mainButtonsPanel.add(confirmButton, gbc);

        gbc.gridx++;
        JButton cancelButton = new JButton("отмена");
        cancelButton.addActionListener(e -> dispose());
        mainPanel.add(cancelButton);
    }

    private void openMainPanel() {
        String sql = "SELECT name, artist_id, event_id FROM awards WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password);
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, entitySelector.getSelectedID());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    nameField.setText(resultSet.getString("name"));
                    artists.setSelectedID(resultSet.getInt("artist_id"));
                    events.setSelectedID(resultSet.getInt("event_id"));
                }
            }

        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполенении запроса произошла ошибка" + e);
        }
        this.getContentPane().removeAll();
        this.getContentPane().add(mainPanel, BorderLayout.CENTER);
        this.getContentPane().add(mainButtonsPanel, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private void openSelectPanel() {
        this.getContentPane().removeAll();
        this.getContentPane().add(selectPanel, BorderLayout.CENTER);
        this.getContentPane().add(selectButtonsPanel, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private void applyChanges() {
        String sql = "UPDATE awards SET name = ?, artist_id = ?, event_id = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password);
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(4, entitySelector.getSelectedID());
            preparedStatement.setString(1, nameField.getText());
            preparedStatement.setInt(2, artists.getSelectedID());
            preparedStatement.setInt(3, events.getSelectedID());

            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Награда успешно изменена");
            dispose();
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполеннеи запроса произошла ошибка" + e);
        }
    }
}
