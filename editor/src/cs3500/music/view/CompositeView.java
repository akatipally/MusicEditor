package cs3500.music.view;


import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;

import cs3500.music.model.IViewModel;

/**
 * A view that takes in both a GuiViewImpl and a MidiViewImpl. It should be able to start, pause
 * and resume playback of the sound, and it should draw a red line that sweeps across marking
 * the current beat. Once the current beat reaches the right-hand edge of the window, the whole
 * window should scroll to bring the next measures of the composition into view.
 */
public class CompositeView implements IGuiView {
  IGuiView visualView;
  AudibleView audibleView;

  /**
   * The constructor for a Composite View object.
   * @param visualView the visual view to work with
   * @param audibleView the audible view to work with
   */
  public CompositeView(IGuiView visualView, AudibleView audibleView) {
    this.visualView = visualView;
    this.audibleView = audibleView;
  }

  @Override
  public void setMouseListener(MouseListener mouse) {
    this.visualView.setMouseListener(mouse);
  }

  @Override
  public void setKeyboardListener(KeyListener keys) {
    this.visualView.setKeyboardListener(keys);
  }

  @Override
  public void setMetaListener(MetaEventListener listener) {
    this.audibleView.acceptMetaListener(listener);
  }

  @Override
  public void goBeginSong() {
    this.visualView.goBeginSong();
  }

  @Override
  public void goEndSong() {
    this.visualView.goEndSong();
  }

  @Override
  public void scrollRight() {
    this.visualView.scrollRight();
  }

  @Override
  public void scrollLeft() {
    this.visualView.scrollLeft();
  }

  @Override
  public void scrollUp() {
    this.visualView.scrollUp();
  }

  @Override
  public void scrollDown() {
    this.visualView.scrollDown();
  }

  @Override
  public void pausePlay() {
    this.audibleView.pausePlay();
  }

  @Override
  public void initialize() {
    //Doesnt do anything for a composite view.
  }

  @Override
  public void renderSong(IViewModel model, int tempo)
          throws InvalidMidiDataException, IllegalArgumentException {
    visualView.renderSong(model, tempo);
    tempo = tempo * 4;
    audibleView.renderSong(model, tempo);
  }

  @Override
  public double getDimensionX() {
    return visualView.getDimensionX();
  }

  @Override
  public double getDimensionY() {
    return visualView.getDimensionY();
  }

  @Override
  public void refresh(boolean paused) {
    this.visualView.refresh(paused);
    if (paused) {
      try {
        this.audibleView.refresh(paused);
      } catch (InvalidMidiDataException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public VisualView getVisual() {
    return (VisualView) this.visualView;
  }

  @Override
  public AudibleView getAudible() {
    return this.audibleView;
  }

  @Override
  public boolean getPaused() {
    return (this.audibleView.getPaused());
  }
}
