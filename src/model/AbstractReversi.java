package cs3500.reversi.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cs3500.reversi.controller.ModelListener;

/**
 * Basic implementation of a reversi game, with its contents and moves.
 */
public abstract class AbstractReversi implements ReversiModel, ModelFeatures {

  // boolean representing if it is black player's turn
  protected boolean blackTurn;
  // count of how many passes made in a row
  protected int consecutivePass;
  // true if game is over, or hasn't started, false if in progress
  protected boolean isGameOver;
  protected final List<ModelListener> modelObservers;


  /**
   * Default basic model of reversi game with gridSize of 11.
   */
  public AbstractReversi() {
    this.consecutivePass = 0;
    this.isGameOver = true;
    this.blackTurn = true;
    this.modelObservers = new ArrayList<>();
  }

  /**
   * Constructs a reversi game with all its information/fields.
   * @param gridSize size of grid
   * @param consecutivePass number of consecutive passes
   * @param isGameOver boolean that tells if game is over or not
   * @param blackTurn boolean that determines whose turn it is
   */
  public AbstractReversi(int gridSize, int consecutivePass, boolean isGameOver,
                         boolean blackTurn) {
    this.consecutivePass = consecutivePass;
    this.isGameOver = isGameOver;
    this.blackTurn = blackTurn;
    this.modelObservers = new ArrayList<>();
  }


  /**
   * Determines if game has started. Throws exception if game is over, or hasn't started.
   */
  public void gameNotStartedWarning() {
    if (this.isGameOver()) {
      throw new IllegalStateException("Game not started yet or game is already over.");
    }
  }

