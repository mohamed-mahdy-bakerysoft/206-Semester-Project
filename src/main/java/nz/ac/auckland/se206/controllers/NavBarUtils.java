package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/** Utility class for setting up the navigation bar and suspect buttons. */
public class NavBarUtils {

  /**
   * Sets up the navigation bar and suspect buttons for the game.
   *
   * @param navBar the navigation bar
   * @param suspect1Button the button for suspect 1
   * @param suspect2Button the button for suspect 2
   * @param suspect3Button the button for suspect 3
   * @param handler the room navigation handler
   */
  public static void setupNavBarAndSuspectButtons(
      VBox navBar,
      Button suspect1Button,
      Button suspect2Button,
      Button suspect3Button,
      RoomNavigationHandler handler) {
    // Set up the navigation bar and suspect buttons
    navBar.setTranslateX(+200);
    navBar.setDisable(true);

    // Set up the suspect buttons
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
