package cs3500.music.controller;

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;

import cs3500.music.view.AudibleView;
import cs3500.music.view.VisualView;

/**
 * A class for a Meta Event Handler to implement MetaEventListener interface.
 */
public class MetaEventHandler implements MetaEventListener {

  VisualView visualView;
  AudibleView audibleView;
  Runnable runnable;

  /**
   * Constructor for a metaEventHandler which keeps the views in sync, using the reference of time
   * obtained from the audible view.
   * @param visualView The visualview that has the red line being updated.
   * @param audibleView The audiblew view.
   */
  public MetaEventHandler(VisualView visualView, AudibleView audibleView) {
    this.visualView = visualView;
    this.audibleView = audibleView;
  }

  @Override
  public void meta(MetaMessage meta) {
    runnable.run();
  }

  /**
   * Method to install the functionality that the eventhandler will use.
   * @param r The runnable.
   */
  public void installRunnable(Runnable r) {
    this.runnable = r;
  }
}
