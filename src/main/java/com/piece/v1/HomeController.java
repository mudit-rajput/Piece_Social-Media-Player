package com.piece.v1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

import java.util.ArrayList;

public class HomeController {

    private static final ArrayList<Slider> sliders = new ArrayList<>();
    private static final ArrayList<Label> durations = new ArrayList<>();

    @FXML
    public Slider s1;
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
    }
}//Transition from LOGIN screen to HOME Screen.

/*
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
}

 */
