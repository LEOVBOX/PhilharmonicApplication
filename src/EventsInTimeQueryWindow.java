import javax.swing.*;
import java.awt.*;
import java.sql.Date;

public class EventsInTimeQueryWindow extends JFrame {
    JPanel mainPanel;
    RangeSelector timeRangeSelector;

    Selector impresarioSelector;

    JButton impresarioButton;

    JButton deleteButton;

    GridBagConstraints gbc;

    JPanel impresarioPanel;

    boolean isImpresario = false;

    public EventsInTimeQueryWindow() {
        super("Запрос");
        try {
            setPreferredSize(new Dimension(500, 200));
            setResizable(true);
            setLocation(0, 0);
            setDefaultCloseOperation(HIDE_ON_CLOSE);
            setLayout(new BorderLayout());

            mainPanel = new JPanel(new GridBagLayout());

            gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.weightx = 1;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.BOTH;

            gbc.weighty = 1;
            JLabel label = new JLabel("<html>Получить перечень концертных мероприятий<br>проведенных в течение заданного периода времени в целом либо указанным организатором.</html>");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            add(label, BorderLayout.NORTH);

            gbc.weighty = 0;

            gbc.gridy++;
            gbc.gridheight = 1;
            timeRangeSelector = new RangeSelector("Временной промежуток");
            mainPanel.add(timeRangeSelector.getPanel(), gbc);

            gbc.gridheight = 1;
            gbc.gridy++;
            impresarioButton = new JButton("Выбрать организатора");
            impresarioButton.addActionListener(e -> {
                isImpresario = true;
                mainPanel.remove(impresarioButton);
                mainPanel.add(deleteButton, gbc);
                gbc.gridy++;
                mainPanel.add(impresarioPanel, gbc);
                mainPanel.revalidate();
                mainPanel.repaint();
            });

            mainPanel.add(impresarioButton, gbc);

            deleteButton = new JButton("Удалить условие");
            deleteButton.addActionListener(e -> {
                isImpresario = false;
                mainPanel.remove(deleteButton);
                mainPanel.remove(impresarioPanel);
                gbc.gridy--;
                mainPanel.add(impresarioButton, gbc);
                mainPanel.revalidate();
                mainPanel.repaint();
            });

            impresarioSelector = new Selector("Организатор", GetUtilities.getNamesMap("impresario", "id", "last_name", "first_name", "surname"));
            impresarioPanel = impresarioSelector.getPanel();

            DialogButtonsPanel dialogButtonsPanel = new DialogButtonsPanel();
            dialogButtonsPanel.cancelButton.addActionListener(e -> dispose());
            dialogButtonsPanel.okButton.addActionListener(e -> applyChanges());

            add(dialogButtonsPanel, BorderLayout.SOUTH);
            add(mainPanel, BorderLayout.CENTER);

            pack();
            setVisible(true);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void applyChanges() {
        Date startDate = timeRangeSelector.getStartDate();
        Date endDate = timeRangeSelector.getEndDate();
        String prepareSQL = "select id, name from \"event\" where date between '%s' and '%s'";
        String sql = String.format(prepareSQL, timeRangeSelector.getStartDate().toString(), timeRangeSelector.getEndDate().toString());
        if (isImpresario) {
            sql += " and organizer_id = " + impresarioSelector.getSelectedID();
        }

        System.out.println(sql);
        new QueryResultWindow(startDate + " : " + endDate, sql);
    }
}
