package cs3500.reversi.provider.controller;

import cs3500.reversi.provider.model.Hexagon;

/**
 * Represents various features and abilities of the view for any view listener to respond to.
 */
public interface ViewFeatures {
  /**
   * Makes a move on the Reversi board at the specified hexagon. This method is to be called when
   * a move is made through the view. If any exception is thrown, this method should handle them
   * accordingly such that the game can continue normally.
   *
   * @param hexagon the hexagon where the move is to be made
   */
  void makeMove(Hexagon hexagon);

  /**
   * Passes the turn in the Reversi game. This method is called when a move is passed through the
   * view. If any exception is thrown, this method should handle them accordingly such that the
   * game can continue normally.
   */
  void pass();
}
