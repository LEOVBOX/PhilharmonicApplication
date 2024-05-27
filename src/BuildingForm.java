import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;

public abstract class BuildingForm extends JFrame {
    protected JTextField capacityField;

    protected JTextField nameField;

    protected JTextField addressField;
    protected Selector typeSelector;

    protected TheatreParamsPanel theatreParamsPanel;

    protected CinemaParamsPanel cinemaParamsPanel;

    protected EstradeParamsPanel estradeParamsPanel;
    protected JPanel typeParamsPanel;
    protected JPanel mainPanel;
    protected JPanel mainButtonsPanel;
    protected JPanel paramsButtonsPanel;

    public BuildingForm() {
        initMainPanel();
        initParamsButtonPanel();
        initMainButtonPanel();
    }

    protected void initMainPanel() {
        mainPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 0;

        JLabel nameLabel = new JLabel("Название");
        nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        mainPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        nameField = new JTextField();
        mainPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel placeLabel = new JLabel("Адрес");
        placeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        mainPanel.add(placeLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        addressField = new JTextField();
        mainPanel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1;
        gbc.gridwidth = 2;
        try {
            typeSelector = new Selector("Тип", GetUtilities.getNamesMap("building_type", "type_id", "type_label"));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "При выполнении запроса произошла ошибка" + e);
        }
        mainPanel.add(typeSelector.getPanel(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        JLabel capacityLabel = new JLabel("Вместимость");
        capacityLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        mainPanel.add(capacityLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        capacityField = new JTextField();
        mainPanel.add(capacityField, gbc);

    }

    protected void showMainPanel() {
        this.getContentPane().removeAll();
        this.getContentPane().add(mainPanel, BorderLayout.CENTER);
        this.getContentPane().add(mainButtonsPanel, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    protected void initParamsButtonPanel() {
        paramsButtonsPanel = new JPanel();
        paramsButtonsPanel.add(Box.createHorizontalGlue());
        paramsButtonsPanel.setLayout(new BoxLayout(paramsButtonsPanel, BoxLayout.X_AXIS));
        JButton backButton = new JButton("назад");
        backButton.addActionListener(e -> showMainPanel());
        JButton confirmButton = new JButton("добавить");
        confirmButton.addActionListener(e -> applyChanges());
        paramsButtonsPanel.add(backButton);
        paramsButtonsPanel.add(confirmButton);
    }

    abstract void initMainButtonPanel();

    protected void showUniqueParamsPanel(String selectedType, Integer building_id) throws SQLException {
        HashMap<String, String> buildingParams = null;
        switch (selectedType) {
            case "Театр" -> {
                if (building_id != null) {
                    buildingParams = getTheatreParams(building_id);
                }
                theatreParamsPanel = new TheatreParamsPanel(buildingParams);

                typeParamsPanel = theatreParamsPanel;
            }
            case "Кинотеатр" -> {
                if (building_id != null) {
                    buildingParams = getCinemaParams(building_id);
                }
                cinemaParamsPanel = new CinemaParamsPanel(buildingParams);

                typeParamsPanel = cinemaParamsPanel;
            }
            case "Эстрада" -> {
                if (building_id != null) {
                    buildingParams = getEstradeParams(building_id);
                }
                estradeParamsPanel = new EstradeParamsPanel(buildingParams);

                typeParamsPanel = estradeParamsPanel;
            }
        }

        this.getContentPane().removeAll();
        this.getContentPane().add(typeParamsPanel, BorderLayout.CENTER);
        this.getContentPane().add(paramsButtonsPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }


    abstract void applyChanges();

    public static HashMap<String, String> getCinemaParams(int building_id) throws SQLException {
        HashMap<String, String> buildingParams = new HashMap<>();
        String sql = "SELECT screen_width, screen_height FROM cinema WHERE building_id = ?";
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, building_id);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    buildingParams.put("screen_width", resultSet.getString("screen_width"));
                    buildingParams.put("screen_height", resultSet.getString("screen_height"));
                }
            }
        }
        return buildingParams;
    }

    public static HashMap<String, String> getTheatreParams(int building_id) throws SQLException {
        HashMap<String, String> buildingParams = new HashMap<>();
        String sql = "SELECT parter_capacity, benoir_capacity, mezzanine_capacity, first_tier_capacity, "+
                "second_tier_capacity, amphitheatre_capacity, galerka_capacity FROM theatre WHERE building_id = ?";
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, building_id);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    buildingParams.put("parter_capacity", resultSet.getString("parter_capacity"));
                    buildingParams.put("benoir_capacity", resultSet.getString("benoir_capacity"));
                    buildingParams.put("mezzanine_capacity", resultSet.getString("mezzanine_capacity"));
                    buildingParams.put("first_tier_capacity", resultSet.getString("first_tier_capacity"));
                    buildingParams.put("second_tier_capacity", resultSet.getString("second_tier_capacity"));
                    buildingParams.put("amphitheatre_capacity", resultSet.getString("amphitheatre_capacity"));
                    buildingParams.put("galerka_capacity", resultSet.getString("galerka_capacity"));
                }
            }
        }
        return buildingParams;
    }

    public static HashMap<String, String> getEstradeParams(int building_id) throws SQLException {
        HashMap<String, String> buildingParams = new HashMap<>();
        String sql = "SELECT stage_width, stage_depth FROM estrade WHERE building_id = ?";
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, building_id);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    buildingParams.put("stage_width", resultSet.getString("stage_width"));
                    buildingParams.put("stage_depth", resultSet.getString("stage_depth"));
                }
            }
        }
        return buildingParams;
    }


    public static String getBuildingTypeTableName(String typeName) {
        switch (typeName) {
            case "Кинотеатр" -> {
                return "cinema";
            }
            case "Театр" -> {
                return "theatre";
            }
            case "Эстрада" -> {
                return "estrade";
            }
        }
        return null;
    }
}
