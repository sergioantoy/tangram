// Copyright 1996-2008  Sergio Antoy
// revision 032008

package tangram.model;

import java.awt.*;
import java.util.*;

/** This class represents the side of a polygon
 *  as a pair of points (x1,y1) and (x2,y2).
 *  Instances ensure that x1 < x2 or y1 < y2 when x1 == x2
 */

public class  Side {

  // ensures that x1 < x2 or y1 < y2 when x1 == x2
  public int x1, y1, x2, y2;
  public Side (int x1, int y1, int x2, int y2) {
    boolean swap = x1 > x2 || x1 == x2 && y1 > y2;
    if (swap) {
      this.x1 = x2;
      this.x2 = x1;
      this.y1 = y2;
      this.y2 = y1;
    } else {
      this.x1 = x1;
      this.y1 = y1;
      this.x2 = x2;
      this.y2 = y2;
    }
  }

  public void translate(int xoffset, int yoffset) {
    x1 += xoffset;
    y1 += yoffset;
    x2 += xoffset;
    y2 += yoffset;
  }

  public boolean equals(Side side) {
    return side.x1 == x1 && side.y1 == y1 && side.x2 == x2 && side.y2 == y2;
  }

  public String toString() { return "Side("+x1+","+y1+","+x2+","+y2+")"; }
}
