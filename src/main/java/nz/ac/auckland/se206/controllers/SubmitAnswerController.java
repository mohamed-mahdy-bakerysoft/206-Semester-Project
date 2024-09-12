package nz.ac.auckland.se206.controllers;

import java.io.IOException; // Add this import for IOException
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionRequest;
import nz.ac.auckland.apiproxy.chat.openai.ChatCompletionResult;
import nz.ac.auckland.apiproxy.chat.openai.ChatMessage;
import nz.ac.auckland.apiproxy.chat.openai.Choice;
import nz.ac.auckland.apiproxy.config.ApiProxyConfig;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.prompts.PromptEngineering;

public class SubmitAnswerController {

  @FXML private Button submitButton;
  @FXML private TextArea answerTxtArea;

  public void initialize() {}

  public void sendAnswer() {
    Map<String, String> map = new HashMap<>();
    map.put("answer", answerTxtArea.getText());

    System.out.println("Answer submitted: " + answerTxtArea.getText());
    intizliaseAndGpt(map);
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
      if (messageContent == null) {
        System.out.println("No content in the message");
      } else if (messageContent.contains("True")) {
        System.out.println("You win");
        App.setRoot("goodEnding");
      } else if (messageContent.contains("False")) {
        App.setRoot("badEnding");
      } else {
        System.out.println("IDK whats wrong");
      }

      // Return the content as a string
      return messageContent;

    } catch (ApiProxyException e) {
      e.printStackTrace();
      return null;
    }
  }
}
