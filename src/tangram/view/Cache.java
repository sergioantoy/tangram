// Copyright 1996-2008  Sergio Antoy
// revision 040308

package tangram.view;

import tangram.model.*;
import static tangram.model.Model.MODEL;
import static tangram.view.GoalView.GOAL_VIEW;
import static tangram.model.Silouette.SILOUETTE;

import static java.lang.Math.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

/** This class precomputes and caches some object
 *  used in drawing the play board and the gaol.
 */

public class Cache {

  /** It's a singleton. */
  private Cache () {}
  /** The instance. */
  public static final Cache CACHE = new Cache ();

  private final static int NOT_ASSIGNED = -1;
  private final static boolean COLOR_GOAL = false;

  /** The storage of the cache. */
  private Hashtable <String, Elem> storage = new Hashtable <String, Elem> ();
  /** The element type stored into the cache. */
  private class Elem {
    // these are allocated once for any configuration
    private int count;
    private MyPolygon [] play;
    private int goalNumber;
    private MyPolygon [] goal;
    // these are re-allocated for any configuration
    private Image unsolvedGoal;
    private Image solvedGoal;
    private Vector <Side> silouette;
  }
  /** The reference for moving data into and out of the cache. */
  private Elem elem;

  /** Get data from the cache.
   *  If the cache does not hold the required data,
   *  compute data and store it into the cache, too.
   */ 
  private void checkCache() {
    //+1 It takes a long time to paint the goal,
    //+1 but refreshing the cache takes only 20 msec.
    //+1 long initTime = System.currentTimeMillis();
    Game currentGame = MODEL.getCurrentGame();
    String currentName = currentGame.fileName;
    elem = storage.get(currentName);
    if (elem == null) {
      // create the polygons for playing and showing the goal
      makeGame();
      storage.put(currentName, elem);
    }
    if (elem.goalNumber != MODEL.getGoalNumber())
      remakeGoal();
    //+1 long elapsed = System.currentTimeMillis() - initTime;
    //+1 System.out.println("checkCache = "+elapsed);
  }

  /** Invalidate the current goal and images.
   *  This should be called when a goal is deleted.
   *  However, the model decreses the goal number
   *  (which cannot be zero) hence it is OK even
   *  if this method is not called.
   */
  public void invalidate() { elem.goalNumber = NOT_ASSIGNED; }

  public MyPolygon [] getPlay() { checkCache(); return elem.play; }
  public MyPolygon [] getGoal() { checkCache(); return elem.goal; }
  public Image getGoalImage() {
    checkCache(); 
    return MODEL.isSolve() ? elem.solvedGoal : elem.unsolvedGoal;
  }
  public Vector <Side> getSilouette() { checkCache(); return elem.silouette; }
  // TODO: check no tangrams overlap in the play
  public boolean isAddable() { return true; }


  // ------------------------------------------------------------------
  /** A piece can be flipped from any position and/or
   *  rotated by 45 degrees increments.
   *  Therefore a piece is congruent to one of 16 shapes.
   *  These shapes are cached inside the representation of
   *  a piece (class MyPolygon) to speed up the execution.
   */

  private MiniShape[][] buildShape(Piece piece) {
    MiniShape[][] shape = new MiniShape[2][8];
    int length = piece.xp.length;

    int [] x = new int [length];
    int [] y = new int [length];
    System.arraycopy(piece.xp, 0, x, 0, length);
    System.arraycopy(piece.yp, 0, y, 0, length);

    // Abbreviations
//    int [] x = piece.xp;
//    int [] y = piece.yp;
    // normal and flipped along the axes
    for (int n = 0; n < 2; n++) {
      shape [n][0] = new MiniShape (x, y,  1,  0,  0,  1, n);
      shape [n][n==0 ? 2:6] = new MiniShape (x, y,  0, -1,  1,  0, n);
      shape [n][4] = new MiniShape (x, y, -1,  0,  0, -1, n);
      shape [n][n==0 ? 6:2] = new MiniShape (x, y,  0,  1, -1,  0, n);
      neg (x);	// do it twice and go back to original
    }
    // rotate 45 degrees
    double nf = 1 / Math.sqrt (2.0);
    for (int k = 0; k < length; k++) {
      double dxr = nf * x[k], dyr = nf * y[k];
      x[k] = round (dxr-dyr); y[k] = round (dxr+dyr);
    }
    // normal and flipped along the diagonals
    for (int n = 0; n < 2; n++) {
      shape [n][1] = new MiniShape (x, y,  1,  0,  0,  1, n);
      shape [n][n==0 ? 3:7] = new MiniShape (x, y,  0, -1,  1,  0, n);
      shape [n][5] = new MiniShape (x, y, -1,  0,  0, -1, n);
      shape [n][n==0 ? 7:3] = new MiniShape (x, y,  0,  1, -1,  0, n);
      neg (x);	// do it twice and go back to original
    }
// Eliminated and replaced with local arrays.
// Because of approximations the original values are not always restored.
    // Restore original values.  Rotate back 45 degrees
//    for (int k = 0; k < length; k++) {
//      double dxr = x[k]+y[k], dyr = y[k]-x[k];
//      x[k] = round (dxr * nf); y[k] = round (dyr * nf);
//    }
    return shape;
  }

