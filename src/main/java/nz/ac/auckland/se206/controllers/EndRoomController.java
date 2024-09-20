package nz.ac.auckland.se206.controllers;

import java.net.URISyntaxException;
import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.states.GameOver;
import nz.ac.auckland.se206.states.Guessing;

/**
 * The EndRoomController class is responsible for managing the end-of-game screen. Depending on
 * whether the player wins or loses, a different sound is played when the game is over.
 */
public class EndRoomController {

  private MediaPlayer player;
  private Media sound;

  /**
   * Initializes the end view by checking the current game state and playing an appropriate sound
   * based on whether the player won or lost the game.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws URISyntaxException if there is an error with the sound file's URI syntax
   */
  @FXML
  public void initialize() throws ApiProxyException, URISyntaxException {
    if (RoomController.getGameContext().getCurrentState() instanceof GameOver) {
      // Check if the player has won or lost and play the respective sound
      if (Guessing.getGameResult()) { // Player won the game
        sound = new Media(App.class.getResource("/sounds/correct_you_win.mp3").toURI().toString());
        player = new MediaPlayer(sound);
        player.play();
      } else { // Player lost the game
        sound =
            new Media(
                App.class.getResource("/sounds/better_luck_next_time.mp3").toURI().toString());
        player = new MediaPlayer(sound);
        player.play();
      }
    }
  }
}
