package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.speech.MP3Player;

/**
 * The LogBookClueController class manages the interaction with the logbook clue in the game. It
 * allows the player to turn pages in the logbook to investigate clues.
 */
public class LogBookClueController extends ClueController {

  @FXML private ImageView leftTurnPage;
  @FXML private ImageView rightTurnPage;
  @FXML private ImageView leftIndicator;
  @FXML private ImageView rightIndicator;
  @FXML private Rectangle rectLogBook;
  @FXML private Rectangle rectPageTurnRight;
  @FXML private Rectangle rectPageTurnRight2;
  @FXML private Rectangle rectPageTurnLeft;
  @FXML private Rectangle rectPageTurnLeft2;
  private MP3Player player;

  /**
   * Initializes the logbook clue view by calling the parent class's initialize method.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  @Override
  public void initialize() throws ApiProxyException {
    // Call the parent class's initialize method
    super.initialize();
    player = new MP3Player("src/main/resources/sounds/turningpage.mp3");
    player.play();
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

  /**
   * Handles mouse hover events on the clues in the room.
   *
   * @param event the mouse event triggered by hovering over a clue
   */
  @FXML
  private void onHoverPageTurn(MouseEvent event) {
    // Get the hovered clue
    Rectangle hoveredClue = (Rectangle) event.getSource();
    // Show the appropriate indicator based on the hovered clue
    switch (hoveredClue.getId()) {
      case "rectPageTurnRight", "rectPageTurnRight2":
        // Show the right page turn image and hide the right indicator
        rightIndicator.setVisible(false);
        rightTurnPage.setVisible(true);
        break;
      case "rectPageTurnLeft", "rectPageTurnLeft2":
        // Show the left indicator and hide the left page turn image
        leftTurnPage.setVisible(true);
        leftIndicator.setVisible(false);
        break;
    }
  }

  /**
   * Handles mouse exit events on the clues in the room.
   *
   * @param event the mouse event triggered by exiting hover of a clue
   */
  @FXML
  private void onExitPageTurn(MouseEvent event) {
    // Get the hovered clue
    Rectangle hoveredClue = (Rectangle) event.getSource();
    // Hide the appropriate indicator based on the hovered clue
    switch (hoveredClue.getId()) {
      case "rectPageTurnRight", "rectPageTurnRight2":
        // Hide the right indicator and show the right page turn image
        rightIndicator.setVisible(true);
        rightTurnPage.setVisible(false);
        break;
      case "rectPageTurnLeft", "rectPageTurnLeft2":
        // Hide the left page turn image and show the left indicator
        leftTurnPage.setVisible(false);
        leftIndicator.setVisible(true);
        break;
    }
  }
}
