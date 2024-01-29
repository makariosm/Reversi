package cs3500.reversi.provider.model;

import java.util.Map;
import java.util.Optional;

/**
 * A read-only Reversi board in which information regarding the board and status of the game can
 * be retrieved, but never modified. The progress of the game can only be simply viewed upon.
 */
public interface ROReversiModel {


  /**
   * Gets the board for the current game.
   *
   * @return the game board
   */
  Map<Hexagon, Disc> getBoard();

  /**
   * Determines whether any move can be made on the board by the given disc color.
   * @param color the disc color
   * @return true if the disc for this turn can be placed on at least one position.
   */
  boolean canPlaceDisc(Disc color);

  /**
   * Checks whether the game is over or not. The game can be declared over when the board is
   * completely filled, no moves can be made on the board, or two consecutive passes occur.
   *
   * @return <code>true</code> if the game is over and <code>false</code> otherwise
   */
  boolean gameOver();

  /**
   * Get the disc at the given hexagonal tile.
   *
   * @param tile the hexagonal tile of the game board
   * @return the disc corresponding to the given position
   * @throws IllegalArgumentException if the given hexagon tile is invalid
   */
  Disc getDisc(Hexagon tile) throws IllegalArgumentException;

  /**
   * Gets the current turn.
   *
   * @return the current turn
   */
  Disc getTurn();

  /**
   * Gets the current score of the game for the current disc. Each hexagonal tile on the game
   * board with a matching disc counts for one point.
   *
   * @param turn the current disc whose turn it is
   * @return the total score for the current disc whose turn it is
   */
  int getScore(Disc turn);

  /**
   * Gets the side length of the regular hexagonal board.
   *
   * @return the side length
   */
  int getGameBoardSideLength();

  /**
   * Gets the number of consecutive passes that are occurring in the game.
   *
   * @return the number of consecutive passes
   */
  int getConsecutivePasses();

  /**
   * Gets the maximum number of consecutive passes that are allowed in the game.
   *
   * @return the maximum number of consecutive passes that are allowed in the game, if there is one
   */
  Optional<Integer> getMaxNumConsecutivePassesAllowed();

  /**
   * Gets the disc whose color won the game. A winner is determined at the end of the game by
   * whichever disc color has the highest score.
   *
   * @return the winner
   * @throws IllegalStateException if the game has not ended
   */
  Disc getWinner() throws IllegalStateException;

  /**
   * Builds a direct copy of the Reversi game.
   *
   * @return a copy of the Reversi game
   */
  MutableReversiModel copyGame();
}
