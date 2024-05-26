import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MultipleGenreArtistQueryWindow extends JFrame {
    JPanel mainPanel;
    ScrollablePanel genresPanel;

    int lastSelectorIndex;

    JButton addGenreButton;
    JButton removeGenreButton;

    ArrayList<Selector> genreSelectors;

    HashMap<String, Integer> genresMap;


    public MultipleGenreArtistQueryWindow() {
        super("Multiple-genre artist query");
        try {
            genresMap = GetUtilities.getNamesMap("genre", "id", "name");
            setPreferredSize(new Dimension(400, 200));
            setResizable(true);
            setLocation(0, 0);
            setDefaultCloseOperation(HIDE_ON_CLOSE);
            setLayout(new BorderLayout());

            JPanel genreButtonsPanel = new JPanel();
            genreButtonsPanel.setLayout(new BoxLayout(genreButtonsPanel, BoxLayout.X_AXIS));

            genreSelectors = new ArrayList<>();
            genreSelectors.add(new Selector("1", genresMap));
            addGenreButton = new JButton("Добавить жанр");

            removeGenreButton = new JButton("Удалить жанр");

            addGenreButton.addActionListener(e -> {
                genreSelectors.add(new Selector(Integer.toString(genreSelectors.size() + 1), genresMap));
                genresPanel.add(genreSelectors.getLast().getPanel());
                lastSelectorIndex++;
            });

            removeGenreButton.addActionListener(e -> {
                if (lastSelectorIndex > 0) {
                    genreSelectors.removeLast();
                    genresPanel.remove(lastSelectorIndex);
                    lastSelectorIndex--;
                }
            });


            genreButtonsPanel.add(addGenreButton);
            genreButtonsPanel.add(removeGenreButton);


            mainPanel = new JPanel(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.weightx = 1;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.weighty = 1;
            gbc.fill = GridBagConstraints.BOTH;

            gbc.gridy++;
            JLabel genresLabel = new JLabel("Жанры в которых выступает артист");
            genresLabel.setHorizontalAlignment(SwingConstants.CENTER);
            mainPanel.add(genresLabel, gbc);

            gbc.gridy++;
            genresPanel = new ScrollablePanel();
            genresPanel.add(genreButtonsPanel);
            genresPanel.add(genreSelectors.getFirst().getPanel());
            lastSelectorIndex = 1;
            gbc.weighty = 3;
            mainPanel.add(genresPanel, gbc);

            DialogButtonsPanel dialogButtonsPanel = new DialogButtonsPanel();
            dialogButtonsPanel.cancelButton.addActionListener(e -> dispose());
            dialogButtonsPanel.okButton.addActionListener(e -> applyChanges());

            add(mainPanel, BorderLayout.CENTER);
            add(dialogButtonsPanel, BorderLayout.SOUTH);
            pack();
            setVisible(true);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private HashSet<Integer> getSelectedGenresSet() {
        HashSet<Integer> selectedGenreId = new HashSet<>();
        for (Selector selector : genreSelectors) {
            selectedGenreId.add(selector.getSelectedID());
        }

        return selectedGenreId;
    }

    private String getSelectedGenres(HashSet<Integer> selectedGenreId) {
        Integer[] idArray = selectedGenreId.toArray(new Integer[0]);
        StringBuilder genres = new StringBuilder("(");
        for (int i = 0; i < idArray.length - 1; i++) {
            genres.append(idArray[i]).append(", ");
        }
        genres.append(idArray[idArray.length - 1]).append(")");

        return genres.toString();
    }

    private String getWindowName() {
        StringBuilder windowName = new StringBuilder("Артистсты, выступающие в жанрах:");
        for (Selector selector: genreSelectors) {
            windowName.append(" ").append(selector.getSelectedName());
        }
        return windowName.toString();
    }

    private void applyChanges() {

        String prepareSQL = """
                SELECT a.id, a.last_name , a.first_name , a.surname
                FROM artist AS a
                JOIN (
                    SELECT artist_id
                    FROM artist_genre
                    WHERE genre_id IN %s  -- Замените на ваши genre_id
                    GROUP BY artist_id
                    HAVING COUNT(DISTINCT genre_id) = %d -- Количество заменить на количество жанров
                ) AS ag ON a.id = ag.artist_id;
                """;

        HashSet<Integer> selectedGenresId = getSelectedGenresSet();
        String resultSQL = String.format(prepareSQL, getSelectedGenres(selectedGenresId), selectedGenresId.size());

        new QueryResultWindow(getWindowName(), resultSQL);

    }
}
