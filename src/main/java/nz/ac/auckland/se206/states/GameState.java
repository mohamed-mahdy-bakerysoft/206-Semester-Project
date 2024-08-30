package nz.ac.auckland.se206.states;

import java.io.IOException;
import java.net.URISyntaxException;
import javafx.scene.input.MouseEvent;

/**
 * Interface representing the state of the game. Defines methods to handle user interactions such as
 * clicking on a rectangle and making a guess.
 */
public interface GameState {

  /**
   * Handles the event when a rectangle is clicked.
   *
   * @param event the mouse event triggered by clicking a rectangle
   * @param rectangleId the ID of the clicked rectangle
   * @throws IOException if there is an I/O error
   * @throws URISyntaxException
   */
  void handleRectangleClick(MouseEvent event, String rectangleId)
      throws IOException, URISyntaxException;

  /**
   * Handles the event when the guess button is clicked.
   *
   * @throws IOException if there is an I/O error
   * @throws URISyntaxException
   */
  void handleGuessClick() throws IOException, URISyntaxException;
}
