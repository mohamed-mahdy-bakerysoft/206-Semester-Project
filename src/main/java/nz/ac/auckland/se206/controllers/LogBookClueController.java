package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;

public class LogBookClueController extends ClueController {

  @FXML private Rectangle rectLogBook;
  @FXML private Rectangle rectPageTurnRight;
  @FXML private Rectangle rectPageTurnRight2;
  @FXML private Rectangle rectPageTurnLeft;
  @FXML private Rectangle rectPageTurnLeft2;

  @Override
  public void initialize() throws ApiProxyException {
    super.initialize();
  }

  public void handleTurnPageClickRight(MouseEvent event) throws IOException {
    Rectangle clicked = (Rectangle) event.getSource();
    if (clicked.getId().equals("rectPageTurnRight")) {
      App.setRoot("logbookclue2");
    } else if (clicked.getId().equals("rectPageTurnRight2")) {
      App.setRoot("logbookclue3");
    }
  }

  public void handleTurnPageClickLeft(MouseEvent event) throws IOException {
    // if (context.getCurrentState()
    //     instanceof Guessing) { // if in guessing phase, the other areas should not be accessible
    // for
    //   // investigation
    //   return;
    // }
    Rectangle clicked = (Rectangle) event.getSource();
    if (clicked.getId().equals("rectPageTurnLeft")) {
      App.setRoot("logbookclue1");
    } else if (clicked.getId().equals("rectPageTurnLeft2")) {
      App.setRoot("logbookclue2");
    }
  }
}
