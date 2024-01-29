package cs3500.reversi.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import cs3500.reversi.provider.model.Disc;
import cs3500.reversi.provider.model.Hexagon;
import cs3500.reversi.provider.model.MutableReversiModel;
import cs3500.reversi.provider.model.ROReversiModel;

/**
 * The adapter class for read only models.
 */
public class RORModelAdapter implements ROReversiModel {

  private final ReadOnlyReversi ourModel;

  public RORModelAdapter(ReadOnlyReversi ourModel) {
    this.ourModel = ourModel;
  }

  /**
   * Gets the board for the current game.
   *
   * @return the game board
   */
  @Override
  public Map<Hexagon, Disc> getBoard() {
    Map<Hexagon, Disc> map = new HashMap<>();
    for (ReversiCell hex : ourModel.getCopyOfGrid().keySet()) {
      map.put(new Hexagon(-hex.getS(), -hex.getR()),
              new Disc(this.ourDiscToTheirs(ourModel.getCopyOfGrid().get(hex))));
    }
    return map;
  }

  private Disc.DiscColor ourDiscToTheirs(DiscStatus status) {
    switch (status) {
      case White:
        return Disc.DiscColor.WHITE;
      case Black:
        return Disc.DiscColor.BLACK;
      case Empty:
        return Disc.DiscColor.NONE;
      default:
        throw new IllegalArgumentException("Disc color not supported");
    }
  }

  /**
   * Determines whether any move can be made on the board by the given disc color.
   * @param color the disc color
   * @return true if able to place disc, false otherwise
   */
  @Override
  public boolean canPlaceDisc(Disc color) {
    return ourModel.anyPossibleMoves();
  }

  /**
   * Checks whether the game is over or not. The game can be declared over when the board is
   * completely filled, no moves can be made on the board, or two consecutive passes occur.
   *
   * @return <code>true</code> if the game is over and <code>false</code> otherwise
   */
  @Override
  public boolean gameOver() {
    return ourModel.isGameOver();
  }

  /**
   * Get the disc at the given hexagonal tile.
   *
   * @param tile the hexagonal tile of the game board
   * @return the disc corresponding to the given position
   * @throws IllegalArgumentException if the given hexagon tile is invalid
   */
  @Override
  public Disc getDisc(Hexagon tile) throws IllegalArgumentException {
    Hex hex = this.hexagonToHex(tile);
    return new Disc(this.ourDiscToTheirs(ourModel.getStatus(hex)));
  }

  private Hex hexagonToHex(Hexagon hexagon) {

    int r = -hexagon.getY();
    int s = -hexagon.getX();
    int q = -r - s;
    return new Hex(q, r, s);
  }

  /**
   * Gets the current turn.
   *
   * @return the current turn
   */
  @Override
  public Disc getTurn() {
    if (ourModel.isBlackTurn()) {
      return new Disc(Disc.DiscColor.BLACK);
    } else {
      return new Disc(Disc.DiscColor.WHITE);
    }
  }

  /**
   * Gets the current score of the game for the current disc. Each hexagonal tile on the game
   * board with a matching disc counts for one point.
   *
   * @param turn the current disc whose turn it is
   * @return the total score for the current disc whose turn it is
   */
  @Override
  public int getScore(Disc turn) {
    if (ourModel.isBlackTurn()) {
      return ourModel.getBlackScore();
    } else {
      return ourModel.getWhiteScore();
    }
  }

  /**
   * Gets the side length of the regular hexagonal board.
   *
   * @return the side length
   */
  @Override
  public int getGameBoardSideLength() {
    return ourModel.getGridSize();
  }

  /**
   * Gets the number of consecutive passes that are occurring in the game.
   *
   * @return the number of consecutive passes
   */
  @Override
  public int getConsecutivePasses() {
    return ourModel.getConsecutivePasses();
  }

  /**
   * Gets the maximum number of consecutive passes that are allowed in the game.
   *
   * @return the maximum number of consecutive passes that are allowed in the game, if there is one
   */
  @Override
  public Optional<Integer> getMaxNumConsecutivePassesAllowed() {
    return Optional.of(2);
  }

  /**
   * Gets the disc whose color won the game. A winner is determined at the end of the game by
   * whichever disc color has the highest score.
   *
   * @return the winner
   * @throws IllegalStateException if the game has not ended
   */
  @Override
  public Disc getWinner() throws IllegalStateException {
    return new Disc(this.ourDiscToTheirs(ourModel.getWinners().get(0)));
  }

  /**
   * Builds a direct copy of the Reversi game.
   *
   * @return a copy of the Reversi game
   */
  @Override
  public MutableReversiModel copyGame() {
    HexReversi model = new HexReversi(ourModel.getGridSize(),
            new HexGrid(ourModel.getCopyOfGrid(), ourModel.getGridSize()),
            this.getConsecutivePasses(), ourModel.isGameOver(), ourModel.isBlackTurn());
    return new ReversiModelAdapter(model);
  }
}
