package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionRequest;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionResult;
import nz.ac.auckland.apiproxy.chat.openai.ChatMessage;
import nz.ac.auckland.apiproxy.chat.openai.Choice;
import nz.ac.auckland.apiproxy.config.ApiProxyConfig;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.GameStateContext;
import nz.ac.auckland.se206.TimeManager;
import nz.ac.auckland.se206.prompts.PromptEngineering;
import nz.ac.auckland.se206.speech.TextToSpeech;
import nz.ac.auckland.se206.states.Guessing;

// improt key event

/**
 * Added Controller class for the intelroom view. Handles user interactions within the room where
 * the user can chat with customers and guess their profession.
 */
public class InteragationRoomController {
  private static GameStateContext context = new GameStateContext();
  private static boolean clueHasBeenInteractedWith = false;
  private String profession;
  private ChatCompletionRequest chatCompletionRequest;
  private static boolean isFirstTimeInit = true;
  private static boolean suspectHasBeenTalkedTo = false;
  private static Map<String, Boolean> suspectHasBeenTalkedToMap = new HashMap<>();
  private static Map<String, String> professionToNameMap = new HashMap<>();

  /**
   * Gets the game context.
   *
   * @return the game context.
   */
  public static GameStateContext getGameContext() {
    return context;
  }

  /**
   * Returns whether the suspect has been talked to.
   *
   * @return true if the suspect has been talked to, false otherwise
   */
  public static boolean getSuspectHasBeenTalkedTo() {
    return suspectHasBeenTalkedTo;
  }

  /**
   * Sets the game over state.
   *
   * @throws IOException if there is an I/O error
   */
  public static void setGameOverState() throws IOException {
    context.setState(context.getGameOverState());
    App.setRoot("badending");
  }

  /**
   * Gets the clueHasBeenInteractedWith boolean to check if the clue has been interacted with.
   *
   * @return the clueHasBeenInteractedWith boolean
   */
  public static boolean getClueHasBeenInteractedWith() {
    return clueHasBeenInteractedWith;
  }

  // Added navbar with buttons
  @FXML private VBox navBar;
  @FXML private Button corridorButton;
  @FXML private Button suspect1Button;
  @FXML private Button suspect2Button;
  @FXML private Button suspect3Button;

  @FXML private Button btnGoIntelRoom;
  @FXML private Button btnGuess;
  @FXML private Button btnBack;
  @FXML private BorderPane mainPane;
  @FXML private Label mins;
  @FXML private Label secs;
  @FXML private ImageView mainLeftArrow;
  @FXML private ImageView mainRightArrow;
  @FXML private ImageView arrowLeft;
  @FXML private ImageView arrowRight;
  @FXML private Group chatGroup;
  @FXML private TextArea txtaChat;
  @FXML private TextField txtInput;
  @FXML private Button btnSend;
  @FXML private Button btnGoBack;

  private MediaPlayer player;
  private Media sound;
  private Media artStudentHmm;
  private Media thiefHmm;
  private Media grumpyTouristHmm;
  private Map<String, StringBuilder> chatHistory;
  private boolean navBarVisible = false;
  private double originalWidth;

  /**
   * Initializes the room view. If it's the first time initialization, it will provide instructions
   * via text-to-speech.
   *
   * @throws URISyntaxException
   */
  @FXML
  public void initialize() throws ApiProxyException {
    // NavBar Initialization
    // Initialize with navBar hidden
    navBar.setTranslateX(-200);
    btnGoIntelRoom.setOnAction(e -> toggleNavBar());
    suspect1Button.setOnAction(
        e -> {
          try {

            goToRoom("IntelRoomOne");
          } catch (IOException e1) {
            e1.printStackTrace();
          }
        });
    suspect2Button.setOnAction(
        e -> {
          try {
            goToRoom("IntelRoomTwo");
          } catch (IOException e1) {
            e1.printStackTrace();
          }
        });
    suspect3Button.setOnAction(
        e -> {
          try {
            goToRoom("IntelRoomThree");
          } catch (IOException e1) {
            e1.printStackTrace();
          }
        });
    corridorButton.setOnAction(
        e -> {
          try {
            goToCorridor();
          } catch (IOException e1) {
            e1.printStackTrace();
          }
        });
    if (isFirstTimeInit) {
      initializeSuspectTalkedToMap();
      initializeRoleToNameMap();
      isFirstTimeInit = false;
    }
    initializeSounds();
    TimeManager timeManager = TimeManager.getInstance();
    timeManager.setTimerLabel(mins, secs);
    this.chatHistory = new HashMap<>();
    chatHistory.put("suspect1.txt", new StringBuilder());
    chatHistory.put("suspect2.txt", new StringBuilder());
    chatHistory.put("thief.txt", new StringBuilder());
    // testing purposes
    System.out.println("Entire Chat history intalizeed");
  }

