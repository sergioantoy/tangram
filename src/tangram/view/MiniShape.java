// Copyright 1996-2008  Sergio Antoy
// revision 032008

package tangram.view;

/** Abstract 1 of the 16 possible shapes of a piece.
 *  A piece can be flipped and rotated by 45 degrees.
 *  Each combination of these choices has a shape abstracted by this class.
 */

public class MiniShape {
  public  int length;
  public  int [] x;
  public  int [] y;
  public MiniShape (int [] a, int [] b,
		    int xcx, int xcy, int ycx, int ycy,
		    int flipped) {
    length = a.length;
    x = new int [length];
    y = new int [length];
    // compute the points
    for (int k = 0; k < length; k++) {
      x [k] = a [k] * xcx + b [k] * xcy;
      y [k] = a [k] * ycx + b [k] * ycy;
    }
//+7    // ensure traversal is always clockwise
//+7    if (flipped == 1) {
//+7      int tl = length - 1;
//+7      for (int k = 0; k < length / 2; k++) {
//+7	int tx = x[k], ty = y[k];
//+7	x[k] = x[tl-k];
//+7	y[k] = y[tl-k];
//+7	x[tl-k] = tx;
//+7	y[tl-k] = ty;
//+7      }
//+7    }
  }

  public void prettyPrint() {
    for (int i = 0; i < length; i++) {
      System.out.print(" ("+x[i]+","+y[i]+")");
    }
    System.out.println();
  }

}
