package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionRequest;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionResult;
import nz.ac.auckland.apiproxy.chat.openai.ChatMessage;
import nz.ac.auckland.apiproxy.chat.openai.Choice;
import nz.ac.auckland.apiproxy.config.ApiProxyConfig;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.TimeManager;
import nz.ac.auckland.se206.prompts.PromptEngineering;
import nz.ac.auckland.se206.speech.TextToSpeech;

/**
 * Controller class for the chat view. Handles user interactions and communication with the GPT
 * model via the API proxy.
 */
public class ChatController {

  private static boolean isFirstTimeInit = true;
  private static boolean suspectHasBeenTalkedTo = false;
  private static Map<String, Boolean> suspectHasBeenTalkedToMap = new HashMap<>();
  private static Map<String, String> professionToNameMap = new HashMap<>();

  /**
   * Returns whether the suspect has been talked to.
   *
   * @return true if the suspect has been talked to, false otherwise
   */
  public static boolean getSuspectHasBeenTalkedTo() {
    return suspectHasBeenTalkedTo;
  }

  @FXML private TextArea txtaChat;
  @FXML private TextField txtInput;
  @FXML private Button btnSend;
  @FXML private ImageView suspect1;
  @FXML private ImageView suspect2;
  @FXML private ImageView suspect3;
  @FXML private Label mins;
  @FXML private Label secs;

  private ChatCompletionRequest chatCompletionRequest;
  private String profession;
  private MediaPlayer player;
  private Media artStudentHmm;
  private Media thiefHmm;
  private Media grumpyTouristHmm;

  /** Initializes the chat view. */
  @FXML
  public void initialize() {
    if (isFirstTimeInit) {
      initializeSuspectTalkedToMap();
      initializeRoleToNameMap();
      isFirstTimeInit = false;
    }
    initializeSounds();
    TimeManager timeManager = TimeManager.getInstance();
    timeManager.setTimerLabel(mins, secs);
  }

  private void initializeSuspectTalkedToMap() {
    suspectHasBeenTalkedToMap.put("Curious Art Student", false);
    suspectHasBeenTalkedToMap.put("Art Thief", false);
    suspectHasBeenTalkedToMap.put("Grumpy Out of Town Tourist", false);
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
    return PromptEngineering.getPrompt(promptFile, map);
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

  /**
   * Sets the profession for the chat context and initializes the ChatCompletionRequest.
   *
   * @param profession the profession to set
   * @throws URISyntaxException
   */
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
            } else {
              playHmmSound(profession);
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

  // Populate this map based on your suspects and user
  private void initializeRoleToNameMap() {
    professionToNameMap.put("Curious Art Student", "Jessica");
    professionToNameMap.put("Art Thief", "William");
    professionToNameMap.put("Grumpy Out of Town Tourist", "Johnson");
    professionToNameMap.put("user", "Investigator"); // for the initial message
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

    // Check if player was successfully created before playing
    if (player != null) {
      player.play();
    } else {
      System.err.println("Hmm sound not available for " + profession);
    }
  }

  /**
   * Appends a chat message to the chat text area.
   *
   * @param msg the chat message to append
   */
  private void appendChatMessage(ChatMessage msg) {
    // Play "hmm" sound if TTS has already been used for this suspect
    if (!msg.getRole().equals("user") && suspectHasBeenTalkedToMap.get(profession)) {
      playHmmSound(profession);
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

  /**
   * Navigates back to the previous view.
   *
   * @param event the action event triggered by the go back button
   * @throws ApiProxyException if there is an error communicating with the API proxy
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onGoBack(ActionEvent event) throws ApiProxyException, IOException {
    App.setRoot("room");
    hideSuspectOnChat();
  }

  /** Displays the suspect on the chat view. */
  @FXML
  public void displaySuspectOnChat() {
    switch (profession) {
      case "Curious Art Student":
        suspect1.setVisible(true);
        break;
      case "Art Thief":
        suspect2.setVisible(true);
        break;
      case "Grumpy Out of Town Tourist":
        suspect3.setVisible(true);
        break;
    }
  }

  /** Hides the suspect on the chat view. */
  @FXML
  public void hideSuspectOnChat() {
    switch (profession) {
      case "Curious Art Student":
        suspect1.setVisible(false);
        break;
      case "Art Thief":
        suspect2.setVisible(false);
        break;
      case "Grumpy Out of Town Tourist":
        suspect3.setVisible(false);
        break;
    }
  }
}
