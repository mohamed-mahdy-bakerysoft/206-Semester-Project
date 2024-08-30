package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.TimeManager;

public class StartRoomController {
  @FXML private Button startButton;

  private MediaPlayer player;
  private Media sound;

  /**
   * Initializes the chat view.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws URISyntaxException
   */
  @FXML
  public void initialize() throws ApiProxyException, URISyntaxException {
    sound = new Media(App.class.getResource("/sounds/welcome.mp3").toURI().toString());
    player = new MediaPlayer(sound);
    player.play();
  }

  @FXML
  private void onStart(ActionEvent event)
      throws ApiProxyException, IOException, URISyntaxException {
    TimeManager timeManager = TimeManager.getInstance();
    timeManager.startTimer();
    sound = new Media(App.class.getResource("/sounds/opening_sound.mp3").toURI().toString());
    player = new MediaPlayer(sound);
    player.play();
    App.setRoot("room");
  }
}
