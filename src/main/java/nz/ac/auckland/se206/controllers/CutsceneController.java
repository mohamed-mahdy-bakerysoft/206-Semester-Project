package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;

public class CutsceneController {

  @FXML private ImageView aliceImage;

  @FXML private Label dialogueText;

  @FXML private Button btnNext;

  @FXML private Button btnSkip;

  @FXML private ImageView newspaperImage;
  @FXML private ImageView paintingImage;
  @FXML private ImageView williamImage;
  @FXML private ImageView frankImage;
  @FXML private ImageView johnImage;

  private List<String> dialogueLines;
  private int currentDialogueIndex;

  // This method initializes the cutscene by loading Alice's dialogue
  @FXML
  public void initialize() {
    currentDialogueIndex = 0;

    // List of dialogue lines for Alice and the player
    dialogueLines = new ArrayList<>();
    dialogueLines.add(
        "Alice: Welcome Agent I am Inspector A, your mentor. We have a case to solve.");
    dialogueLines.add(
        "Alice: Your mission is to find out who stole this famous painting created by the late"
            + "owner of the George St Art gallery Teressa Harris.");
    dialogueLines.add("Alice: There are 3 suspects you need to investigate");
    dialogueLines.add("Alice: Frank the art Curator, son of the late artist Teresa Harris.");
    dialogueLines.add("Alice: William, the head of security of the gallery.");
    dialogueLines.add("Alice: And John the Janitor, known to be an ex-convict.");
    dialogueLines.add("Alice: Chat with all of these suspects and find clues in the crime scene.");
    dialogueLines.add("Alice: Good luck Agent, the fate of the painting is in your hands.");

    // Display the first line of dialogue
    displayNextDialogue();
  }

  // This method displays the next dialogue in the sequence
  @FXML
  private void onNextDialogue() {
    // Check if there are more dialogue lines to display
    if (currentDialogueIndex < dialogueLines.size()) {
      displayNextDialogue();
    } else {
      // Once the dialogue ends, proceed to the game scene
      startGame();
    }
  }

  // This method skips the cutscene and goes directly to the game
  @FXML
  private void onSkipCutscene() throws IOException {
    startGame(); // Directly transition to the game
  }

  private void displayNextDialogue() {
    // Display the current dialogue text
    dialogueText.setText(dialogueLines.get(currentDialogueIndex));

    // Control image visibility based on current dialogue index
    switch (currentDialogueIndex) {
      case 1: // After Alice introduces the suspects
        // Make the newspaper image visible
        newspaperImage.setVisible(true);
        paintingImage.setVisible(true);
        break;
      case 2: // After Alice introduces Frank
        newspaperImage.setVisible(false);
        paintingImage.setVisible(false);
        break;
      case 3: // After Alice introduces the curator
        frankImage.setVisible(true);
        break;
      case 4: // After Alice introduces William
        williamImage.setVisible(true);
        break;
      case 5: // After Alice introduces John
        johnImage.setVisible(true);
        break;
      case 6: // After Alice finishes the introduction
        frankImage.setVisible(false);
        williamImage.setVisible(false);
        johnImage.setVisible(false);
        break;

      default:
        // Hide all other images by default
        newspaperImage.setVisible(false);
        paintingImage.setVisible(false);
        williamImage.setVisible(false);
        frankImage.setVisible(false);
        johnImage.setVisible(false);
        break;
    }

    // Increment the index for the next line
    currentDialogueIndex++;

    // Disable the "Next" button during dialogue transitions for smoother flow
    btnNext.setDisable(true);

    // Add a slight pause before enabling the "Next" button again
    PauseTransition pause = new PauseTransition(Duration.seconds(2)); // Adjust duration if needed
    pause.setOnFinished(event -> btnNext.setDisable(false));
    pause.play();
  }

  // This method handles the transition to the actual game start scene
  private void startGame() {
    try {
      App.setRoot("room"); // Assuming 'room' is the first game scene
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
