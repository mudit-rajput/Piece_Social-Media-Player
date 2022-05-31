package com.piece.v1;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MusicPlayerController implements EventHandler<Event> {
    @FXML
    Button playPause;
    @FXML
    public static ArrayList<TilePane> songWindows = new ArrayList<>();

    private static final MediaView mediaView = new MediaView();
    private static final File Songs = new File("@resources//Songs");
    private static final ArrayList<File> songs = new ArrayList<>(Arrays.asList(Songs.listFiles()));
    private static String song = new File("@"+"").toURI().toString();

    @FXML
    public static HashMap<ArrayList<File>, ArrayList<TilePane>> tiles = new HashMap<>();

    public static void addSongs(){
        tiles.put(songs, songWindows);
    }

    private static String musicPlayerStatus = "stop";
    protected static MediaPlayer Piece = new MediaPlayer(new Media(song));

    @Override
    public void handle(Event event) {
        if (musicPlayerStatus.equals("stop")){
            song = new File(songs.get(x).getPath()).toURI().toString();
            Piece = new MediaPlayer(new Media(song));
            Piece.play();
        }
    }

    @Override
    public void handle(Event event) {
        clickedID = ((Control) event.getSource()).getId();
        playPause.setId(clickedID);
        if (Piece.getStatus().equals(MediaPlayer.Status.READY)) {
            for (int x = 0; x < playButtons.size(); x++) {
                if (playButtons.get(x).getId().equals(clickedID)) {
                    playing.setId(clickedID);

                    slider();
                    System.out.println("Music Played!");
                    Image pauseImage = new Image("D:\\MinorProject\\Piece\\src\\Resources\\Pause.png");
                    ImageView pauseV = new ImageView(pauseImage);
                    if (playButtons.get(x).getId().equals(playing.getId())) {
                        pauseV.setPreserveRatio(true);
                        pauseV.fitWidthProperty().bind(playButtons.get(x).widthProperty());
                        pauseV.fitHeightProperty().bind(playButtons.get(x).heightProperty());
                        playButtons.get(x).setGraphic(pauseV);
                    }
                    break;
                }
            }
        } else if (Piece.getStatus().equals(MediaPlayer.Status.PLAYING)) {
            if (playing.getId().equals(clickedID)) {
                Piece.pause();
                slider();
                System.out.println("Music Paused!");
                Image playImage = new Image("D:\\MinorProject\\Piece\\src\\Resources\\Play.png");
                ImageView playV = new ImageView(playImage);
                for (Button playButton : playButtons) {
                    if (playButton.getId().equals(playing.getId())) {
                        playV.setPreserveRatio(true);
                        playV.fitWidthProperty().bind(playButton.widthProperty());
                        playV.fitHeightProperty().bind(playButton.heightProperty());
                        playButton.setGraphic(playV);
                    }
                }
            } else {
                Piece.stop();
                for (int x = 0; x < playButtons.size(); x++) {
                    if (playButtons.get(x).getId().equals(clickedID)) {
                        playing.setId(clickedID);
                        song = new File(songs.get(x).getPath()).toURI().toString();
                        Piece = new MediaPlayer(new Media(song));
                        Piece.play();
                        slider();
                        System.out.println("Music Played!");
                        Image pauseImage = new Image("D:\\MinorProject\\Piece\\src\\Resources\\Pause.png");
                        ImageView pauseV = new ImageView(pauseImage);
                        if (playButtons.get(x).getId().equals(playing.getId())) {
                            pauseV.setPreserveRatio(true);
                            pauseV.fitWidthProperty().bind(playButtons.get(x).widthProperty());
                            pauseV.fitHeightProperty().bind(playButtons.get(x).heightProperty());
                            playButtons.get(x).setGraphic(pauseV);
                        }
                        break;
                    }
                }
            }
        } else if (Piece.getStatus().equals(MediaPlayer.Status.PAUSED)) {
            Piece.play();
            playing.setId(clickedID);
            slider();
            System.out.println("Music Played!");
            Image pauseImage = new Image("D:\\MinorProject\\Piece\\src\\Resources\\Pause.png");
            ImageView pauseV = new ImageView(pauseImage);
            for (Button playButton : playButtons) {
                if (playButton.getId().equals(playing.getId())) {
                    pauseV.setPreserveRatio(true);
                    pauseV.fitWidthProperty().bind(playButton.widthProperty());
                    pauseV.fitHeightProperty().bind(playButton.heightProperty());
                    playButton.setGraphic(pauseV);
                }
            }
        }


    }

}