  private final int round (double d) {
    return (int) (d + (d >= 0 ? 0.5 : -0.5));
  }
    
  private final void neg (int [] x) {
    for (int k = 0; k < x.length; k++) x[k] = -x[k];
  }
  
  private void makeGame() {
    Game game = MODEL.getCurrentGame();
    elem = new Elem();
    // Allocate the polygons arrays making up the play and the goal
    elem.count = game.piece.length;
    Config [] config = game.configSet.elementAt(0);
    elem.play = new MyPolygon [elem.count];
    elem.goal = new MyPolygon [elem.count];
    for (int i = 0; i < elem.count; i++) {
      // Construct the pre-computed shapes for this polygon
      MiniShape [][] shape = buildShape(game.piece[i]);
      tangram.model.Piece.Color tmp1 = game.piece[i].color;
      java.awt.Color color
	= new java.awt.Color(tmp1.red, tmp1.green, tmp1.blue);
      Config tmp = config [i];
      elem.play [i]
	= new MyPolygon(shape, color,
			game.piece[i].id, game.piece[i].name,
			tmp.flip, tmp.rotate,
			tmp.dx, tmp.dy);
      elem.goal [i] = elem.play [i].clone();
    }
    // The first time invalidate the cached goalNumber
    invalidate();
    remakeGoal();
  }

  private void remakeGoal() {
    if (elem.goalNumber != MODEL.getGoalNumber()) {
      elem.goalNumber = MODEL.getGoalNumber();
      Config [] config 
	= MODEL.getCurrentGame().configSet.elementAt(elem.goalNumber);
      for (int i = 0; i < elem.count; i++) {
	Config tmp = config[i];
	elem.goal[i].set(tmp.flip, tmp.rotate, tmp.dx, tmp.dy);
      }
      // Compute the bounds of the goal
      int x0 = Integer.MAX_VALUE;
      int y0 = Integer.MAX_VALUE;
      int x1 = Integer.MIN_VALUE;
      int y1 = Integer.MIN_VALUE;
      for (MyPolygon polygon : elem.goal) {
	Rectangle bounds = polygon.getBounds();
	x0 = min(x0,bounds.x);
	y0 = min(y0,bounds.y);
	x1 = max(x1,bounds.x+bounds.width);
	y1 = max(y1,bounds.y+bounds.height);
      }
      int width  = x1-x0+1;
      int height = y1-y0+1;
      // Shift the entire goal to the origin
      for (MyPolygon polygon : elem.goal) polygon.translate(-x0, -y0);
      // Make the silouette at this time
      // If construction fails, the returned value is null
      elem.silouette = SILOUETTE.makeSilouette(elem.goal);
      elem.unsolvedGoal = makeGoalImage(false, width, height); 
      elem.solvedGoal = makeGoalImage(true, width, height); 
    }
  }

  private Image makeGoalImage(boolean choice, int width, int height) {
    Image image = GOAL_VIEW.createImage (width, height);
    Graphics og = image.getGraphics ();
    og.setColor (Color.black);
    og.fillRect(0, 0, width, height);
    for (MyPolygon polygon : elem.goal) {
      if (COLOR_GOAL) {
	Color color = new Color(min(polygon.color.getRed()+192, 255),
				min(polygon.color.getGreen()+192, 255),
				min(polygon.color.getBlue()+192, 255));
	og.setColor (choice ? color : Color.white);
      } else {
	og.setColor (Color.white);
      }
      og.fillPolygon (polygon.xpoints, polygon.ypoints, polygon.npoints);
      og.setColor (choice ? Color.black : Color.white);
      og.drawPolygon (polygon.xpoints, polygon.ypoints, polygon.npoints);
    }
    return image.getScaledInstance (width/4, height/4, Image.SCALE_SMOOTH);
    // SCALE_FAST is not so good
    // SCALE_DEFAULT and SCALE_REPLICATE are OK for goal, but not for solve
    // SCALE_AREA_AVERAGING works as well as SMOOTH
  }


  // ------------------------------------------------------------------

  /** Add the configuration in the play board as a goal.
   *  The addition only affects the current execution.
   *  To make it permanent, use "save" in the "goals" menu.
   *  Must run as an application, not as an applet.
   */
  public void addConfig() {
    // Extract config from play
    Config [] config = new Config[elem.count];
    for (MyPolygon polygon : elem.play) {
      int id = polygon.id;
      config[id] = new Config(id, polygon.getRotate(), polygon.getFlip(),
			      polygon.getXOrig(), polygon.getYOrig());
    }
    MODEL.addConfig(config);
  }

  //   It is defined only in the model
  //   public void deleteConfig()


  // ------------------------------------------------------------------
  /** Move the argument to the last position in the array by
   *  shifting down the rest.  The order in the array is such that
   *  if polygon A comes before polygon B, then polygon B is on
   *  top of polygon A, if they overlap.
   */
  public void raise (MyPolygon polygon) {
    MyPolygon [] array = elem.play;
    int count = array.length;
    for (int i = count-1; i >= 0; i--) {
      if (array[i] == polygon) {
	for (int k = i; k < count-1; k++) array[k] = array[k+1];
	array[count-1] = polygon;
	return;
      }
    }
  }

}

