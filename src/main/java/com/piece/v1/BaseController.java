package com.piece.v1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;


public class BaseController {

    @FXML
    public Button logoutB;
    @FXML
    public Button homeB;
    @FXML
    public Button aboutB;
    @FXML
    public Button chatB;

    @FXML
    public void homeButton() throws Exception {
        Parent root = FXMLLoader.load(Main.class.getResource("Home.fxml"));
        Utilities.stage(root, Utilities.stage2, 1280, 800);
        System.out.println("Home screen invoked!");
    } //Takes user back to the HOME screen and refreshes the home screen.

    public void chatButton() throws Exception {
        Parent root = FXMLLoader.load(Main.class.getResource("Chat.fxml"));
        Utilities.stage(root, Utilities.stage2, 1280, 800);
        System.out.println("Chat screen invoked!");
    } //Takes user to the ABOUT US screen.

    public void aboutButton() throws Exception {
        Parent root = FXMLLoader.load(Main.class.getResource("About.fxml"));
        Utilities.stage(root, Utilities.stage2, 1280, 800);
        System.out.println("About screen invoked!");
    } //Takes user to the ABOUT screen.

    public void logoutButton() throws Exception {
        for (Integer songId : MusicPlayerController.mediaPlayersMap.keySet()) {
            MusicPlayerController.mediaPlayersMap.get(MusicPlayerController.songId).stop();
            MusicPlayerController.mediaPlayersMap.get(MusicPlayerController.songId).dispose();
        }
        MusicPlayerController.mediaPlayersMap.clear();
        Utilities.stage2.close();
        Utilities.stage1.close();
        System.out.println("User Logged out");
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Utilities.stage1.setTitle("Piece");
        Utilities.stage(root, Utilities.stage1, 500, 300);
    } //Logs user out by changing screen from HOME to LOGIN.

}
