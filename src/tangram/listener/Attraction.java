// Copyright 1996-2008  Sergio Antoy
// revision 032008

package tangram.listener;

import tangram.view.*;
import tangram.model.Side;
import static tangram.view.Cache.CACHE;
import static tangram.model.Model.MODEL;

import java.awt.*;
import java.applet.*;
import java.awt.event.*;

/** This class is intended to check whether a piece that has just
 *  been touched (moved, flipped rotated), say A, is "close enough"
 *  to another piece, say B.  If it is, it computes how much A has
 *  to be moved to place it adjacent to B.
 *  <P>
 *  The definition of close enough is that a corner of A and
 *  a corner of B are separated by 6 pixel or less in both
 *  coordinate directions.
 */

public class Attraction {

  /** It's a singleton. */
  private Attraction () {}
  /** The instance. */
  public static final Attraction ATTRACTION = new Attraction ();

  private final static int near = 6;
  
  public boolean adjust (MyPolygon polygon, Point delta) {
    // Easy silouette is checked first -- more appropriate
    if (MODEL.isEasy() || MODEL.isAlwaysEasy()) {
      if (sideAdjust (polygon, delta)) return true;
    }
    // Pieces on top are checked first -- more intuitive
    MyPolygon [] array = CACHE.getPlay();
    for (int i = array.length-1; i >= 0; i--) {
      MyPolygon tmp = array[i];
      if (tmp == polygon) continue;
      if (polygonAdjust (polygon, tmp, delta)) return true;
    }
    // delta.x = delta.y = 0;
    return false;
  }

  private boolean sideAdjust (MyPolygon beingMoved, Point delta) {
    for (Side side : CACHE.getSilouette()) {
      for (int j = 0; j < beingMoved.npoints; j++) {
	if (Math.abs (side.x1 - beingMoved.xpoints[j]) <= near &&
	    Math.abs (side.y1 - beingMoved.ypoints[j]) <= near) {
	  // this is the adjustment to superimpose the vertices
	  delta.x = side.x1 - beingMoved.xpoints[j];
	  delta.y = side.y1 - beingMoved.ypoints[j];
	  return true;
	}
	if (Math.abs (side.x2 - beingMoved.xpoints[j]) <= near &&
	    Math.abs (side.y2 - beingMoved.ypoints[j]) <= near) {
	  // this is the adjustment to superimpose the vertices
	  delta.x = side.x2 - beingMoved.xpoints[j];
	  delta.y = side.y2 - beingMoved.ypoints[j];
	  return true;
	}
      }
    }
    return false;
  }

  private boolean polygonAdjust (MyPolygon beingMoved, 
				 Polygon attractor,
				 Point delta) {
    for (int i = 0; i < attractor.npoints; i++) {
      // for each vertex of this shape
      for (int j = 0; j < beingMoved.npoints; j++) {
	if (Math.abs (attractor.xpoints[i] - beingMoved.xpoints[j]) <= near &&
	    Math.abs (attractor.ypoints[i] - beingMoved.ypoints[j]) <= near) {
	  // this is the adjustment to superimpose the vertices
	  delta.x = attractor.xpoints[i] - beingMoved.xpoints[j];
	  delta.y = attractor.ypoints[i] - beingMoved.ypoints[j];
	  return true;
	}
      }
    }
    return false;
  }
}
