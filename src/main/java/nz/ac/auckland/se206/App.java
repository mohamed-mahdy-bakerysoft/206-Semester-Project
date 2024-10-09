package nz.ac.auckland.se206;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
// import InteragationRoomController
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.controllers.InteragationRoomController;

/**
 * This is the entry point of the JavaFX application. This class initializes and runs the JavaFX
 * application.
 */
public class App extends Application {

  private static Scene scene;
  private static Map<String, Parent> sceneCache = new HashMap<>();
  private static Map<String, InteragationRoomController> controllerCache = new HashMap<>();

  /**
   * The main method that launches the JavaFX application.
   *
   * @param args the command line arguments
   */
  public static void main(final String[] args) {
    launch();
  }

  /**
   * Sets the root of the scene to the specified FXML file.
   *
   * @param fxml the name of the FXML file (without extension)
   * @throws IOException if the FXML file is not found
   */
  public static void setRoot(String fxml) throws IOException {
    scene.setRoot(loadFxml(fxml));
  }

  public static void setSameRoot(Parent root) {
    scene.setRoot(root);
  }

  public static void openChat(MouseEvent event, String profession)
      throws IOException, URISyntaxException {
    String sceneKey = event.getSource().toString();

    Parent root;
    InteragationRoomController chatController;

    // Check if the scene is already cached
    if (sceneCache.containsKey(sceneKey)) {
      root = sceneCache.get(sceneKey);
      chatController = controllerCache.get(sceneKey);
      chatController.setTime();
    } else {
      FXMLLoader loader = null;
      System.out.println("ID In app Is: " + profession);
      switch (profession) {
        case "rectRoomOne":
          loader = new FXMLLoader(App.class.getResource("/fxml/IntelRoomOne.fxml"));
          break;
        case "rectRoomTwo":
          loader = new FXMLLoader(App.class.getResource("/fxml/IntelRoomTwo.fxml"));
          break;
        case "rectRoomThree":
          loader = new FXMLLoader(App.class.getResource("/fxml/IntelRoomThree.fxml"));
          break;
        default:
          System.err.println("Id is " + profession + " so error");
          return;
      }
      root = loader.load();

      chatController = loader.getController();

      // Cache the loaded scene and controller
      sceneCache.put(sceneKey, root);
      controllerCache.put(sceneKey, chatController);
    }

    scene.setRoot(root);
  }

  /**
   * Loads the FXML file and returns the associated node. The method expects that the file is
   * located in "src/main/resources/fxml".
   *
   * @param fxml the name of the FXML file (without extension)
   * @return the root node of the FXML file
   * @throws IOException if the FXML file is not found
   */
  private static Parent loadFxml(final String fxml) throws IOException {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml")).load();
  }

  /**
   * Shows the ending scene based on the provided ending type.
   *
   * @param ending the type of ending ("good_ending" or "bad_ending")
   * @throws IOException if the FXML file for the ending is not found
   */
  public static void showEnding(String ending) throws IOException {
    switch (ending) {
      case "good_ending":
        // Set the root to the good ending scene
        setRoot("goodending");
        break;
      case "bad_ending":
        // Set the root to the bad ending scene
        setRoot("badending");
        break;
      default:
        // Throw an exception if the ending type is unknown
        throw new IllegalArgumentException("Unknown ending type: " + ending);
    }
  }

  // make a method that returns the current roots controller
  public static InteragationRoomController getController() {
    return controllerCache.get(scene.toString());
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "room" scene.
   *
   * @param stage the primary stage of the application
   * @throws IOException if the "src/main/resources/fxml/room.fxml" file is not found
   */
  @Override
  public void start(final Stage stage) throws IOException {
    // add icon to the window:
    stage
        .getIcons()
        .add(new javafx.scene.image.Image(App.class.getResourceAsStream("/images/icon.png")));

    SceneManager.addUi(AppUi.START, loadFxml("start"));
    SceneManager.addUi(AppUi.CUTSCENE, loadFxml("cutscenes"));
    SceneManager.addUi(AppUi.ROOM, loadFxml("room"));
    SceneManager.addUi(AppUi.GOOD_END, loadFxml("goodending"));
    SceneManager.addUi(AppUi.BAD_END, loadFxml("badending"));
    SceneManager.addUi(AppUi.CAMERA, loadFxml("clue1"));
    SceneManager.addUi(AppUi.BIN, loadFxml("clue2"));
    SceneManager.addUi(AppUi.MAP, loadFxml("clue3"));
    // Added Intel room Here
    // Corridor
    SceneManager.addUi(AppUi.INTELROOM, loadFxml("Intel_Draft"));
    // RoomOne
    SceneManager.addUi(AppUi.INTELROOMONE, loadFxml("IntelRoomOne"));
    // RoomTwo
    SceneManager.addUi(AppUi.INTELROOMTWO, loadFxml("IntelRoomTwo"));
    // RoomThree
    SceneManager.addUi(AppUi.INTELROOMTHREE, loadFxml("IntelRoomThree"));

    scene = new Scene(SceneManager.getUiRoot(AppUi.START));
    stage.setScene(scene);
    stage.setHeight(730);
    stage.setWidth(1100);
    stage.show();
  }
}
