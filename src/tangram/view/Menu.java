// Copyright 1996-2008  Sergio Antoy
// version 03202008

package tangram.view;

import tangram.model.*;
import static tangram.model.Model.MODEL;
import static tangram.view.PlayView.PLAY_VIEW;
import static tangram.view.Action.ACTION;

import java.util.*;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Menu extends JMenuBar {


    // javax.swing.JPopupMenu.setDefaultLightWeightPopupEnabled(false);
    // http://forum.java.sun.com/thread.jspa?threadID=560961&tstart=0

  /** It's a singleton. */
  private Menu () { initialize(); }
  /** The instance. */
  public static final Menu MENU = new Menu ();

  // ------------------------------------------------------------------
  // The entries of the Menu

  private ButtonGroup group = new ButtonGroup();

  private JMenu file = new JMenu ("File");
  private JMenu open = new JMenu ("Open");
  private JMenuItem xdefault = new JMenuItem ("Default");
  private JMenuItem quit = new JMenuItem ("Quit");

  private JMenu options = new JMenu ("Options");
  private JCheckBoxMenuItem alwaysEasy = new JCheckBoxMenuItem ("Always easy");
  private JCheckBoxMenuItem quiet = new JCheckBoxMenuItem ("Quiet");

  private JMenu goals = new JMenu ("Goals");
  private JMenuItem add = new JMenuItem ("add");
  private JMenuItem delete = new JMenuItem ("delete");
  private JMenuItem save = new JMenuItem ("save");

  private JMenu help = new JMenu ("Help");
  private JMenuItem howto = new JMenuItem ("How to");
  private JMenuItem about = new JMenuItem ("About");

  private void initialize () {
    add (file);
    file.add(open);
    Vector<String> list = new Vector<String>();
    for (Game game : MODEL.getGames())
      list.add(game.fileName);
    Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
    String current = MODEL.getCurrentGame().fileName;
    for (String name : list) {
      JRadioButtonMenuItem jrbm = new JRadioButtonMenuItem (name);
      open.add (jrbm);
      group.add (jrbm);
      jrbm.addActionListener (new ActionListener () {
	  public void actionPerformed (ActionEvent e) {
	    ACTION.open (((JMenuItem) e.getSource ()).getText ());
	  }});
      if (name.equals(current)) jrbm.setSelected (true);
    }
    ACTION.setGroup(group);
    // ------------------------------------------------------------------
    file.add(xdefault);
    xdefault.setMnemonic('D');
    xdefault.addActionListener (new ActionListener () {
	public void actionPerformed (ActionEvent e) {
	  ACTION.xdefault();
	}});    
    // ------------------------------------------------------------------
    file.add(quit);
    if (ACTION.getIsApplet()) quit.setEnabled(false);
    quit.setMnemonic('Q');
    quit.addActionListener (new ActionListener () {
	public void actionPerformed (ActionEvent e) {
	  ACTION.quit();
	}});
    // ------------------------------------------------------------------
    add (options);
    options.add (alwaysEasy);
    ACTION.setAlwaysEasy(alwaysEasy);
    alwaysEasy.addActionListener (new ActionListener () {
	public void actionPerformed (ActionEvent e) {
	  ACTION.alwaysEasy (((JCheckBoxMenuItem) e.getSource ()).isSelected ());
	}});
    options.add (quiet);
    ACTION.setQuiet(quiet);
    quiet.setSelected (true);
    quiet.addActionListener (new ActionListener () {
	public void actionPerformed (ActionEvent e) {
	  ACTION.quiet (((JCheckBoxMenuItem) e.getSource ()).isSelected ());
	}});
    // ------------------------------------------------------------------
    add (goals);
    goals.add(add);
    add.setAccelerator(KeyStroke.getKeyStroke('+'));
    add.addActionListener (new ActionListener () {
        public void actionPerformed (ActionEvent e) {
          ACTION.addConfig();
        }});
    goals.add(delete);
    delete.setAccelerator(KeyStroke.getKeyStroke('-'));
    delete.setEnabled(false);  // goal #0 
    ACTION.setDelete(delete);
    delete.addActionListener (new ActionListener () {
        public void actionPerformed (ActionEvent e) {
          ACTION.deleteConfig();
        }});
    goals.add(save);
    if (ACTION.getIsApplet()) save.setEnabled(false);
    save.addActionListener (new ActionListener () {
        public void actionPerformed (ActionEvent e) {
          ACTION.save();
        }});
    // ------------------------------------------------------------------
    add (help);
    help.add (howto);
    quit.setMnemonic('H');
    howto.addActionListener (new ActionListener () {
	public void actionPerformed (ActionEvent e) {
	  ACTION.howto ();
	}});
    help.add (about);
    quit.setMnemonic('A');
    about.addActionListener (new ActionListener () {
	public void actionPerformed (ActionEvent e) {
	  ACTION.about ();
	}});
  }

  // ------------------------------------------------------------------
  // The "real" listeners that do all the work are in ACTION.

}
