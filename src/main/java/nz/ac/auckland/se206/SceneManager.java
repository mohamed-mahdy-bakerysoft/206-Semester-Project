package nz.ac.auckland.se206;

import java.io.IOException;
import java.util.HashMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * The SceneManager class is responsible for managing different UI scenes within the application. It
 * loads scenes on demand and caches them for future use.
 */
public class SceneManager {

  public enum AppUi {
    ROOM,
    ROOM2,
    ROOM3,
    START,
    CUTSCENE,
    GOOD_END,
    BAD_END,
    CAMERA,
    BIN,
    MAP,
    INTELROOM,
    INTELROOMONE,
    INTELROOMTWO,
    INTELROOMTHREE
  }

  private static HashMap<AppUi, Parent> sceneMap = new HashMap<>();

  /**
   * Retrieves the root node of the UI scene associated with the specified AppUi enum constant. If
   * the scene is not already loaded, it loads it from the corresponding FXML file.
   *
   * @param appUi the enum constant representing the UI scene to retrieve
   * @return the root node of the UI scene
   * @throws IOException if the FXML file is not found
   */
  public static Parent getUiRoot(AppUi appUi) throws IOException {
    if (!sceneMap.containsKey(appUi)) {
      // Load the FXML file associated with appUi
      String fxmlFileName = getFxmlFileName(appUi);
      FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/" + fxmlFileName + ".fxml"));
      Parent uiRoot = loader.load();
      sceneMap.put(appUi, uiRoot);
    }
    return sceneMap.get(appUi);
  }

  private static String getFxmlFileName(AppUi appUi) {
    switch (appUi) {
      case ROOM:
        return "room";
      case ROOM2:
        return "room2";
      case ROOM3:
        return "room3";
      case START:
        return "start";
      case CUTSCENE:
        return "cutscenes";
      case GOOD_END:
        return "goodending";
      case BAD_END:
        return "badending";
      case CAMERA:
        return "clue1";
      case BIN:
        return "clue2";
      case MAP:
        return "clue3";
      case INTELROOM:
        return "Intel_Draft";
      case INTELROOMONE:
        return "IntelRoomOne";
      case INTELROOMTWO:
        return "IntelRoomTwo";
      case INTELROOMTHREE:
        return "IntelRoomThree";
      default:
        throw new IllegalArgumentException("Unknown AppUi: " + appUi);
    }
  }

  /**
   * Changes the current scene to the specified AppUi.
   *
   * @param appUi the enum constant representing the UI scene to switch to
   * @throws IOException if the FXML file is not found
   */
  public static void setScene(AppUi appUi) throws IOException {
    Parent root = getUiRoot(appUi);
    Scene scene = App.getScene();
    if (scene == null) {
      scene = new Scene(root);
      App.getPrimaryStage().setScene(scene);
    } else {
      scene.setRoot(root);
    }
  }
}
