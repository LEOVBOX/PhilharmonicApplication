import javax.swing.*;
import java.awt.*;

public class ArtistsImpresarioQueryWindow extends JFrame {

    Selector impresarioSelector;
    public ArtistsImpresarioQueryWindow() {
        try {
            setPreferredSize(new Dimension(500, 200));
            setResizable(false);
            setLocation(0, 0);
            setDefaultCloseOperation(HIDE_ON_CLOSE);
            setLayout(new BorderLayout());
            setLayout(new BorderLayout());
            JLabel label = new JLabel("Получить список артистов, работающих с некоторым импресарио.");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            add(label, BorderLayout.NORTH);

            impresarioSelector = new Selector("Импресарио", GetUtilities.getNames(true));
            add(impresarioSelector.getPanel(), BorderLayout.CENTER);


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
        String sql = "select id, last_name, first_name, surname from artist join work_with on\n" +
                "artist.id = work_with.artist_id \n" +
                "where impresario_id = " + impresarioSelector.getSelectedID();
        new QueryResultWindow("Артисты работающие с " + impresarioSelector.getSelectedName(), sql);
    }
}
