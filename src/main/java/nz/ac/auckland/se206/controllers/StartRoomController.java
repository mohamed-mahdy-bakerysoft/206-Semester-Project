package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import nz.ac.auckland.se206.SceneManager;
import nz.ac.auckland.se206.TimeManager;

/**
 * The StartRoomController class manages the interaction within the starting room of the game. It
 * handles the initialization of sounds and timer when the game starts.
 */
public class StartRoomController {

  @FXML private Button startButton; // The button to start the game

  private MediaPlayer player; // MediaPlayer to play sounds
  private Media sound; // Media object to hold sound files

  /**
   * Initializes the start room view by playing the welcome sound when the room is loaded.
   *
   * @throws URISyntaxException if there is an issue with the URI of the sound file
   */
  @FXML
  public void initialize() throws URISyntaxException {
    TimeManager.getInstance().resetTimerRestart(); // Reset the timer before starting
    // Load and play the welcome sound
    sound = new Media(getClass().getResource("/sounds/welcome.mp3").toURI().toString());
    player = new MediaPlayer(sound);
    player.play();
  }

  /**
   * Handles the action when the "Start" button is clicked. It plays an opening sound, starts the
   * game timer, and transitions the player to the next room.
   *
   * @param event the action event triggered by clicking the "Start" button
   * @throws IOException if there is an I/O error while changing scenes
   * @throws URISyntaxException if there is an issue with the URI of the sound file
   */
  @FXML
  private void onStart(ActionEvent event) throws IOException, URISyntaxException {
    // Play the opening sound
    sound = new Media(getClass().getResource("/sounds/opening_sound.mp3").toURI().toString());
    player = new MediaPlayer(sound);
    player.play();

    // Transition to the cutscenes scene
    SceneManager.setScene(SceneManager.AppUi.CUTSCENE);

    // Start the timer only after the "Start" button is pressed.
    TimeManager.getInstance().startTimer();
  }
}
