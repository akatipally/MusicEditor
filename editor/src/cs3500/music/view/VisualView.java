package cs3500.music.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import javax.sound.midi.MetaEventListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


import cs3500.music.commons.Note;
import cs3500.music.model.IViewModel;

/**
 * A VisualView is an implementation of IMusicView that displays a Song in GUI form.
 * It contains three different Panels that have the Note drawings and the labels for beats and
 * notes with scroll bars around them, laid into a Frame.
 */

public class VisualView extends JFrame implements IGuiView {
  JPanel p;
  NoteLabelsPanel noteLabelsPanel;
  BeatsPanel beatsPanel;
  NotesPanel notesPanel;
  JScrollPane scrollNotesPane;
  MouseListener mouse;
  KeyListener keys;
  IViewModel viewModel;
  private int beat;
  private boolean paused;

  /**
   * Constructor for a GUI view that takes in the ViewModel that holds in all information of the
   * constructed song.
   *
   * @param viewModelIn The viewModel for the song that is being rendered.
   */
  public VisualView(IViewModel viewModelIn) {
    super();
    viewModel = viewModelIn;
    this.p = new JPanel(new BorderLayout());
    notesPanel = new NotesPanel(viewModel, beat);
    beatsPanel = new BeatsPanel(viewModel);
    noteLabelsPanel = new NoteLabelsPanel(viewModel);
    this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    JPanel container = new JPanel();
    container.setLayout(new BorderLayout());
    container.add(notesPanel, BorderLayout.CENTER);
    container.add(beatsPanel, BorderLayout.NORTH);
    container.add(noteLabelsPanel, BorderLayout.WEST);
    scrollNotesPane = new JScrollPane(container);
    this.getContentPane().add(scrollNotesPane, BorderLayout.CENTER);
    Note highestNote = viewModel.getHighestNote();
    Note lowestNote = viewModel.getLowestNote();
    int numberOfDistinctNotes = 0;
    try {
      numberOfDistinctNotes = highestNote.notesBetweenTwoNotes(lowestNote);
    } catch (Exception e) {
      numberOfDistinctNotes = 0;
    }
    int numberOfBeats = viewModel.getEndBeat();
    System.out.println(numberOfBeats);
    this.pack();
    // Below are seemingly random numbers -
    // they were chosen because they're the combination that bests renders our notes.
    notesPanel.setPreferredSize(new Dimension(numberOfBeats * 37, numberOfDistinctNotes * 31 + 20));
    beatsPanel.setPreferredSize(new Dimension(numberOfBeats * 37, 10));
    noteLabelsPanel.setPreferredSize(new Dimension(30, numberOfDistinctNotes * 31 + 20));
    this.setSize(new Dimension(numberOfBeats * 37 + 50, numberOfDistinctNotes * 31 + 75));
  }

  @Override
  public void initialize() {
    this.setVisible(true);
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(700, 700);
  }

  @Override
  public void renderSong(IViewModel model, int tempo) throws IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException("Invalid View Model!");
    } else {
      this.initialize();
    }
  }

  @Override
  public void goBeginSong() {
    scrollNotesPane.getHorizontalScrollBar().setValue(0);
  }

  @Override
  public void goEndSong() {
    int end = scrollNotesPane.getHorizontalScrollBar().getMaximum();
    scrollNotesPane.getHorizontalScrollBar().setValue(end);
  }

  @Override
  public void scrollRight() {
    int currentPosition = scrollNotesPane.getHorizontalScrollBar().getValue();
    scrollNotesPane.getHorizontalScrollBar().setValue(currentPosition + 100);
  }

  @Override
  public void scrollLeft() {
    int currentPosition = scrollNotesPane.getHorizontalScrollBar().getValue();
    scrollNotesPane.getHorizontalScrollBar().setValue(currentPosition - 100);
  }

  @Override
  public void scrollUp() {
    int currentPosition = scrollNotesPane.getVerticalScrollBar().getValue();
    scrollNotesPane.getVerticalScrollBar().setValue(currentPosition - 100);
  }

  @Override
  public void scrollDown() {
    int currentPosition = scrollNotesPane.getVerticalScrollBar().getValue();
    scrollNotesPane.getVerticalScrollBar().setValue(currentPosition + 100);
  }

  @Override
  public void pausePlay() {
    // does not do anything
  }

  @Override
  public double getDimensionX() {
    return this.getPreferredSize().getWidth();
  }

  @Override
  public double getDimensionY() {
    return this.getPreferredSize().getHeight();
  }

  @Override
  public void refresh(boolean paused) {
    if (!paused) {
      this.viewModel.incrementBeat();
    }
    this.notesPanel.setBeat(viewModel.getCurrBeat());
    this.resizeWindow();
    this.repaint();
    this.notesPanel.repaint();
    // scroll the bar when red line reaches end of panel
    if (notesPanel.redLinePos % (30) == 0) {
      int currentPosition = scrollNotesPane.getHorizontalScrollBar().getValue();
      scrollNotesPane.getHorizontalScrollBar().setValue(currentPosition + 30);
    }
  }

  @Override
  public VisualView getVisual() {
    return this;
  }

  /**
   * Resizes the window if more measures were added.
   */
  public void resizeWindow() {
    Note highestNote = viewModel.getHighestNote();
    Note lowestNote = viewModel.getLowestNote();
    int numberOfDistinctNotes = highestNote.notesBetweenTwoNotes(lowestNote);
    int numberOfBeats = viewModel.getEndBeat();
    notesPanel.setPreferredSize(new Dimension(numberOfBeats * 37, numberOfDistinctNotes * 31 + 20));
    beatsPanel.setPreferredSize(new Dimension(numberOfBeats * 37, 10));
    noteLabelsPanel.setPreferredSize(new Dimension(30, numberOfDistinctNotes * 31 + 20));
  }

  @Override
  public AudibleView getAudible() {
    throw new IllegalArgumentException("No view here.");
  }

  @Override
  public void setMouseListener(MouseListener mouse) {
    this.mouse = mouse;
    this.notesPanel.addMouseListener(mouse);
  }

  @Override
  public void setKeyboardListener(KeyListener keys) {
    this.keys = keys;
    this.addKeyListener(keys);
    this.notesPanel.addKeyListener(keys);
  }

  @Override
  public void setMetaListener(MetaEventListener listener) {
    // does not do anything
  }

  @Override
  public boolean getPaused() {
    throw new IllegalArgumentException("You can't do that!");
  }

}
