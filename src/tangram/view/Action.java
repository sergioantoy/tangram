// Copyright 1996-2008  Sergio Antoy
// revision 032008

package tangram.view;

import static tangram.model.Model.MODEL;
import static tangram.xml.GameWriter.GAME_WRITER;
import static tangram.view.About.ABOUT;
import static tangram.view.Sorry.SORRY;
import static tangram.view.HowTo.HOWTO;
import static tangram.view.Cache.CACHE;
import static tangram.view.PlayView.PLAY_VIEW;
import static tangram.view.GoalView.GOAL_VIEW;
import static tangram.listener.MouseHandler.MOUSE_HANDLER;

import java.util.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.applet.*;
import javax.swing.*;

/** This class executes actions that requested via the menu
 *  and/or via the keyboard.
 */
public class Action {

  /** It's a singleton. */
  private Action () {}
  /** The instance. */
  public static final Action ACTION = new Action ();

  // ------------------------------------------------------------------
  // The resources.  
  // There is no much reason to code setters and getters !?

  private Hashtable <String, Image> imageTable
    = new Hashtable <String, Image> ();
  public void setImageTable(Hashtable <String, Image> imageTable) {
    this.imageTable = imageTable;
  }
  public Hashtable <String, Image> getImageTable() {
    return imageTable;
  }

  private AudioClip sound;
  public void setSound(AudioClip sound) { this.sound = sound; }
  public AudioClip getSound() { return sound; }

  private URL document;
  public void setDocument(URL document) { this.document = document; }
  public URL getDocument() { return document; }

  private JCheckBoxMenuItem alwaysEasy;
  public void setAlwaysEasy(JCheckBoxMenuItem alwaysEasy) {
    this.alwaysEasy = alwaysEasy;
  }
  public JCheckBoxMenuItem getAlwaysEasy() { return alwaysEasy; }

  private JCheckBoxMenuItem quiet;
  public void setQuiet(JCheckBoxMenuItem quiet) { this.quiet = quiet; }
  public JCheckBoxMenuItem getQuiet() { return quiet; }

  private boolean isApplet;
  public void setIsApplet(boolean isApplet) { this.isApplet = isApplet; }
  public boolean getIsApplet() { return isApplet; }

  private JToggleButton easy;
  public void setEasy(JToggleButton easy) { this.easy = easy; }

  private JToggleButton solve;
  public void setSolve(JToggleButton solve) { this.solve = solve; }

  private JMenuItem delete;
  public void setDelete(JMenuItem delete) { this.delete = delete; }

  private ButtonGroup group ;
  public void setGroup(ButtonGroup group) { this.group = group; }

  private JFrame frame;
  public void setFrame(JFrame frame) { this.frame = frame; }

  // ------------------------------------------------------------------
  // The "real" listeners that do the work.
  // The indirection is needed because of the scope rule.

  private void modelDefault() {
    MODEL.xdefault();
    // just resetted values
    alwaysEasy.setSelected(MODEL.isAlwaysEasy());
    easy.setSelected(MODEL.isEasy());
    solve.setSelected(MODEL.isSolve());
    quiet.setSelected(MODEL.isQuiet());
    delete.setEnabled(false);
    if (! isApplet) frame.setTitle(MODEL.getCurrentGame().gameName);
  }

  public void open (String gameName) {
    modelDefault();
    MODEL.setCurrentGame(gameName);
    GOAL_VIEW.repaint ();
    PLAY_VIEW.repaint ();
    if (! isApplet) frame.setTitle(gameName);
  }
  public void alwaysEasy (boolean choice) { 
    MODEL.alwaysEasy (choice);
    alwaysEasy.setSelected (choice);
    PLAY_VIEW.repaint ();
  }

  public void easy () { 
    MODEL.toggleEasy ();
    easy.setSelected (MODEL.isEasy());
    PLAY_VIEW.repaint ();
  }

  public void quiet (boolean choice) { MODEL.setQuiet (choice); }
  public void quit () { if (! isApplet) System.exit(1); }
  public void xdefault () { 
    modelDefault();
    String gameName = MODEL.getCurrentGame().gameName;
    for (Enumeration <AbstractButton> e = group.getElements();
	 e.hasMoreElements(); ) {
      AbstractButton button = e.nextElement();	 
      if (gameName.equals(button.getText())) {
	button.setSelected (true);
	break;
      }
    }
    GOAL_VIEW.repaint ();
    PLAY_VIEW.repaint ();
  }
  public void about () { ABOUT.show (); }
  public void howto () { HOWTO.show (); }

  public void solve () { 
    MODEL.toggleSolve ();
    solve.setSelected (MODEL.isSolve());
    GOAL_VIEW.repaint();
 }

  /** Add the configuration in the play board as a goal.
   *  The addition only affects the current execution.
   *  To make it permanent, use "save" in the "goals" menu.
   *  Must run as an application, not as an applet.
   */
  public void addConfig() { 
    if (CACHE.isAddable()) {
      CACHE.addConfig();
      next();
    }
    else SORRY.show("Sorry, I cannot handle this goal.");
  }

  /** Delete the goal displayed in the Control panel.
   *  Show another goal.
   *  Cannot be the initial goal.
   *  Must run as an application, not as an applet.
   *  Repaint goal view and play board (for "easy").
   */
  public void deleteConfig() {
    // It cannot be goal # 0
    MODEL.deleteConfig(); 
    easy.setSelected(MODEL.isEasy());
    solve.setSelected(MODEL.isSolve());
    delete.setEnabled(MODEL.getGoalNumber() != 0);
    GOAL_VIEW.repaint();
    PLAY_VIEW.repaint ();
  }

  /** Save the configurations.
   *  Will not work for applets.
   */
  public void save() {
    try { GAME_WRITER.saveGame(); }
    catch (IOException ex) { SORRY.show("Sorry, I cannot save the goals."); }
  }

  // ------------------------------------------------------------------

  public void previous() { 
    boolean repaintPlayView = MODEL.isEasy() || MODEL.isAlwaysEasy();
    easy.setSelected (false);
    solve.setSelected (false);
    GOAL_VIEW.clear();
    MODEL.previousGoal();
    delete.setEnabled(MODEL.getGoalNumber() != 0);
    GOAL_VIEW.repaint();
    if (repaintPlayView) PLAY_VIEW.repaint ();
  }

  public void next() { 
    boolean repaintPlayView = MODEL.isEasy() || MODEL.isAlwaysEasy();
    easy.setSelected (false);
    solve.setSelected (false);
    GOAL_VIEW.clear();
    MODEL.nextGoal();
    delete.setEnabled(MODEL.getGoalNumber() != 0);
    GOAL_VIEW.repaint();
    if (repaintPlayView) PLAY_VIEW.repaint ();
  }

  // ------------------------------------------------------------------

  public void rotate(boolean choice) { MOUSE_HANDLER.rotate (choice); }

  public void flip() { MOUSE_HANDLER.flip (); }

  // ------------------------------------------------------------------

  private boolean debugFlag = false;
  public void debug() { debugFlag = ! debugFlag; }
  public boolean isDebug() { return debugFlag; }

}
