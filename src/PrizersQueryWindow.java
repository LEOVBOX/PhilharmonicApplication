import javax.swing.*;
import java.awt.*;

// Получить список призеров указанного конкурса.
public class PrizersQueryWindow extends JFrame {
    Selector competitionSelector;
    public PrizersQueryWindow() {
        super("Запрос");
        try {
            setPreferredSize(new Dimension(500, 200));
            setResizable(false);
            setLocation(0, 0);
            setDefaultCloseOperation(HIDE_ON_CLOSE);
            setLayout(new BorderLayout());
            setLayout(new BorderLayout());
            JLabel label = new JLabel("Получить список призеров, указанного конкурса.");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            add(label, BorderLayout.NORTH);

            competitionSelector = new Selector("Конкурс", GetUtilities.getCompetitions());
            add(competitionSelector.getPanel(), BorderLayout.CENTER);


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
        String sql = "select artist.id, last_name, first_name, surname from artist join awards on\n" +
                "artist.id = awards.artist_id where event_id = " + competitionSelector.getSelectedID();
        new QueryResultWindow("Призеры конкурса " + competitionSelector.getSelectedName(), sql);
    }
}
