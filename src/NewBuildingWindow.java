import javax.swing.*;
import java.awt.*;

public class NewBuildingWindow extends JFrame {
    String name;

    String[] buildingTypes = {"Филармония", "Кинотеатр"};

    JTextField capacityField;

    JTextField nameField;

    JTextField addressField;
    JComboBox typeSelector;


    public NewBuildingWindow() {
        super("New building");
        try {
            setPreferredSize(new Dimension(400, 200));
            setResizable(false);
            setLocation(0, 0);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLayout(new BorderLayout());
            JPanel mainPanel = new JPanel(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1;
            gbc.weighty = 1;


            JLabel nameLabel = new JLabel("Название");
            nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            mainPanel.add(nameLabel, gbc);

            gbc.gridx = 1;
            gbc.weightx = 1;
            gbc.gridwidth = 2;
            nameField = new JTextField();
            mainPanel.add(nameField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 1;

            JLabel placeLabel = new JLabel("Адрес");
            placeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            mainPanel.add(placeLabel, gbc);

            gbc.gridx = 1;
            gbc.gridwidth = 2;
            addressField = new JTextField();
            mainPanel.add(addressField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 1;
            JLabel organizerLabel = new JLabel("Тип");
            organizerLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            mainPanel.add(organizerLabel, gbc);

            gbc.gridx = 1;
            gbc.gridwidth = 2;
            typeSelector = new JComboBox(buildingTypes);
            mainPanel.add(typeSelector, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 1;

            JLabel capacityLabel = new JLabel("Вместимость");
            capacityLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            mainPanel.add(capacityLabel, gbc);


            gbc.gridx = 1;
            gbc.gridwidth = 2;

            capacityField = new JTextField();
            mainPanel.add(capacityField, gbc);


            gbc.gridx = 1;
            gbc.gridy = 5;
            gbc.gridwidth = 1;
            JButton cancelButton = new JButton("cancel");
            cancelButton.addActionListener(e -> dispose());
            mainPanel.add(cancelButton, gbc);

            gbc.gridx = 2;
            JButton okButton = new JButton("ok");
            mainPanel.add(okButton, gbc);

            add(mainPanel, BorderLayout.CENTER);

            pack();
            setVisible(true);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
