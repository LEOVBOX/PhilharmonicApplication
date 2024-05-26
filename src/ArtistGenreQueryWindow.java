import javax.swing.*;
import java.awt.*;

public class ArtistGenreQueryWindow extends JFrame {
    Selector genres;
    public ArtistGenreQueryWindow() {
        super("Запрос");
        try {
            setPreferredSize(new Dimension(500, 200));
            setResizable(false);
            setLocation(0, 0);
            setDefaultCloseOperation(HIDE_ON_CLOSE);
            setLayout(new BorderLayout());
            setLayout(new BorderLayout());
            JLabel label = new JLabel("Получить список артистов, выступающих в некотором жанре.");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            add(label, BorderLayout.NORTH);

            genres = new Selector("Жанр", GetUtilities.getNamesMap("genre", "id", "name"));
            add(genres.getPanel(), BorderLayout.CENTER);


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
        String sql = "select id, last_name, first_name, surname from artist join\n" +
                "artist_genre on artist.id = artist_genre.artist_id\n" +
                "where genre_id = " + genres.getSelectedID();
        new QueryResultWindow("Артисты выступающие в жанре " + genres.getSelectedName(), sql);
    }
}

