import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ImpresarioArtistWindow extends JFrame {
    ArrayList<JComboBox<String>> genresSelectors;

    JPanel mainPanel;
    JComboBox<String> impresarioSelector;
    JComboBox<String> artistSelector;
    JButton okButton;

    JLabel impresarioLabel;
    JLabel artistLabel;

    HashMap<String, Integer> impresarioNames;

    HashMap<String, Integer> artistNames;

    public ImpresarioArtistWindow() {
        super("Работает с");
        try {
            setPreferredSize(new Dimension(400, 200));
            setResizable(false);
            setLocation(0, 0);
            setDefaultCloseOperation(HIDE_ON_CLOSE);
            setLayout(new BorderLayout());
            try {
                impresarioNames = GetUtilities.getNames(true);
                artistNames = GetUtilities.getNames(false);
            }
            catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                        + e.getMessage());
            }

            mainPanel = new JPanel(new GridBagLayout());
            genresSelectors = new ArrayList<>();

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 3;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1;
            gbc.weighty = 1;

            impresarioLabel = new JLabel("Выберете импресарио");

            mainPanel.add(impresarioLabel, gbc);

            gbc.gridy = 1;
            gbc.gridwidth = 2;
            impresarioSelector = new JComboBox<>(impresarioNames.keySet().toArray(new String[0]));
            mainPanel.add(impresarioSelector, gbc);

            gbc.gridy = 2;
            gbc.gridx = 0;

            artistLabel = new JLabel("Выберете артиста");
            mainPanel.add(artistLabel, gbc);

            gbc.gridy = 3;
            artistSelector = new JComboBox<>(artistNames.keySet().toArray(new String[0]));
            mainPanel.add(artistSelector, gbc);

            gbc.gridx = 1;
            gbc.gridy = 4;
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

    private String createSQLQuery(int artist_id, int impresario_id) {
        return "insert into work_with (artist_id, impresario_id) values"
                + "('"
                + artist_id +
                "', '"
                + impresario_id +
                "')";

    }

    private void applyChanges() {
        // Создаем новую запись в таблице impresario_genre
        // Получаем id impresario
        String selectedImpresario = (String) impresarioSelector.getSelectedItem();
        int impresarioID = impresarioNames.get(selectedImpresario);

        String selectedArtist = (String) artistSelector.getSelectedItem();
        int artistID = artistNames.get(selectedArtist);
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(createSQLQuery(artistID, impresarioID));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }

        dispose();
    }
}
