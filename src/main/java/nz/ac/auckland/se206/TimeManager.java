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
import nz.ac.auckland.se206.states.GameStarted;
import nz.ac.auckland.se206.states.Guessing;

// import map

public class TimeManager {
  private static TimeManager instance;

  private static int interval = 300; // 300 for 5 minutes

  public static synchronized TimeManager getInstance() {
    if (instance == null) {
      instance = new TimeManager();
    }
    return instance;
  }

  private String formattedMinutes;
  private String formattedSeconds;
  private Timeline timeline;
  private Label mins;
  private Label secs;
  private MediaPlayer player;
  private Media sound;

  public TimeManager() {
    initialiseTimer();
    updateTimerLabels(); // Set initial label values
  }

  public void initialiseTimer() {
    timeline =
        new Timeline(
            new KeyFrame(
                javafx.util.Duration.seconds(1),
                e -> {
                  try {
                    decrementTime();
                  } catch (IOException e1) {
                    e1.printStackTrace();
                  } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                  }
                  updateTimerInGame();
                }));
    timeline.setCycleCount(Timeline.INDEFINITE);
  }

  public void decrementTime() throws IOException, URISyntaxException {
    // if seconds and minutes are 0, handle it according to game state
    if (interval == 0) {
      if (RoomController.getGameContext().getCurrentState() instanceof GameStarted
          && InteragationRoomController.getSuspectsHaveBeenTalkedTo()
          && RoomController
              .getClueHasBeenInteractedWith()) { // if game started state and suspects have been
        // talked to and clue has been interacted with,
        // move to guessing state
        System.out.println("Game started state to guessing state");
        GameStateContext context = GameStateContext.getInstance();
        context.setState(context.getGuessingState());
        System.out.println("Now in guessing state");
        App.setRoot("whosThief");
        setInterval(60);
        return;
      }
      if (RoomController.getGameContext().getCurrentState()
          instanceof GameOver) { // if game is over, do nothing
        stopTimer();
        System.out.println("Game is over");
        return;
      } else if (RoomController.getGameContext().getCurrentState() instanceof Guessing
          || !(InteragationRoomController.getSuspectsHaveBeenTalkedTo()
              && RoomController
                  .getClueHasBeenInteractedWith())) { // if already in guessing state OR player has
        // not investigated, move to game over state
        SubmitAnswerController.setIsFirstTime(false);
        if (SubmitAnswerController.getAnswer() != null) {
          Map<String, String> map = SubmitAnswerController.intiateanswer();
          SubmitAnswerController intiateanswer = new SubmitAnswerController();
          intiateanswer.intizliaseAndGpt(map);
        } else {
          String thief = SubmitAnswerController.getThief();
          // cehck if thief is null
          if (thief == null) {
            App.setRoot("badending");
            TimeManager.getInstance().stopTimer();
            return;
          }
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
      } else { // move to guessing state and give 60 seconds to guess
        // RoomController.getGameContext()
        //     .setState(RoomController.getGameContext().getGuessingState());
        App.setRoot("whosThief");
        sound = new Media(App.class.getResource("/sounds/make_a_guess.mp3").toURI().toString());
        player = new MediaPlayer(sound);
        player.play();
        setGuessTimer();
        startTimer();
        System.out.println("Now in guessing state");
      }
      timeline.stop();
    } else { // else decrement the timer by 1 second
      interval--;
      int minutes = interval / 60;
      int seconds = interval % 60;
      formattedMinutes = String.format("%01d", minutes);
      formattedSeconds = String.format("%02d", seconds);
    }
  }

  private void setGuessTimer() {
    interval = 61; // added a second so that GUI could show 1 minute
    initialiseTimer();
  }

  public void startTimer() {
    updateTimerInGame();
    timeline.play();
  }

  public void stopTimer() {
    timeline.stop();
  }

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

  public void setTimerLabel(Label mins, Label secs) {
    this.mins = mins;
    this.secs = secs;
    updateTimerLabels(); // Update the labels immediately after setting them
  }

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

  // make a reset function
  public void resetTimer() {
    interval = 0;
    updateTimerLabels();
  }

  public void resetTimerRestart() {
    interval = 300; // Reset to 5 minutes
    updateTimerLabels();
    stopTimer(); // Stop the current timer if it was running
  }

  // make a setter for interval
  public void setInterval(int interval1) {
    interval = interval1;
  }

  // make a getter for time interval
  public int getInterval() {
    return interval;
  }
}
