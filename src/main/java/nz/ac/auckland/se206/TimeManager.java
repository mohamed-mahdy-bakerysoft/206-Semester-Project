package nz.ac.auckland.se206;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import nz.ac.auckland.se206.controllers.InteragationRoomController;
import nz.ac.auckland.se206.controllers.RoomController;
import nz.ac.auckland.se206.controllers.SubmitAnswerController;
import nz.ac.auckland.se206.states.GameOver;
import nz.ac.auckland.se206.states.Guessing;

/**
 * The TimeManager class is responsible for managing the game's timer. It handles time countdowns,
 * state transitions based on time remaining, and updates the timer on the UI.
 */
public class TimeManager {
  private static TimeManager instance;
  private static int interval = 300; // 300 for 5 minutes
  private String formattedMinutes;
  private String formattedSeconds;
  private Timeline timeline;
  private Label mins;
  private Label secs;
  private MediaPlayer player;
  private Media sound;

  /**
   * Returns the singleton instance of the TimeManager class. Ensures only one instance of the class
   * exists at any time.
   *
   * @return the singleton instance of TimeManager
   */
  public static synchronized TimeManager getInstance() {
    if (instance == null) {
      instance = new TimeManager();
    }
    return instance;
  }

  /** Constructor for the TimeManager class. Initializes the timer and sets initial label values. */
  public TimeManager() {
    initialiseTimer();
    updateTimerLabels(); // Set initial label values
  }

  /** Initializes the timer with a one-second interval. */
  public void initialiseTimer() {
    timeline =
        new Timeline(
            new KeyFrame(
                javafx.util.Duration.seconds(1),
                e -> {
                  try {
                    decrementTime();
                  } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                  }
                  updateTimerInGame();
                }));
    timeline.setCycleCount(Timeline.INDEFINITE);
  }

  /**
   * Decrements the timer by one second. Handles different game states when time runs out, such as
   * moving to the guessing state or game over.
   *
   * @throws IOException if there is an I/O error
   * @throws URISyntaxException if there is an error with the URI syntax
   */
  public void decrementTime() throws IOException, URISyntaxException {
    if (interval == 0) {
      handleTimeExpiry();
    } else { // Decrement the timer
      interval--;
      int minutes = interval / 60;
      int seconds = interval % 60;
      formattedMinutes = String.format("%01d", minutes);
      formattedSeconds = String.format("%02d", seconds);
    }
  }

  /** Handles what happens when time runs out based on the game's current state. */
  private void handleTimeExpiry() throws IOException, URISyntaxException {
    if (RoomController.getGameContext().getCurrentState() instanceof GameOver) {
      stopTimer();
      System.out.println("Game is over");
    } else if (RoomController.getGameContext().getCurrentState() instanceof Guessing
        || !(InteragationRoomController.getSuspectsHaveBeenTalkedTo()
            && RoomController.getClueHasBeenInteractedWith())) {
      handleGameOverState();
    } else { // Move to guessing state and give 60 seconds to guess
      App.setRoot("whosThief");
      sound = new Media(App.class.getResource("/sounds/make_a_guess.mp3").toURI().toString());
      player = new MediaPlayer(sound);
      player.play();
      setGuessTimer();
      startTimer();
      System.out.println("Now in guessing state");
    }
    timeline.stop();
  }

  /** Sets the timer to 61 seconds for the guessing state. */
  private void setGuessTimer() {
    interval = 61; // Set 1 minute for guessing state
    initialiseTimer();
  }

  /** Starts the timer and updates the time on the game UI. */
  public void startTimer() {
    updateTimerInGame();
    timeline.play();
  }

  /** Stops the timer. */
  public void stopTimer() {
    timeline.stop();
  }

  /** Updates the time labels in the game UI. */
  public void updateTimerInGame() {
    Platform.runLater(
        () -> {
          Parent currentRoot = SceneManager.getUiRoot(SceneManager.AppUi.ROOM);
          if (currentRoot != null) {
            Label minutesLabel = (Label) currentRoot.lookup("#mins");
            Label secondsLabel = (Label) currentRoot.lookup("#secs");

            if (minutesLabel != null && secondsLabel != null) {
              mins.setText(formattedMinutes);
              secs.setText(formattedSeconds);
            } else {
              System.out.println("Label elements not found");
            }
          } else {
            System.out.println("Current root is null");
          }
        });
  }

  /**
   * Sets the timer labels for the minutes and seconds and updates them immediately.
   *
   * @param mins the label for minutes
   * @param secs the label for seconds
   */
  public void setTimerLabel(Label mins, Label secs) {
    this.mins = mins;
    this.secs = secs;
    updateTimerLabels(); // Update the labels immediately after setting them
  }

  /** Resets the timer labels and stops the timer. */
  public void resetTimer() {
    interval = 0;
    updateTimerLabels();
  }

  /** Resets the timer to 5 minutes and stops any running timer. */
  public void resetTimerRestart() {
    interval = 300; // Reset to 5 minutes (300 seconds)
    updateTimerLabels();
    stopTimer();
  }

  /**
   * Sets a custom interval for the timer.
   *
   * @param interval1 the interval in seconds
   */
  public void setInterval(int interval1) {
    interval = interval1;
  }

  /**
   * Retrieves the current time interval.
   *
   * @return the time interval in seconds
   */
  public int getInterval() {
    return interval;
  }

  /** Updates the timer labels (minutes and seconds) to display the current remaining time. */
  private void updateTimerLabels() {
    int minutes = interval / 60;
    int seconds = interval % 60;
    formattedMinutes = String.format("%01d", minutes);
    formattedSeconds = String.format("%02d", seconds);
    if (mins != null && secs != null) {
      mins.setText(formattedMinutes);
      secs.setText(formattedSeconds);
    }
  }

  /**
   * Handles the game over state by checking the current thief and navigating to the correct ending.
   *
   * @throws IOException if there is an I/O error
   */
  private void handleGameOverState() throws IOException {
    SubmitAnswerController.setIsFirstTime(false);
    if (SubmitAnswerController.getAnswer() != null) {
      Map<String, String> map = SubmitAnswerController.intiateanswer();
      SubmitAnswerController intiateanswer = new SubmitAnswerController();
      intiateanswer.intizliaseAndGpt(map);
    } else {
      String thief = SubmitAnswerController.getThief();
      if (thief == null) {
        App.setRoot("badending");
        stopTimer();
        return;
      }
      if (thief.equals("janitor")) {
        App.setRoot("badending");
      } else if (thief.equals("hos")) {
        App.setRoot("goodending2");
      } else if (thief.equals("curator")) {
        App.setRoot("badending");
      } else {
        System.err.println("error");
      }
    }
  }
}
