package cs3500.music.view;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import javax.sound.midi.MetaEventListener;

/**
 * Interface for GUI-specific methods that make no sense for the other view types,
 * such as methods for dealing with keyboard and mouse.
 */
public interface IGuiView extends IMusicView {

  /**
   * To set the mouse listener for the view.
   * @param mouse The mouse listener set.
   */
  void setMouseListener(MouseListener mouse);

  /**
   * To set the keyboard listener for the view.
   * @param keys The key listener set.
   */
  void setKeyboardListener(KeyListener keys);

  /**
   * To set the meta listener for the view.
   * @param listener The meta listener set.
   */
  void setMetaListener(MetaEventListener listener);

  /**
   * Goes to the beginning of the song in the view (not audio).
   */
  void goBeginSong();

  /**
   * Goes to the end of the song in the view (not audio).
   */
  void goEndSong();

  /**
   * Scrolls right by 100 pixels on the visual view.
   */
  void scrollRight();

  /**
   * Scrolls left by 100 pixels on the visual view.
   */
  void scrollLeft();

  /**
   * Scrolls up by 100 pixels on the visual view.
   */
  void scrollUp();

  /**
   * Scrolls down by 100 pixels on the visual view.
   */
  void scrollDown();

  /**
   * Pauses or plays the song.
   */
  void pausePlay();

  /**
   * Gives the horizontal length of the view.
   * @return the X dimension as a double
   */
  double getDimensionX();

  /**
   * Gives the vertical length of the view.
   * @return the Y dimension as a double
   */
  double getDimensionY();

  /**
   * Refreshed the view, based on if its paused or not.
   * @param paused if the view is paused
   */
  void refresh(boolean paused);

  /**
   * Gets the Visual view.
   * @return the visual view
   */
  VisualView getVisual();

  /**
   * Gets the Audible view.
   * @return the audible view
   */
  AudibleView getAudible();

  /**
   * Gets the pause state of the song.
   * @return true if it is paused
   */
  boolean getPaused();
}
