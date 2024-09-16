package nz.ac.auckland.se206.controllers;

import java.io.IOException; // Add this import for IOException
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
import nz.ac.auckland.se206.prompts.PromptEngineering;

// improt java fxml text

public class SubmitAnswerController {
  private static String feed;
  private static String thief;

  @FXML private Button submitButton;
  @FXML private Button viewfeedback;
  @FXML private Button viewfeedback2;
  @FXML private TextArea answerTxtArea;
  @FXML private Rectangle janitor;
  @FXML private Rectangle hos;
  @FXML private Rectangle curator;
  @FXML private TextArea feedback;
  @FXML private TextArea feedback2;

  public void initialize() {}

  public void sendAnswer() {
    System.err.println(thief);
    Map<String, String> map = new HashMap<>();
    map.put("answer", answerTxtArea.getText());
    map.put("thief", thief);
    System.out.println("Thief: " + thief);

    System.out.println("Answer submitted: " + answerTxtArea.getText());
    intizliaseAndGpt(map);
  }

  @FXML
  private void appendfeedback() {
    if (thief.equals("hos")) {
      feedback.appendText(feed);
    } else if (thief.equals("curator")) {
      feedback2.appendText(feed);
    } else if (thief.equals("janitor")) {
      feedback2.appendText(feed);
    }
  }

  @FXML
  private void handleRectangleClick(MouseEvent event) throws IOException, URISyntaxException {
    Rectangle clickedRectangle = (Rectangle) event.getSource();
    thief = clickedRectangle.getId();

    App.setRoot("submitanswer");
  }

  private String getSystemPrompt(Map<String, String> data) {
    return PromptEngineering.getPrompt("winorloss.txt", data);
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
      System.err.println("stuff about to run here........................");
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
      System.out.println("Message content: " + feed);
      // Return the content as a string
      return messageContent;

    } catch (ApiProxyException e) {
      e.printStackTrace();
      return null;
    }
  }
}
