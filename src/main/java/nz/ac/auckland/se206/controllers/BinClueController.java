package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.DraggableMaker;

/**
 * The BinClueController class controls the functionality of the scene where the player interacts
 * with clues from a bin. It handles draggable objects (like rubbish) and keycard interactions
 * within the scene.
 */
public class BinClueController extends ClueController {

  /** Button to navigate back to the previous scene. */
  @FXML private Button backButton;

  /** Labels for displaying the countdown timer minutes and seconds. */
  @FXML private Label mins;

  @FXML private Label secs;

  /** ImageView elements representing the draggable rubbish objects and keycard in the bin. */
  @FXML private ImageView rubbish1;

  @FXML private ImageView rubbish2;
  @FXML private ImageView rubbish3;
  @FXML private ImageView rubbish4;
  @FXML private ImageView rubbish5;
  @FXML private ImageView keyCard;
  @FXML private ImageView keyCard2;

  /** Rectangle element used to highlight the backdrop when the keycard is found. */
  @FXML private Rectangle rectBackDrop;

  /** Instance of DraggableMaker to enable dragging behavior for the rubbish objects. */
  private DraggableMaker draggableMaker = new DraggableMaker();

  /**
   * Initializes the bin clue scene by making the rubbish objects draggable. This method is called
   * automatically after the FXML components have been loaded.
   *
   * @throws ApiProxyException if there is an issue initializing API proxy-related operations
   */
  @Override
  public void initialize() throws ApiProxyException {
    // Call the initialize method from the superclass ClueController
    super.initialize();

    // Make the first rubbish object draggable
    draggableMaker.makeDraggable(rubbish1);

    // Make the second rubbish object draggable
    draggableMaker.makeDraggable(rubbish2);

    // Make the third rubbish object draggable
    draggableMaker.makeDraggable(rubbish3);

    // Make the fourth rubbish object draggable
    draggableMaker.makeDraggable(rubbish4);

    // Make the fifth rubbish object draggable
    draggableMaker.makeDraggable(rubbish5);
  }

  /**
   * Handles the event when the keycard is clicked, making the keycard and its backdrop visible.
   *
   * @param event the mouse event triggered by clicking the keycard
   * @throws IOException if there is an issue navigating to another scene
   */
  public void handleKeyCard(MouseEvent event) throws IOException {
    rectBackDrop.setVisible(true);
    keyCard2.setVisible(true);
  }

  /**
   * Handles the event when the back button is clicked, hiding the keycard and its backdrop.
   *
   * @param event the mouse event triggered by clicking the back button
   * @throws IOException if there is an issue navigating to another scene
   */
  public void handleClickBack(MouseEvent event) throws IOException {
    rectBackDrop.setVisible(false);
    keyCard2.setVisible(false);
  }
}
