package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
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

// improt key event

/**
 * Added Controller class for the intelroom view. Handles user interactions within the room where
 * the user can chat with customers and guess their profession.
 */
public class InteragationRoomController {
  private static boolean clueHasBeenInteractedWith = false;
  private String profession;
  private ChatCompletionRequest chatCompletionRequest;
  private static boolean isFirstTimeInit = true;
  private static Map<String, Boolean> suspectHasBeenTalkedToMap = new HashMap<>();
  private static Map<String, String> professionToNameMap = new HashMap<>();
  private static GameStateContext context = GameStateContext.getInstance();
  private static boolean isChatOpened = false;

  // make a setter for isChatOpened
  public static void setIsChatOpened(boolean isChatOpened1) {
    isChatOpened = isChatOpened1;
  }

  /**
   * Gets the game context.
   *
   * @return the game context.
   */
  public static GameStateContext getGameContext() {
    return context;
  }

  private static void initializeSuspectTalkedToMap() {
    suspectHasBeenTalkedToMap.put("Art Currator", false);
    suspectHasBeenTalkedToMap.put("Art Thief", false);
    suspectHasBeenTalkedToMap.put("Janitor", false);
  }

  /**
   * Returns whether the suspects have been talked to.
   *
   * @return true if all 3 suspects have been talked to, false otherwise
   */
  public static boolean getSuspectsHaveBeenTalkedTo() {
    System.out.println(suspectHasBeenTalkedToMap.get("Art Currator"));
    System.out.println(suspectHasBeenTalkedToMap.get("Janitor"));
    System.out.println(suspectHasBeenTalkedToMap.get("Art Thief"));

    return suspectHasBeenTalkedToMap.get("Art Currator")
        && suspectHasBeenTalkedToMap.get("Art Thief")
        && suspectHasBeenTalkedToMap.get("Janitor");
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

  // @FXML private Button btnGoIntelRoom;
  @FXML private Button btnBack;
  @FXML private BorderPane mainPane;
  @FXML private Label mins;
  @FXML private Label secs;
  @FXML private ImageView Currator0;
  @FXML private ImageView Currator1;
  @FXML private ImageView Currator2;
  @FXML private ImageView Thief0;
  @FXML private ImageView Thief1;
  @FXML private ImageView Thief2;
  @FXML private ImageView Janitor0;
  @FXML private ImageView Janitor1;
  @FXML private ImageView Janitor2;
  @FXML private Group chatGroup;
  @FXML private TextArea txtaChat;
  @FXML private TextField txtInput;
  @FXML private Button btnSend;
  @FXML private Button btnGoBack;

  private MediaPlayer player;
  private Media sound;
  private Media artCurratorHmm;
  private Media thiefHmm;
  private Media janitorHmm;

  @SuppressWarnings("unused")
  private Map<String, StringBuilder> chatHistory;

  private boolean navBarVisible = false;
  private int originalWidth = 1100;
  private Random random = new Random();

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
    navBar.setTranslateX(+200);
    navBar.setDisable(true);
    // btnGoIntelRoom.setOnAction(e -> toggleNavBar());
    suspect1Button.setOnAction(
        e -> {
          try {
            InteragationRoomController.setIsChatOpened(false);
            goToRoom("IntelRoomOne");
          } catch (IOException e1) {
            e1.printStackTrace();
          }
        });
    suspect2Button.setOnAction(
        e -> {
          try {
            InteragationRoomController.setIsChatOpened(false);
            goToRoom("IntelRoomTwo");
          } catch (IOException e1) {
            e1.printStackTrace();
          }
        });
    suspect3Button.setOnAction(
        e -> {
          try {
            InteragationRoomController.setIsChatOpened(false);
            goToRoom("IntelRoomThree");
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
    // Initialize the game context with the charHistory
    this.chatHistory = context.getChatHistory();
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

  public void setProfession(String profession) throws URISyntaxException, InterruptedException {
    this.profession = profession;

    // Disable the send button when the profession is being set
    btnSend.setDisable(true);
    // Only display the initial message if no previous conversation exists
    if (!suspectHasBeenTalkedToMap.get(profession)) {
      appendChatMessage(new ChatMessage("user", getInitialMessageForProfession(profession)));
    }

    try {
      ApiProxyConfig config = ApiProxyConfig.readConfig();
      chatCompletionRequest =
          new ChatCompletionRequest(config)
              .setN(1)
              .setTemperature(0.8)
              .setTopP(0.7)
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
            // Enable the send button after the response is processed
            btnSend.setDisable(false);
            if (!suspectHasBeenTalkedToMap.get(profession)) {
              suspectHasBeenTalkedToMap.put(profession, true); // Mark TTS as used for this suspect
            }
          });

      task.setOnFailed(
          event -> {
            // Enable the send button after the response is processed
            btnSend.setDisable(false);
            task.getException().printStackTrace();
          });

      new Thread(task).start();
    } catch (ApiProxyException e) {
      e.printStackTrace();
    }
  }

  // NavBar Methods
  @FXML
  private void onToggleNavBar() {
    TranslateTransition translateTransition = new TranslateTransition(Duration.millis(500), navBar);
    FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), navBar);

    if (navBarVisible) {
      // Slide out and fade out, then reduce the window size
      translateTransition.setToX(200); // Move back off-screen to the right
      fadeTransition.setToValue(0); // Fade out to invisible
      navBarVisible = false;
      navBar.setDisable(true); // Disable the navBar
    } else {
      navBar.setDisable(false); // Enable the navBar
      // Slide in and fade in, then increase the window size
      translateTransition.setByX(-200); // Move into view
      fadeTransition.setToValue(1); // Fade in to fully visible
      navBarVisible = true;
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

  /**
   * Generates the system prompt based on the profession.
   *
   * @return the system prompt string
   */
  private String getSystemPrompt() {
    // Retrieve the chat history from the game context
    Map<String, StringBuilder> chatHistory = context.getChatHistory();
    // Create a map to store the profession and chat history
    Map<String, String> map = new HashMap<>();
    String promptFile = null;

    // Determine the prompt file based on the profession
    switch (profession) {
      case "Art Currator":
        promptFile = "suspect1.txt";
        break;
      case "Art Thief":
        promptFile = "thief.txt";
        break;
      case "Janitor":
        promptFile = "suspect2.txt";
        break;
    }

    // Populate the map with the profession and chat history
    map.put("profession", profession);
    map.put("chathistory", chatHistory.get(promptFile).toString());

    // For testing purposes, print the chat history and entire chat history
    System.out.println("Chat history: ");
    System.out.println(chatHistory.get(promptFile).toString());
    System.out.println("Entire Chat history: ");
    System.out.println(chatHistory);

    // Generate and return the prompt using the PromptEngineering class
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

  /**
   * Generates an initial message based on the given profession.
   *
   * @param profession the profession of the person being addressed (e.g., "Art Currator", "Art
   *     Thief", "Janitor")
   * @return a string containing the initial message tailored to the given profession
   */
  private String getInitialMessageForProfession(String profession) {
    // Switch case to determine the initial message based on the profession
    switch (profession) {
      case "Art Currator":
        return "Hey can you tell me what happened here? I'm investigating this case."; // Case for
      // the art
      // currator
      case "Art Thief":
        return "Hi sir. I'm one of the investigators on this job. Can you tell me what happened"
            + " here?"; // case for Art Thief
      case "Janitor":
        return "Hello. I'm investigating this case on behalf of PI Masters. Can you tell me what"
            + " happened here?"; // case for Janitor
      default:
        return "Hello, I am investigating this case. Can you tell me what happened"
            + " here?"; // default case
    }
  }

  private void initializeRoleToNameMap() {
    professionToNameMap.put("Art Currator", "Frank");
    professionToNameMap.put("Art Thief", "William");
    professionToNameMap.put("Janitor", "John");
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
    sendMessage();
  }

  /**
   * Handles the "Enter" key press in the TextField to send a message.
   *
   * @param event the KeyEvent triggered by pressing a key
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onHandleEnterKey(KeyEvent event) throws ApiProxyException, IOException {
    if (event.getCode() == KeyCode.ENTER) {
      if (!btnSend.isDisabled()) {
        sendMessage();
      }
    }
  }

  /**
   * Sends a message to the GPT model.
   *
   * @param event the action event triggered by the send button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  private void sendMessage() throws ApiProxyException, IOException {
    String message = txtInput.getText().trim();
    if (message.isEmpty()) {
      return;
    }
    txtInput.clear();
    ChatMessage msg = new ChatMessage("user", message);
    appendChatMessage(msg);
    btnSend.setDisable(true); // Disable the send button while processing the message
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
          btnSend.setDisable(false); // Re-enable the send button
          // Check if TTS should be used
          if (!suspectHasBeenTalkedToMap.get(profession)) {
            suspectHasBeenTalkedToMap.put(profession, true); // Mark TTS as used for this suspect
          }
        });

    task.setOnFailed(
        event1 -> {
          btnSend.setDisable(false); // Re-enable the send button
          task.getException().printStackTrace();
        });

    new Thread(task).start();
  }

  // Method to play "hmm" sound based on profession
  private void playHmmSound(String profession) {
    // Stop the current player if it is playing
    if (player != null) {
      player.stop();
    }

    // Switch case to determine which sound to play based on profession
    switch (profession) {
      case "Art Currator":
        // If the profession is "Art Currator" and the sound is available, create a new MediaPlayer
        if (artCurratorHmm != null) {
          player = new MediaPlayer(artCurratorHmm);
        }
        break;
      case "Art Thief":
        // If the profession is "Art Thief" and the sound is available, create a new MediaPlayer
        if (thiefHmm != null) {
          player = new MediaPlayer(thiefHmm);
        }
        break;
      case "Janitor":
        // If the profession is "Janitor" and the sound is available, create a new MediaPlayer
        if (janitorHmm != null) {
          player = new MediaPlayer(janitorHmm);
        }
        break;
    }

    // Play the selected sound
    player.play();
  }

  private void appendChatMessage(ChatMessage msg) {
    Map<String, StringBuilder> chatHistory = context.getChatHistory();
    int randomIndex = random.nextInt(3); // Generates 0, 1, or 2

    if (!msg.getRole().equals("user")) {
      System.out.println("playing hmm sound from " + profession);
      playHmmSound(profession);
    }

    // Adding to history and change the image of the person after each sentence
    switch (profession) {
      case "Art Currator":
        chatHistory.get("suspect1.txt").append(msg.getRole() + ": " + msg.getContent() + "\n\n");
        setImageVisibility("Currator", randomIndex);
        break;
      case "Art Thief":
        chatHistory.get("thief.txt").append(msg.getRole() + ": " + msg.getContent() + "\n\n");
        setImageVisibility("Thief", randomIndex);
        break;
      case "Janitor":
        setImageVisibility("Janitor", randomIndex);
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

  // Helper method to set visibility based on the random index
  private void setImageVisibility(String suspectType, int randomIndex) {
    // Hide all images first
    hideAllImages(suspectType);

    // Show the selected image based on the random index
    switch (randomIndex) {
      case 0:
        // Show suspectType + "1" image (e.g. Currator1)
        getImageView(suspectType + "0").setVisible(true);
        break;
      case 1:
        // Show suspectType + "2" image (e.g. Currator2)
        getImageView(suspectType + "1").setVisible(true);
        break;
      case 2:
        // Show suspectType + "3" image (e.g. Currator3)
        getImageView(suspectType + "2").setVisible(true);
        break;
    }
  }

  // Hide all images for a given suspect
  private void hideAllImages(String suspectType) {
    getImageView(suspectType + "0").setVisible(false);
    getImageView(suspectType + "1").setVisible(false);
    getImageView(suspectType + "2").setVisible(false);
  }

  // Method to get the ImageView by ID (you can implement this based on your FXML IDs)
  private ImageView getImageView(String imageId) {
    Parent currentRoot = navBar.getScene().getRoot(); // Get the root of the current scene
    return (ImageView) currentRoot.lookup("#" + imageId); // Adjust based on your FXML structure
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
   * @throws InterruptedException
   */
  @FXML
  private void onHandleRectangleClick(MouseEvent event)
      throws IOException, URISyntaxException, InterruptedException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();

    // Identify which suspect was clicked and set the profession accordingly
    String suspectId = clickedRectangle.getId();
    switch (suspectId) {
      case "rectPerson1":
        profession = "Art Currator";
        break;
      case "rectPerson2":
        profession = "Art Thief";
        break;
      case "rectPerson3":
        profession = "Janitor";
        break;
    }

    // Display the chat UI
    // Initialize the chat with the suspect
    // This method handles initializing chat context

    if (!isChatOpened) {
      chatGroup.setVisible(true); // Ensure chat group is visible
      txtaChat.clear();
      txtInput.clear();

      setProfession(profession);
      isChatOpened = true; // prevent further calls
    } else {
      System.out.println("Chat has already been opened!");
    }
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
        && InteragationRoomController.getSuspectsHaveBeenTalkedTo()) { // TO DO: &&
      // chatController.getSuspectHasBeenTalkedTo()
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
      artCurratorHmm =
          new Media(App.class.getResource("/sounds/Curatorhmmm.mp3").toURI().toString());
      thiefHmm = new Media(App.class.getResource("/sounds/HOShuh.mp3").toURI().toString());
      janitorHmm = new Media(App.class.getResource("/sounds/janhmmm.mp3").toURI().toString());

      // Check if any Media is null
      if (artCurratorHmm == null || thiefHmm == null || janitorHmm == null) {
        throw new IllegalArgumentException("Failed to load one or more sound files.");
      }
    } catch (URISyntaxException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
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
    InteragationRoomController.setIsChatOpened(false);
    App.setRoot("room");
  }

  public static void resetSuspectsTalkedToMap() {
    suspectHasBeenTalkedToMap.put("Art Currator", false);
    suspectHasBeenTalkedToMap.put("Art Thief", false);
    suspectHasBeenTalkedToMap.put("Janitor", false);
  }
}
