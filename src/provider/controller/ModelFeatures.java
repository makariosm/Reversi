package cs3500.reversi.provider.controller;

/**
 * Represents various features and abilities of the model for any model listener to respond to.
 */
public interface ModelFeatures {

  /**
   * Notifies each model listener that the game has started.
   */
  void notifyGameStarted();

  /**
   * Notifies each model listener that the game has mutated in some way. The mutation that has
   * occurred is not specified and is therefore unknown, so listeners should not mutate the game
   * at all here.
   */
  void notifyModelStateUpdated();

  /**
   * Notifies each model listener that the player turn of the game has changed.
   */
  void notifyTurnUpdated();
}
