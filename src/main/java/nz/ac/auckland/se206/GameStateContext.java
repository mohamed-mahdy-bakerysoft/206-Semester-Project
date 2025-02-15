package nz.ac.auckland.se206;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.apiproxy.chat.openai.ChatMessage;
import nz.ac.auckland.se206.controllers.InteragationRoomController;
import nz.ac.auckland.se206.states.GameOver;
import nz.ac.auckland.se206.states.GameStarted;
import nz.ac.auckland.se206.states.GameState;
import nz.ac.auckland.se206.states.Guessing;

/**
 * Context class for managing the state of the game. Handles transitions between different game
 * states and maintains game data such as the professions and rectangle IDs.
 */
public class GameStateContext {

  private final String rectIdToGuess;
  private final String professionToGuess;
  private final Map<String, String> rectanglesToProfession;
  private final GameStarted gameStartedState;
  private final Guessing guessingState;
  private final GameOver gameOverState;
  private static GameStateContext instance;

  /**
   * Gets the instance of the GameStateContext.
   *
   * @return the instance of the GameStateContext
   */
  public static GameStateContext getInstance() {
    if (instance == null) {
      instance = new GameStateContext();
    }
    return instance;
  }

  private Map<String, List<ChatMessage>> chatHistory;
  private GameState gameState;

  /** Constructs a new GameStateContext and initializes the game states and professions. */
  private GameStateContext() {
    gameStartedState = new GameStarted(this);
    guessingState = new Guessing(this);
    gameOverState = new GameOver(this);

    gameState = gameStartedState; // Initial state

    chatHistory = new HashMap<>();
    chatHistory.put("suspect1.txt", new ArrayList<>());
    chatHistory.put("thief.txt", new ArrayList<>());
    chatHistory.put("suspect2.txt", new ArrayList<>());

    rectanglesToProfession = new HashMap<>();
    rectanglesToProfession.put("rectPerson1", "Art Currator");
    rectanglesToProfession.put("rectPerson2", "Art Thief");
    rectanglesToProfession.put("rectPerson3", "Janitor");
    rectIdToGuess = "rectPerson2";
    professionToGuess = "Art Thief";
  }

  /**
   * Gets the chat history for all suspects.
   *
   * @return the chat history for all suspects
   */
  public Map<String, List<ChatMessage>> getChatHistory() {
    return chatHistory;
  }

  /**
   * Sets the current state of the game.
   *
   * @param state the new state to set
   */
  public void setState(GameState state) {
    this.gameState = state;
  }

  /**
   * Gets the initial game started state.
   *
   * @return the game started state
   */
  public GameState getGameStartedState() {
    return gameStartedState;
  }

  /**
   * Gets the guessing state.
   *
   * @return the guessing state
   */
  public GameState getGuessingState() {
    return guessingState;
  }

  /**
   * Gets the game over state.
   *
   * @return the game over state
   */
  public GameState getGameOverState() {
    return gameOverState;
  }

  /**
   * Gets the profession to be guessed.
   *
   * @return the profession to guess
   */
  public String getProfessionToGuess() {
    return professionToGuess;
  }

  /**
   * Gets the ID of the rectangle to be guessed.
   *
   * @return the rectangle ID to guess
   */
  public String getRectIdToGuess() {
    return rectIdToGuess;
  }

  /**
   * Gets the profession associated with a specific rectangle ID.
   *
   * @param rectangleId the rectangle ID
   * @return the profession associated with the rectangle ID
   */
  public String getProfession(String rectangleId) {
    return rectanglesToProfession.get(rectangleId);
  }

  /**
   * Handles the event when a rectangle is clicked.
   *
   * @param event the mouse event triggered by clicking a rectangle
   * @param rectangleId the ID of the clicked rectangle
   * @throws IOException if there is an I/O error
   * @throws URISyntaxException if there is an error with the URI syntax for media files
   */
  public void handleRectangleClick(MouseEvent event, String rectangleId)
      throws IOException, URISyntaxException {
    gameState.handleRectangleClick(event, rectangleId);
  }

  /**
   * Handles the event when the guess button is clicked.
   *
   * @throws IOException if there is an I/O error
   * @throws URISyntaxException if there is an error with the URI syntax for media files
   */
  public void handleGuessClick() throws IOException, URISyntaxException {
    gameState.handleGuessClick();
  }

  // Get the current state of the game
  public GameState getCurrentState() {
    return gameState;
  }

  /**
   * Resets the game state back to the initial game started state and clears the chat history for
   * all suspects.
   */
  public void reset() {
    // Reset the game state back to the initial game started state
    this.gameState = this.gameStartedState;

    // Clear the chat history for all suspects
    for (List<ChatMessage> historyList : chatHistory.values()) {
      if (historyList != null) {
        historyList.clear();
      }
    }

    // Reset clue interaction and other static variables
    InteragationRoomController.resetStaticVariables();
  }
}
