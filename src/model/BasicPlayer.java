package cs3500.reversi.model;

import java.util.Objects;
import cs3500.reversi.strategies.HexStrategy;

/**
 * This is the implementation class of any type of player.
 */
public class BasicPlayer implements Player {

  private final DiscStatus playerType;
  private final HexStrategy hexStrategy;
  private final ReadOnlyReversi model;

  /**
   * Constructs a BasicPlayer.
   * @param model      the model of the game the player is playing
   * @param playerType the type of disc the player will be able to put down
   * @param strategy   how the player chooses where they will play on the board
   */
  public BasicPlayer(ReadOnlyReversi model, DiscStatus playerType, HexStrategy strategy) {
    if (playerType == DiscStatus.Empty) {
      throw new IllegalArgumentException("Player cannot be empty.");
    }
    this.playerType = playerType;
    this.model = model;
    this.hexStrategy = strategy;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ReversiCell play() throws IllegalAccessException {
    return hexStrategy.chooseHex(this.model, this.playerType);
  }

  /**
   * Gets the hex that a given strategy based on the other implementation.
   */
  public ReversiCell theirPlay() throws IllegalAccessException {
    try {
      return hexStrategy.chooseHex(this.model, this.playerType);
    }
    catch (IllegalAccessException e) {
      throw new IllegalAccessException("Cannot make move with current strategy");
    }
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public DiscStatus getPlayerType() {
    return this.playerType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isHumanPlayer() {
    if (Objects.isNull(hexStrategy)) {
      return true;
    }
    else {
      return hexStrategy.isHuman();
    }
  }


}
