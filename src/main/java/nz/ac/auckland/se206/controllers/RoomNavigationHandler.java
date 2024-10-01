package nz.ac.auckland.se206.controllers;

import java.io.IOException;

public interface RoomNavigationHandler {
  void goToRoom(String roomName) throws IOException;
}
