package nz.ac.auckland.se206.controllers;

import java.net.URISyntaxException;
import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.states.GameOver;
import nz.ac.auckland.se206.states.Guessing;

public class EndRoomController {
  private MediaPlayer player;
  private Media sound;

  /**
   * Initializes the end view.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws URISyntaxException
   */
  @FXML
  public void initialize() throws ApiProxyException, URISyntaxException {
    if (RoomController.getGameContext().getCurrentState()
        instanceof GameOver) { // only plays audio if game is over
      if (Guessing.getGameResult()) { // if true
        sound = new Media(App.class.getResource("/sounds/correct_you_win.mp3").toURI().toString());
        player = new MediaPlayer(sound);
        player.play();
      } else {
        sound =
            new Media(
                App.class.getResource("/sounds/better_luck_next_time.mp3").toURI().toString());
        player = new MediaPlayer(sound);
        player.play();
      }
    }
  }
}
