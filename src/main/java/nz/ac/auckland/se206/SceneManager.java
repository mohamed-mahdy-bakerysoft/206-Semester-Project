package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.scene.Parent;

public class SceneManager {
  public enum AppUi {
    ROOM,
    CHAT,
    START,
    GOOD_END,
    BAD_END,
    CAMERA,
    BIN,
    MAP,
    // Added Intel room Here as ENUM
    // Corridor
    INTELROOM,
    // RoomOne
    INTELROOMONE,
    // RoomTwo
    INTELROOMTWO,
    // RoomThree
    INTELROOMTHREE
  }

  private static HashMap<AppUi, Parent> sceneMap = new HashMap<AppUi, Parent>();

  public static void addUi(AppUi appUi, Parent uiRoot) {
    sceneMap.put(appUi, uiRoot);
  }

  public static Parent getUiRoot(AppUi appUi) {
    return sceneMap.get(appUi);
  }
}
