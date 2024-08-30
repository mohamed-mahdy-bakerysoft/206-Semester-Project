package nz.ac.auckland.se206.states;

import java.io.IOException;
import java.net.URISyntaxException;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;

/**
 * The Guessing state of the game. Handles the logic for when the player is making a guess about the
 * profession of the characters in the game.
 */
public class Guessing implements GameState {
  private static boolean win = false;

  public static boolean getGameResult() {
    return win;
  }

  private final GameStateContext context;
  private MediaPlayer player;
  private Media sound;

  /**
   * Constructs a new Guessing state with the given game state context.
   *
   * @param context the context of the game state
   */
  public Guessing(GameStateContext context) {
    this.context = context;
  }

  /**
   * Handles the event when the guess button is clicked. Since the player has already guessed, it
   * notifies the player.
   *
   * @throws IOException if there is an I/O error
   */
  @Override
  public void handleGuessClick() throws IOException {}

  /**
   * Handles the event when a rectangle is clicked. Checks if the clicked rectangle is a customer
   * and updates the game state accordingly.
   *
   * @param event the mouse event triggered by clicking a rectangle
   * @param rectangleId the ID of the clicked rectangle
   * @throws IOException if there is an I/O error
   * @throws URISyntaxException
   */
  @Override
  public void handleRectangleClick(MouseEvent event, String rectangleId)
      throws IOException, URISyntaxException {
    // while in guessing state, prompting the user to guess suspects and not interact with any more
    // clues
    if (rectangleId.equals("rectOfficer")
        || rectangleId.equals("rectPaperClue")
        || rectangleId.equals("rectBin")
        || rectangleId.equals("rectSecurityCamera")) {
      sound = new Media(App.class.getResource("/sounds/click_on_suspects.mp3").toURI().toString());
      player = new MediaPlayer(sound);
      player.play();
      return;
    }

    if (rectangleId.equals(
        context
            .getRectIdToGuess())) { // if player correctly clicks on the thief, display good ending
      win = true;
      App.showEnding("good_ending");
      sound = new Media(App.class.getResource("/sounds/correct_you_win.mp3").toURI().toString());
      player = new MediaPlayer(sound);
      player.play();
    } else { // if suspect chooses incorrectly, display bad ending
      App.showEnding("bad_ending");
      sound =
          new Media(App.class.getResource("/sounds/better_luck_next_time.mp3").toURI().toString());
      player = new MediaPlayer(sound);
      player.play();
    }

    context.setState(context.getGameOverState());
  }
}
