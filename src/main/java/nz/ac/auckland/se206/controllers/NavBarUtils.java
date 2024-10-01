package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class NavBarUtils {

  public static void setupNavBarAndSuspectButtons(
      VBox navBar,
      Button suspect1Button,
      Button suspect2Button,
      Button suspect3Button,
      RoomNavigationHandler handler) {

    navBar.setTranslateX(+200);
    navBar.setDisable(true);

    suspect1Button.setOnAction(e -> handleRoomNavigation("IntelRoomOne", handler));
    suspect2Button.setOnAction(e -> handleRoomNavigation("IntelRoomTwo", handler));
    suspect3Button.setOnAction(e -> handleRoomNavigation("IntelRoomThree", handler));
  }

  private static void handleRoomNavigation(String roomName, RoomNavigationHandler handler) {
    try {
      handler.goToRoom(roomName);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
