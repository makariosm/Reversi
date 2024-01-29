package cs3500.reversi.model;

/**
 * Represents a player to be used in a game of Reversi.
 */
public interface Player {

  /**
   * Finds the hex the player would like to place their disc in based on their given strategy.
   * @return the hex the player wants to play in
   * @throws IllegalAccessException if there are no moves for the player to make based on their
   *                                given strategy
   */
  ReversiCell play() throws IllegalAccessException;

  ReversiCell theirPlay() throws IllegalAccessException;

  /**
   * Gets the type of disc that the player gets to place in a game (Black or White).
   * @return the disc type which is a {@link DiscStatus}
   */
  DiscStatus getPlayerType();

  /**
   * Is this player a human or an AI player?.
   * @return true if human, false otherwise
   */
  boolean isHumanPlayer();
}
