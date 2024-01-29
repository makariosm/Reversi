package cs3500.reversi.provider.model;

import cs3500.reversi.provider.controller.ModelFeatures;

/**
 * A mutable Reversi board in which proper gameplay and modifications to the board can be made.
 */
public interface MutableReversiModel extends ROReversiModel {
  /**
   * Places the current disc at a specified position, ending the current turn.
   *
   * @param tile the hexagonal tile of the game board
   * @throws IllegalStateException if the game is over OR the move cannot be legally played
   * @throws IllegalArgumentException if the given hexagon tile is invalid
   */
  void placeDisc(Hexagon tile) throws IllegalStateException, IllegalArgumentException;

  /**
   * Immediately ends the current turn without making a move.
   *
   * @throws IllegalStateException if the game is over
   */
  void passTurn() throws IllegalStateException;

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
  int addFeatures(ModelFeatures feature) throws IllegalArgumentException;

  /**
   * Starts the Reversi game, notifies all model listeners that the game has started, and notifies
   * the first model listener that the game has started.
   *
   * @throws IllegalStateException if the game has already started
   */
  void startGame() throws IllegalStateException;
}
