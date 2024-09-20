package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.App;

public class LogBookClueController extends ClueController {

  @FXML private Rectangle rectLogBook;
  @FXML private Rectangle rectPageTurnRight;
  @FXML private Rectangle rectPageTurnRight2;
  @FXML private Rectangle rectPageTurnLeft;
  @FXML private Rectangle rectPageTurnLeft2;
  private MediaPlayer player;
  private Media sound;

  @Override
  public void initialize() throws ApiProxyException {
    super.initialize();
    try {
      sound = new Media(App.class.getResource("/sounds/turningpage.mp3").toURI().toString());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    player = new MediaPlayer(sound);
  }

  public void handleTurnPageClickRight(MouseEvent event) throws IOException {
    Rectangle clicked = (Rectangle) event.getSource();
    if (clicked.getId().equals("rectPageTurnRight")) {
      App.setRoot("logbookclue2");
    } else if (clicked.getId().equals("rectPageTurnRight2")) {
      App.setRoot("logbookclue3");
    }
    player.play();
  }

  public void handleTurnPageClickLeft(MouseEvent event) throws IOException {
    Rectangle clicked = (Rectangle) event.getSource();
    if (clicked.getId().equals("rectPageTurnLeft")) {
      App.setRoot("logbookclue1");
    } else if (clicked.getId().equals("rectPageTurnLeft2")) {
      App.setRoot("logbookclue2");
    }
    player.play();
  }
}
