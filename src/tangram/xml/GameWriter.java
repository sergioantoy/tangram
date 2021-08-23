// Copyright 1996-2008  Sergio Antoy
// revision 032008

/**
 * This class writes an XML document that defines a tangram game.
 * The information comes from the corresponding internal representation.
 * The definition consists of the pieces and some configurations.
 */

package tangram.xml;

import tangram.model.*;
import static tangram.model.Model.MODEL;
import tangram.view.MyPolygon;

import java.util.*;
import java.io.*;

public class GameWriter {

  /** It's a singleton. */
  private GameWriter () {}
  /** The instance. */
  public static final GameWriter GAME_WRITER = new GameWriter ();

  public void saveGame () throws IOException {
    Game game = MODEL.getCurrentGame();
    String name = "res/xml/" + game.fileName + ".xml";
    PrintStream outfile = new PrintStream (name);
    writeFile(outfile, game);
    outfile.close();
  }

  private void writeFile(PrintStream outfile, Game game) {
    // Header
    outfile.println("<?xml version=\"1.0\" standalone=\"yes\"?>");
    outfile.println("<!-- "+System.getProperty("user.name")+" "+new Date()+" -->");
    outfile.println();
    outfile.println("<game name=\""+game.gameName+"\">");
    // Game
    writePiece(outfile, game.piece, game.configSet.elementAt(0));
    // Configurations
    writeConfig(outfile, game.configSet);
    outfile.println("</game>");
  }

  private void writePiece(PrintStream outfile, 
			  Piece [] pieceSet, 
			  Config [] config) {    
    outfile.println("  <pieceList>");
    int count = pieceSet.length;
    for (int p = 0; p < count; p++) {
      Piece piece = pieceSet [p];
      outfile.println("    <piece id=\""+piece.id+"\">");
      outfile.println("      <pieceName>"+piece.name+"</pieceName>");
      outfile.println("      <polygon>");
      for (int i = 0; i < piece.xp.length; i++) {
	outfile.println("        <point>");
	outfile.println("          <xp>"+piece.xp[i]+"</xp>");
	outfile.println("          <yp>"+piece.yp[i]+"</yp>");
	outfile.println("        </point>");
      }
      outfile.println("      </polygon>");
      outfile.println("      <color>");
      outfile.println("        <red>"+piece.color.red+"</red>");
      outfile.println("        <green>"+piece.color.green+"</green>");
      outfile.println("        <blue>"+piece.color.blue+"</blue>");
      outfile.println("      </color>");
      outfile.println("    </piece>");      
    }
    outfile.println("  </pieceList>");
  }

  private void writeConfig(PrintStream outfile, Vector <Config []> configSet) {
    outfile.println("  <configList>");
    for (Config [] config : configSet) {
      outfile.println("    <config>");
      for (int i = 0; i < config.length; i++) {
        Config item = config[i];
        outfile.println("      <state id=\""+i+"\">");
        outfile.println("        <rotate>"+item.rotate+"</rotate>");
        outfile.println("        <flip>"+item.flip+"</flip>");
        outfile.println("        <shift>");
        outfile.println("          <dx>"+item.dx+"</dx>");
        outfile.println("          <dy>"+item.dy+"</dy>");
        outfile.println("        </shift>");
        outfile.println("      </state>");
      }
      outfile.println("    </config>");
    }
    outfile.println("  </configList>");
  }
}
