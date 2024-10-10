package nz.ac.auckland.se206.speech;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

/**
 * The MP3Player class is responsible for playing MP3 audio files in a separate thread. It provides
 * methods to play and stop the audio.
 */
public class MP3Player {

  private final String filename;
  private AdvancedPlayer player;
  private Thread playerThread;
  private boolean playing; // Flag to track if the player is currently playing

  /**
   * Constructs an MP3Player with the specified filename.
   *
   * @param filename the path to the MP3 file to be played
   */
  public MP3Player(String filename) {
    this.filename = filename;
    this.playing = false; // Initialize playing state to false
  }

  /**
   * Plays the MP3 file in a separate thread. If the file is not found or cannot be played, an
   * exception is printed to the console.
   */
  public void play() {
    try {
      FileInputStream fis = new FileInputStream(filename);
      BufferedInputStream bis = new BufferedInputStream(fis);
      player = new AdvancedPlayer(bis);

      playerThread =
          new Thread(
              () -> {
                try {
                  playing = true; // Set playing state to true when playback starts
                  player.play();
                } catch (JavaLayerException e) {
                  e.printStackTrace();
                } finally {
                  playing = false; // Set playing state to false when playback finishes
                }
              });

      playerThread.start();
    } catch (FileNotFoundException | JavaLayerException e) {
      e.printStackTrace();
      playing = false; // Ensure playing state is false if an exception occurs
    }
  }

  /** Stops the currently playing MP3 file, if any, and interrupts the player thread. */
  public void stop() {
    if (player != null) {
      player.close(); // Close the player to stop the audio
    }
    if (playerThread != null) {
      playerThread.interrupt(); // Interrupt the thread if it's running
    }
    playing = false; // Ensure the playing state is set to false
  }

  /**
   * Returns whether the MP3 file is currently playing.
   *
   * @return true if the MP3 file is playing, false otherwise
   */
  public boolean isPlaying() {
    return playing;
  }
}
