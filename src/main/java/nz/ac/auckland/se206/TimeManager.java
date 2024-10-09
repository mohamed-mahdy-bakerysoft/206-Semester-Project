package nz.ac.auckland.se206;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import nz.ac.auckland.se206.controllers.EndingController;
import nz.ac.auckland.se206.controllers.InteragationRoomController;
import nz.ac.auckland.se206.controllers.RoomController;
import nz.ac.auckland.se206.controllers.SubmitAnswerController;
import nz.ac.auckland.se206.states.GameOver;
import nz.ac.auckland.se206.states.GameStarted;
import nz.ac.auckland.se206.states.Guessing;

/**
 * The TimeManager class is responsible for managing the game's timer. It handles time countdowns,
 * state transitions based on time remaining, and updates the timer on the UI.
 */
public class TimeManager {
  private static TimeManager instance;

  private static int interval; // 300 for 5 minutes
  private static MediaPlayer player;
  private static Media sound;
  private boolean isTimerStarted = false;

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

  @FXML private Label mins;
  @FXML private Label secs;
  @FXML private Label dot;

  private String formattedMinutes;
  private String formattedSeconds;
  private Timeline timeline;

  /** Constructor for the TimeManager class. Initializes the timer and sets initial label values. */
  public TimeManager() {
    initialiseTimer();
    updateTimerLabels(); // Set initial label values
  }

