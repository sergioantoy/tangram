// Copyright 1996-2008  Sergio Antoy
// revision 032008

package tangram.model;

import java.util.*;
import java.awt.Polygon;

public class Model {

  /** It's a singleton. */
  private Model () {}
  /** The instance. */
  public static final Model MODEL = new Model ();

  // ------------------------------------------------------------------
  // Game handling

  private Hashtable <String, Game> gameSet = new Hashtable <String, Game> ();
  private String currentGame;
  private int goalNumber = 0;

  public void xdefault() {
    goalNumber = 0;
    alwaysEasy = false;
    easy = false;
    quiet = true;
    solve = false;
    currentGame = "Broken cross";
  }

  /** Add a game to the program.
   *  It is generally called during the initialization
   *  when games are read from some file or resource.
   */
  public void addGame(Game game) {
    gameSet.put(game.fileName, game);
    xdefault();
  }

  public void setCurrentGame(String currentGame) {
    this.currentGame = currentGame;
    goalNumber = 0;
  }

  public Game getCurrentGame() { return gameSet.get(currentGame); }

  public Collection<Game> getGames () { return gameSet.values(); }

  public int getGoalNumber() { return goalNumber; }

  // ------------------------------------------------------------------
  // Control goal

  private int auxGoal() {
    easy = false;
    solve = false;
    return gameSet.get(currentGame).configSet.size();
  }

  public void previousGoal() {
    int last = auxGoal();
    if (--goalNumber < 0) goalNumber = last-1;
  }

  public void nextGoal() {
    int last = auxGoal();
    if (++goalNumber >= last) goalNumber = 0;
  }

  // ------------------------------------------------------------------
  // Configurations

  /** Add the configuration in the play board as a goal.
   *  The addition only affects the current execution.
   *  To make it permanent, use "save" in the "goals" menu.
   *  Must run as an application, not as an applet.
   */
  public void addConfig(Config [] config) {
    Game game = gameSet.get(currentGame);
    game.configSet.add(goalNumber+1,config);
  }
  
  /** Delete the goal displayed in the Control panel.
   *  Show the previous goal.
   *  Cannot be the initial goal.
   *  The deletion only affects the current execution.
   *  To make it permanent, use "save" in the "goals" menu.
   *  Must run as an application, not as an applet.
   */
  public void deleteConfig() {
    if (goalNumber > 0) {
      Game game = gameSet.get(currentGame);
      game.configSet.remove(goalNumber);
      --goalNumber;
      easy = false;
      solve = false;
    }
  }
  

  // ------------------------------------------------------------------
  // Control always easy

  private boolean alwaysEasy;
  public void alwaysEasy (boolean value) { alwaysEasy = value; }
  public boolean isAlwaysEasy () { return alwaysEasy; }

  // ------------------------------------------------------------------
  // Control easy

  private boolean easy;
  public void clearEasy () { easy = false; }
  public void toggleEasy () { easy = ! easy; }
  public boolean isEasy () { return easy; }

  // ------------------------------------------------------------------
  // Control quiet
  
  private boolean quiet;
  
  public void setQuiet (boolean value) { quiet = value; }
  public boolean isQuiet () { return quiet; }
 
  // ------------------------------------------------------------------
  // Control solve
  
  private boolean solve;
  public void clearSolve () { solve = false; }
  public void toggleSolve () { solve = ! solve; }
  public boolean isSolve () { return solve; }

}

