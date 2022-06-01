package com.piece.v1;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;


public class MusicPlayerController implements Initializable {
    private static int songNo = 1;
    private static HashMap<Integer, MediaPlayer> mediaPlayersMap = new HashMap<>();
    @FXML
    public Label songName;
    @FXML
    public Button previousSong;
    @FXML
    public Button nextSong;
    @FXML
    public ImageView songIcon;

    public void playPauseLogic() {
        System.out.println("reached");
        if (mediaPlayersMap.get(songNo).getStatus().equals(MediaPlayer.Status.UNKNOWN)) {
            System.out.println("A");
            mediaPlayersMap.get(songNo).play();
            System.out.println(mediaPlayersMap.get(songNo).getStatus());
        } else if (mediaPlayersMap.get(songNo).getStatus().equals(MediaPlayer.Status.PLAYING)) {
            System.out.println("B");
            mediaPlayersMap.get(songNo).pause();
        } else if (mediaPlayersMap.get(songNo).getStatus().equals(MediaPlayer.Status.PAUSED)) {
            System.out.println("C");
            mediaPlayersMap.get(songNo).play();
        }
    }

    public void nextSong() throws SQLException {
        if (songNo != 3) {
            ++songNo;
            handle();
            ResultSet rs = Utilities.connection.createStatement().executeQuery("SELECT song_icon, song_name FROM songs WHERE song_id =" + songNo + ";");
            while (rs.next()) {
                File file = new File("src//main//resources//com//piece//v1//Images//" + rs.getString(1));
                String imagePath = new File(file.getPath()).toURI().toString();
                Image image = new Image(imagePath);
                songIcon.setImage(image);
                songName.setText(rs.getString(2));
                System.out.println(rs.getString(2));
            }
        }
    }

    public void previousSong() throws SQLException {
        if (songNo != 1) {
            --songNo;
            handle();
            ResultSet rs = Utilities.connection.createStatement().executeQuery("SELECT song_icon, song_name FROM songs WHERE song_id =" + songNo + ";");
            while (rs.next()) {
                File file = new File("src//main//resources//com//piece//v1//Images//" + rs.getString(1));
                String imagePath = new File(file.getPath()).toURI().toString();
                Image image = new Image(imagePath);
                songIcon.setImage(image);
                songName.setText(rs.getString(2));
                System.out.println(rs.getString(2));
            }
        }
    }

    public void handle() throws SQLException {
        ResultSet resultSet = Utilities.connection.createStatement().executeQuery("SELECT song_file FROM songs WHERE song_id =" + songNo + ";");
        if (!mediaPlayersMap.containsKey(songNo)) {
            while (resultSet.next()) {
                File file = new File("src//main//resources//com//piece//v1//Songs//" + resultSet.getString(1));
                String song = new File(file.getPath()).toURI().toString();
                Media media = new Media(song);
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                mediaPlayersMap.put(songNo, mediaPlayer);
                playPauseLogic();
            }
        }

        playPauseLogic();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
