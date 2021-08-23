// Copyright 1996-2008  Sergio Antoy
// revision 032008

package tangram.view;

import static tangram.model.Model.MODEL;
import static tangram.view.GoalView.GOAL_VIEW;
import static tangram.view.PlayView.PLAY_VIEW;
import static tangram.view.Action.ACTION;
import  tangram.model.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

// This class should be a singleton,
// but it requires dynamic initialization.

public class GoalMngr extends JPanel {

  private JButton previous;
  private JButton next;

  private JToggleButton easy = new JToggleButton ("Easy");
  private JToggleButton solve = new JToggleButton ("Solve");

  public GoalMngr() {
    Hashtable <String, Image> imageTable = ACTION.getImageTable();    
    previous = new JButton
      ("Prev Goal", new ImageIcon (imageTable.get ("previous")));
    previous.setMnemonic('P');
    next = new JButton
      ("Next Goal", new ImageIcon (imageTable.get ("next")));
    next.setMnemonic('N');
    setLayout (new BorderLayout (16, 16));
    JPanel direction = new JPanel (false);
        direction.setLayout (new BorderLayout (0, 6));
        direction.add (previous, BorderLayout.NORTH);
	previous.addActionListener (new ActionListener () {
	    public void actionPerformed (ActionEvent e) {
              ACTION.previous();
	    }});
	direction.add (next, BorderLayout.SOUTH);
	next.addActionListener (new ActionListener () {
	    public void actionPerformed (ActionEvent e) {
              ACTION.next();
	    }});
    add (direction, BorderLayout.NORTH);
    add (GOAL_VIEW, BorderLayout.CENTER);
    JPanel help = new JPanel (false);
        help.setLayout (new BorderLayout (0, 6));
        help.add (easy, BorderLayout.NORTH);
	easy.setMnemonic('E');
        ACTION.setEasy(easy);
	easy.addActionListener (new ActionListener () {
	    public void actionPerformed (ActionEvent e) {
	      ACTION.easy();
	    }});
        help.add (solve, BorderLayout.SOUTH);
        ACTION.setSolve(solve);
	solve.setMnemonic('S');
	solve.addActionListener (new ActionListener () {
	    public void actionPerformed (ActionEvent e) {
              ACTION.solve();
	    }});
    add (help, BorderLayout.SOUTH);
    setBorder (
        new TitledBorder (LineBorder.createGrayLineBorder(), "Control"));
  }

  private static final Dimension dimension = new Dimension (170, 360);
  public Dimension getMinimumSize () { return dimension; }
  public Insets getInsets() { return new Insets (35, 25, 25, 25); }
}
