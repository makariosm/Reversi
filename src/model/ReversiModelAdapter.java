package cs3500.reversi.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import cs3500.reversi.provider.controller.ModelFeatures;
import cs3500.reversi.provider.model.Disc;
import cs3500.reversi.provider.model.Hexagon;
import cs3500.reversi.provider.model.MutableReversiModel;

/**
 * Adapter class for the reversi model. Implements provider's model implementation to combine.
 * with previous design.
 */
public class ReversiModelAdapter implements MutableReversiModel {
  private final ReversiModel ourModel;

  /**
   * Constructs a reversi model adapter.
   * @param model the reversi model (previous design) class to call methods on.
   */
  public ReversiModelAdapter(ReversiModel model) {
    this.ourModel = model;
  }

  private Hex hexagonToHex(Hexagon hexagon) {

    // their x, our s (negated)
    // their y, our r (negated)

    int r = - hexagon.getY();
    int s = - hexagon.getX();
    int q = - r - s;
    return new Hex(q, r, s);
  }

  /**
   * Places the current disc at a specified position, ending the current turn.
   *
   * @param tile the hexagonal tile of the game board
   * @throws IllegalStateException    if the game is over OR the move cannot be legally played
   * @throws IllegalArgumentException if the given hexagon tile is invalid
   */
  @Override
  public void placeDisc(Hexagon tile) throws IllegalStateException, IllegalArgumentException {
    int r = - tile.getY();
    int s = - tile.getX();
    int q = - r - s;

    boolean blackTurn = ourModel.isBlackTurn();
    if (blackTurn) {
      ourModel.placeDisc(DiscStatus.Black, new Hex(q, r, s));
    }
    else {
      ourModel.placeDisc(DiscStatus.White, new Hex(q, r, s));
    }
  }

  /**
   * Immediately ends the current turn without making a move.
   *
   * @throws IllegalStateException if the game is over
   */
  @Override
  public void passTurn() throws IllegalStateException {
    boolean blackTurn = ourModel.isBlackTurn();
    if (blackTurn) {
      ourModel.pass(DiscStatus.Black);
    }
    else {
      ourModel.pass(DiscStatus.White);
    }
  }

  /**
   * Adds a feature to the model representing a model listener and gets a unique number for it.
   * The purpose of this unique number is for each listener to be able to uniquely identify
   * themselves from the other listeners. Whether the listener does indeed utilize this number or
   * not and how they choose to do so is completely irrelevant to the model. The model simply
   * offers this information to each listener.
   *
   * @param feature the feature to be added
   * @return a unique identification number for the feature to be added
   * @throws IllegalArgumentException if the feature to be added is <code>null</code> OR has
   *                                  already been added
   */
  @Override
  public int addFeatures(ModelFeatures feature) throws IllegalArgumentException {
    return 0;
  }

  /**
   * Starts the Reversi game, notifies all model listeners that the game has started, and notifies
   * the first model listener that the game has started.
   *
   * @throws IllegalStateException if the game has already started
   */
  @Override
  public void startGame() throws IllegalStateException {
    ourModel.startGame();
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
      map.put(new Hexagon(- hex.getS(), - hex.getR()),
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
   *
   * @param color the disc color
   * @return true if the disc for this turn can be placed on at least one position
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

  /**
   * Gets the current turn.
   *
   * @return the current turn
   */
  @Override
  public Disc getTurn() {
    if (ourModel.isBlackTurn()) {
      return new Disc(Disc.DiscColor.BLACK);
    }
    else {
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
    }
    else {
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
