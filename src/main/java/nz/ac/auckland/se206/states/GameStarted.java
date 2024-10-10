package nz.ac.auckland.se206.states;

import java.io.IOException;
import java.net.URISyntaxException;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.speech.MP3Player;

/**
 * The GameStarted state of the game. Handles the initial interactions when the game starts,
 * allowing the player to chat with characters and prepare to make a guess.
 */
public class GameStarted implements GameState {

  private final GameStateContext context;
  private MediaPlayer player;
  private Media sound;
  private MP3Player player2;

  /**
   * Constructs a new GameStarted state with the given game state context.
   *
   * @param context the context of the game state
   */
  public GameStarted(GameStateContext context) {
    this.context = context;
    player2 = new MP3Player("src/main/resources/sounds/makeaguess.mp3");
  }

  /**
   * Handles the event when a rectangle is clicked. Depending on the clicked rectangle, it either
   * provides an introduction or transitions to the chat view.
   *
   * @param event the mouse event triggered by clicking a rectangle
   * @param rectangleId the ID of the clicked rectangle
   * @throws IOException if there is an I/O error
   * @throws URISyntaxException if there is an error with the URI syntax for media files
   */
  @Override
  public void handleRectangleClick(MouseEvent event, String rectangleId)
      throws IOException, URISyntaxException {
    // Transition to chat view or provide an introduction based on the clicked rectangle
    switch (rectangleId) {
      case "rectOfficer":
        sound = new Media(App.class.getResource("/sounds/police.mp3").toURI().toString());
        player = new MediaPlayer(sound);
        player.play();
        return;
    }
    System.out.println("ID In GameStarted Is: " + rectangleId);
    App.openChat(event, rectangleId);
  }

  /**
   * Handles the event when the guess button is clicked. Prompts the player to make a guess and
   * transitions to the guessing state.
   *
   * @throws IOException if there is an I/O error
   * @throws URISyntaxException if there is an error with the URI syntax for media files
   */
  @Override
  public void handleGuessClick() throws IOException, URISyntaxException {
    player2.play();
    context.setState(context.getGuessingState());
    App.setRoot("whosThief");
  }
}
