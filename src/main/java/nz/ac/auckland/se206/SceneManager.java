package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.scene.Parent;

/**
 * The SceneManager class is responsible for managing different UI scenes within the application. It
 * allows storing, retrieving, and switching between various scenes based on an enum that represents
 * different parts of the application.
 */
public class SceneManager {

  /**
   * The AppUi enum defines different scenes in the application. Each enum constant corresponds to a
   * specific view or part of the user interface.
   */
  public enum AppUi {
    ROOM,
    CHAT,
    START,
    CUTSCENE,
    GOOD_END,
    BAD_END,
    CAMERA,
    BIN,
    MAP,
    INTELROOM, // General intel room
    INTELROOMONE, // Room for Art Currator
    INTELROOMTWO, // Room for Art Thief
    INTELROOMTHREE // Room for Janitor
  }

  private static HashMap<AppUi, Parent> sceneMap = new HashMap<>();

  /**
   * Adds a UI scene to the scene map. Each scene is associated with an AppUi enum constant, which
   * can later be used to retrieve the corresponding scene.
   *
   * @param appUi the enum constant representing the UI scene
   * @param uiRoot the root node of the scene to be added
   */
  public static void addUi(AppUi appUi, Parent uiRoot) {
    sceneMap.put(appUi, uiRoot);
  }

  /**
   * Retrieves the root node of the UI scene associated with the specified AppUi enum constant.
   *
   * @param appUi the enum constant representing the UI scene to retrieve
   * @return the root node of the UI scene, or null if the scene has not been added
   */
  public static Parent getUiRoot(AppUi appUi) {
    return sceneMap.get(appUi);
  }
}
