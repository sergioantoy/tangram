// Copyright 1996-2008  Sergio Antoy
// revision 032008

package tangram.listener;

import tangram.view.*;

import static tangram.model.Model.MODEL;
import static tangram.view.Cache.CACHE;
import static tangram.view.Action.ACTION;
import static tangram.listener.Attraction.ATTRACTION;
import static tangram.view.PlayView.PLAY_VIEW;

import java.awt.*;
import java.applet.*;
import java.awt.event.*;

/** This class handles all the mouse events on the play board.
 *  Left Click        => raise the piece
 *  Left Drag         => ditto
 *  Left Double Click => flip
 *  Middle Click      => rotate clockwise
 *  Right Click       => rotate counterclockwise
 */

public class MouseHandler implements MouseMotionListener, MouseListener {

  /** It's a singleton. */
  private MouseHandler () {}
  /** The instance. */
  public static final MouseHandler MOUSE_HANDLER = new MouseHandler ();

  /** The polygon that may be dragged around.
   *  Defined to pass info between mousePressed and mouseDragged.
   */
  private MyPolygon toDrag = null;
  /** The start point of a mouse drag (where the mouse was pressed). */
  private Point base = new Point ();
  /** The movement of a mouse drag. */
  private Point deltaMove = new Point ();
  /** The movement of an adjustment. */
  private Point deltaAdjust = new Point ();
  /** If true, then the tangram has moved.  If false, don't know. */
  private boolean hasMoved = false;

  public void mouseDragged (MouseEvent evt) {
    if (toDrag != null) {
      // movement relative to current base
      deltaMove.x = evt.getX () - base.x;
      deltaMove.y = evt.getY () - base.y;
      toDrag.translate (deltaMove.x, deltaMove.y);
      // reset base
      base.setLocation (evt.getX (), evt.getY ());
      raise_and_paint ();
    }
  }

  public void mousePressed (MouseEvent evt) {
    int x = evt.getX ();
    int y = evt.getY ();
    base.setLocation(x, y);
    // The order of the polygons in the arry is bottom to top.
    // So the last polygon must be checked for inclusion first.
    MyPolygon [] array = CACHE.getPlay();
    for (int i = array.length-1; i >= 0; i--) {
      MyPolygon polygon = array[i];
      if (polygon.contains (x, y)) {
	// don't change the polygon
	// record it because it could be dragged
	// and raise it because it was clicked
	toDrag = polygon;
	if ((evt.getModifiers() & InputEvent.BUTTON2_MASK)
	    == InputEvent.BUTTON2_MASK) rotate (true);
	else if ((evt.getModifiers() & InputEvent.BUTTON3_MASK)
	    == InputEvent.BUTTON3_MASK) rotate (false);
	else if (evt.getClickCount() == 2) flip ();
	else raise_and_paint ();
	return;
      }
    }
  }

  public void mouseReleased (MouseEvent e) { toDrag = null; }

  public void mouseMoved (MouseEvent evt) {}
  public void mouseEntered (MouseEvent e) {}
  public void mouseExited (MouseEvent e) {}
  public void mouseClicked (MouseEvent e) {}

  public void flip() {
    if (toDrag != null) {
      toDrag.flip (base);
      hasMoved = true;
      raise_and_paint ();
    }
  }

  public void rotate(boolean choice) {
    if (toDrag != null) {
      toDrag.rotate (base, choice);
      hasMoved = true;
      raise_and_paint ();
    }
  }

  // keep tangram entirely within applet when a mouse dowm
  private void raise_and_paint () {
    if (toDrag != null) {
      checkProximity ();
      CACHE.raise (toDrag);
      int ax = 0, 
          ay = 0;
      int wd = PLAY_VIEW.getSize ().width, 
          ht = PLAY_VIEW.getSize ().height;

      // move back the tangram if is went outside this panel
      int x0 = toDrag.getBounds().x, y0 = toDrag.getBounds().y;
      int x1 = toDrag.getBounds().width+x0+1,
          y1 = toDrag.getBounds().height+y0+1;
      // adjust, if necessary
      if (x0 < 0) ax = -x0;
      else if (x1 > wd) ax = wd - x1;
      if (y0 < 0) ay = -y0;
      else if (y1 > ht) ay = ht - y1;

      //+2    if (base.x < 0) ax = -base.x;
      //+2    else if (base.x > wd) ax = wd - base.x;
      //+2    if (base.y < 0) ay = -base.y;
      //+2    else if (base.y > ht) ay = ht - base.y;

      //+3    int xc = toDrag.getBounds().x + toDrag.getBounds().width / 2;
      //+3    int yc = toDrag.getBounds().y + toDrag.getBounds().height / 2;
      //+3    if (xc < 0) ax = -xc;
      //+3    else if (xc > wd) ax = wd-xc;
      //+3    if (yc < 0) ay = -yc;
      //+3    else if (yc > ht) ay = ht - yc;

      if (ax != 0 || ay != 0) toDrag.translate (ax, ay);
      if (! toDrag.contains (base.x, base.y)) toDrag = null;
      PLAY_VIEW.repaint ();
    }
  }

  private void checkProximity() {
    // Slightly adjust to get corner overlapping
    if (ATTRACTION.adjust (toDrag, deltaAdjust)) {
      if (! MODEL.isQuiet ()) {
	// Why do I need deltaMove ???
	// to decide when there is a visible movement and make a sound.
	// For flips and rotations, there is always a visible movement
	if (hasMoved ||
	    (deltaAdjust.x != 0 && deltaMove.x + deltaAdjust.x != 0) ||
	    (deltaAdjust.y != 0 && deltaMove.y + deltaAdjust.y != 0)) {
	  ACTION.getSound().play ();
	  hasMoved = false;
	}
      }
      toDrag.translate (deltaAdjust.x, deltaAdjust.y);
      // NEXT INSTRUCTION HAS AN AMAZING EFFECT
      // It makes the program believe that we started to pull the tangram
      // from a different position we really started with !!!
      // The position is in the opposite direction from which we are pulling,
      // So after a while the tangram follows the mouse, no matter what.
      base.translate (deltaAdjust.x, deltaAdjust.y);
    }
  }

}
