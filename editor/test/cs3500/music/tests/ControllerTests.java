package cs3500.music.tests;

import org.junit.Test;


import java.awt.event.KeyEvent;

import javax.sound.midi.MetaMessage;

import cs3500.music.commons.Note;
import cs3500.music.commons.Octave;
import cs3500.music.commons.Pitch;
import cs3500.music.controller.IMusicController;
import cs3500.music.controller.KeyboardHandler;
import cs3500.music.controller.MetaEventHandler;
import cs3500.music.controller.MouseHandler;
import cs3500.music.controller.MusicController;
import cs3500.music.model.IMusicEditor;
import cs3500.music.model.IViewModel;
import cs3500.music.model.MusicEditor;
import cs3500.music.model.ViewModel;
import cs3500.music.util.CompositionBuilder;
import cs3500.music.view.AudibleView;
import cs3500.music.view.CompositeView;
import cs3500.music.view.IGuiView;
import cs3500.music.view.VisualView;

import static org.junit.Assert.assertEquals;

/**
 * To test the Controller and Composite View Functionality.
 */
public class ControllerTests {

  private static IViewModel viewModel;
  IMusicController controller;
  StringBuilder a = new StringBuilder();
  IGuiView visualView;
  AudibleView audibleView;
  /**
   * To initialize the data.
   */

  private void initialize() {
    CompositionBuilder<IMusicEditor> builder;
    IMusicEditor editor;
    editor = new MusicEditor();
    editor.createNewSheet();
    viewModel = new ViewModel(editor, 0 , 4, editor.getTempo());
    visualView = new VisualView(viewModel);
    audibleView = new AudibleView(viewModel);
    IGuiView compositeView = new CompositeView(visualView, audibleView);
    controller = new MusicController(editor, compositeView);
    MouseHandler mouse = new MouseHandler();
    compositeView.setMouseListener(mouse);
    mouse.installRunnable(mouseTester);
    KeyboardHandler keys = new KeyboardHandler();
    compositeView.setKeyboardListener(keys);
    keys.installRunnable(0, keyTester, KeyboardHandler.ActionType.TYPED);
    MetaEventHandler meta = new MetaEventHandler((VisualView) visualView, audibleView);
    compositeView.setMetaListener(meta);
    meta.installRunnable(metaTester);
  }


  Runnable mouseTester = () -> this.confirmMouse();

  Runnable keyTester = () -> this.confirmKeys();

  Runnable metaTester = () -> this.confirmMeta();

  // This tests if a keyboard handler can call the proper runnable based on a key event
  @Test
  public void testKeyHandler() {
    this.initialize();
    StringBuilder testString = new StringBuilder();
    KeyboardHandler keyboardHandler = new KeyboardHandler();
    Runnable addToString = () -> testString.append("The Runnable works by the Keyboard Handler!");
    keyboardHandler.installRunnable(KeyEvent.VK_T, addToString, KeyboardHandler.ActionType.PRESSED);
    keyboardHandler.keyPressed(new KeyEvent(new VisualView(viewModel), 0, 0, 0,
            KeyEvent.VK_T, 't', 0));
    assertEquals(testString.toString(), "The Runnable works by the Keyboard Handler!");
  }

  // This tests if a mouse event is fired
  @Test
  public void testMouse() {
    initialize();
    controller.onClick(5, 5);
    assertEquals(this.a.toString().contains("The mouse event fired."), true);
  }

  // This tests if a meta event was sent
  @Test
  public void testMeta() {

    assertEquals(this.a.toString().contains("The meta message was sent."), true);
  }

  public void confirmMouse() {
    this.a.append("The mouse event fired.");
  }

  public void confirmKeys() {
    this.a.append("The key event fired.");
  }

  public void confirmMeta() {
    this.a.append("The meta message was sent.");
  }

  // to test if a meta handler completes the required actions
  @Test
  public void testMetaHandler() {
    this.initialize();
    StringBuilder testString = new StringBuilder();
    VisualView visualView = new VisualView(viewModel);
    AudibleView audibleView = new AudibleView(viewModel);
    MetaEventHandler metaEventHandler = new MetaEventHandler(visualView, audibleView);
    Runnable addToString = () -> testString.append("The Runnable works by the Meta Handler!");
    metaEventHandler.installRunnable(addToString);
    MetaMessage metaMessage = new MetaMessage();
    metaEventHandler.meta(metaMessage);
    assertEquals(testString.toString(), "The Runnable works by the Meta Handler!");
  }

  // to test adding a rest to a song by the controller successfully adds 4 beats to the song
  @Test
  public void testAddRest() {
    this.initialize();
    IMusicEditor editor = new MusicEditor();
    editor.createNewSheet();
    viewModel = new ViewModel(editor, 0 , 4, editor.getTempo());
    IGuiView visualView = new VisualView(viewModel);
    AudibleView audibleView = new AudibleView(viewModel);
    IGuiView compositeView = new CompositeView(visualView, audibleView);
    editor.addSingleNote(0, new Note(Pitch.A, Octave.EIGHT, true, 3, 4), 5, 0);
    controller = new MusicController(editor, compositeView);
    assertEquals(editor.getBeats(0).size(), 5);
    controller.addRest();
    assertEquals(editor.getBeats(0).size(), 9);
  }

  // to test clicking on a notes panel by the controller successfully adds a note to the song
  @Test
  public void testOnClick() {
    this.initialize();
    IMusicEditor editor = new MusicEditor();
    editor.createNewSheet();
    viewModel = new ViewModel(editor, 0 , 4, editor.getTempo());
    IGuiView visualView = new VisualView(viewModel);
    AudibleView audibleView = new AudibleView(viewModel);
    IGuiView compositeView = new CompositeView(visualView, audibleView);
    editor.addSingleNote(0, new Note(Pitch.A, Octave.EIGHT, true, 3, 4), 5, 0);
    editor.addSingleNote(0, new Note(Pitch.A, Octave.SEVEN, true, 3, 4), 5, 0);
    controller = new MusicController(editor, compositeView);
    assertEquals(editor.allNotes().size(), 10);
    try {
      if (visualView.getPaused()) {
        controller.onClick(100, 50);
      }
    } catch (IllegalArgumentException i) {
      System.out.println("Song is not paused!");
    }
    controller.onClick(100, 50);
    assertEquals(editor.allNotes().size(), 11);
  }
}
