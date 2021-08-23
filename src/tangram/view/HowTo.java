// Copyright 1996-2008  Sergio Antoy
// revision 032008

package tangram.view;

import static tangram.view.Action.ACTION;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class HowTo {

  /** It's a singleton. */
  private HowTo () {}
  /** The instance. */
  public static final HowTo HOWTO = new HowTo ();

  private Frame frame;

  private class MyFrame extends Frame {
    private MyFrame () {
      super ("How To");
      addWindowListener (
			 new WindowAdapter () {
			   public void windowClosing (WindowEvent e) {
			     e.getWindow ().setVisible (false);
			   }});
      JScrollPane jscrollpane = new JScrollPane (createEditorPane());
      jscrollpane.setPreferredSize(new Dimension(600,300));
      add(jscrollpane);
      setResizable (true);
      pack ();
      setLocation (75, 75);
    }
    /*
      public Insets getInsets () { 
      Insets i = super.getInsets ();
      return new Insets (i.top+10, i.left+6, i.bottom+6, i.right+6);
      }
    */
  }

  public void show () {
    if (frame == null) frame = new MyFrame ();
    frame.setVisible (true);
  }

  public Dimension getPreferredSize () { return new Dimension(400, 300); }

  private JEditorPane createEditorPane() {
    JEditorPane editorPane = new JEditorPane();
    editorPane.setEditable(false);
    URL helpURL = ACTION.getDocument();
    try {
      editorPane.setPage(helpURL);
    } catch (IOException e) {
      System.err.println("Attempted to read a bad URL: " + helpURL);
    }
    return editorPane;
  }

}