  public void setTime() {
    TimeManager timeManager = TimeManager.getInstance();
    timeManager.setTimerLabel(mins, secs);
  }

  /**
   * Handles the key pressed event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyPressed(KeyEvent event) {
    // System.out.println("Key " + event.getCode() + " pressed");
  }

  /**
   * Handles the key released event.
   *
   * @param event the key event
   */
  @FXML
  public void onKeyReleased(KeyEvent event) {
    // System.out.println("Key " + event.getCode() + " released");
  }

  public void setProfession(String profession) throws URISyntaxException {
    this.profession = profession;

    // Only display the initial message if no previous conversation exists
    if (!suspectHasBeenTalkedToMap.get(profession)) {
      appendChatMessage(new ChatMessage("user", getInitialMessageForProfession(profession)));
    }

    try {
      ApiProxyConfig config = ApiProxyConfig.readConfig();
      chatCompletionRequest =
          new ChatCompletionRequest(config)
              .setN(1)
              .setTemperature(0.2)
              .setTopP(0.5)
              .setMaxTokens(80);

      // Run GPT request in a background thread
      Task<ChatMessage> task =
          new Task<>() {
            @Override
            protected ChatMessage call() throws ApiProxyException {
              return runGpt(new ChatMessage("system", getSystemPrompt()));
            }
          };

      task.setOnSucceeded(
          event -> {
            ChatMessage resultMessage = task.getValue();
            appendChatMessage(resultMessage);
            if (!suspectHasBeenTalkedToMap.get(profession)) {
              TextToSpeech.speak(resultMessage.getContent(), profession);
              suspectHasBeenTalkedToMap.put(profession, true); // Mark TTS as used for this suspect
            }
          });

      task.setOnFailed(
          event -> {
            task.getException().printStackTrace();
          });

      new Thread(task).start();
    } catch (ApiProxyException e) {
      e.printStackTrace();
    }
  }

