// Copyright 1996-2008  Sergio Antoy
// revision 032008

package tangram.view;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Sorry {

  /** It's a singleton. */
  private Sorry () {}
  /** The instance. */
  public static final Sorry SORRY = new Sorry ();

  private Label label = new Label();
  private MyFrame frame;

  private class MyFrame extends Frame {
    private MyFrame () {
      super ("Sorry");
      addWindowListener (new WindowAdapter () {
	  public void windowClosing (WindowEvent e) {
	    e.getWindow ().setVisible (false);
	  }});
      // setBackground (new Color (225,225,125));
      Panel total = new Panel ();
      total.setLayout (new GridLayout (1, 1, 0, 0));
      add(label);
      // Should I add a button for closing?
      pack ();
      setLocation (225, 185);
    }
    // Does not get the right size the second time around
    // public void validateTree() { super.validateTree(); validate(); }
    public Insets getInsets () { 
      Insets i = super.getInsets ();
      return new Insets (i.top+6, i.left+6, i.bottom+6, i.right+6);
    }
  }
  public void show (String string) {
    // close old window, if around
    if (frame != null) frame.setVisible (false);
    label.setText(string);
    // Create new window to get the right size
    frame = new MyFrame ();
    frame.setResizable (false);
    frame.setVisible (true);
  }
}
