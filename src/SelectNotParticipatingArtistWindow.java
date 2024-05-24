import javax.swing.*;
import java.awt.*;
import java.sql.Date;

// Получить список артистов, не участвовавших ни в каких конкурсах в течение определенного периода времени.

public class SelectNotParticipatingArtistWindow extends JFrame {
    JPanel mainPanel;
    RangeSelector timeRangeSelector;

    public SelectNotParticipatingArtistWindow() {
        super("Запрос");
        try {
            setPreferredSize(new Dimension(800, 200));
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

            JLabel label = new JLabel("Получить список артистов, не участвовавших ни в каких конкурсах в течение определенного периода времени.");
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
                        select artist.id, last_name, first_name, surname from artist where artist.id not in (select artist.id from artist join awards on
                        artist.id = awards.artist_id join "event" on
                        event.id  = awards.event_id
                        where "event"."date" between '%s' and '%s')
                """;

        String sql = String.format(prepareSQL, startDate.toString(), endDate.toString());

        new QueryResultWindow(startDate + " - " + endDate, sql);
    }
}
