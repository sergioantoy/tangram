// Copyright 1996-2008  Sergio Antoy
// revision 032008

package tangram.model;

import java.awt.*;
import java.util.*;

import static tangram.view.Action.ACTION;

/** This class represents the silouette of a goal
 *  which is drawn on the play board by the "easy" option.
 */

public class Silouette {

  /** It's a singleton. */
  private Silouette () {}
  /** The instance. */
  public static final Silouette SILOUETTE = new Silouette ();

  public Vector <Side> makeSilouette (Polygon [] goal) {
     // create the set of all sides
    Vector <Side> sideSet = new Vector <Side> ();
    for (Polygon p : goal) {
      for (int k = 0; k < p.npoints; k++) {
	// connect the last point to the first
	int j = k == p.npoints-1 ? 0 : k+1;
	Side side = new Side (p.xpoints [k], p.ypoints [k],
			      p.xpoints [j], p.ypoints [j]);
	sideSet.add(side);
      }
    }
    while (! filter(sideSet)) {};
    /*
    System.out.println("========================-");
    for (Side side : sideSet)
      System.out.println(side);
    */
    return sideSet;
  }


  /** Modify sides to obtain a clean perimeter. 
   *  Return false if further modifications are necessary.
   */
  private boolean filter(Vector <Side> sideSet) {
    boolean result = true;
    // Remove matching sides
    int i = 0;
    loop:
    while (i < sideSet.size () - 1) {
      Side current = sideSet.elementAt (i);
      // System.out.println("Current "+i+" "+current);
      int j = i+1;
      while (j < sideSet.size ()) {
	Side compare = sideSet.elementAt (j);
	// System.out.println("Compare "+j+" "+compare);
	if (difference(current, compare)) {
	  /*
	  System.out.print(count);
	  if (count >= 1)
	    System.out.print("  "+one);
	  if (count >= 2)
	    System.out.print("  "+two);
	  System.out.println();
	  */
	  result = false;
	  // Remove higher index first, because of shift
	  sideSet.removeElementAt(j);
	  sideSet.removeElementAt(i);	  
	  switch (count) {
	  case 0: break;
	  case 1: sideSet.add(one); break;
	  case 2: sideSet.add(one); sideSet.add(two); break;
	  }
	  continue loop;
	}
	j++;
      }
      i++;
    }
    return result;
  }
  
  /** Variables to efficiently return data from method difference */
  private int count;
  private Side one;
  private Side two;
    
  private boolean difference(Side first, Side second) {
    //    System.out.println("difference "+first+"  "+second);

    // This is a simple test that probably succeeds most of the times:
    // the two sides are not on the same line => leave them alone.
    // If second is not on the line of first, nothing to check.
    // Compute the line the current side lies on
    int a = first.y2 - first.y1;
    int b = first.x2 - first.x1;
    int c = b * first.y1 - a * first.x1;
    if ( (a * second.x1 - b * second.y1 + c != 0) ||
	 (a * second.x2 - b * second.y2 + c != 0) ) {
      // System.out.println("not on same line");
      return false;
    }

    // This is a simple test that succeeds maybe 10-20% of the times:
    // the two sides are equal => remove both and add nothing.
    if (first.equals(second)) {
      count = 0;
      return true;
    }

    // Remember, the two sides are on the same line
    // If one side ends where the other begins, join them
    if (first.x2 == second.x1 && first.y2 == second.y1) {
      count = 1;
      one = new Side(first.x1, first.y1, second.x2, second.y2);
      // System.out.println("Replace "+first+" "+second);
      // System.out.println("with "+one);
      return true;
    }
    // Other case of above test
    if (first.x1 == second.x2 && first.y1 == second.y2) {
      count = 1;
      one = new Side(second.x1, second.y1, first.x2, first.y2);
      // System.out.println("Replace "+first+" "+second);
      // System.out.println("with "+one);
      return true;
    }

    // If the two sides, which are on the same line,
    // do not overlap at all, then leave them alone.
    if ((first.x2 < second.x1) ||
	(first.x2 == second.x1 && first.y2 < second.y1) ||
	(second.x2 < first.x1) ||
	(second.x2 == first.x1 && second.y2 < first.y1)) {
      // System.out.println("No overlap "+first+" "+second);
      return false;
    }

    // The two sides overlap.
    // There are many tedious cases.
    // It would be nice a simple formula.

    // If one side includes the other, take the differemce
    if (first.x1 == second.x1 && first.y1 == second.y1) {
      count = 1;
      one = new Side(first.x2, first.y2, second.x2, second.y2);
      // System.out.println("Replace "+first+" "+second);
      // System.out.println("with "+one);
      return true;
    }
    if (first.x2 == second.x2 && first.y2 == second.y2) {
      count = 1;
      // System.out.println("Replace "+first+" "+second);
      // System.out.println("with "+one);
      one = new Side(first.x1, first.y1, second.x1, second.y1);
      return true;
    }

    // If the two sides overlap, after handling all the
    // previous cases, take head-head and tail-tail.
    count = 2;
    one = new Side(first.x1, first.y1, second.x1, second.y1);
    two = new Side(first.x2, first.y2, second.x2, second.y2);
      // System.out.println("Replace "+first+" "+second);
      // System.out.println("with "+one+" "+two);
    return true;
  }

}
