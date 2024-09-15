package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.TimeManager;

public class ClueController {

  @FXML private Button backButton;
  @FXML private Label mins;
  @FXML private Label secs;

  /**
   * Initializes the clue view.
   *
   * @throws ApiProxyException if there is an error communicating with the API proxy
   */
  @FXML
  public void initialize() throws ApiProxyException {
    TimeManager timeManager = TimeManager.getInstance();
    timeManager.setTimerLabel(mins, secs);
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
    Button clickedButton = (Button) event.getSource();
    if (clickedButton.getId().equals("backButton2")) {
      App.setRoot("room2");
    } else if (clickedButton.getId().equals("backButton3")) {
      App.setRoot("room3");
    } else {
      App.setRoot("room");
    }
  }

  @FXML
  private void handleInitialLogBookClick(MouseEvent event) throws IOException {
    Rectangle clicked = (Rectangle) event.getSource();
    if (clicked.getId().equals("rectLogBook")) {
      App.setRoot("logbookclue1");
    }
  }

  @FXML
  private void handleInitialComputerClick() throws IOException {
    App.setRoot("computerclue1");
  }
}
