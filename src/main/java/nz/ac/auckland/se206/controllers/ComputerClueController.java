package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;

public class ComputerClueController extends ClueController {

  private static int passwordGuesses = 0;
  public static boolean passwordHasBeenGuessed = false;

  public static boolean getPasswordHasBeenGuessed() {
    return passwordHasBeenGuessed;
  }

  private static void incrementPasswordGuesses() {
    passwordGuesses++;
  }

  @FXML private Rectangle rectOpenEmail;
  @FXML private Rectangle rectEscape;
  @FXML private Rectangle rectBackToInbox;
  @FXML private Rectangle rectInternetExplorer;
  @FXML private TextField passwordTxtField;
  @FXML private ImageView passwordHint;

  @Override
  public void initialize() throws ApiProxyException {
    super.initialize();
  }

  public void handleComputerClick(MouseEvent event) throws IOException {
    Rectangle clicked = (Rectangle) event.getSource();
    switch (clicked.getId()) {
      case "rectInternetExplorer":
      case "rectBackToInbox":
        App.setRoot("computerclue2");
        break;
      case "rectOpenEmail":
        App.setRoot("computerclue3");
        break;
      case "rectEscape":
        App.setRoot("computerclue1");
        break;
    }
  }

  public void handlePassword(MouseEvent event) throws IOException {
    incrementPasswordGuesses();
    if (passwordTxtField.getText().equals("williamthegoat")) {
      App.setRoot("computerclue1");
      passwordHasBeenGuessed = true;
    } else if (passwordGuesses >= 2
        && !passwordTxtField
            .getText()
            .equals("")) { // if not empty and user has guessed more than twice
      passwordTxtField.clear();
      passwordHint.setVisible(true);
    }
  }

  public void handleEnter(KeyEvent keyEvent) throws IOException {
    if (keyEvent.getCode().toString().equals("ENTER")) {
      handlePassword(null);
    }
  }
}
