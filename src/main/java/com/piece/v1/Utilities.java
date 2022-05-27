package com.piece.v1;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class Utilities {

    static private final String localHost = "jdbc:mysql://localhost:3306/PIECE";
    static private final String rootUser = "satwik";
    static private final String password = "1234";
    static public Connection connection;
    public static String likedBy = "Liked by ";
    protected static String Name;
    protected static int userID;
    protected static Stage stage1 = new Stage();
    protected static Stage stage2 = new Stage();
    protected ArrayList<String> users = new ArrayList<>();
    protected ArrayList<String> passwords = new ArrayList<>();

    static public void mysqlConnection() throws Exception {

        try {
            System.out.println("Driver loaded...");
            connection = DriverManager.getConnection(localHost, rootUser, password);
            System.out.println("Database connection established...");

        } catch (SQLException exception) {
            throw exception;
        }
    } //Establishes the connection between JAVA and MYSQL using a JDBC driver.

    public static void stage(Parent root, Stage stage, int v, int v1) {
        Scene scene = new Scene(root, v, v1);
        stage.setResizable(false);
        stage.setFullScreen(false);
        stage.setScene(scene);
        stage.show();
    }
}