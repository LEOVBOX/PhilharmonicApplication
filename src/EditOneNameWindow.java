import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class EditOneNameWindow extends JFrame{
    JTextField name;
    Selector entitySelector;
    JPanel editPanel;
    JPanel bottomButtonsPanel;
    JButton deleteButton;
    JButton editButton;
    JButton cancelButton;
    JButton okButton;
    JButton goBackButton;
    JPanel selectPanel;
    JPanel confirmButtonPanel;
    String tableName;
    String idLabel;
    String nameLabel;
    String entityName;

    private void initSelectPanel() {
        selectPanel = new JPanel(new BorderLayout());
        selectPanel.add(entitySelector.getPanel(), BorderLayout.CENTER);
        selectPanel.add(bottomButtonsPanel, BorderLayout.SOUTH);
    }

    private void initEditPanel() {
        editPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 1;


        JLabel nameLabel = new JLabel(entityName);
        nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        editPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.gridwidth = 2;
        name = new JTextField();
        editPanel.add(name, gbc);
    }

    public EditOneNameWindow(String tableName, String idLabel, String nameLabel, String entityName) {
        super("Edit " + entityName);
        this.tableName = tableName;
        this.idLabel = idLabel;
        this.nameLabel = nameLabel;
        this.entityName = entityName;
        try {
            setLayout(new BorderLayout());

            setSize(new Dimension(400, 200));
            setResizable(true);
            setLocation(0, 0);
            setDefaultCloseOperation(HIDE_ON_CLOSE);

            initBottomPanel();
            initConfirmButtonPanel();
            entitySelector = new Selector(entityName, GetUtilities.getNamesMap(tableName, idLabel, nameLabel));

            initSelectPanel();
            initEditPanel();

            add(selectPanel, BorderLayout.CENTER);

            pack();
            setVisible(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }
    }

    public void openEditPanel() {
        name.setText(entitySelector.getSelectedName());
        this.setSize(new Dimension(500, 300));
        this.getContentPane().removeAll();
        this.getContentPane().add(editPanel, BorderLayout.CENTER);
        this.getContentPane().add(confirmButtonPanel, BorderLayout.SOUTH);
        revalidate();
        repaint();

    }

    public void openSelectPanel() {
        this.setSize(new Dimension(400, 200));
        this.getContentPane().removeAll();
        this.getContentPane().add(selectPanel, BorderLayout.CENTER);
        this.getContentPane().add(bottomButtonsPanel, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private void applyChanges() {
        String preparedSQL = "UPDATE %s SET %s = ? WHERE %s = ?";
        String sql = String.format(preparedSQL, tableName, nameLabel, idLabel);
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name.getText());
            statement.setInt(2, entitySelector.getSelectedID());

            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Запись успешно изменена");
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }
    }

    private void delete() {
        String preparedSQL = "DELETE FROM %s WHERE %s = ?";
        String sql = String.format(preparedSQL, tableName, idLabel);
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, entitySelector.getSelectedID());
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

        deleteButton = new JButton("удалить");
        bottomButtonsPanel.add(deleteButton, gbc);

        gbc.gridx++;
        editButton = new JButton("изменить");
        bottomButtonsPanel.add(editButton, gbc);

        gbc.gridx++;
        cancelButton = new JButton("отмена");
        bottomButtonsPanel.add(cancelButton, gbc);

        editButton.addActionListener(e -> openEditPanel());
        cancelButton.addActionListener(e -> dispose());
        deleteButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null, "Нажмите OK, чтобы продолжить", "Диалоговое окно", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                delete();
            }
        });

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
        okButton = new JButton("применить изменения");
        okButton.addActionListener(e -> applyChanges());
        confirmButtonPanel.add(okButton, gbc);

        gbc.gridx++;
        JButton cancelButton = new JButton("отмена");
        cancelButton.addActionListener(e -> dispose());
        confirmButtonPanel.add(cancelButton);
    }
}
