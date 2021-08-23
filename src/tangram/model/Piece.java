// Copyright 1996-2008  Sergio Antoy
// revision 032008

package tangram.model;

/** This class represents a piece of game.
 *  It defines the piece's id, name, shape and color.
 *  It also defines methods to access these data.
 */

public class Piece {
  public final int id;
  public final String name;
  public final int [] xp;
  public final int [] yp;
  public final Color color;
  public static class Color {
    public final int red, green, blue;
    public Color (int red, int green, int blue) {
      this.red = red;
      this.green = green;
      this.blue = blue;
    }
  }
  public Piece(int id, String name, int [] xp, int [] yp, Color color) {
    this.id = id;
    this.name = name;
    this.xp = xp;
    this.yp = yp;
    this.color = color;
  }

  public void prettyPrint() {
    System.out.println("Piece "+id);
    System.out.println("      "+name);
    System.out.print  ("      ");
    for (int i : xp) System.out.print(i+" ");
    System.out.println();
    System.out.print  ("      ");
    for (int i : yp) System.out.print(i+" ");
    System.out.println();
    System.out.println("      "+color.red+" "+color.green+" "+color.blue);
  }
}
