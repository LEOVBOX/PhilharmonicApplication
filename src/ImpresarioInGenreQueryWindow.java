import javax.swing.*;
import java.awt.*;

// Получить список импресарио определенного жанра.
public class ImpresarioInGenreQueryWindow extends JFrame {
    Selector genreSelector;
    public ImpresarioInGenreQueryWindow() {
        super("Запрос");
        try {
            setPreferredSize(new Dimension(500, 200));
            setResizable(false);
            setLocation(0, 0);
            setDefaultCloseOperation(HIDE_ON_CLOSE);
            setLayout(new BorderLayout());
            setLayout(new BorderLayout());
            JLabel label = new JLabel("Получить список импресарио определенного жанра.");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            add(label, BorderLayout.NORTH);

            genreSelector = new Selector("Жанр", GetUtilities.getNamesMap("genre", "id", "name"));
            add(genreSelector.getPanel(), BorderLayout.CENTER);

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
        String sql = "SELECT id, last_name, first_name, surname FROM impresario JOIN impresario_genre ON \n" +
                "impresario.id = impresario_genre.impresario_id where genre_id = " + genreSelector.getSelectedID();
        new QueryResultWindow("Импресарио в жанре " + genreSelector.getSelectedName(), sql);
    }
}
