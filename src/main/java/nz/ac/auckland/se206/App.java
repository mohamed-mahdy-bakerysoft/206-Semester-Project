package nz.ac.auckland.se206;

import java.io.IOException;
import java.net.URISyntaxException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.controllers.ChatController;

/**
 * This is the entry point of the JavaFX application. This class initializes and runs the JavaFX
 * application.
 */
public class App extends Application {

  private static Scene scene;

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
   * Opens the chat view and sets the profession in the chat controller.
   *
   * @param event the mouse event that triggered the method
   * @param profession the profession to set in the chat controller
   * @throws IOException if the FXML file is not found
   * @throws URISyntaxException if there is an error with the URI syntax
   */
  public static void openChat(MouseEvent event, String profession)
      throws IOException, URISyntaxException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/chat.fxml"));
    Parent root = loader.load();

    ChatController chatController = loader.getController();
    chatController.setProfession(profession);
    chatController.displaySuspectOnChat();
    scene.setRoot(root);
  }

  public static void showEnding(String ending) throws IOException {
    switch (ending) {
      case "good_ending":
        setRoot("goodending");
        break;
      case "bad_ending":
        setRoot("badending");
        break;
    }
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "room" scene.
   *
   * @param stage the primary stage of the application
   * @throws IOException if the "src/main/resources/fxml/room.fxml" file is not found
   */
  @Override
  public void start(final Stage stage) throws IOException {
    SceneManager.addUi(AppUi.START, loadFxml("start"));
    SceneManager.addUi(AppUi.ROOM, loadFxml("room"));
    SceneManager.addUi(AppUi.CHAT, loadFxml("chat"));
    SceneManager.addUi(AppUi.GOOD_END, loadFxml("goodending"));
    SceneManager.addUi(AppUi.BAD_END, loadFxml("badending"));
    SceneManager.addUi(AppUi.CAMERA, loadFxml("clue1"));
    SceneManager.addUi(AppUi.BIN, loadFxml("clue2"));
    SceneManager.addUi(AppUi.MAP, loadFxml("clue3"));

    scene = new Scene(SceneManager.getUiRoot(AppUi.START));
    stage.setScene(scene);
    stage.show();
  }
}
