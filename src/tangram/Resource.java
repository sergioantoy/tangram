// Copyright 1996-2008  Sergio Antoy
// revision 07232008

package tangram;

import java.util.*;
import java.applet.*;
import java.awt.*;

/** This class holds the resources of this program.
 *  It is a singleton, which makes it easy to
 * retrieve a resource from anywhere in the program.
 */
public class Resource {

  /** It's a singleton. */
  private Resource () {}
  /** The instance. */
  public static final Resource RESOURCE = new Resource ();

  public static final String [] id = {
    "Tangram & other puzzles",
    "v0.86, 7/23/2008",
    "www.cs.pdx.edu/~antoy",
  };

  public boolean isApplet;
  public AudioClip sound;
  public Hashtable <String, Image> imageTable;

  /** This method must be called BEFORE constructing
   *  other components of the program.
   */
  public void set(boolean isApplet,
		  AudioClip sound,
		  Hashtable <String, Image> imageTable) {
    this.isApplet = isApplet;
    this.sound = sound;
    this.imageTable = imageTable;
  }
}
