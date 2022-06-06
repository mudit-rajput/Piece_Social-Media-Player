package com.piece.v1;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.SneakyThrows;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class LikeButtonController implements Initializable {
    public static ArrayList<String> names = new ArrayList<>();
    public static HashMap<Integer, ArrayList> namesMap = new HashMap<>();
    @FXML
    public Button likeButton;
    @FXML
    public Label likedByLabel;
    public String likedByNames = "";

    public void handle() throws SQLException {
        ResultSet rs = Utilities.connection.createStatement().executeQuery("SELECT * FROM social WHERE music_id=" + MusicPlayerController.songId + " AND user_id=" + Utilities.userId + ";");
        if (!rs.next()) {
            System.out.println("Liked by " + Utilities.Name);
            String sql = "INSERT INTO social (music_id, user_id)" + "VALUES (?,?)";
            PreparedStatement preparedStatement = Utilities.connection.prepareStatement(sql);
            preparedStatement.setInt(1, MusicPlayerController.songId);
            preparedStatement.setInt(2, Utilities.userId);
            preparedStatement.executeUpdate();
            likeButton.setText("Liked");
            names.add(Utilities.Name);
            updateStringAndShow();
        } else {
            System.out.println("Unliked by " + Utilities.Name);
            likeButton.setText("Like");
            String sql = "DELETE FROM social WHERE music_id = " + "'" + MusicPlayerController.songId + "'" + "AND user_id = " + "'" + Utilities.userId + "'";
            PreparedStatement preparedStatement = Utilities.connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            names.remove(Utilities.Name);
            updateStringAndShow();
        }
    }

    public void updateStringAndShow() {
        likedByNames = "";
        for (String name : names) {
            likedByNames = likedByNames + name + ", ";
        }
        likedByLabel.setText(likedByNames);
    }

    public void countAndShowNames() throws SQLException {
        ResultSet countOfSongs = Utilities.connection.createStatement().executeQuery("SELECT COUNT(DISTINCT music_id) FROM social;");
        while (countOfSongs.next()) {
            ResultSet rs = Utilities.connection.createStatement().executeQuery("SELECT user_name FROM users WHERE user_id IN (SELECT user_id FROM social WHERE music_id=" + countOfSongs.getInt(1) + ");");
            names.clear();
            while (rs.next()) {
                names.add(rs.getString(1));
            }
            updateStringAndShow();
            likedByLabel.setText(likedByNames);
            ResultSet resultSet = Utilities.connection.createStatement().executeQuery("SELECT * FROM social WHERE music_id=" + countOfSongs.getInt(1) + " AND user_id=" + Utilities.userId + ";");
            if (!resultSet.next()) {
                System.out.println("Liked by " + Utilities.Name);
                likeButton.setText("Liked");
            } else {
                System.out.println("Unliked by " + Utilities.Name);
                likeButton.setText("Like");
            }
            namesMap.put(countOfSongs.getInt(1), names);
        }
    }

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countAndShowNames();
    }
}