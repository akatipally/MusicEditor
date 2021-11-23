package cs3500.music.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;

/**
 * Class to handle mouse actions.
 */
public class MouseHandler implements MouseListener {

  // the x coordinate of the click
  private int x;
  // the y coordinate of the click
  private int y;

  Runnable runnable;

  /**
   * The constructor for a mouseHandler.
   */
  public MouseHandler() {
    this.x = -1;
    this.y = -1;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    // check if left click
    if (SwingUtilities.isLeftMouseButton(e)) {
      x = e.getX();
      y = e.getY();
      runnable.run();
    }
  }

  @Override
  public void mousePressed(MouseEvent e) {
    //No mousepressed events needed.
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    //No mousereleased events needed.
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    //No mouseentered events needed.
  }

  @Override
  public void mouseExited(MouseEvent e) {
    //No mouseexited events needed.
  }

  /**
   * Method that allows a client to install their desired functionality to the mouseHandler.
   * @param r The runnable.
   */
  public void installRunnable(Runnable r) {
    this.runnable = r;
  }

  /**
   * Method to get the x coordinate clicked.
   * @return The x coordinate.
   */
  public int getX() {
    return this.x;
  }

  /**
   * Method to get the y coordinate clicked.
   * @return The y coordinate.
   */
  public int getY() {
    return this.y;
  }

}
