package nz.ac.auckland.se206.controllers;

import java.io.IOException; // Add this import for IOException
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
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

// improt java fxml text

public class SubmitAnswerController {
  private static String feed;
  private static String thief;
  private static String answer;
  private static boolean isFirstTime = true;
  public static TimeManager timeManager = TimeManager.getInstance();

  // make getters for feed and thief
  public static String getFeed() {
    return feed;
  }

  public static String getThief() {
    return thief;
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

  // make a getter for answer
  public static String getAnswer() {
    return answer;
  }

  // make a setter for isfirst time
  public static void setIsFirstTime(boolean isFirstTime) {
    SubmitAnswerController.isFirstTime = isFirstTime;
  }

  public void initialize() {

    if (isFirstTime == true) {

      timeManager.stopTimer();
      timeManager.setInterval(60);
    }
    timeManager.startTimer();
    timeManager.setTimerLabel(mins, secs);
    // GameStateContext context = RoomController.getGameContext();
    // context.setState(context.getGuessingState());
  }

  public void sendAnswer() {
    timeManager.stopTimer();
    System.err.println(thief);
    if (answerTxtArea.getText().isEmpty()) {
      return;
    } else {
      Map<String, String> map = new HashMap<>();
      map.put("answer", answerTxtArea.getText());
      map.put("thief", thief);
      System.out.println("Thief: " + thief);

      System.out.println("Answer submitted: " + answerTxtArea.getText());
      intizliaseAndGpt(map);
    }
  }

  public static Map<String, String> intiateanswer() {
    Map<String, String> map = new HashMap<>();
    map.put("answer", answer);
    map.put("thief", thief);
    System.out.println("Thief: " + thief);

    System.out.println("Answer submitted: " + answer);
    return map;
  }

  @FXML
  private void savefeedback() {
    answer = answerTxtArea.getText();
    System.out.println("Answer: " + answer);
  }

  @FXML
  private void appendfeedback() {
    if (thief.equals("hos")) {
      feedback.appendText(feed);
    } else if (thief.equals("curator")) {
      feedback2.appendText(feed);
    } else if (thief.equals("janitor")) {
      feedback2.appendText(feed);
    } else {
      feedback2.appendText("Feedback not avaiable");
    }
  }

  @FXML
  private void handleRectangleClick(MouseEvent event) throws IOException, URISyntaxException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    thief = clickedRectangle.getId();
    isFirstTime = false;
    App.setRoot("submitanswer");
  }

  private String getSystemPrompt(Map<String, String> data) {
    if (thief.equals("hos")) {
      System.out.println("hos Toouched......................");
      return PromptEngineering.getPrompt("win.txt", data);
    } else if (thief.equals("curator")) {
      System.out.println("curator Toouched......................");
      return PromptEngineering.getPrompt("loss.txt", data);
    } else if (thief.equals("janitor")) {
      System.out.println("janitor Toouched......................");
      return PromptEngineering.getPrompt("loss.txt", data);
    }
    System.err.println("error");
    return "error";
  }

  private ChatCompletionRequest chatCompletionRequest;

  public void intizliaseAndGpt(Map<String, String> data) {
    try {
      ApiProxyConfig config = ApiProxyConfig.readConfig();
      chatCompletionRequest =
          new ChatCompletionRequest(config)
              .setN(1)
              .setTemperature(0.2)
              .setTopP(0.5)
              .setMaxTokens(100);
      runGpt(new ChatMessage("system", getSystemPrompt(data)));
      System.out.println("stuff about to run here........................");
      System.out.println("thief is: " + thief);
      if (thief.equals("janitor")) {
        App.setRoot("badending");
      } else if (thief.equals("hos")) {
        App.setRoot("goodending2");
      } else if (thief.equals("curator")) {
        App.setRoot("badending");

      } else {
        System.err.println("error");
      }
    } catch (ApiProxyException e) {
      e.printStackTrace();
    } catch (IOException e) { // Add this catch block for IOException
      e.printStackTrace();
    }
  }

  private String runGpt(ChatMessage msg)
      throws ApiProxyException, IOException { // Fixed IOException import
    chatCompletionRequest.addMessage(msg);
    try {
      ChatCompletionResult chatCompletionResult = chatCompletionRequest.execute();
      Choice result = chatCompletionResult.getChoices().iterator().next();
      chatCompletionRequest.addMessage(result.getChatMessage());

      // Extract the content as a String
      String messageContent = result.getChatMessage().getContent();

      // set meesage content onto the text feedback

      feed = messageContent;
      EndingController.setFeed(feed);
      EndingController.setThief(thief);
      System.out.println("Message content: " + feed);
      // Return the content as a string
      return messageContent;

    } catch (ApiProxyException e) {
      e.printStackTrace();
      return null;
    }
  }

  // Rest function for the submit answer controller
  public static void reset() {
    feed = null;
    thief = null;
    answer = null;
    isFirstTime = true; // Reset to ensure the timer logic behaves correctly
  }
}