  /**
   * Determines if it is the correct player's turn, or throws.
   * @param playerType the given player type
   */
  public void wrongPlayerWarning(DiscStatus playerType) {
    if ((this.blackTurn && playerType == DiscStatus.White)
            || (!this.blackTurn && playerType == DiscStatus.Black)) {
      throw new IllegalArgumentException("Not your turn!");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public abstract void setup();

  /**
   * {@inheritDoc}
   */
  @Override
  public void startGame() {
    modelObservers.get(0).play(blackTurn);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public abstract void placeDisc(DiscStatus playerType, ReversiCell cell);

  /**
   * {@inheritDoc}
   */
  public boolean anyPossibleMoves() {
    return !this.getPossibleMoves().isEmpty();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public abstract List<ReversiCell> getPossibleMoves();

  /**
   * Tries to place disc and returns list of booleans of successful moves made.
   */
  public List<Boolean> validateTryPlaceDisc(ReversiCell cell) {
    List<ReversiCell> loAdjacentOpposite = this.getAdjacentOpposite(cell);
    List<Boolean> loMovesMade = new ArrayList<>();

    for (int i = 0; i < loAdjacentOpposite.size(); i++) {
      // for every existing adjacent opponent hex
      if (loAdjacentOpposite.get(i) != null) {
        // calls method to make move and make applicable changes if allowable
        // adds a boolean to loMoves (true if a successful move, false if not)
        loMovesMade.add(this.tryPlaceDisc(loAdjacentOpposite.get(i), i));
      }
    }
    return loMovesMade;
  }

  /**
   * Determines if a move is legal to the given cell is legal. Uses cube coordinates system
   *
   * @param cell the desired cell
   * @return true if the move is legal at the given coordinates, false otherwise
   */
  @Override
  public abstract boolean isMoveLegalAt(ReversiCell cell);

  public abstract Boolean tryPlaceDisc(ReversiCell adjHex, int i);

  /**
   * {@inheritDoc}
   */
  public abstract int numOppositeHexes(ReversiCell adjHex, int qDir, int rDir, int sDir);


  /**
   * Returns a list of all booleans checking if the given hex has a move in all 6 directions.
   * Side Effect: if a move can be made in a certain direction, make the move, then check other
   * directions.
   *
   * @param cell The desire cell.
   * @return list of booleans each boolean resembles if a move can be made in a certain direction.
   */
  public abstract List<Boolean> validatePlaceDiscMove(ReversiCell cell);

  /**
   * Determines if a list of booleans contains an element equal to true.
   *
   * @param loBool the list of booleans.
   * @return true if exists, false if all elements are false.
   */
  public boolean checkListContainsTrue(List<Boolean> loBool) {
    for (Boolean bool : loBool) {
      if (bool) {
        return true;
      }
    }
    return false;
  }

  /**
   * Gets all adjacent opponent hexes to hex of given coordinates.
   *
   * @param cell The desired cell
   * @return list of hexes of adjacent cells.
   */
  public abstract List<ReversiCell> getAdjacentOpposite(ReversiCell cell);

  /**
   * Determined if given list of hexes are all null.
   *
   * @param list The list of hexes.
   * @return true if all null, false otherwise.
   */
  public boolean checkListNull(List<ReversiCell> list) {
    for (ReversiCell hex : list) {
      if (hex != null) {
        return false;
      }
    }
    return true;
  }

  /**
   * Based on if the disc being placed at the target location is valid,
   * flips hexes in between
   * and outputs if a valid move and changes were made.
   *
   * @param targetHex The clicked hex by the player.
   * @param adjHex    The adjacent opponent cell from the target cell.
   * @param i         Represents the direction the adjHex is in respect to the targetHex.
   * @return True if valid move was made, false otherwise.
   */
  public abstract boolean placeDiscHelper(ReversiCell targetHex, ReversiCell adjHex, int i);

  /**
   * Makes disc status of clicked cell the player's color, and for every opponent hex in between.
   * flip to player color
   *
   * @param targetHex         The hex at the coordinates that player clicked.
   * @param numHexesInBetween The number of opponent hexes in between player hexes.
   * @param qDir              The number change in axis q when traveling along path of adjacent
   *                          cell.
   * @param rDir              The number change in axis r when traveling along path of adjacent
   *                          cell.
   * @param sDir              The number change in axis s when traveling along path of adjacent
   *                          cell.
   */
  public abstract void makeChanges(ReversiCell targetHex,
                                   int numHexesInBetween, int qDir, int rDir, int sDir);

  /**
   * {@inheritDoc}
   */
  @Override
  public void pass(DiscStatus playerType) {
    gameNotStartedWarning();
    wrongPlayerWarning(playerType);
    this.consecutivePass += 1;
    blackTurn = !blackTurn;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isGameOver() {
    if (this.consecutivePass >= 2) {
      this.isGameOver = true;
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean isBlackTurn() {
    return blackTurn;
  }

  /**
   * Returns copy of current reversi model grid.
   *
   * @return copy of grid
   */
  @Override
  public abstract Map<ReversiCell, DiscStatus> getCopyOfGrid();

  /**
   * {@inheritDoc}
   */
  @Override
  public int getBlackScore() {
    return getScoreHelper(DiscStatus.Black);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getWhiteScore() {
    return getScoreHelper(DiscStatus.White);
  }

  /**
   * Calculates how many hexes on a grid are of the given disc status.
   *
   * @param status The disc status we are calculating for.
   * @return The number of hexes on a grid are of the given disc status.
   */
  public abstract int getScoreHelper(DiscStatus status);

  /**
   * {@inheritDoc}
   */
  @Override
  public abstract DiscStatus getStatus(ReversiCell cell);

  /**
   * {@inheritDoc}
   */
  @Override
  public abstract int getGridSize();

  /**
   * Gets players that won the game.
   * In case of tie returns a list of both players.
   *
   * @return list of players that won the game.
   */
  @Override
  public List<DiscStatus> getWinners() {
    List<DiscStatus> winners = new ArrayList<>();
    if (this.getBlackScore() == this.getWhiteScore()) {
      winners.add(DiscStatus.Black);
      winners.add(DiscStatus.White);
    } else if (this.getBlackScore() > this.getWhiteScore()) {
      winners.add(DiscStatus.Black);
    } else {
      winners.add(DiscStatus.White);
    }
    return winners;
  }

  @Override
  public int getConsecutivePasses() {
    return this.consecutivePass;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addFeatures(ModelListener features) {
    this.modelObservers.add(features);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void switchController(boolean blackTurn) {
    if (blackTurn) {
      modelObservers.get(1).play(this.blackTurn);
      modelObservers.get(0).repaint();
    } else {
      modelObservers.get(0).play(this.blackTurn);
      modelObservers.get(1).repaint();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void endOther(DiscStatus status) {
    if (status == DiscStatus.White) {
      modelObservers.get(0).end();
    } else {
      modelObservers.get(1).end();
    }
  }

  @Override
  public void updateBoth() {
    for (ModelListener m : this.modelObservers) {
      m.repaint();
    }
  }
}