  // NavBar Methods
  private void toggleNavBar() {
    TranslateTransition translateTransition = new TranslateTransition(Duration.millis(500), navBar);
    FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), navBar);
    // Get the current stage from the scene
    Stage stage = (Stage) navBar.getScene().getWindow();
    originalWidth = stage.getWidth();

    if (navBarVisible) {
      // Slide out and fade out, then reduce the window size
      translateTransition.setByX(-200); // Move back off-screen to the right
      fadeTransition.setToValue(0); // Fade out to invisible
      navBarVisible = false;

      // Reduce the window size after the transition
      translateTransition.setOnFinished(e -> stage.setWidth(originalWidth - 200));
    } else {
      // Slide in and fade in, then increase the window size
      translateTransition.setByX(200); // Move into view
      fadeTransition.setToValue(1); // Fade in to fully visible
      navBarVisible = true;

      // Increase the window size during the transition
      stage.setWidth(originalWidth + 200);
    }

    // Play both transitions
    translateTransition.play();
    fadeTransition.play();
  }

  private void goToRoom(String roomName) throws IOException {
    // Before navigating, reset the window size if navBar is visible
    Stage stage = (Stage) navBar.getScene().getWindow();
    stage.setWidth(originalWidth);
    // Handle room switching logic
    App.setRoot(roomName);
  }

  private void goToCorridor() throws IOException {
    Stage stage = (Stage) navBar.getScene().getWindow();
    stage.setWidth(originalWidth);
    App.setRoot("Intel_Draft");
  }

  /**
   * Generates the system prompt based on the profession.
   *
   * @return the system prompt string
   */
  private String getSystemPrompt() {
    Map<String, String> map = new HashMap<>();
    String promptFile = null;
    switch (profession) {
      case "Curious Art Student":
        promptFile = "suspect1.txt";
        break;
      case "Art Thief":
        promptFile = "thief.txt";
        break;
      case "Grumpy Out of Town Tourist":
        promptFile = "suspect2.txt";
        break;
    }
    map.put("profession", profession);
    map.put("chathistory", chatHistory.get(promptFile).toString());
    // testing purposes
    System.out.println("Chat history: ");
    System.out.println(chatHistory.get(promptFile).toString());
    System.out.println("Entire Chat history: ");
    System.out.println(chatHistory);
    return PromptEngineering.getPrompt(promptFile, map);
  }

  /**
   * Runs the GPT model with a given chat message.
   *
   * @param msg the chat message to process
   * @return the response chat message
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  private ChatMessage runGpt(ChatMessage msg) throws ApiProxyException {
    chatCompletionRequest.addMessage(msg);
    ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();

    Choice result = chatCompletionResult.getChoices().iterator().next();
    chatCompletionRequest.addMessage(result.getChatMessage());
    return result.getChatMessage();
  }

  private String getInitialMessageForProfession(String profession) {
    switch (profession) {
      case "Curious Art Student":
        return "Hey can you tell me what happened here? I'm investigating this case.";
      case "Art Thief":
        return "Hi sir. I'm one of the investigators on this job. Can you tell me what happened"
            + " here?";
      case "Grumpy Out of Town Tourist":
        return "Hello. I'm investigating this case on behalf of PI Masters. Can you tell me what"
            + " happened here?";
      default:
        return "Hello, I am investigating this case. Can you tell me what happened here?";
    }
  }

  private void initializeSuspectTalkedToMap() {
    suspectHasBeenTalkedToMap.put("Curious Art Student", false);
    suspectHasBeenTalkedToMap.put("Art Thief", false);
    suspectHasBeenTalkedToMap.put("Grumpy Out of Town Tourist", false);
  }

  private void initializeRoleToNameMap() {
    professionToNameMap.put("Curious Art Student", "Jessica");
    professionToNameMap.put("Art Thief", "William");
    professionToNameMap.put("Grumpy Out of Town Tourist", "Johnson");
    professionToNameMap.put("user", "Investigator");
  }

  /**
   * Sends a message to the GPT model.
   *
   * @param event the action event triggered by the send button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onSendMessage(ActionEvent event) throws ApiProxyException, IOException {
    String message = txtInput.getText().trim();
    if (message.isEmpty()) {
      return;
    }
    txtInput.clear();
    ChatMessage msg = new ChatMessage("user", message);
    appendChatMessage(msg);
    suspectHasBeenTalkedTo = true;

    // Run user message in a background thread
    Task<ChatMessage> task =
        new Task<>() {
          @Override
          protected ChatMessage call() throws ApiProxyException {
            return runGpt(msg);
          }
        };

    task.setOnSucceeded(
        event1 -> {
          ChatMessage resultMessage = task.getValue();
          appendChatMessage(resultMessage);
          // Check if TTS should be used
          if (!suspectHasBeenTalkedToMap.get(profession)) {
            TextToSpeech.speak(resultMessage.getContent(), profession);
            suspectHasBeenTalkedToMap.put(profession, true); // Mark TTS as used for this suspect
          }
        });

    task.setOnFailed(
        event1 -> {
          task.getException().printStackTrace();
        });

    new Thread(task).start();
  }

  // Method to play "hmm" sound based on profession
  private void playHmmSound(String profession) {
    if (player != null) {
      player.stop();
    }
    switch (profession) {
      case "Curious Art Student":
        if (artStudentHmm != null) {
          player = new MediaPlayer(artStudentHmm);
        }
        break;
      case "Art Thief":
        if (thiefHmm != null) {
          player = new MediaPlayer(thiefHmm);
        }
        break;
      case "Grumpy Out of Town Tourist":
        if (grumpyTouristHmm != null) {
          player = new MediaPlayer(grumpyTouristHmm);
        }
        break;
      default:
        return;
    }
  }

  private void appendChatMessage(ChatMessage msg) {
    if (!msg.getRole().equals("user") && suspectHasBeenTalkedToMap.get(profession)) {
      playHmmSound(profession);
    }

    switch (profession) {
      case "Curious Art Student":
        chatHistory.get("suspect1.txt").append(msg.getRole() + ": " + msg.getContent() + "\n\n");
        break;
      case "Art Thief":
        chatHistory.get("thief.txt").append(msg.getRole() + ": " + msg.getContent() + "\n\n");
        break;
      case "Grumpy Out of Town Tourist":
        chatHistory.get("suspect2.txt").append(msg.getRole() + ": " + msg.getContent() + "\n\n");

        break;
    }

    // Start the text animation after a small delay to allow "hmm" sound to finish
    new Thread(
            () -> {
              try {
                Thread.sleep(500); // Adjust delay to match the length of the "hmm" sound
                Platform.runLater(() -> writeOnTextAnimation(msg.getRole(), msg.getContent()));
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            })
        .start();
  }

  /**
   * Creates a write-on text animation for a chat message.
   *
   * @param role the role of the message sender (e.g., "system", "user")
   * @param text the content of the message
   */
  private void writeOnTextAnimation(String role, String text) {
    String displayName;
    if (role.equals("user")) {
      displayName = professionToNameMap.get("user"); // Use the mapped name or default to role
    } else {
      displayName = professionToNameMap.get(profession); // Use the mapped name or default to role
    }

    Task<Void> task =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            StringBuilder currentText = new StringBuilder(displayName + ": ");
            for (char ch : text.toCharArray()) {
              currentText.append(ch);
              String finalText = currentText.toString();
              Platform.runLater(
                  () -> {
                    // Use String replacement to ensure text is placed correctly
                    String previousText = txtaChat.getText();
                    int lastMessageIndex = previousText.lastIndexOf(displayName + ":");
                    if (lastMessageIndex != -1) {
                      txtaChat.setText(
                          previousText.substring(0, lastMessageIndex) + finalText + "\n");
                    } else {
                      txtaChat.appendText(finalText + "\n");
                    }
                  });
              Thread.sleep(35); // Adjust delay for typing effect
            }
            // Small delay to prevent overlap
            Thread.sleep(1200); // MODIFY to prevent overlap...
            return null;
          }
        };
    new Thread(task).start();
  }

  /**
   * Handles mouse clicks on rectangles representing people in the room.
   *
   * @param event the mouse event triggered by clicking a rectangle
   * @throws IOException if there is an I/O error
   * @throws URISyntaxException
   */
  @FXML
  private void handleRectangleClick(MouseEvent event) throws IOException, URISyntaxException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();

    // Identify which suspect was clicked and set the profession accordingly
    String suspectId = clickedRectangle.getId();
    switch (suspectId) {
      case "rectPerson1":
        profession = "Curious Art Student";
        break;
      case "rectPerson2":
        profession = "Art Thief";
        break;
      case "rectPerson3":
        profession = "Grumpy Out of Town Tourist";
        break;
    }

    // Display the chat UI
    chatGroup.setVisible(true); // Ensure chat group is visible
    txtaChat.clear();
    txtInput.clear();

    // Initialize the chat with the suspect
    setProfession(profession); // This method handles initializing chat context
  }

  /**
   * Handles the guess button click event.
   *
   * @param event the action event triggered by clicking the guess button
   * @throws IOException if there is an I/O error
   * @throws URISyntaxException
   */
  @FXML
  private void handleGuessClick(ActionEvent event) throws IOException, URISyntaxException {
    chatGroup.setVisible(false);
    if (clueHasBeenInteractedWith
        && InteragationRoomController
            .getSuspectHasBeenTalkedTo()) { // TO DO: && chatController.getSuspectHasBeenTalkedTo()
      System.out.println("Now in guessing state");
      context.handleGuessClick();
    } else {
      sound =
          new Media(
              App.class
                  .getResource("/sounds/investigate_more_before_guessing.mp3")
                  .toURI()
                  .toString());
      player = new MediaPlayer(sound);
      player.play();
    }
    App.setRoot("room");
  }

  @FXML
  private void handleBackToCrimeSceneClick(ActionEvent event) throws IOException {
    // Before navigating, reset the window size if navBar is visible
    Stage stage = (Stage) navBar.getScene().getWindow();
    stage.setWidth(originalWidth);
    App.setRoot("room");
  }

  @FXML
  private void handleRoomsClick(MouseEvent event) throws IOException, URISyntaxException {
    Rectangle clickedRoom = (Rectangle) event.getSource();
    context.handleRectangleClick(event, clickedRoom.getId());
  }

  // Initialize sound resources only once
  private void initializeSounds() {
    try {
      artStudentHmm =
          new Media(App.class.getResource("/sounds/art_student_hmm.mp3").toURI().toString());
      thiefHmm = new Media(App.class.getResource("/sounds/thief_hmm.mp3").toURI().toString());
      grumpyTouristHmm =
          new Media(App.class.getResource("/sounds/grumpy_tourist_hmm.mp3").toURI().toString());

      // Check if any Media is null
      if (artStudentHmm == null || thiefHmm == null || grumpyTouristHmm == null) {
        throw new IllegalArgumentException("Failed to load one or more sound files.");
      }
    } catch (URISyntaxException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
    }
  }

  /**
   * Handles the left arrow click event to change perspective of room to the left.
   *
   * @param event mouse click
   * @throws IOException if the FXML file is not found
   */
  public void onClickLeft(MouseEvent event) throws IOException {
    if (context.getCurrentState()
        instanceof Guessing) { // if in guessing phase, the other areas should not be accessible for
      // investigation
      return;
    }
    ImageView clickedArrow = (ImageView) event.getSource();
    if (clickedArrow.getId().equals("mainArrowLeft")) {
      App.setRoot("Intel_Draft");
    }
  }

  /**
   * Navigates back to the previous view.
   *
   * @param event the action event triggered by the go back button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onGoBack(ActionEvent event) throws ApiProxyException, IOException {
    chatGroup.setVisible(false); // Ensure chat group is visible
    App.setRoot("Intel_Draft");
  }
}
