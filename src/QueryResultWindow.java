import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class QueryResultWindow extends JFrame {
    String sqlQuery;

    public QueryResultWindow(String windowName, String sqlQuery) {
        super(windowName);
        try {
            this.sqlQuery = sqlQuery;
            JPanel panel = new JPanel(new BorderLayout());
            add(panel);
            // Данные для таблицы
            Object[][] data = getDataFromDatabase();
            // Заголовки столбцов
            String[] columnNames = getColumnNamesFromDatabase();

            // Создаем модель таблицы и JTable
            DefaultTableModel model = new DefaultTableModel(data, columnNames);
            JTable table = new JTable(model);

            panel.add(new JScrollPane(table), BorderLayout.CENTER);


            setSize(new Dimension(640, 480));
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "При выполнении запроса произошла ошибка\n"
                    + e.getMessage());
        }
    }

    private Object[][] getDataFromDatabase() throws SQLException {
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password);
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            resultSet.last();
            int rowCount = resultSet.getRow();
            resultSet.beforeFirst();

            Object[][] data = new Object[rowCount][columnCount];

            int rowIndex = 0;
            while (resultSet.next()) {
                for (int colIndex = 1; colIndex <= columnCount; colIndex++) {
                    data[rowIndex][colIndex - 1] = resultSet.getObject(colIndex);
                }
                rowIndex++;
            }
            return data;
        }
    }


    private String[] getColumnNamesFromDatabase() throws SQLException {
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url,
                ConnectionConfig.username, ConnectionConfig.password)) {
            // SQL-запрос
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sqlQuery)) {

                // Получаем метаданные для определения названий столбцов
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Создаем массив для названий столбцов
                String[] columnNames = new String[columnCount];

                for (int colIndex = 1; colIndex <= columnCount; colIndex++) {
                    columnNames[colIndex - 1] = metaData.getColumnName(colIndex);
                }
                return columnNames;
            }
        }
    }
}
