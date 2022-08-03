package com.test.task.database;


import com.test.task.logger.LoggerProvider;

import java.sql.*;
import java.util.List;

public class ConnectionToDB {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/testb1";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    private static final String HIGH_COMMA = "\'";

    public static final int NUMBER_OF_COLUMNS_IN_DATA = 5;

    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;
    private static ConnectionToDB instance;

    public static ConnectionToDB getInstance() {
        if (instance == null) {
            instance = new ConnectionToDB();
        }
        return instance;
    }

    public boolean executionOfProcedure(List<String> elementArray, int numberOfRecords) {


        try {
            // opening database connection to MySQL server
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // getting Statement object to execute query
            statement = connection.createStatement();
            int i = 0;
            int amountOfLeftRecords = elementArray.size() / NUMBER_OF_COLUMNS_IN_DATA;


            for (int j = NUMBER_OF_COLUMNS_IN_DATA; j < elementArray.size() + 1; j = j + NUMBER_OF_COLUMNS_IN_DATA) {

                String query = "insert into data (id,date_field,latin,ru,big_integer,big_number) " +
                        "values(" + ++i + "," + HIGH_COMMA + elementArray.get(j - 5) + HIGH_COMMA + "," + HIGH_COMMA + elementArray.get(j - 4) + HIGH_COMMA + ","
                        + HIGH_COMMA + elementArray.get(j - 3) + HIGH_COMMA + "," + Integer.parseInt(elementArray.get(j - 2)) + "," + Double.parseDouble(elementArray.get(j - 1)) + ");";

                amountOfLeftRecords--;
                LoggerProvider.getLOG().info(amountOfLeftRecords + " records left to import");
                statement.executeUpdate(query);


            }

        } catch (SQLException sqlEx) {
            LoggerProvider.getLOG().error("SQLException occurred");
            return false;
        } finally {
            closeResources();
        }
        return true;
    }


    public boolean executeSumOfBigNumbers() {

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            statement = connection.createStatement();

            String query = "SELECT SUM(big_integer) FROM data;";

            resultSet = statement.executeQuery(query);
            resultSet.next();
            System.out.println("Sum of all big integers = " + resultSet.getString(1));


        } catch (SQLException e) {
            e.printStackTrace();
            LoggerProvider.getLOG().error("SQLException occurred");
            return false;
        } finally {

            closeResources();
        }
        return true;

    }

    public boolean executeMedianOfFloatNumbers() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            statement = connection.createStatement();

            String query = "SET @rowindex := -1;";
                    String querySecond =
                    "SELECT" +
                    " AVG(g.bNumber)" +
                    "FROM" +
                    " (SELECT @rowindex:=@rowindex + 1 AS rowindex, " +
                    "data.big_number AS bNumber" +
                    " FROM data" +
                    " ORDER BY data.big_number) AS g" +
                    " WHERE " +
                    "g.rowindex IN (FLOOR(@rowindex / 2) , CEIL(@rowindex / 2));";
                    statement.execute(query);
            resultSet = statement.executeQuery(querySecond);
            resultSet.next();
            System.out.println("Median of float numbers = " + resultSet.getString(1));


        } catch (SQLException e) {
            e.printStackTrace();
            LoggerProvider.getLOG().error("SQLException occurred");
            return false;
        } finally {

            closeResources();
            try {
                resultSet.close();
            } catch (SQLException se) {
                LoggerProvider.getLOG().error("SQLException occurred");
                return false;
            }
        }
        return true;

    }


    private boolean closeResources() {
        try {
            connection.close();
        } catch (SQLException se) {
            LoggerProvider.getLOG().error("SQLException occurred");
            return false;
        }
        try {
            statement.close();
        } catch (SQLException se) {
            LoggerProvider.getLOG().error("SQLException occurred");
            return false;
        }
        return true;
    }


}
