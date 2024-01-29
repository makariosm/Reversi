package cs3500.reversi.controller;

/**
 * The features the controller can use based on inputs in the view.
 */
public interface InputFeatures {

  /**
   * The controller attempts to place a disc using the player's strategy.
   */
  void controllerPlaceDisc();

  /**
   * The controller attempts to pass on behalf of the player.
   */
  void controllerPass();


}

