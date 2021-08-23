// Copyright 1996-2008  Sergio Antoy
// revision 032008

package tangram.view;

import static tangram.view.Cache.CACHE;
import static tangram.view.Sorry.SORRY;
import tangram.model.Side;
import static tangram.model.Model.MODEL;

import static tangram.listener.MouseHandler.MOUSE_HANDLER;

import javax.swing.*;
import java.awt.*;

public class PlayView extends Component {

  /** It's a singleton. */
  private PlayView () {}
  /** The instance. */
  public static final PlayView PLAY_VIEW = new PlayView ();

  public void paint (Graphics g) {
    // drawGrid(g);
    if (MODEL.isEasy() || MODEL.isAlwaysEasy()) {
      // Compute the bounds of the goal
      int x0 = Integer.MAX_VALUE;
      int y0 = Integer.MAX_VALUE;
      int x1 = Integer.MIN_VALUE;
      int y1 = Integer.MIN_VALUE;
      for (MyPolygon polygon : CACHE.getGoal()) {
	Rectangle bounds = polygon.getBounds();
	x0 = Math.min(x0,bounds.x);
	y0 = Math.min(y0,bounds.y);
	x1 = Math.max(x1,bounds.x+bounds.width);
	y1 = Math.max(y1,bounds.y+bounds.height);
      }
      int width  = x1-x0;          // +1
      int height = y1-y0;          // +1
      int xoffset = (getSize().width - width) / 2 - x0;
      int yoffset = (getSize().height - height) / 2 - y0;
      g.setColor (Color.white);
      for (MyPolygon polygon : CACHE.getGoal()) {
	polygon.translate(xoffset, yoffset);
	g.fillPolygon (polygon);
	//	g.drawPolygon (polygon);
      }
      g.setColor(Color.black);
      for (Side side : CACHE.getSilouette()) {
	side.translate(xoffset, yoffset);
	g.drawLine(side.x1, side.y1, side.x2, side.y2);
      }
    }
    for (MyPolygon polygon : CACHE.getPlay()) {
      g.setColor(polygon.color);
      g.fillPolygon (polygon);
      g.setColor (Color.black);
      g.drawPolygon (polygon);
    }
  }

//  private void drawGrid (Graphics g) {
//    g.setColor(Color.gray.brighter());
//    for (int x = 0; x <= width; x += 100)
//      g.drawLine(x,0,x,height);
//    for (int y = 0; y <= height; y += 100)
//      g.drawLine(0,y,width,y);
//  }

  public Dimension getPreferredSize () { return new Dimension (501, 300); }
  public Insets getInsets() { return new Insets (5, 5, 5, 5); }

}
