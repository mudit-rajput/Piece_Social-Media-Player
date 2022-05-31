package com.piece.v1;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

public class HomeController implements EventHandler<Event> {

    @FXML
    public static Button playing = new Button();

    private static final File Images = new File("D://MinorProject//Piece//src//Resources//Images");
    private static final ArrayList<File> images = new ArrayList<File>(Arrays.asList(Images.listFiles()));
    private static final ArrayList<Button> playButtons = new ArrayList<>();
    private static final ArrayList<Slider> sliders = new ArrayList<>();
    private static final ArrayList<Label> durations = new ArrayList<>();
    @FXML
    public Button likeB1;
    @FXML
    public Button likeB2;
    @FXML
    public Button likeB3;
    @FXML
    public Button play1;
    @FXML
    public Button play2;
    @FXML
    public Button play3;
    @FXML
    public Label duration1;
    @FXML
    public Label duration2;
    @FXML
    public Label duration3;
    @FXML
    public Slider s1;
    @FXML
    public Slider s2;
    @FXML
    public Slider s3;
    @FXML
    public Label lb1;
    @FXML
    public Label lb2;
    @FXML
    public Label lb3;
    private int j = 0;

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

    public void homePage() throws Exception {
        Parent root = FXMLLoader.load(Main.class.getResource("Home.fxml"));
        Utilities.stage(root, Utilities.stage2, 1280, 800);
        System.out.println("Home screen loaded");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);  //Shows a dialog box upon login which welcomes user.
        alert.setContentText(Utilities.Name);
        alert.setHeaderText("Welcome");
        alert.show();
        MusicPlayerController.addSongs();
    } //Transition from LOGIN screen to HOME Screen.


    public void social() throws Exception {
        Statement stmt = Utilities.connection.createStatement();

        if (j == 0) {
            stmt.executeUpdate("INSERT INTO SOCIAL(MUSICID, ID) VALUES (1," + Utilities.userID + ")");
            j = 1;
            likeB1.setText("Liked");
            lb1.setText("Liked by you");
        } else if (j == 1) {
            stmt.executeUpdate("DELETE FROM SOCIAL WHERE MUSICID=1 AND ID=" + Utilities.userID);
            likeB1.setText("Like");
            j = 0;
        }

    }

    public void slider() {
        for (int x = 0; x < playButtons.size(); x++) {
            if (playButtons.get(x).getId().equals(playing.getId())) {
                Double time = Piece.getTotalDuration().toSeconds();
                sliders.get(x).maxProperty().bind(Bindings.createDoubleBinding(
                        () -> time,
                        Piece.totalDurationProperty()));

                int finalX = x;
                Piece.currentTimeProperty().addListener((ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) -> {
                    sliders.get(finalX).setValue(newValue.toSeconds());
                });
                int finalX1 = x;
                int finalX2 = x;
                int finalX5 = x;
                Piece.currentTimeProperty().addListener(ov -> {
                    if (!sliders.get(finalX1).isValueChanging()) {
                        double total = Piece.getTotalDuration().toMillis();
                        double current = Piece.getCurrentTime().toMillis();
                        sliders.get(finalX2).setValue(current);
                        durations.get(finalX5).setText(getTimeString(current) + "/" + getTimeString(total));
                    }
                });
                int finalX3 = x;
                int finalX4 = x;
                sliders.get(x).valueProperty().addListener(ov -> {
                    if (sliders.get(finalX3).isValueChanging()) {
                        Piece.seek(new Duration(sliders.get(finalX4).getValue()));
                    }
                });
            }
        }
    }

    public void refreshButton() throws SQLException {
        Statement id = Utilities.connection.createStatement();
        Statement name = Utilities.connection.createStatement();
        ResultSet rs = id.executeQuery("SELECT ID FROM SOCIAL WHERE MUSICID=1");
        rs.next();
        while (rs.next()) {
            ResultSet rs1 = name.executeQuery("SELECT NAME FROM USERS WHERE USERID=" + rs.getInt(1));
            rs1.next();
            Utilities.likedBy = Utilities.likedBy + rs1.getString(1) + " ";
            rs1.close();
        }
        rs.close();
        System.out.println(Utilities.likedBy);
        lb1.setText(Utilities.likedBy);
    }

}

