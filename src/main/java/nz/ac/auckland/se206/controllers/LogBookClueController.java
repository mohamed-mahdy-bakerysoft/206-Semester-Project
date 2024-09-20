package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;

/**
 * The LogBookClueController class manages the interaction with the logbook clue in the game. It
 * allows the player to turn pages in the logbook to investigate clues.
 */
public class LogBookClueController extends ClueController {

  @FXML private Rectangle rectLogBook;
  @FXML private Rectangle rectPageTurnRight;
  @FXML private Rectangle rectPageTurnRight2;
  @FXML private Rectangle rectPageTurnLeft;
  @FXML private Rectangle rectPageTurnLeft2;
  private MediaPlayer player;
  private Media sound;

  /**
   * Initializes the logbook clue view by calling the parent class's initialize method.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  @Override
  public void initialize() throws ApiProxyException {
    super.initialize();
    try {
      sound = new Media(App.class.getResource("/sounds/turningpage.mp3").toURI().toString());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    player = new MediaPlayer(sound);
  }

  /**
   * Handles the event when the right page turn button is clicked. It changes the view to the next
   * logbook page.
   *
   * @param event the mouse event triggered by clicking a page turn button
   * @throws IOException if there is an error loading the next page
   */
  public void handleTurnPageClickRight(MouseEvent event) throws IOException {
    Rectangle clicked = (Rectangle) event.getSource();
    if (clicked.getId().equals("rectPageTurnRight")) {
      App.setRoot("logbookclue2");
    } else if (clicked.getId().equals("rectPageTurnRight2")) {
      App.setRoot("logbookclue3");
    }
    player.play();
  }

  /**
   * Handles the event when the left page turn button is clicked. It changes the view to the
   * previous logbook page.
   *
   * @param event the mouse event triggered by clicking a page turn button
   * @throws IOException if there is an error loading the previous page
   */
  public void handleTurnPageClickLeft(MouseEvent event) throws IOException {
    Rectangle clicked = (Rectangle) event.getSource();
    if (clicked.getId().equals("rectPageTurnLeft")) {
      App.setRoot("logbookclue1");
    } else if (clicked.getId().equals("rectPageTurnLeft2")) {
      App.setRoot("logbookclue2");
    }
    player.play();
  }
}
