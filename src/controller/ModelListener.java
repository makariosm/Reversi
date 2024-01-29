package cs3500.reversi.controller;

/**
 * The features that an object should implement if it listens to the model (controller).
 */
public interface ModelListener {

  /**
   * Listens to the model to see when it should try to make a move for the player.
   * @param blackTurn takes in if it's black's turn because it needs to see if it can make a move
   *                  with its own player.
   */
  void play(boolean blackTurn);

  /**
   * Repaints the view inside the listener to update it, to the most current status from the model.
   */
  void repaint();

  /**
   * Signals the end of the game for the controller to stop trying to make moves.
   */
  void end();
}

