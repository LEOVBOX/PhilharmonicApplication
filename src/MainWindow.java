import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

    String url = "jdbc:postgresql://94.139.247.116:5432/dbstud";
    String username = "l_shaikhutdinov";
    String password = "Cdr2F$30M%Nk";

    Connection connection;

    private void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection closed.");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void initMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;

        newArtistButton = new JButton("Добавить нового артиста");
        newArtistButton.addActionListener(e -> new NewArtistWindow(connection));
        mainPanel.setBackground(Color.GRAY);
        mainPanel.add(newArtistButton, gbc);

        gbc.gridy = 1;
        newImpresarioButton = new JButton("Добавить нового импресарио");
        newImpresarioButton.addActionListener(e -> new NewImpresarioWindow(connection));
        mainPanel.add(newImpresarioButton, gbc);

        gbc.gridy = 2;
        newGenreButton = new JButton("Добавить новый жанр");
        newGenreButton.addActionListener(e -> new NewGenreWindow(connection));
        mainPanel.add(newGenreButton, gbc);

        gbc.gridy = 3;
        newEventButton = new JButton("Добавить новое мероприятие");
        //newEventButton.addActionListener(e -> new NewEventWindow());
        this.getContentPane().add(mainPanel);
    }

    private void connectBD() {
        try {
            connectButton.setText("Подключение...");
            connectButton.setEnabled(false);
            connection = DriverManager.getConnection(url, username, password);
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
        connectButton.addActionListener(e -> connectBD());
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

            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    closeConnection();
                    JOptionPane.showMessageDialog(getParent(), "Соединение успешно закрыто");
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
    }
}