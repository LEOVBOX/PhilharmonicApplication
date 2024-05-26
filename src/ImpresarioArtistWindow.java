import javax.swing.*;
import java.awt.*;
import java.sql.*;
public class ImpresarioArtistWindow extends JFrame {

    JPanel mainPanel;
    Selector impresarioSelector;
    Selector artistSelector;


    public ImpresarioArtistWindow() {
        super("Работает с");
        try {
            setPreferredSize(new Dimension(400, 200));
            setResizable(false);
            setLocation(0, 0);
            setDefaultCloseOperation(HIDE_ON_CLOSE);
            setLayout(new BorderLayout());

            mainPanel = new JPanel(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1;
            gbc.weighty = 1;


            gbc.gridy = 0;
            gbc.gridwidth = 1;
            impresarioSelector = new Selector("Импресарио", GetUtilities.getNamesMap("impresario", "id", "last_name", "first_name", "surname"));
            mainPanel.add(impresarioSelector.getPanel(), gbc);

            gbc.gridy++;
            artistSelector = new Selector("Артист", GetUtilities.getNamesMap("artist", "id", "last_name", "first_name", "surname"));
            mainPanel.add(artistSelector.getPanel(), gbc);


            DialogButtonsPanel dialogButtonsPanel = new DialogButtonsPanel();
            dialogButtonsPanel.cancelButton.addActionListener(e -> dispose());
            dialogButtonsPanel.okButton.addActionListener(e -> applyChanges());

            add(mainPanel, BorderLayout.CENTER);
            add(dialogButtonsPanel, BorderLayout.SOUTH);
            pack();
            setVisible(true);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void applyChanges() {
        String insertSQL = "INSERT INTO work_with (artist_id, impresario_id) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password);
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {

            statement.setInt(1, artistSelector.getSelectedID());
            statement.setInt(2, impresarioSelector.getSelectedID());

            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Связь успешно созданна");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }

    }
}
