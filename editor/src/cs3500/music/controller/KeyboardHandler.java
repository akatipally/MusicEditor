package cs3500.music.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to handle keyboard actions. Has maps for all the possible
 * runnables that can be installed for all possible key actions.
 */
public class KeyboardHandler implements KeyListener {
  Map<Integer, Runnable> keyTyped;
  Map<Integer, Runnable> keyPressed;
  Map<Integer, Runnable> keyReleased;

  /**
   * The constructor that initalizes a KeyboardHandler.
   */
  public KeyboardHandler() {
    keyTyped = new HashMap<Integer, Runnable>();
    keyPressed = new HashMap<Integer, Runnable>();
    keyReleased = new HashMap<Integer, Runnable>();
  }

  @Override
  public void keyTyped(KeyEvent e) {
    if (keyTyped.containsKey(e.getKeyCode())) {
      keyTyped.get(e.getKeyCode()).run();
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if (keyPressed.containsKey(e.getKeyCode())) {
      keyPressed.get(e.getKeyCode()).run();
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    if (keyReleased.containsKey(e.getKeyCode())) {
      keyReleased.get(e.getKeyCode()).run();
    }
  }

  /**
   * Allow clients of your class (such as your cs3500.music.controller) to install Runnables.
   * for the various key events that are of interest.
   * @param i the integer value in the map to put it at
   * @param r the runnable to add at the event
   * @param a the type of keyboard action to work with
   */
  public void installRunnable(Integer i, Runnable r, ActionType a) {
    switch (a) {
      case TYPED:
        keyTyped.put(i, r);
        break;
      case PRESSED:
        keyPressed.put(i, r);
        break;
      case RELEASED:
        keyReleased.put(i, r);
        break;
      default:
        break;
    }
  }

  /**
   * The three different kinds of actions that a keyevent can take on.
   */
  public enum ActionType {
    TYPED, PRESSED, RELEASED
  }
}

