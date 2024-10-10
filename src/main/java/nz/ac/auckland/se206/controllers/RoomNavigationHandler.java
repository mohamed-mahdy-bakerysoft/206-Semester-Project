package nz.ac.auckland.se206.controllers;

import java.io.IOException;

/** Interface for handling navigation between rooms in the game. */
public interface RoomNavigationHandler {
  void goToRoom(String roomName) throws IOException;
}
