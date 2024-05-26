import javax.swing.*;
import java.awt.*;

public class BuildingsQueryWindow extends JFrame {
    Selector typeSelector;
    JPanel mainPanel;
    RangeSelector capacitySelector;

    public BuildingsQueryWindow() {
        super("Запрос");
        try {
            setPreferredSize(new Dimension(500, 200));
            setResizable(true);
            setLocation(0, 0);
            setDefaultCloseOperation(HIDE_ON_CLOSE);
            setLayout(new BorderLayout());

            mainPanel = new JPanel(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.weightx = 1;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.BOTH;

            JLabel label = new JLabel("Получить перечень культурных сооружений указанного типа или вместимости");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            add(label, BorderLayout.NORTH);

            add(mainPanel, BorderLayout.CENTER);

            typeSelector = new Selector("Тип здания", GetUtilities.getNamesMap("building_type", "type_id", "type_label"));
            mainPanel.add(typeSelector.getPanel(), gbc);

            gbc.gridy++;
            gbc.gridheight = 2;
            capacitySelector = new RangeSelector("Вместимость");
            capacitySelector.setStartField("0");
            capacitySelector.setEndField("max");
            mainPanel.add(capacitySelector.getPanel(), gbc);

            DialogButtonsPanel dialogButtonsPanel = new DialogButtonsPanel();
            dialogButtonsPanel.cancelButton.addActionListener(e -> dispose());
            dialogButtonsPanel.okButton.addActionListener(e -> applyChanges());

            add(dialogButtonsPanel, BorderLayout.SOUTH);

            pack();
            setVisible(true);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void applyChanges() {
        String sql = "select id, name, capacity from building where type_id = " + typeSelector.getSelectedID();
        // capacity filter
        if (!(capacitySelector.endField.getText().equals("max") && capacitySelector.startField.getText().equals("0"))) {
            String typeTableName = GetUtilities.getBuildingTypeTableName(typeSelector.getSelectedName());

            sql = "SELECT id, name, capacity FROM building JOIN " + typeTableName + " ON building.id = " + typeTableName + ".building_id WHERE building.capacity > " +
                    capacitySelector.getStartIntValue();
            if (!capacitySelector.endField.getText().equals("max")) {
                sql += " and building.capacity < " + capacitySelector.getEndIntValue();
            }
            System.out.println(sql);
        }

        new QueryResultWindow("Здания типа: " + typeSelector.getSelectedName(), sql);
    }
}
