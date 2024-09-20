package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionRequest;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionResult;
import nz.ac.auckland.apiproxy.chat.openai.ChatMessage;
import nz.ac.auckland.apiproxy.chat.openai.Choice;
import nz.ac.auckland.apiproxy.config.ApiProxyConfig;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.TimeManager;
import nz.ac.auckland.se206.prompts.PromptEngineering;

/**
 * The SubmitAnswerController class manages the submission of answers in the guessing game. It
 * includes interactions with the OpenAI GPT model, progress bar handling, and timer management.
 */
public class SubmitAnswerController {

  private static String feed;
  private static String thief;
  private static String answer;
  private static boolean isFirstTime = true;
  public static TimeManager timeManager = TimeManager.getInstance();

  /**
   * Gets the current feedback.
   *
   * @return the feedback from the GPT response
   */
  public static String getFeed() {
    return feed;
  }

  /**
   * Gets the current selected thief.
   *
   * @return the thief chosen by the player
   */
  public static String getThief() {
    return thief;
  }

  /**
   * Gets the current answer.
   *
   * @return the answer provided by the player
   */
  public static String getAnswer() {
    return answer;
  }

  /**
   * Sets the value of the isFirstTime flag.
   *
   * @param isFirstTime boolean indicating if it's the player's first interaction
   */
  public static void setIsFirstTime(boolean isFirstTime) {
    SubmitAnswerController.isFirstTime = isFirstTime;
  }

  /** Resets the submit answer controller state, including the timer, feed, and thief data. */
  public static void reset() {
    feed = null;
    thief = null;
    answer = null;
    isFirstTime = true; // Reset to ensure the timer logic behaves correctly
  }

  @FXML private Button submitButton;
  @FXML private Button viewfeedback;
  @FXML private Button viewfeedback2;
  @FXML private TextArea answerTxtArea;
  @FXML private Rectangle janitor;
  @FXML private Rectangle hos;
  @FXML private Rectangle curator;
  @FXML private TextArea feedback;
  @FXML private TextArea feedback2;
  @FXML private Label mins;
  @FXML private Label secs;
  @FXML private ProgressBar progressBar;
  private MediaPlayer player;
  private Media sound;

  private ChatCompletionRequest chatCompletionRequest;

  /**
   * Initializes the controller, setting up the timer and resetting the first-time flag if
   * necessary.
   */
  public void initialize() throws URISyntaxException {

    if (isFirstTime == true) {
      timeManager.stopTimer();
      timeManager.setInterval(60);
    }
    timeManager.startTimer();
    timeManager.setTimerLabel(mins, secs);
  }

  /**
   * Sends the player's answer and processes the submission asynchronously. It includes progress bar
   * updates and invokes GPT model interactions.
   */
  public void sendAnswer() {
    submitButton.setDisable(true);
    timeManager.stopTimer();
    if (answerTxtArea.getText().isEmpty()) {
      return;
    }

    Map<String, String> map = new HashMap<>();
    map.put("answer", answerTxtArea.getText());
    map.put("thief", thief);

    progressBar.setVisible(true); // Show the progress bar

    Task<Void> task =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            updateProgress(0, 100); // Start progress
            intizliaseAndGpt(map);
            for (int i = 0; i <= 100; i++) {
              updateProgress(i, 100); // Update the progress
              Thread.sleep(20); // Simulate progress
            }
            return null;
          }
        };

    task.setOnSucceeded(
        event -> {
          progressBar.setVisible(false); // Hide progress bar when task is done
          // Handle successful feedback logic here
        });

    task.setOnFailed(
        event -> {
          progressBar.setVisible(false); // Hide progress bar if the task fails
          task.getException().printStackTrace();
        });

    progressBar.progressProperty().bind(task.progressProperty());
    new Thread(task).start(); // Run the task in a background thread
  }

  /**
   * Initiates the answer submission process.
   *
   * @return a map containing the answer and thief
   */
  public static Map<String, String> intiateanswer() {
    Map<String, String> map = new HashMap<>();
    map.put("answer", answer);
    map.put("thief", thief);
    return map;
  }

  /** Saves the current answer entered in the text area. */
  @FXML
  private void savefeedback() {
    answer = answerTxtArea.getText();
  }

  /** Appends feedback based on the selected thief. */
  @FXML
  private void appendfeedback() {
    if (thief.equals("hos")) {
      feedback.appendText(feed);
    } else if (thief.equals("curator")) {
      feedback2.appendText(feed);
    } else if (thief.equals("janitor")) {
      feedback2.appendText(feed);
    } else {
      feedback2.appendText("Feedback not available");
    }
  }

  /**
   * Handles the event when a rectangle representing a suspect is clicked.
   *
   * @param event the mouse event triggered by clicking a suspect rectangle
   * @throws IOException if there is an I/O error
   * @throws URISyntaxException if there is a URI error
   */
  @FXML
  private void handleRectangleClick(MouseEvent event) throws IOException, URISyntaxException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    thief = clickedRectangle.getId();
    isFirstTime = false;
    App.setRoot("submitanswer");
  }

  /**
   * Handles the "Enter" key press to submit the answer.
   *
   * @param event the key event triggered by pressing the "Enter" key
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void handleEnterKey(KeyEvent event) throws IOException {
    if (event.getCode().equals(KeyCode.ENTER)) {
      sendAnswer(); // Call the same method for submitting the answer
    }
  }

  /**
   * Generates the system prompt based on the suspect (thief) selected.
   *
   * @param data a map containing relevant data for prompt generation
   * @return the generated system prompt
   */
  private String getSystemPrompt(Map<String, String> data) {
    if (thief.equals("hos")) {
      return PromptEngineering.getPrompt("win.txt", data);
    } else if (thief.equals("curator") || thief.equals("janitor")) {
      return PromptEngineering.getPrompt("loss.txt", data);
    }
    return "error";
  }

  /**
   * Initializes and runs the GPT-based interaction asynchronously.
   *
   * @param data a map containing the player's answer and thief
   */
  public void intizliaseAndGpt(Map<String, String> data) {
    Task<Void> task =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            try {
              ApiProxyConfig config = ApiProxyConfig.readConfig();
              chatCompletionRequest =
                  new ChatCompletionRequest(config)
                      .setN(1)
                      .setTemperature(0.2)
                      .setTopP(0.5)
                      .setMaxTokens(100);
              runGpt(new ChatMessage("system", getSystemPrompt(data)));
              return null;
            } catch (ApiProxyException | IOException e) {
              e.printStackTrace();
              return null;
            }
          }
        };

    task.setOnSucceeded(
        event -> {
          // UI updates or logic after GPT response
          try {
            if (thief.equals("janitor") || thief.equals("curator")) {
              App.setRoot("badending");
            } else if (thief.equals("hos")) {
              App.setRoot("goodending2");
            }
          } catch (IOException e) {
            e.printStackTrace();
          }
          submitButton.setDisable(false);
        });

    new Thread(task).start(); // Start the background task
  }

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the content of the GPT response message
   * @throws ApiProxyException if there is an error with the GPT API
   * @throws IOException if there is an I/O error
   */
  private String runGpt(ChatMessage msg) throws ApiProxyException, IOException {
    chatCompletionRequest.addMessage(msg);
    ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
    Choice result = chatCompletionResult.getChoices().iterator().next();
    chatCompletionRequest.addMessage(result.getChatMessage());

    String messageContent = result.getChatMessage().getContent();
    feed = messageContent;
    EndingController.setFeed(feed);
    EndingController.setThief(thief);
    return messageContent;
  }
}
