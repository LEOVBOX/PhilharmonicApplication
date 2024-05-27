import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainWindow extends JFrame {
    JButton connectButton;
    JPanel connectPanel;
    JPanel mainPanel;
    JPanel addPanel;
    JPanel queryPanel;

    JPanel editPanel;

    private void initMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.LIGHT_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;

        JButton openAddPanelButton = new JButton("Открыть меню добавления");
        openAddPanelButton.addActionListener(e -> openAddPanel());
        mainPanel.add(openAddPanelButton, gbc);

        gbc.gridy++;

        JButton openQueryPanelButton = new JButton("Открыть меню запросов");
        openQueryPanelButton.addActionListener(e -> openQueryPanel());
        mainPanel.add(openQueryPanelButton, gbc);

        gbc.gridy++;
        JButton openEditPanelButton = new JButton("Открыть меню редактирования");
        openEditPanelButton.addActionListener(e -> openEditPanel());
        mainPanel.add(openEditPanelButton, gbc);

    }

    private JPanel initGoBackPanel() {
        JPanel goBackPanel = new JPanel();
        goBackPanel.setBackground(Color.GRAY);
        goBackPanel.setLayout(new BoxLayout(goBackPanel, BoxLayout.X_AXIS));
        JButton goBackButton = new JButton("назад");
        goBackButton.addActionListener(e -> openMainPanel());
        goBackPanel.add(goBackButton);

        goBackPanel.add(Box.createHorizontalGlue());
        return goBackPanel;
    }

    private void initQueryPanel() {
        queryPanel = new JPanel();
        queryPanel.setLayout(new BorderLayout());

        JPanel goBackPanel = initGoBackPanel();

        queryPanel.add(goBackPanel, BorderLayout.NORTH);

        JPanel queryMenuPanel = new JPanel();
        queryMenuPanel.setBackground(Color.GRAY);
        queryMenuPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;
        gbc.gridy = 0;
        gbc.gridx = 0;

        JButton getBuildingsButton = new JButton("1. Получить перечень культурных сооружений указанного типа в целом или удовлетворяющих заданным характеристикам ");
        getBuildingsButton.addActionListener(e -> new BuildingsQueryWindow());
        queryMenuPanel.add(getBuildingsButton, gbc);

        gbc.gridy++;
        JButton getArtistsGenreButton = new JButton("2. Получить список артистов, выступающих в некотом жанре");
        getArtistsGenreButton.addActionListener(e -> new ArtistGenreQueryWindow());
        queryMenuPanel.add(getArtistsGenreButton, gbc);

        gbc.gridy++;
        JButton getArtistsImpresarioButton = new JButton("3. Получить список артистов, работающих с некоторым импресарио");
        getArtistsImpresarioButton.addActionListener(e -> new ArtistsImpresarioQueryWindow());
        queryMenuPanel.add(getArtistsImpresarioButton, gbc);

        gbc.gridy++;
        JButton getArtistMultGenreButton = new JButton("4. Получить список артистов, выступающих более чем в одним жанре с их указанием");
        getArtistMultGenreButton.addActionListener(e -> new MultipleGenreArtistQueryWindow());
        queryMenuPanel.add(getArtistMultGenreButton, gbc);

        gbc.gridy++;
        JButton getImpresarioArtistsButton = new JButton("5. Получить список импресарио указанного артиста");
        getImpresarioArtistsButton.addActionListener(e -> new ImpreasriosArtistQueryWindow());
        queryMenuPanel.add(getImpresarioArtistsButton, gbc);

        gbc.gridy++;
        JButton getConcertsButton = new JButton("6. Получить перечень концертных мероприятий, проведенных в течение заданного периода времени в целом либо указанным организатором");
        getConcertsButton.addActionListener(e -> new EventsInTimeQueryWindow());
        queryMenuPanel.add(getConcertsButton, gbc);

        gbc.gridy++;
        JButton getPrizersButton = new JButton("7. Получить список призеров указанного конкурса.");
        getPrizersButton.addActionListener(e -> new PrizersQueryWindow());
        queryMenuPanel.add(getPrizersButton, gbc);

        gbc.gridy++;
        JButton getEventsInBuildingButton = new JButton("8. Получить перечень концертных мероприятий, проведенных в указанном культурном сооружении");
        getEventsInBuildingButton.addActionListener(e -> new EventsInBuildingQuery());
        queryMenuPanel.add(getEventsInBuildingButton, gbc);

        gbc.gridy++;
        JButton getImpresarioInGenreButton = new JButton("9. Получить список импресарио определенного жанра");
        getImpresarioInGenreButton.addActionListener(e -> new ImpresarioInGenreQueryWindow());
        queryMenuPanel.add(getImpresarioInGenreButton, gbc);

        gbc.gridy++;
        JButton getNotParticipatingArtistsButton = new JButton("10. Получить список артистов, не участвовавших ни в каких конкурсах в течение определенного периода времени.");
        getNotParticipatingArtistsButton.addActionListener(e -> new SelectNotParticipatingArtistWindow());
        queryMenuPanel.add(getNotParticipatingArtistsButton, gbc);

        gbc.gridy++;
        JButton getOrganizerStatisticsButton = new JButton("11. Получить список организаторов культурных мероприятий и число проведенных ими концертов в течение определенного периода времени.");
        getOrganizerStatisticsButton.addActionListener(e -> new OrganizerTimeQueryWindow());
        queryMenuPanel.add(getOrganizerStatisticsButton, gbc);

        gbc.gridy++;
        JButton getBuildingsEventsButton = new JButton("12. Получить перечень культурных сооружений, а также даты проведения на них культурных мероприятий в течение определенного периода времени.");
        getBuildingsEventsButton.addActionListener(e -> new BuildingsEventsWindow());
        queryMenuPanel.add(getBuildingsEventsButton, gbc);

        queryPanel.add(queryMenuPanel, BorderLayout.CENTER);

    }

    private void initEditPanel() {
        editPanel = new JPanel();
        editPanel.setLayout(new BorderLayout());

        JPanel goBackPanel = initGoBackPanel();
        editPanel.add(goBackPanel, BorderLayout.NORTH);

        JPanel editMenuPanel = new JPanel();
        editMenuPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;

        JButton editArtistButton = new JButton("Редактировать артиста");
        editArtistButton.addActionListener(e -> new EditPersonForm("artist"));
        editMenuPanel.setBackground(Color.GRAY);
        editMenuPanel.add(editArtistButton, gbc);

        gbc.gridy++;
        JButton newImpresarioButton = new JButton("Редактировать импресарио");
        newImpresarioButton.addActionListener(e -> new EditPersonForm("impresario"));
        editMenuPanel.add(newImpresarioButton, gbc);

        gbc.gridy++;
        JButton newGenreButton = new JButton("Редактировать жанр");
        newGenreButton.addActionListener(e -> new EditOneNameWindow("genre", "id", "name", "Жанр"));
        editMenuPanel.add(newGenreButton, gbc);

        gbc.gridy++;
        JButton newEventTypeButton = new JButton("Редактировать тип мероприятия");
        newEventTypeButton.addActionListener(e -> new EditOneNameWindow("event_type", "type_id", "name", "Тип мероприятия"));
        editMenuPanel.add(newEventTypeButton, gbc);

        gbc.gridy++;
        JButton newEventButton = new JButton("Редактировать мероприятие");
        newEventButton.addActionListener(e -> new EditEventWindow());
        editMenuPanel.add(newEventButton, gbc);

        gbc.gridy++;
        JButton impresarioGenreButton = new JButton("Удалить жанр импресарио");
        impresarioGenreButton.addActionListener(e -> new DeleteImpresarioGenreWindow());
        editMenuPanel.add(impresarioGenreButton, gbc);

        gbc.gridy++;
        JButton artistGenreButton = new JButton("Удалить жанр артиста");
        artistGenreButton.addActionListener(e -> new DeleteArtistGenreWindow());
        editMenuPanel.add(artistGenreButton, gbc);

        gbc.gridy++;
        JButton workWithButton = new JButton("Удалить свзяь импресарио-артист");
        workWithButton.addActionListener(e -> new DeleteImpresarioArtistWindow());
        editMenuPanel.add(workWithButton, gbc);

        gbc.gridy++;
        JButton newBuildingButton = new JButton("Редактировать сооружение");
        newBuildingButton.addActionListener(e -> new EditBuildingWindow());
        editMenuPanel.add(newBuildingButton, gbc);

        gbc.gridy++;
        JButton newAwardButton = new JButton("Редактировать награду");
        newAwardButton.addActionListener(e -> new EditAwardWindow());
        editMenuPanel.add(newAwardButton, gbc);

        gbc.gridy++;
        JButton addArtistToEventButton = new JButton("Добавить артиста на мероприятие");
        addArtistToEventButton.addActionListener(e -> new NewArtistEventWindow());
        editMenuPanel.add(addArtistToEventButton, gbc);

        editPanel.add(editMenuPanel, BorderLayout.CENTER);
    }

    private void initAddPanel() {
        addPanel = new JPanel();
        addPanel.setLayout(new BorderLayout());

        JPanel goBackPanel = initGoBackPanel();

        addPanel.add(goBackPanel, BorderLayout.NORTH);

        JPanel addMenuPanel = new JPanel();
        addMenuPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;

        JButton newArtistButton = new JButton("Добавить нового артиста");
        newArtistButton.addActionListener(e -> new NewArtistWindow());
        addMenuPanel.setBackground(Color.GRAY);
        addMenuPanel.add(newArtistButton, gbc);

        gbc.gridy++;
        JButton newImpresarioButton = new JButton("Добавить нового импресарио");
        newImpresarioButton.addActionListener(e -> new NewImpresarioWindow());
        addMenuPanel.add(newImpresarioButton, gbc);

        gbc.gridy++;
        JButton newGenreButton = new JButton("Добавить новый жанр");
        newGenreButton.addActionListener(e -> new NewGenreWindow());
        addMenuPanel.add(newGenreButton, gbc);

        gbc.gridy++;
        JButton newEventTypeButton = new JButton("Добавить новый тип мероприятия");
        newEventTypeButton.addActionListener(e -> new NewEventTypeWindow());
        addMenuPanel.add(newEventTypeButton, gbc);

        gbc.gridy++;
        JButton newEventButton = new JButton("Добавить новое мероприятие");
        newEventButton.addActionListener(e -> new NewEventWindow());
        addMenuPanel.add(newEventButton, gbc);

        gbc.gridy++;
        JButton impresarioGenreButton = new JButton("Импресарио-жанр");
        impresarioGenreButton.addActionListener(e -> new NewGenreRelationWindow("Жанр-импресарио", true));
        addMenuPanel.add(impresarioGenreButton, gbc);

        gbc.gridy++;
        JButton artistGenreButton = new JButton("Артист-жанр");
        artistGenreButton.addActionListener(e -> new NewGenreRelationWindow("Жанр-артист", false));
        addMenuPanel.add(artistGenreButton, gbc);

        gbc.gridy++;
        JButton workWithButton = new JButton("Создать связь атрист-импресарио");
        workWithButton.addActionListener(e -> new ImpresarioArtistWindow());
        addMenuPanel.add(workWithButton, gbc);

        gbc.gridy++;
        JButton newBuildingButton = new JButton("Добавить культурное сооружение");
        newBuildingButton.addActionListener(e -> new NewBuildingWindow());
        addMenuPanel.add(newBuildingButton, gbc);

        gbc.gridy++;
        JButton newAwardButton = new JButton("Добавить новую награду");
        newAwardButton.addActionListener(e -> new NewAwardWindow());
        addMenuPanel.add(newAwardButton, gbc);

        gbc.gridy++;
        JButton addArtistToEventButton = new JButton("Добавить артиста на мероприятие");
        addArtistToEventButton.addActionListener(e -> new NewArtistEventWindow());
        addMenuPanel.add(addArtistToEventButton, gbc);

        addPanel.add(addMenuPanel, BorderLayout.CENTER);
    }

    private void openPanel(JPanel panel) {
        this.getContentPane().removeAll();
        this.getContentPane().add(panel);
        revalidate();
        repaint();
    }


    private void openQueryPanel() {
        setSize(new Dimension(1000, 480));
        openPanel(queryPanel);
    }

    private void openAddPanel() {
        setSize(new Dimension(640, 480));
        openPanel(addPanel);
    }

    void openMainPanel() {
        setSize(new Dimension(480, 480));
        openPanel(mainPanel);
    }

    void openEditPanel() {
        openPanel(editPanel);
    }


    private void connectBD() {
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password)) {
            connectButton.setBackground(Color.red);
            connectButton.setEnabled(false);
            System.out.println("Successful connected");
            openMainPanel();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Ошибка подключения к базе данных\n" + e.getMessage());
            connectButton.setText("Подключиться к базе данных");
            connectButton.setEnabled(true);
        }
    }

    private void initConnectPanel() {
        connectPanel = new JPanel();
        connectPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;

        connectButton = new JButton("Подключиться к базе данных");
        connectButton.addActionListener(e -> {
            connectButton.setText("Подключение...");
            connectBD();
        });
        connectPanel.add(connectButton, gbc);
    }

    public MainWindow() {
        super("Philharmonic");
        try {
            setSize(new Dimension(640, 480));
            initConnectPanel();
            initMainPanel();
            initAddPanel();
            initQueryPanel();
            initEditPanel();
            add(connectPanel);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setVisible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
    }
}