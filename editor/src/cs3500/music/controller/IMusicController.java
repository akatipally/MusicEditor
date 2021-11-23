package cs3500.music.controller;

/**
 * Controller to work with Music Editor and the different views. This controller consists of a
 * model and any IGuIView, although it is primarily designed to handle a Composite view.
 * The controller also has three different event handlers, for keys, clicks, and meta messages
 * which all work to take in user input and keep the model and view consistent with one another.
 * A viewmodel is also used to keep up the barrier between the model and view being aware
 * of each others existance.
 */
public interface IMusicController {

  /**
   * This method will set up the keyboardHandler for the controller, and install the runnables
   * which our implementation uses for key events. It can be easily extended and implemented
   * for clients to install their own runnables.
   */
  void createKeyboardHandler();

  /**
   * This method sets up the mouseHandler for the controller. Installs runnables just like the
   * key handler. It can also be extended for clients to add their runnables.
   */
  void createMouseHandler();

  /**
   * This method sets up the metaEventHandler, which is what keeps the two parts of a composite
   * view in sync.
   */
  void createMetaHandler();

  /**
   * The method that takes care of adding rests to the end of a song.
   */
  void addRest();

  /**
   * The method that deals with click coordinates, using their locations to figure out what note
   * was clicked and how to deal with it depending on what the method finds there in the model.
   * @param x The x coordinate clicked.
   * @param y The y coordinate clicked.
   */
  void onClick(int x, int y);

}
