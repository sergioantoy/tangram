// Copyright 1996-2008  Sergio Antoy
// revision 032008

/**
 * This class reads an XML document that defines a tangram game
 * and constructs the corresponding internal representation.
 * The definition consists of the pieces and some configurations.
 */

package tangram.xml;

import tangram.model.*;

import java.util.*;
import java.io.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

public class GameReader {

  /** It's a singleton. */
  private GameReader () {}
  /** The instance. */
  public static final GameReader GAME_READER = new GameReader ();

  private ContentHandler contentHandler = new GameContentHandler();
  private String fileName;

  public Game doit (String fileName, InputStream is) {
    this.fileName = fileName;
    try {
      XMLReader xmlReader = XMLReaderFactory.createXMLReader();
      xmlReader.setContentHandler(contentHandler);
      xmlReader.parse(new InputSource (is));
    } catch(Exception ex) {
      ex.printStackTrace();
      System.exit(1);
    } 
    return game;
  }

  // This is the returned object
  private Game game;

  // These are the components of a game
  private String gameName; 
  private Piece [] piece;
  private Vector <Config []> configSet;

  private class GameContentHandler extends DefaultHandler {
    // TODO: should lastElement be a stringbuffer?
    private String lastElement;
    private Stack <String> stack = new Stack <String> ();

    public void startElement(String uri, 
			     String localName, 
			     String qName, 
			     Attributes attributes) {
      lastElement = "";
      // The following elements starts a list of unknown length
      // So I push a marker (the element name) on the stack.
      // When the end of the element is found
      // I descend the stack to find the length of the list.
      if (localName.equals("pieceList") ||
	  localName.equals("polygon")) {
	stack.push(localName);
      }
      // The following elements have exactly one attribute.
      // I push this attribute on the stack for later processing.
      if (localName.equals("game") ||
	  localName.equals("piece") ||
	  localName.equals("state")) {
	stack.push(attributes.getValue(0));
      }
      // Initialize the list of configurations
      if (localName.equals("configList")) {
	configSet = new Vector <Config []> ();
      }
    }

    public void endElement(String uri, String localName, String qName) {
      // The following elements have a value.
      // This value has been saved in variable lastElement.
      // I push on the stack for later processing.
      if (localName.equals("xp") ||
	  localName.equals("yp") ||
	  localName.equals("pieceName") || 
	  localName.equals("red") || 
	  localName.equals("green") || 
	  localName.equals("blue") ||
	  localName.equals("rotate") ||
	  localName.equals("flip") ||
	  localName.equals("dx") ||
	  localName.equals("dy")
	  ) {
	stack.push(lastElement);
	// System.out.println("Pushing "+lastElement);
	return;
      } 
      // The following elements end a structure.
      // Some component of the structure are on the stack as string.
      // Other components may be stored in ptogram variables.
      // The components are recovered, converted and composed.
      if (localName.equals("pieceList")) {
	piece = getPieceSetFromStack(stack);
	return;
      }
      if (localName.equals("config")) {
	configSet.add(getConfigFromStack(stack));
	return;
      }
      if (localName.equals("game")) {
	// The attribute of tag game should be the file name
	// If it is not, change it to the file name for writing back.
        String internalName = stack.pop();
	game = new Game(fileName, internalName, piece, configSet);
	return;
      }
      // The following elements do not require any action
    }

    public void characters(char[] ch, int start, int length) {
      lastElement += new String(ch, start, length);
    }

    // ------------------------------------------------------------------

    private Piece [] getPieceSetFromStack(Stack <String> stack) {
      // Tow find how many pieces there are in the game
      // count how many element "polygon" there are
      // from the top to element "pieceList"
      int count = 0;
      int top = stack.size();
      for (;;)  {
	String string = stack.elementAt(--top);
	if (string.equals("polygon")) count++;
	if (string.equals("pieceList")) break;
      }
      piece = new Piece [count];

      while (count > 0) {
	int blue  = Integer.parseInt(stack.pop());
	int green = Integer.parseInt(stack.pop());
	int red   = Integer.parseInt(stack.pop());
	Piece.Color color = new Piece.Color(red, green, blue);
	// Tricky indexing next !
	int index = stack.size();
	while (! stack.elementAt(--index).equals("polygon")) {}
	int length = (stack.size() - index - 1) / 2;
	int [] xp = new int [length];
	int [] yp = new int [length];
	while (--length >= 0) {
	  yp[length] = Integer.parseInt(stack.pop());
	  xp[length] = Integer.parseInt(stack.pop());
	}
	// Remove the "polygon" marker
	stack.pop();
	String name = stack.pop();
	int id = Integer.parseInt(stack.pop());
	piece[--count] = new Piece(id, name, xp, yp, color);
      }
      // Pop marker "pieceList"
      stack.pop();
      return piece;
    }

    private Config [] getConfigFromStack(Stack <String> stack) {
      int count = piece.length;
      // there should be as many "state" in a "config" as
      // there are "piece" in a "game"
      Config [] config = new Config [count];
      while (count-- > 0) {
	int dy = Integer.parseInt(stack.pop());
	int dx = Integer.parseInt(stack.pop());
	int flip = Integer.parseInt(stack.pop());
	int rotate = Integer.parseInt(stack.pop());
	int id = Integer.parseInt(stack.pop());
	config [id] = new Config(id, rotate, flip, dx, dy);
      }
      return config;
    }

    /** For debugging only. */
    private void printStack(Stack <String> stack) {
      for (int i = 0; i < stack.size(); i++)
	System.out.println(stack.elementAt(i));
    }

  }


}
