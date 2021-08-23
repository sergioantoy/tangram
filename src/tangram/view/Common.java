// Copyright 1996-2008  Sergio Antoy
// revision 032008

package tangram.view;

import tangram.model.*;
import tangram.listener.*;
import static tangram.view.Action.ACTION;
import static tangram.view.PlayView.PLAY_VIEW;
import static tangram.listener.MouseHandler.MOUSE_HANDLER;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

/** This class holds all the visual components of the main window.
 *  That is the menu, the control panel and the play board.
 */
public class Common extends JPanel {
  
  // This class should be a singleton.

  public Common () {
    // layout
    JPanel borderedPlay = new JPanel (true);
    borderedPlay.setLayout (new GridLayout (1, 1, 0, 0));
    borderedPlay.add (PLAY_VIEW);
    borderedPlay.setBorder 
      (new TitledBorder (LineBorder.createGrayLineBorder(), "Playboard"));
    JPanel dummy = new JPanel (false);
    dummy.setLayout (new BorderLayout ());
    /*
    JPanel west = new JPanel (false);
    west.setLayout (new BorderLayout ());
    west.add("North", new GoalMngr());
    west.add("Center", new Canvas());
    dummy.add ("West", west);
    */
    dummy.add ("West", new GoalMngr());
    dummy.add ("Center", borderedPlay);
    setLayout (new BorderLayout ());
    add ("Center", dummy);

    PLAY_VIEW.addMouseListener (MOUSE_HANDLER);
    PLAY_VIEW.addMouseMotionListener (MOUSE_HANDLER);

    handleKeyEvents();
  }
  
  // Even with zero, it looks like 2 pixels are still there !?
  public Insets getInsets() { return new Insets (0, 0, 0, 0); }
  public Insets getBorderInsets() { return new Insets (0, 0, 0, 0); }
  
  // I thought that I should use a KeyListener, but this works

  private void handleKeyEvents() {
    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher
      (new KeyEventDispatcher() {
	  public boolean dispatchKeyEvent(KeyEvent e) {
	    // This example converts all typed keys to upper case
	    if (e.getID() == KeyEvent.KEY_TYPED) {
	      char c = Character.toUpperCase(e.getKeyChar());
	      switch (c) {
	      case 'Q' : ACTION.quit(); break;
	      case 'D' : ACTION.xdefault(); break;
	      case 'E' : ACTION.easy(); break;
	      case 'S' : ACTION.solve(); break;
	      case 'P' : ACTION.previous(); break;
	      case 'N' : ACTION.next(); break;
	      case '+' : ACTION.addConfig(); break;
	      case '-' : ACTION.deleteConfig(); break;
	      case 'H' : ACTION.howto(); break;
	      case 'A' : ACTION.about(); break;
	      case 'R' : ACTION.rotate(false); break;
	      case 'T' : ACTION.rotate(true); break;
	      case 'F' : ACTION.flip(); break;
	      case '%' : ACTION.debug(); break;
	      }
	    }
	    // done with the event
	    return false;
	  }
        });
  }

}
