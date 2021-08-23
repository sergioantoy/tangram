// Copyright 1996-2008  Sergio Antoy
// revision 032008

package tangram.model;

import java.util.*; 


/** This class represents a tangram game.
 *  It defines the game's name, pieces, and goals.
 *  It also defines methods to access these data.
 */

public class Game {

  /** The name of the game. */
  public final String gameName;
  /** The name of the file. */
  public final String fileName;
  /** The set of pieces of the game.  */
  public final Piece [] piece;
  /** The configurations of the game. */
  public final Vector <Config []> configSet;

  public Game (String fileName, String gameName, Piece [] piece, Vector <Config []> configSet) {
	this.fileName = fileName;
    this.gameName = gameName;
    this.piece = piece;
    this.configSet = configSet;
  }

//  // The configs are scanned forward and backward, circularly
//  private int configCount = 0;
//  /** Return the next configuration, circularly. */
//  public Config [] nextConfig () {
//    configCount = configCount++ % configSet.size();
//    return configSet.elementAt (configCount);
//  }
//  /** Return the previous configuration, circularly. */
//  public Config [] previousConfig () {
//    configCount = configCount > 0 ? --configCount : configSet.size()-1;
//    return configSet.elementAt (configCount);
//  }

  public void prettyPrint() {
    System.out.println(gameName);
    for (Piece p : piece)
      p.prettyPrint();
    for (Config [] cs : configSet) {
      System.out.println("Config");
      for (Config c : cs)
	c.prettyPrint();
    }
  }
}
