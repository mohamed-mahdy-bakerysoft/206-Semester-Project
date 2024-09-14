package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import nz.ac.auckland.apiproxy.exceptions.ApiProxyException;
import nz.ac.auckland.se206.DraggableMaker;

public class BinClueController extends ClueController {
  @FXML private Button backButton;
  @FXML private Label mins;
  @FXML private Label secs;
  @FXML private ImageView Rubbish1;
  @FXML private ImageView Rubbish2;
  @FXML private ImageView Rubbish3;
  @FXML private ImageView Rubbish4;
  @FXML private ImageView Rubbish5;
  @FXML private ImageView Keycard;

  DraggableMaker draggableMaker = new DraggableMaker();

  @Override
  public void initialize() throws ApiProxyException {
    super.initialize();
    draggableMaker.makeDraggable(Rubbish1);
    draggableMaker.makeDraggable(Rubbish2);
    draggableMaker.makeDraggable(Rubbish3);
    draggableMaker.makeDraggable(Rubbish4);
    draggableMaker.makeDraggable(Rubbish5);
    draggableMaker.makeDraggable(Keycard);
  }
}
