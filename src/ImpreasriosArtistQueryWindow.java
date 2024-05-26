import javax.swing.*;
import java.awt.*;

// Получить список импресарио указанного артиста.
public class ImpreasriosArtistQueryWindow extends JFrame{
    Selector aritstSelector;
    public ImpreasriosArtistQueryWindow() {
        try {
            setPreferredSize(new Dimension(500, 200));
            setResizable(false);
            setLocation(0, 0);
            setDefaultCloseOperation(HIDE_ON_CLOSE);
            setLayout(new BorderLayout());
            setLayout(new BorderLayout());
            JLabel label = new JLabel("Получить список импресарио, указанного артиста.");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            add(label, BorderLayout.NORTH);

            aritstSelector = new Selector("Артист", GetUtilities.getNames("artist"));
            add(aritstSelector.getPanel(), BorderLayout.CENTER);


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
        String sql = "select id, last_name, first_name, surname from impresario join work_with on\n" +
                "impresario.id = work_with.impresario_id \n" +
                "where artist_id = " + aritstSelector.getSelectedID();
        new QueryResultWindow("Импресарио работающие с " + aritstSelector.getSelectedName(), sql);
    }
}
