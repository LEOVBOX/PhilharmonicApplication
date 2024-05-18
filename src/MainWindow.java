import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainWindow extends JFrame {
    JButton connectButton;
    JPanel connectPanel;
    JPanel mainPanel;
    JButton newArtistButton;
    JButton newImpresarioButton;
    JButton newGenreButton;
    JButton newEventButton;
    JButton impresarioGenreButton;
    JButton artistGenreButton;

    private void initMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;

        newArtistButton = new JButton("Добавить нового артиста");
        newArtistButton.addActionListener(e -> new NewArtistWindow());
        mainPanel.setBackground(Color.GRAY);
        mainPanel.add(newArtistButton, gbc);

        gbc.gridy = 1;
        newImpresarioButton = new JButton("Добавить нового импресарио");
        newImpresarioButton.addActionListener(e -> new NewImpresarioWindow());
        mainPanel.add(newImpresarioButton, gbc);

        gbc.gridy = 2;
        newGenreButton = new JButton("Добавить новый жанр");
        newGenreButton.addActionListener(e -> new NewGenreWindow());
        mainPanel.add(newGenreButton, gbc);

        gbc.gridy = 3;
        newEventButton = new JButton("Добавить новое мероприятие");
        //newEventButton.addActionListener(e -> new NewEventWindow());

        gbc.gridy = 4;
        impresarioGenreButton = new JButton("Импресарио-жанр");
        impresarioGenreButton.addActionListener(e -> new NewGenreRelationWindow("Жанр-импресарио", true));
        mainPanel.add(impresarioGenreButton, gbc);

        gbc.gridy = 5;
        artistGenreButton = new JButton("Артист-жанр");
        artistGenreButton.addActionListener(e -> new NewGenreRelationWindow("Жанр-артист", false));
        mainPanel.add(artistGenreButton, gbc);





        this.getContentPane().add(mainPanel);


    }

    private void connectBD() {
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password)) {
            connectButton.setBackground(Color.red);
            connectButton.setEnabled(false);
            System.out.println("Successful connected");
            this.getContentPane().remove(connectPanel);
            initMainPanel();
            revalidate(); // Обновляем содержимое JFrame
            repaint();   // Перерисовываем JFrame
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