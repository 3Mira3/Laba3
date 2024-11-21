package stu.cn.ua.laba3rest;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.sql.DataSource;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/data")
public class Controller {

    @Autowired
    private DataSource dataSource;

    /*@PostMapping("/execute")
    public String executeQuery(@RequestParam String query) {
        String result;
        try (Connection connection = dataSource.getConnection()) {
            result = parseAndExecuteCommand(connection, query);
        } catch (SQLException e) {
            result = "Database Error: " + e.getMessage();
        } catch (Exception e) {
            result = "Error: " + e.getMessage();
        }
        return result;
    }*/

    private String parseAndExecuteCommand(Connection connection, String query) throws SQLException {
        Pattern insertPattern = Pattern.compile("insert (\\w+)\\((.*)\\)", Pattern.CASE_INSENSITIVE);
        Pattern deletePattern = Pattern.compile("delete (\\w+)\\((.*)\\)", Pattern.CASE_INSENSITIVE);
        Pattern updatePattern = Pattern.compile("update (\\w+)\\((.*)\\)", Pattern.CASE_INSENSITIVE);
        Pattern selectPattern = Pattern.compile("select (\\w+)", Pattern.CASE_INSENSITIVE);

        Matcher insertMatcher = insertPattern.matcher(query);
        Matcher deleteMatcher = deletePattern.matcher(query);
        Matcher updateMatcher = updatePattern.matcher(query);
        Matcher selectMatcher = selectPattern.matcher(query);

        if (insertMatcher.matches()) {
            return handleInsert(connection, insertMatcher.group(1), insertMatcher.group(2));
        } else if (deleteMatcher.matches()) {
            return handleDelete(connection, deleteMatcher.group(1), deleteMatcher.group(2));
        } else if (updateMatcher.matches()) {
            return handleUpdate(connection, updateMatcher.group(1), updateMatcher.group(2));
        } else if (selectMatcher.matches()) {
            return handleSelect(connection, selectMatcher.group(1));
        }
        return "Wrong command.";
    }

    private String handleInsert(Connection connection, String tableName, String values) throws SQLException {
        String sql;
        PreparedStatement preparedStatement;

        switch (tableName.toLowerCase()) {
            case "country":
                sql = "INSERT INTO country (name, priority) VALUES (?, ?)";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, extractValue(values, "name"));
                preparedStatement.setInt(2, Integer.parseInt(extractValue(values, "priority")));
                break;

            case "participants":
                sql = "INSERT INTO participants (name, tour_name) VALUES (?, ?)";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, extractValue(values, "name"));
                preparedStatement.setInt(2, Integer.parseInt(extractValue(values, "tour_name")));
                break;

            case "tour":
                sql = "INSERT INTO tour (name, prise) VALUES (?, ?)";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, extractValue(values, "name"));
                preparedStatement.setInt(2, Integer.parseInt(extractValue(values, "prise")));
                break;

            default:
                return "Wrong table name!";
        }

        int rowsAffected = preparedStatement.executeUpdate();
        return "Inserted " + rowsAffected + " row(s) into " + tableName;
    }

    private String handleDelete(Connection connection, String tableName, String values) throws SQLException {
        String sql;
        PreparedStatement preparedStatement;

        switch (tableName.toLowerCase()) {
            case "country":
            case "participants":
            case "tour":
                sql = "DELETE FROM " + tableName + " WHERE id = ?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, Integer.parseInt(extractValue(values, "id")));
                break;

            default:
                return "Wrong table name!";
        }
        int rowsAffected = preparedStatement.executeUpdate();
        return "Deleted " + rowsAffected + " row(s) from " + tableName;
    }

    private String handleUpdate(Connection connection, String tableName, String values) throws SQLException {
        String sql;
        PreparedStatement preparedStatement;

        switch (tableName.toLowerCase()) {
            case "country":
                sql = "UPDATE country SET name = ?, priority = ? WHERE id = ?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, extractValue(values, "name"));
                preparedStatement.setInt(2, Integer.parseInt(extractValue(values, "priority")));
                preparedStatement.setInt(3, Integer.parseInt(extractValue(values, "id")));
                break;

            case "participants":
                sql = "UPDATE participants SET name = ? WHERE tour_name = ?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, extractValue(values, "name"));
                preparedStatement.setInt(2, Integer.parseInt(extractValue(values, "tour_name")));
                break;

            case "tour":
                sql = "UPDATE tour SET name = ?, prise = ? WHERE id = ?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, extractValue(values, "name"));
                preparedStatement.setInt(2, Integer.parseInt(extractValue(values, "prise")));
                preparedStatement.setInt(3, Integer.parseInt(extractValue(values, "id")));
                break;

            default:
                return "Wrong table name!";
        }

        int rowsAffected = preparedStatement.executeUpdate();
        return "Updated " + rowsAffected + " row(s) in " + tableName;
    }

    private String handleSelect(Connection connection, String tableName) throws SQLException {
        StringBuilder result = new StringBuilder();
        String sql = "SELECT * FROM " + tableName;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                result.append(metaData.getColumnName(i)).append("\t");
            }
            result.append("<br>");

            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    result.append(resultSet.getString(i)).append("\t");
                }
                result.append("<br>");
            }
        }

        return result.toString();
    }

    private String extractValue(String values, String fieldName) {
        Pattern fieldPattern = Pattern.compile(fieldName + "=\\'([^']+)\\'", Pattern.CASE_INSENSITIVE);
        Matcher fieldMatcher = fieldPattern.matcher(values);

        if (fieldMatcher.find()) {
            return fieldMatcher.group(1);
        }
        return "";
    }

    private void validateCountryInput(String values) {
        String name = extractValue(values, "name");
        String priority = extractValue(values, "priority");

        if (!name.matches("[a-zA-Z]+")) {
            throw new IllegalArgumentException("Country name must contain only letters.");
        }

        if (!priority.matches("\\d+")) {
            throw new IllegalArgumentException("Country priority must contain only digits.");
        }
    }

    private void validateParticipantsInput(String values) {
        String name = extractValue(values, "name");
        String tourName = extractValue(values, "tour_name");

        if (!name.matches("[a-zA-Z]+")) {
            throw new IllegalArgumentException("Participant name must contain only letters.");
        }

        if (!tourName.matches("[a-zA-Z0-9]+")) {
            throw new IllegalArgumentException("Tour name must contain only letters and digits.");
        }
    }

    private void validateTourInput(String values) {
        String name = extractValue(values, "name");
        String prise = extractValue(values, "prise");

        if (!name.matches("[a-zA-Z]+")) {
            throw new IllegalArgumentException("Tour name must contain only letters.");
        }

        if (!prise.matches("\\d+")) {
            throw new IllegalArgumentException("Tour prise must contain only digits.");
        }
    }

//}


    @PostMapping("/execute")
    public String executeQuery(@RequestParam String query) {
        StringBuilder result = new StringBuilder();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            if (query.trim().toLowerCase().startsWith("select")) {
                try (ResultSet resultSet = statement.executeQuery(query)) {
                    int columnCount = resultSet.getMetaData().getColumnCount();
                    while (resultSet.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            result.append(resultSet.getString(i)).append(" ");
                        }
                        result.append("<br>");
                    }
                }
            } else {
                int rowsAffected = statement.executeUpdate(query);
                result.append("Rows affected: ").append(rowsAffected);
            }
        } catch (SQLException e) {
            result.append("Error: ").append(e.getMessage());
        }
        return result.toString();
    }
    }



