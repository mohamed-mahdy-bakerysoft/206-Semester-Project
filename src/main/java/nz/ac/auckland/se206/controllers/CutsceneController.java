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

  private List<String> dialogueLines;
  private int currentDialogueIndex;

  // This method initializes the cutscene by loading Alice's dialogue
  @FXML
  public void initialize() {
    currentDialogueIndex = 0;

    // List of dialogue lines for Alice and the player
    dialogueLines = new ArrayList<>();
    dialogueLines.add(
        "Alice: Hi there, investigator. I'm Alice. I've been following this case closely.");
    dialogueLines.add(
        "Alice: We have three key suspects: an art curator, a janitor, and a notorious art thief.");
    dialogueLines.add("Player: Can you tell me more about them?");
    dialogueLines.add("Alice: Sure. The curator has a clean record, but he's hiding something.");
    dialogueLines.add(
        "Alice: The janitor is a bit suspicious too, always sneaking around the building.");
    dialogueLines.add("Alice: And the art thief... well, he's a known criminal, but he's smart.");
    dialogueLines.add("Player: Sounds complicated. I'll need to investigate them all.");

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

  // This method displays the next dialogue in the list with a slight pause
  private void displayNextDialogue() {
    dialogueText.setText(dialogueLines.get(currentDialogueIndex));

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
