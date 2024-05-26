import javax.swing.*;
import java.awt.*;

// Получить перечень концертных мероприятий, проведенных в указанном культурном сооружении.
public class EventsInBuildingQuery extends JFrame {
    Selector buildingSelector;
    public EventsInBuildingQuery() {
        super("Запрос");
        try {
            setPreferredSize(new Dimension(500, 200));
            setResizable(false);
            setLocation(0, 0);
            setDefaultCloseOperation(HIDE_ON_CLOSE);
            setLayout(new BorderLayout());
            setLayout(new BorderLayout());
            JLabel label = new JLabel("Получить перечень концертных мероприятий, проведенных в указанном культурном сооружении");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            add(label, BorderLayout.NORTH);

            buildingSelector = new Selector("Сооружение", GetUtilities.getNamesMap("building", "id", "name", "address"));
            add(buildingSelector.getPanel(), BorderLayout.CENTER);

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
        String sql = "select id, name from \"event\" where place_id = " + buildingSelector.getSelectedID();
        new QueryResultWindow("Мероприятия в " + buildingSelector.getSelectedName(), sql);
    }
}
