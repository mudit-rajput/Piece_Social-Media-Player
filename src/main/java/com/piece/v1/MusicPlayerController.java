package com.piece.v1;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import lombok.SneakyThrows;

import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;


public class MusicPlayerController implements Initializable {
    public static final HashMap<Integer, MediaPlayer> mediaPlayersMap = new HashMap<>();
    public static int songId = 1;
    @FXML
    public Label songName;
    @FXML
    public Button previousSong;
    @FXML
    public Button nextSong;
    @FXML
    public ImageView songIcon;
    @FXML
    public Button playPauseButton;
    @FXML
    public ImageView playPauseImage;
    @FXML
    public Slider slider;
    @FXML
    public Label timeStamp;

    public static String getTimeString(double millis) {
        millis /= 1000;
        String s = formatTime(millis % 60);
        millis /= 60;
        String m = formatTime(millis % 60);
        millis /= 60;
        String h = formatTime(millis % 24);
        return h + ":" + m + ":" + s;
    }

    public static String formatTime(double time) {
        int t = (int) time;
        if (t > 9) {
            return String.valueOf(t);
        }
        return "0" + t;
    }

    private void playPauseLogic() {
        System.out.println("reached playPauseLogic and current media player's status is: " + mediaPlayersMap.get(songId).getStatus());
        if (mediaPlayersMap.get(songId).getStatus().equals(MediaPlayer.Status.STOPPED) || mediaPlayersMap.get(songId).getStatus().equals(MediaPlayer.Status.PAUSED) || mediaPlayersMap.get(songId).getStatus().equals(MediaPlayer.Status.READY)) {
            System.out.println("Playing now!");
            changePlayPauseImage("Playing");
            mediaPlayersMap.get(songId).play();
            bindTimeStamp();
            System.out.println("status after playing: " + mediaPlayersMap.get(songId).getStatus());
        } else if (mediaPlayersMap.get(songId).getStatus().equals(MediaPlayer.Status.PLAYING)) {
            System.out.println("Paused now!");
            changePlayPauseImage("Paused");
            mediaPlayersMap.get(songId).pause();
            bindTimeStamp();
            System.out.println("status after pausing: " + mediaPlayersMap.get(songId).getStatus());
        }
    }

    private void changePlayPauseImage(String status) {
        String playPauseImagePath = "src//main//resources//com//piece//v1//Images//";
        File file;
        if (status.equals("Playing"))
            file = new File(playPauseImagePath + "Pause.png");
        else
            file = new File(playPauseImagePath + "Play.png");
        String imagePath = new File(file.getPath()).toURI().toString();
        Image image = new Image(imagePath);
        playPauseImage.setImage(image);
    }

    public void nextSong() throws SQLException {
        if (songId != 3) {
            LikeButtonController likeButtonController = new LikeButtonController();
            likeButtonController.countAndShowNames();
            ++songId;
            resetPlayers();
            ResultSet rs = Utilities.connection.createStatement().executeQuery("SELECT song_icon, song_name FROM songs WHERE song_id =" + songId + ";");
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
        if (songId != 1) {
            LikeButtonController likeButtonController = new LikeButtonController();
            likeButtonController.countAndShowNames();
            --songId;
            resetPlayers();
            ResultSet rs = Utilities.connection.createStatement().executeQuery("SELECT song_icon, song_name FROM songs WHERE song_id =" + songId + ";");
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
        ResultSet resultSet = Utilities.connection.createStatement().executeQuery("SELECT song_file FROM songs WHERE song_id =" + songId + ";");
        if (!mediaPlayersMap.containsKey(songId)) {
            while (resultSet.next()) {
                File file = new File("src//main//resources//com//piece//v1//Songs//" + resultSet.getString(1));
                String song = new File(file.getPath()).toURI().toString();
                Media media = new Media(song);
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                if (mediaPlayer.getStatus().equals(MediaPlayer.Status.UNKNOWN)) {
                    File trial = new File("src//main//resources//com//piece//v1//Images//Play.png");
                    String imagePath = new File(trial.getPath()).toURI().toString();
                    Image image = new Image(imagePath);
                    playPauseImage.setImage(image);
                }
                mediaPlayersMap.put(songId, mediaPlayer);
                playPauseLogic();
            }
        } else {
            playPauseLogic();
        }
    }

    private void bindTimeStamp() {
        timeStamp.setText(getTimeString(mediaPlayersMap.get(songId).getCurrentTime().toMillis()) + "/" + getTimeString(mediaPlayersMap.get(songId).getTotalDuration().toMillis()));
        slider.maxProperty().bind(Bindings.createDoubleBinding(
                () -> mediaPlayersMap.get(songId).getTotalDuration().toSeconds(),
                mediaPlayersMap.get(songId).totalDurationProperty()));
    }

    private void resetPlayers() {
        for (Integer songNo : mediaPlayersMap.keySet()) {
            mediaPlayersMap.get(songNo).stop();
            mediaPlayersMap.get(songNo).setStartTime(mediaPlayersMap.get(songNo).getStartTime());
        }
    }


    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ResultSet resultSet = Utilities.connection.createStatement().executeQuery("SELECT song_id, song_file FROM songs;");
        while (resultSet.next()) {
            File file = new File("src//main//resources//com//piece//v1//Songs//" + resultSet.getString(2));
            String song = new File(file.getPath()).toURI().toString();
            Media media = new Media(song);
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayersMap.put(resultSet.getInt(1), mediaPlayer);
        }

        mediaPlayersMap.get(songId).currentTimeProperty().addListener(ov -> {
            double total = mediaPlayersMap.get(songId).getTotalDuration().toMillis();
            double current = mediaPlayersMap.get(songId).getCurrentTime().toMillis();
            timeStamp.setText(getTimeString(current) + "/" + getTimeString(total));
        });

        slider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            mediaPlayersMap.get(songId).seek(Duration.seconds(newValue.doubleValue()));
            double total = mediaPlayersMap.get(songId).getTotalDuration().toMillis();
            double current = mediaPlayersMap.get(songId).getCurrentTime().toMillis();
            timeStamp.setText(getTimeString(current) + "/" + getTimeString(total));
        });

    }

}
