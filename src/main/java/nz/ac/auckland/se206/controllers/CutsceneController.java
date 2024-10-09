package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.TimeManager;

public class CutsceneController {
  public static TimeManager timeManager =
      TimeManager.getInstance(); // Singleton instance of TimeManager

  public CutsceneController() {
    this.currentDialogueIndex = 0;
    this.leftProgressBar = new ProgressBar();
    this.rightProgressBar = new ProgressBar();
    this.dialogueLines = new ArrayList<>();
  }

  @FXML private ImageView aliceImage;

  @FXML private Label dialogueText;
  @FXML private Label mins;
  @FXML private Label dot;
  @FXML private Label secs;

  @FXML private Button btnNext;

  @FXML private Button btnSkip;

  @FXML private ImageView newspaperImage;
  @FXML private ImageView paintingImage;
  @FXML private ImageView williamImage;
  @FXML private ImageView frankImage;
  @FXML private ImageView johnImage;

  @FXML private ProgressBar leftProgressBar;
  @FXML private ProgressBar rightProgressBar;


  private List<String> dialogueLines;
  private int currentDialogueIndex;
  private Timeline progressTimeline;

  // This method initializes the cutscene by loading Alice's dialogue
  @FXML
  public void initialize() {
      timeManager.setTimerLabel(mins, secs, dot);
    
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

    // Set initial progress bar values (full)
    leftProgressBar.setProgress(1.0);
    rightProgressBar.setProgress(1.0);

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

  // Method to automatically update progress bars and advance the dialogue
  private void startProgressBarAutoSkip() {
    progressTimeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(0.05),
                event -> {
                  // Reduce progress on both sides
                  leftProgressBar.setProgress(leftProgressBar.getProgress() - 0.01);
                  rightProgressBar.setProgress(rightProgressBar.getProgress() - 0.01);

                  // If progress reaches 0, automatically move to the next dialogue
                  if (leftProgressBar.getProgress() <= 0.0) {
                    onNextDialogue();
                  }
                }));

    // Set the cycle count so it stops after both bars reach zero
    progressTimeline.setCycleCount(100); // Adjust as needed for timing
    progressTimeline.play();
  }

  private void displayNextDialogue() {
    // Reset the progress bars for each new dialogue
    leftProgressBar.setProgress(1.0);
    rightProgressBar.setProgress(1.0);

    // Start the progress bars animation for auto-skipping
    startProgressBarAutoSkip();

    // Display the current dialogue text
    dialogueText.setText(dialogueLines.get(currentDialogueIndex));

    // Control image visibility based on current dialogue index
    switch (currentDialogueIndex) {
      case 1: // After Alice introduces the suspects
        newspaperImage.setVisible(true);
        paintingImage.setVisible(true);
        break;
      case 2:
        newspaperImage.setVisible(false);
        paintingImage.setVisible(false);
        break;
      case 3:
        frankImage.setVisible(true);
        break;
      case 4:
        williamImage.setVisible(true);
        break;
      case 5:
        johnImage.setVisible(true);
        break;
      case 6:
        frankImage.setVisible(false);
        williamImage.setVisible(false);
        johnImage.setVisible(false);
        break;
      default:
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

  /**
   * Handles the "Enter" key press to skip the current dialogue.
   *
   * @param event the KeyEvent triggered by pressing a key
   * @throws IOException if there is an I/O error
   */
  @FXML
  private void onHandleEnterKey(KeyEvent event) throws IOException {
    if (event.getCode() == KeyCode.ENTER && !btnNext.isDisabled()) {
      if (progressTimeline != null) {
        progressTimeline.stop(); // Stop the auto-skip progress bar timeline
      }
      onNextDialogue(); // Trigger the next dialogue
    }
  }

  // This method handles the transition to the actual game start scene
  private void startGame() {
    if (progressTimeline != null) {
      progressTimeline.stop(); // Stop the timeline if it's running
    }

    try {
      App.setRoot("room"); // Assuming 'room' is the first game scene
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
