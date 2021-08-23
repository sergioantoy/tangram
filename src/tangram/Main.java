// Copyright 1996-2008  Sergio Antoy
// revision 032008

/**
 * This is the main class of the program.
 * It reads the program resources, such as sounds and images,
 * and it constructs the main permanent window.
 * The program can run both as an applet and an application.
 */

package tangram;

import static tangram.xml.GameReader.GAME_READER;
import static tangram.model.Model.MODEL;
import static tangram.view.Action.ACTION;
import static tangram.view.Menu.MENU;
import static tangram.Resource.RESOURCE;

import tangram.model.*;
import tangram.listener.*;
import tangram.view.*;

import java.applet.*;
import java.io.*;
import java.awt.*;
import java.net.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
//import javax.swing.border.*;
import java.util.jar.*;
import java.util.zip.*;

/** An applet that can run as an application too. */
public class Main extends JApplet {
  private Common common;
  private tangram.view.Menu menu;

  /** Initialize the applet. */
  public void init () {
    initResources (true);
    getContentPane().add (common);
    setJMenuBar (MENU);
  }

  /** The entry point when running as an application. */
  public static void main (String [] args) {
    Main main = new Main ();
    main.initResources (false);
    AFrame frame = main.new AFrame ();
    frame.setVisible(true);
    ACTION.setFrame(frame);
  }

  public String getAppletInfo() { 
    return RESOURCE.id[0] + "\n" +
	   RESOURCE.id[1] + "\n" +
	   RESOURCE.id[2];
  }

  private void initResources (boolean isApplet) {
    ACTION.setIsApplet(isApplet);
    initSound();
    initDocument();
    initImages(isApplet);
    initGames();
    common = new Common ();
  }

  private void initSound() {
    String soundPath = "res/sounds/touched.au";
    AudioClip sound = Applet.newAudioClip 
      (getClass(). getClassLoader(). getResource (soundPath));
    ACTION.setSound(sound);
  }

  private void initDocument() {
    String documentPath = "res/html/howto.html";
    URL document = getClass().getClassLoader().getResource(documentPath);
    ACTION.setDocument(document);
  }

  private void initImages(boolean isApplet) {
    Hashtable <String, Image> imageTable
      = new Hashtable <String, Image> ();
    String imageDir  = "res/images/";
    String [] imageFile = {
      "author", "previous", "next"
    };
    String imageSuffix = ".gif";
    for (int i = 0; i < imageFile.length; i++) {
      String imagePath = imageDir + imageFile [i] + imageSuffix;
      Image currentImage;
      URL url = getClass(). getClassLoader(). getResource (imagePath);
      if (isApplet) currentImage = getImage (url);
      else currentImage = Toolkit.getDefaultToolkit ().getImage (url);
      imageTable.put (imageFile [i], currentImage);
    }
    ACTION.setImageTable(imageTable);
  }

  private void initGames() {
      String xmlDir = "res/xml/";
      try {
	  URL url = getClass().getClassLoader().getResource(xmlDir);
	  if (url.getProtocol().equals("jar")) {
	      URLConnection connection = url.openConnection();
	      JarFile jarFile = ((JarURLConnection) connection).getJarFile();
	      Enumeration<? extends ZipEntry> e = jarFile.entries();
	      while (e.hasMoreElements()) {
		  ZipEntry entry = e.nextElement();
		  String fileName = entry.getName();
		  if (fileName.startsWith(xmlDir) && 
		      fileName.endsWith(".xml")) {
		      InputStream is = jarFile.getInputStream(entry);
		      String prefix = fileName.replaceFirst(xmlDir,"").replaceFirst("\u002Exml$","");
		      MODEL.addGame(GAME_READER.doit(prefix, is));
		  }
	      }
	  } else {
	      File directory = new File(xmlDir);
	      for (String fileName : directory.list()) {
		  if (fileName.endsWith(".xml")) {
		      String fullFileName = xmlDir + fileName;
		      InputStream is = getClass().getClassLoader().getResourceAsStream(fullFileName);
		      String prefix = fileName.replaceFirst("\u002Exml$","");
		      MODEL.addGame(GAME_READER.doit(prefix, is));
		  }
	      }
	  }
      } catch(IOException ex) {
	  ex.printStackTrace();
	  System.exit(1);
      }
  }

  /**
     Frame for holding the GUI 
     All user events are handled by the GUI, except
     those for closing the frame and the program.
  */
  private class AFrame extends JFrame {
    /** Construct the frame and add the GUI to it. */
    AFrame () {
      super (MODEL.getCurrentGame().gameName);
      // setResizable (false);
      setJMenuBar (MENU);
      getContentPane ().add (common, BorderLayout.CENTER);
      addWindowListener (new WindowAdapter () {
			   public void windowClosing (WindowEvent e) {
			     System.exit (0);
			   }});
      pack ();
      setLocation (200, 200);
    }
  }
}

//+6        String xmlDir = "res/xml/";
//+6        URL url = getClass().getClassLoader().getResource(xmlDir);
//+6        File dir = new File(url.getPath());
//+6        System.out.println("Directory: "+dir.getName());
//+6        String [] myfiles = dir.list();
//+6        for (String fileName : myfiles)
//+6  	  System.out.println("File: "+fileName);


//+5        try {
//+5  	  InputStream stream = url.openStream() ;
//+5  	  System.out.println("Stream: "+stream);
//+5        } catch (IOException ex) {
//+5  	  ex.printStackTrace();
//+5  	  System.exit(1);
//+5        }

//+4      URL directoryUrl = getClass().getClassLoader().getResource(xmlDir);
//+4      System.out.println("Directory: "+directoryUrl);
//+4      String fileNameUrl = directoryUrl.getFile();
//+4      File fileUrl = new File(fileNameUrl);
//+4      String [] filesUrl = fileUrl.list();
//+4      for (String fileName : filesUrl)
//+4    	System.out.println("File: "+fileName); 


//+2      //+1 File fileUrl = new File(directoryUrl.getPath());
//+2      File fileUrl = null;
//+2      try {
//+2  	fileUrl = new File(directoryUrl.toURI());
//+2      } catch (URISyntaxException ex) {
//+2  	ex.printStackTrace();
//+2      }

//+3      File fileJar = new File(getClass().getClassLoader().getResource(xmlDir).getFile());
//+3      System.out.println("Directory: "+fileJar);
//+3      String [] filesJar = fileJar.list();
//+3      for (String fileName : filesJar)
//+3  	System.out.println("File: "+fileName); 
