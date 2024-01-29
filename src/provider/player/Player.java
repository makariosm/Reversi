package cs3500.reversi.provider.player;

import java.util.Optional;

import cs3500.reversi.provider.strategy.Move;

/**
 * A player for the Reversi game.
 */
public interface Player {
  /**
   * Allows player to select a move to play. The player may or may not return an actual move.
   */
  Optional<Move> selectMove();
}
