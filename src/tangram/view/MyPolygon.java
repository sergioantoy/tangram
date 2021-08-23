// Copyright 1996-2008  Sergio Antoy
// revision 032008

package tangram.view;

import static java.lang.Math.*;

import java.awt.*;

public class MyPolygon extends Polygon implements Cloneable {
  private final MiniShape [][] shape;
  public final Color color;
  public final int id;
  public final String name;
  private int rotate;
  private int flip;
  private int xorig;
  private int yorig;
  public MyPolygon (MiniShape [][] shape, Color color,
		    int id, String name,
		    int flip, int rotate,
		    int xorig, int yorig) {
    super(shape[flip][rotate].x, shape[flip][rotate].y, 
	  shape[flip][rotate].x.length);
    this.shape = shape;
    this.color = color;
    this.id = id;
    this.name = name;
    this.rotate = rotate;
    this.flip = flip;
    this.xorig = xorig;
    this.yorig = yorig;
    super.translate(xorig, yorig);
  }

  public MyPolygon clone() {
    try {
      MyPolygon p = (MyPolygon) super.clone();
      p.xpoints = xpoints.clone ();
      p.ypoints = ypoints.clone ();
      p.invalidate();
      return p;
    } catch (CloneNotSupportedException e) { 
      throw new InternalError("MyPolygon clone");
    }
  }

  public void flip(Point base) {
    flip = 1 - flip; 
    replaceShape();
    // Flip around the event point
    translate(2 * (base.x - xorig), 0);
  }

  private static final double r = Math.sqrt (2.0) / 2; 

  public void rotate(Point base, boolean v) {
    if (flip == 0) rotate += v ? 1 : -1;
    else rotate -= v ? 1 : -1;
    rotate = (rotate + 8) % 8;
    int cosa = base.x - xpoints[0];             // cos a * d
    int sina = base.y - ypoints[0];             // sin a * d
    // move rotation point to base
    translate(cosa, sina);
    replaceShape();
    int dx, dy;
    if (v) {
      // clockwise
      dx = (int) round(- cosa * r + sina * r);  // cos (a-45) * d
      dy = (int) round(- sina * r - cosa * r);  // sin (a-45) * d
    } else {
      // counterclockwise
      dx = (int) round(- cosa * r - sina * r);  // cos (a+45) * d
      dy = (int) round(- sina * r + cosa * r);  // sin (a+45) * d
    }
    // move back away from base
    translate(dx, dy);
  }

  public void translate(int dx, int dy) {
    xorig += dx;
    yorig += dy;
    super.translate(dx, dy);
  }

  private void replaceShape() {
    for (int i = 0; i < npoints; i++) {
      xpoints[i] = shape[flip][rotate].x[i];
      ypoints[i] = shape[flip][rotate].y[i];
    }
    invalidate();
    super.translate(xorig, yorig);
  }

  public void set(int flip, int rotate, int xorig, int yorig) {
    this.flip = flip;
    this.rotate = rotate;
    this.xorig = xorig;
    this.yorig = yorig;
    replaceShape();
  }

  public int getFlip() { return flip; }
  public int getRotate() { return rotate; }
  public int getXOrig() { return xorig; }
  public int getYOrig() { return yorig; }

  public String toString () {
    StringBuffer retval = new StringBuffer ();
    retval.append ("MyPolygon: ");
    for (int k = 0; k < npoints; k++) {
      retval.append (" (");
      retval.append (xpoints[k]);
      retval.append (",");
      retval.append (ypoints[k]);
      retval.append (")");
    }
    /*
    retval.append ("  Bounds[");
    retval.append (bounds.x);
    retval.append (",");
    retval.append (bounds.y);
    retval.append (",");
    retval.append (bounds.width);
    retval.append (",");
    retval.append (bounds.height);
    retval.append ("]\n");
    */
    return retval.toString ();
  }

//+debug  private void debug() {
//+debug    for (int i = 0; i < 2 ; i++) {
//+debug      for (int j = 0; j < 8 ; j++) {
//+debug	System.out.println("shape["+i+"]["+j+"]");
//+debug	shape[i][j].prettyPrint();
//+debug      }
//+debug    }
//+debug  }
}
