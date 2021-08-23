// Copyright 1996-2008  Sergio Antoy
// revision 032008

package tangram.view;

import static tangram.view.Cache.CACHE;

import java.awt.*;
import javax.swing.*;

public class GoalView extends JPanel {

  /** It's a singleton. */
  private GoalView () { setBackground(Color.black); }

  /** The instance. */
  public static final GoalView GOAL_VIEW = new GoalView ();

    private boolean clearFlag = false;

    public void clear() {
	clearFlag = true;
	paintImmediately(0, 0, getWidth (), getHeight ());  
	clearFlag = false;
    }

  public void paintComponent (Graphics g) {
    super.paintComponent(g);
    if (! clearFlag) {
      Image image = CACHE.getGoalImage();
      int x = (getWidth () - image.getWidth (this)) / 2;
      int y = (getHeight () - image.getHeight (this)) / 2;
      g.drawImage (image, x, y, this);
    }
  }

}
