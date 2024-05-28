import java.sql.*;
import java.util.HashMap;

public class GetUtilities {

    public static HashMap<String, Integer> getNamesMap(String tableName, String idLabel, String... nameLabels) throws SQLException {
        HashMap<String, Integer> names = new HashMap<>();
        String preparedSQL = "SELECT %s, %s FROM %s";
        String nameLabel = String.join(", ", nameLabels);
        String sql = String.format(preparedSQL, idLabel, nameLabel, tableName);

        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password)) {
            Statement statement = connection.createStatement();

            try (ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    Integer id = resultSet.getInt(idLabel);
                    StringBuilder name = new StringBuilder();
                    for (int i = 0; i < nameLabels.length; i++) {
                        name.append(resultSet.getString(nameLabels[i]));
                        if (i != nameLabels.length - 1)
                            name.append(" ");
                    }

                    names.put(name.toString(), id);
                }
            }
        }

        return names;
    }

    public static HashMap<String, Integer> getCompetitionsMap() throws SQLException {
        HashMap<String, Integer> competitions = new HashMap<>();
        try (Connection connection = DriverManager.getConnection(ConnectionConfig.url, ConnectionConfig.username, ConnectionConfig.password);
             Statement statement = connection.createStatement()) {

            try (ResultSet resultSet = statement.executeQuery("""
                    select id, name from event\s
                    join event_type on event.type_id = event_type.type_id\s
                    where event_type.type_name = 'Конкурс'""")) {
                while (resultSet.next()) {
                    Integer id = resultSet.getInt("id");
                    String name = resultSet.getString("name");

                    competitions.put(name, id);
                }
            }
        }
        return competitions;
    }
}
