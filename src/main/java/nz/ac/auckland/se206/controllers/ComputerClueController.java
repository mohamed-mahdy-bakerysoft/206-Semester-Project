package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;

public class ComputerClueController extends ClueController {
  @FXML private Rectangle rectOpenEmail;
  @FXML private Rectangle rectEscape;
  @FXML private Rectangle rectBackToInbox;
  @FXML private Rectangle rectInternetExplorer;

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
}
