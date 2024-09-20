package nz.ac.auckland.se206;

import javafx.scene.Node;

/**
 * The DraggableMaker class allows making JavaFX nodes draggable by mouse events. It captures the
 * mouse press and drag events to update the position of the node.
 */
public class DraggableMaker {
  private double mouseAnchorX;
  private double mouseAnchorY;

  /**
   * Makes the specified JavaFX node draggable. When the node is pressed and dragged by the mouse,
   * its layout position will be updated.
   *
   * @param node the JavaFX node to make draggable
   */
  public void makeDraggable(Node node) {
    node.setOnMousePressed(
        mouseEvent -> {
          // Capture the initial position of the mouse relative to the node
          mouseAnchorX = mouseEvent.getX();
          mouseAnchorY = mouseEvent.getY();
        });

    node.setOnMouseDragged(
        mouseEvent -> {
          // Update the layout position of the node based on the mouse drag
          node.setLayoutX(mouseEvent.getSceneX() - mouseAnchorX);
          node.setLayoutY(mouseEvent.getSceneY() - mouseAnchorY);
        });
  }
}
