package com.marotech.skillhub.repository;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.*;

public class QueryResultAsString {

    public static String query(String dbUrl, String username, String password,
                               String driverClassName, String query, ResultsType resultsType)
            throws SQLException, ClassNotFoundException {
        if (ResultsType.TABULAR == resultsType) {
            return queryAsTabular(dbUrl, username, password, driverClassName, query);
        }
        return queryAsJSON(dbUrl, username, password, driverClassName, query);
    }

    private static String queryAsJSON(String dbUrl, String username, String password,
                                      String driverClassName, String query)
            throws SQLException, ClassNotFoundException {
        // Load the JDBC driver
        Class.forName(driverClassName);

        // Establish a connection
        try (Connection conn = DriverManager.getConnection(dbUrl, username, password)) {
            // Create a statement
            try (Statement stmt = conn.createStatement()) {
                // Execute the query
                try (ResultSet resultSet = stmt.executeQuery(query)) {
                    // Get the metadata
                    ResultSetMetaData metaData = resultSet.getMetaData();

                    // Create a JsonArray to hold all rows
                    JsonArray jsonArray = new JsonArray();

                    // Iterate over the rows
                    while (resultSet.next()) {
                        // Create a JsonObject for each row
                        JsonObject jsonObject = new JsonObject();

                        // Iterate over the columns
                        for (int i = 1; i <= metaData.getColumnCount(); i++) {
                            // Get the column name
                            String columnName = metaData.getColumnName(i);

                            // Get the column value as an object
                            Object value = resultSet.getObject(i);

                            // Add the column name and value to the JsonObject
                            if (value == null) {
                                jsonObject.addProperty(columnName, (String) null); // Handle null values
                            } else {
                                jsonObject.addProperty(columnName, value.toString());
                            }
                        }

                        // Add the JsonObject to the JsonArray
                        jsonArray.add(jsonObject);
                    }

                    // Convert the JsonArray to a JSON string using Gson
                    Gson gson = new Gson();
                    return gson.toJson(jsonArray);
                }
            }
        }
    }

    private static String queryAsTabular(String dbUrl, String username, String password,
                                         String driverClassName, String query)
            throws SQLException, ClassNotFoundException {
        // Load the JDBC driver
        Class.forName(driverClassName);
        // Establish a connection
        try (Connection conn = DriverManager.getConnection(dbUrl, username, password)) {
            // Create a statement
            try (Statement stmt = conn.createStatement()) {
                // Execute the query
                try (ResultSet resultSet = stmt.executeQuery(query)) {
                    // Get the metadata
                    ResultSetMetaData metaData = resultSet.getMetaData();

                    // Initialize the result string
                    StringBuilder resultString = new StringBuilder();

                    // Iterate over the rows
                    while (resultSet.next()) {
                        // Iterate over the columns
                        for (int i = 1; i <= metaData.getColumnCount(); i++) {
                            // Get the column value as an object
                            Object value = resultSet.getObject(i);

                            // Convert the object to a string
                            String stringValue = String.valueOf(value);

                            // Append the string value
                            resultString.append(stringValue).append(" | ");
                        }
                        // Append a newline after each row
                        resultString.append("\n");
                    }

                    // Return the result string
                    return resultString.toString();
                }
            }
        }
    }
}