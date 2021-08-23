// Copyright 1996-2008  Sergio Antoy
// revision 032008

package tangram.model;

/** This class represents a configuration of
 *  a single piece of the of game.
 */

public class Config {
  public final int id;
  public final int rotate;
  public final int flip;
  public final int dx;
  public final int dy;
  public Config(int id, int rotate, int flip, int dx, int dy) {
    this.id = id;
    this.rotate = rotate;
    this.flip = flip;
    this.dx = dx;
    this.dy = dy;
  }
  public void prettyPrint() {
    System.out.println("    "+id+" "+rotate+" "+flip+" ("+dx+","+dy+")");
  }
}
