import javax.swing.*;
import java.awt.*;
import java.sql.Date;

public class BuildingsEventsWindow extends JFrame {
    JPanel mainPanel;
    RangeSelector timeRangeSelector;

    public BuildingsEventsWindow() {
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

            JLabel label = new JLabel("<html>Получить перечень культурных сооружений<br>а также даты проведения на них культурных мероприятий в течение определенного периода времени.</html>");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            add(label, BorderLayout.NORTH);

            add(mainPanel, BorderLayout.CENTER);

            gbc.gridy++;
            gbc.gridheight = 2;
            timeRangeSelector = new RangeSelector("Временной промежуток");
            mainPanel.add(timeRangeSelector.getPanel(), gbc);

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
        Date startDate = timeRangeSelector.getStartDate();
        Date endDate = timeRangeSelector.getEndDate();
        String prepareSQL = """
                        SELECT building.id, building.name, date, event.name FROM building LEFT JOIN "event" ON
                        building.id = "event".place_id AND date between '%s' AND '%s'
                """;

        String sql = String.format(prepareSQL, startDate.toString(), endDate.toString());

        new QueryResultWindow(startDate + " : " + endDate, sql);
    }
}