  /**
   * Initializes the timer with a one-second interval. This method sets up a Timeline that
   * decrements the timer every second and updates the timer in the game UI.
   */
  public void initialiseTimer() {
    if (timeline != null) {
      timeline.stop();
    }
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

  // make a getter for player
  public MediaPlayer getPlayer() {
    return player;
  }

  /**
   * Decrements the timer by one second. Handles different game states when time runs out, such as
   * moving to the guessing state or game over.
   *
   * @throws IOException if there is an I/O error
   * @throws URISyntaxException if there is an error with the URI syntax
   */
  public void decrementTime() throws IOException, URISyntaxException {
    // If the labels are found, update their text with the formatted time
    if (mins != null && secs != null) {
      mins.setText(formattedMinutes);
      secs.setText(formattedSeconds);
      if ((RoomController.getGameContext().getCurrentState() instanceof GameStarted
              && interval <= 31)
          || (RoomController.getGameContext().getCurrentState() instanceof Guessing
              && interval <= 11)) {
        // check if media player is playing
        if (player == null) {
          sound = new Media(App.class.getResource("/sounds/ticking.mp3").toURI().toString());
          player = new MediaPlayer(sound);
          player.play();
        }

        if (interval % 2 == 0) {
          // Blinking effect
          mins.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
          dot.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
          secs.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        } else {
          mins.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
          dot.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
          secs.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        }

      } else {
        if (player != null) {
          player.stop();
          player = null;
        }
        // Reset to default styles when time is more than 30 seconds
        mins.setStyle("-fx-text-fill: white; -fx-font-weight: normal;");
        dot.setStyle("-fx-text-fill: white; -fx-font-weight: normal;");
        secs.setStyle("-fx-text-fill: white; -fx-font-weight: normal;");
      }
    }

    if (interval == 0) {
      if (player != null) {
        player.stop();
        player = null;
      }

      // if (!InteragationRoomController.getSuspectsHaveBeenTalkedTo()) {
      //   String msg = "You did not talk to all the three suspects in time.";
      //   String msg2 = "";
      //   if (!RoomController.getClueHasBeenInteractedWith()) {
      //     msg2 = "You did not interact with a clue in time.";
      //   }
      //   String finalmsg = msg + "\n" + msg2;
      //   EndingController.setFeed(finalmsg);
      //   App.setRoot("badending");
      // }

      if (RoomController.getGameContext().getCurrentState() instanceof GameStarted
          && (!InteragationRoomController.getSuspectsHaveBeenTalkedTo()
              || !RoomController.getClueHasBeenInteractedWith())) {
        String msg = "";
        String msg2 = "";
        if (!InteragationRoomController.getSuspectsHaveBeenTalkedTo()) {
          msg = "You did not talk to all the three suspects in time.";
        }
        if (!RoomController.getClueHasBeenInteractedWith()) {
          msg2 = "You did not interact with a clue in time.";
        }

        String finalMsg = msg + "\n" + msg2;
        if (msg.equals("")) {
          finalMsg = msg2;
        }
        EndingController.setFeed(finalMsg);
        App.setRoot("badending");
        return;
      }

      // Check if the game is in the started state, suspects have been talked to, and clue has been
      // interacted with
      if (RoomController.getGameContext().getCurrentState() instanceof GameStarted
          && InteragationRoomController.getSuspectsHaveBeenTalkedTo()
          && RoomController.getClueHasBeenInteractedWith()) {
        // Move to guessing state
        System.out.println("Game started state to guessing state");
        GameStateContext context = GameStateContext.getInstance();
        context.setState(context.getGuessingState());
        System.out.println("Now in guessing state");
        App.setRoot("whosThief");
        sound = new Media(App.class.getResource("/sounds/make_a_guess.mp3").toURI().toString());
        player = new MediaPlayer(sound);
        player.play();
        setInterval(60);
        return;
      }
      // Check if the game is over
      if (RoomController.getGameContext().getCurrentState() instanceof GameOver) {
        stopTimer();
        System.out.println("Game is over");
        return;
      }
      // Check if the game is in the guessing state or if the player has not investigated
      else if (RoomController.getGameContext().getCurrentState() instanceof Guessing
          || !(InteragationRoomController.getSuspectsHaveBeenTalkedTo()
              && RoomController.getClueHasBeenInteractedWith())) {
        // Move to game over state
        SubmitAnswerController.setIsFirstTime(false);
        if (SubmitAnswerController.getAnswer() != null) {
          Map<String, String> map = SubmitAnswerController.intiateanswer();
          SubmitAnswerController intiateanswer = new SubmitAnswerController();
          intiateanswer.intizliaseAndGpt(map);
        } else {
          String thief = SubmitAnswerController.getThief();
          // Check if thief is null
          if (thief == null) {
            EndingController.setFeed("You did not pick a thief in time.");
            App.setRoot("badending");
            TimeManager.getInstance().stopTimer();
            return;
          }
          // Handle different thief outcomes
          if (thief.equals("janitor")) {
            App.setRoot("badending");
            TimeManager.getInstance().stopTimer();
          } else if (thief.equals("hos")) {
            App.setRoot("goodending2");
          } else if (thief.equals("curator")) {
            App.setRoot("badending");
            TimeManager.getInstance().stopTimer();
          } else {
            System.err.println("error");
          }
        }
        timeline.stop();
        return;
      } else {
        // Move to guessing state and give 60 seconds to guess
        App.setRoot("whosThief");
        sound = new Media(App.class.getResource("/sounds/make_a_guess.mp3").toURI().toString());
        player = new MediaPlayer(sound);
        player.play();
        setGuessTimer();
        startTimer();
        System.out.println("Now in guessing state");
      }
      timeline.stop();
    } else {
      // Decrement the timer by 1 second
      interval--;
      int minutes = interval / 60;
      int seconds = interval % 60;
      formattedMinutes = String.format("%01d", minutes);
      formattedSeconds = String.format("%02d", seconds);
    }
  }

  /** Sets the timer to 61 seconds for the guessing state. */
  private void setGuessTimer() {
    interval = 61; // Set 1 minute for guessing state
    initialiseTimer();
  }

  /** Starts the timer and updates the time on the game UI. */
  public void startTimer() {
    if (!isTimerStarted) {
      updateTimerInGame();
      timeline.play();
      isTimerStarted = true;
      System.out.println("Timer started");
    }
  }

  /** Stops the timer. */
  public void stopTimer() {
    timeline.stop();
    isTimerStarted = false;
    System.out.println("Timer stopped");
  }

  /**
   * Updates the timer in the game UI. This method is run on the JavaFX application thread to ensure
   * that UI updates are performed safely.
   */
  public void updateTimerInGame() {
    Platform.runLater(
        () -> {
          // Get the current root of the ROOM scene
          Parent currentRoot;
          try {
            currentRoot = SceneManager.getUiRoot(SceneManager.AppUi.ROOM);
            if (currentRoot != null) {
              // Look up the labels for minutes and seconds in the current root
              Label minutesLabel = (Label) currentRoot.lookup("#mins");
              Label secondsLabel = (Label) currentRoot.lookup("#secs");

              // If the labels are found, update their text with the formatted time
              if (minutesLabel != null && secondsLabel != null) {
                mins.setText(formattedMinutes);
                secs.setText(formattedSeconds);
              } else {
                System.out.println("Label elements not found");
              }
            } else {
              System.out.println("Current root is null");
            }
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
  }

  /**
   * Sets the timer labels for the minutes and seconds and updates them immediately.
   *
   * @param mins the label for minutes
   * @param secs the label for seconds
   */
  public void setTimerLabel(Label mins, Label secs, Label dot) {
    this.mins = mins;
    this.secs = secs;
    this.dot = dot;
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

  /**
   * Updates the timer labels (minutes and seconds) to display the current remaining time. This
   * method calculates the remaining minutes and seconds from the interval and updates the
   * corresponding labels if they are not null.
   */
  private void updateTimerLabels() {
    // Calculate the remaining minutes and seconds
    int minutes = interval / 60;
    int seconds = interval % 60;

    // Format the minutes and seconds to ensure they are displayed correctly
    formattedMinutes = String.format("%01d", minutes);
    formattedSeconds = String.format("%02d", seconds);

    // Update the labels if they are not null
    if (mins != null && secs != null) {
      mins.setText(formattedMinutes);
      secs.setText(formattedSeconds);
    }
  }
}
