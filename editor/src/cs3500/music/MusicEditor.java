package cs3500.music;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.sound.midi.InvalidMidiDataException;

import cs3500.music.controller.MusicController;
import cs3500.music.model.IMusicEditor;
import cs3500.music.model.IViewModel;
import cs3500.music.model.ViewModel;
import cs3500.music.util.CompositionBuilder;
import cs3500.music.util.MusicBuilder;
import cs3500.music.util.MusicReader;
import cs3500.music.view.AudibleView;
import cs3500.music.view.CompositeView;
import cs3500.music.view.IGuiView;
import cs3500.music.view.IMusicView;
import cs3500.music.view.VisualView;

/**
 * This is the main runtime class of the entire editor. To use it, the main method must be run
 * with the arguments of the path to the music file to play as well as the argument for what
 * kind of view to use. The options are 'console', 'midi' and 'visual', which display console,
 * audio, and graphical views respectively.
 */

public class MusicEditor {

  private static IViewModel viewModel;

  /**
   * The MusicEditor constructor creates the Editor, MusicBuilder, and ViewModel which are the
   * necessary components to building a view.
   */
  public MusicEditor() {
    CompositionBuilder<IMusicEditor> builder;
    IMusicEditor editor;
    editor = new cs3500.music.model.MusicEditor();
    builder = new MusicBuilder();
    editor = builder.build();
    MusicEditor.viewModel = new ViewModel(editor, 0 , 4, editor.getTempo());
  }

  /**
   * The main method for running our program.
   * @param args the arguments given to the program
   * @throws IOException if invalid input or output
   * @throws InvalidMidiDataException if invalid MIDI data
   */
  public static void main(String[] args) throws IOException, InvalidMidiDataException {
    FileReader fileName = null;
    try {
      fileName = new FileReader(args[0]);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    MusicReader reader = new MusicReader();
    CompositionBuilder builder = new MusicBuilder();
    reader.parseFile(fileName, builder);
    IMusicEditor editor = (cs3500.music.model.MusicEditor) builder.build();
    IViewModel model = new ViewModel(editor, 0, 4, editor.getTempo());
    IMusicView view = null;
    if (args[1].equals("console")) {
      view = IMusicView.create("console", model);
    } else if (args[1].equals("visual")) {
      view = IMusicView.create("visual", model);
    } else if (args[1].equals("midi")) {
      view = IMusicView.create("midi", model);
    } else if (args[1].equals("composite")) {
      IGuiView visual = new VisualView(model);
      AudibleView audio = new AudibleView(model);
      IGuiView compositeView = new CompositeView(visual, audio);
      MusicController controller = new MusicController(editor, compositeView);
      try {
        compositeView.initialize();
        compositeView.renderSong(model, model.getTempo());
      } catch (InvalidMidiDataException e) {
        e.printStackTrace();
      }
    } else {
      throw new InvalidMidiDataException("Invalid input!");
    }
    if (!args[1].equals("composite")) {
      try {
        view.initialize();
        view.renderSong(model, model.getTempo());
      } catch (InvalidMidiDataException e) {
        e.printStackTrace();
      }
    }
  }
}