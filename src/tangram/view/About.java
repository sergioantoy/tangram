// Copyright 1996-2008  Sergio Antoy
// revision 07232008

package tangram.view;

import static tangram.view.Action.ACTION;
import static tangram.Resource.RESOURCE;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class About {

  /** It's a singleton. */
  private About () {}
  /** The instance. */
  public static final About ABOUT = new About ();

  private Frame frame;
  private Image image;

  private class MyImage extends Canvas {
    public Dimension getPreferredSize () {
      MediaTracker tracker = new MediaTracker (this);
      image = ACTION.getImageTable().get("author");
      int ID = image.hashCode ();
      tracker.addImage (image, ID);
      try { tracker.waitForID (ID); }
      catch (InterruptedException e) {}
      return new Dimension (image.getWidth (this),
			    image.getHeight (this));
    }
    public void paint (Graphics g) { g.drawImage (image, 0, 0, this); }
  }
  private class MyButton extends Panel {
    private class Closer implements ActionListener {
      public void actionPerformed (ActionEvent e) {
	frame.setVisible (false);
      }
    }
    private MyButton () {
      Button button = new Button ("Close");
      button.setBackground (Color.lightGray);
      button.addActionListener (new Closer ());
      setLayout (new GridLayout (1, 1, 0, 0));
      add (button);
    }
    public Insets getInsets () { return new Insets (8, 8, 8, 8); }
  }
  private class MyFrame extends Frame {
    private MyFrame () {
      super ("About");
      addWindowListener (
			 new WindowAdapter () {
			   public void windowClosing (WindowEvent e) {
			     e.getWindow ().setVisible (false);
			   }});
      setBackground (new Color (225,225,125));
      Panel text = new Panel ();
      text.setLayout (new GridLayout (RESOURCE.id.length, 1, 0, 0));
      for (int i = 0; i < RESOURCE.id.length; i++)
	text.add (new Label (RESOURCE.id [i], Label.CENTER));
      Panel picture = new Panel ();
      picture.add (new MyImage());
      Panel whole = new Panel ();
      whole.setLayout (new BorderLayout (4,4));
      whole.add ("North", text);
      whole.add ("Center", picture);
      whole.add ("South", new MyButton ());
      add (whole);
      setResizable (false);
      pack ();
      setLocation (225, 185);
    }
    public Insets getInsets () { 
      Insets i = super.getInsets ();
      return new Insets (i.top+10, i.left+6, i.bottom+6, i.right+6);
    }
  }
  public void show () {
    if (frame == null) frame = new MyFrame ();
    frame.setVisible (true);
  }
}
