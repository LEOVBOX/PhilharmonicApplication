import javax.swing.*;
import java.awt.*;
import java.sql.Date;

// Получить список организаторов культурных мероприятий и число проведенных ими концертов в течение определенного периода времени.
public class OrganizerTimeQueryWindow extends JFrame {
    JPanel mainPanel;
    RangeSelector timeRangeSelector;

    public OrganizerTimeQueryWindow() {
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

            JLabel label = new JLabel("Получить список организаторов культурных мероприятий и число проведенных ими концертов в течение определенного периода времени.");
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
        SELECT i.id, i.first_name, i.last_name, i.surname, COUNT(e.id) AS event_count
        FROM impresario i
        LEFT JOIN event e ON i.id = e.organizer_id
        WHERE e.date BETWEEN '%s' AND '%s'
        GROUP BY i.id, i.first_name, i.last_name, i.surname
        ORDER BY event_count DESC
        """;

        String sql = String.format(prepareSQL, startDate.toString(), endDate.toString());

        new QueryResultWindow(startDate + " : " + endDate, sql);
    }
}
