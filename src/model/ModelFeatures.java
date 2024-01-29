package cs3500.reversi.model;

import cs3500.reversi.controller.ModelListener;

/**
 * The features that a model should have to be able to interact with the view and controller.
 */
public interface ModelFeatures {

  /**
   * Starts the game by telling the black controller to make its first move since black always goes.
   * first.
   */
  void startGame();

  /**
   * Add listeners to the model.
   * @param features the object that wants to subscribe to the model
   */
  void addFeatures(ModelListener features);

  /**
   * Accurately switch between both controllers. If black made a move tell white to make a move.
   * and vice versa.
   * @param blackTurn is it black's turn?
   */
  void switchController(boolean blackTurn);

  /**
   * At this point, the game is over and one controller already sent its end message, so the model
   * calls the other controller to send its end message as well.
   * @param status the controller that already ended on its own with the given {@link DiscStatus}
   */
  void endOther(DiscStatus status);

  void updateBoth();
}
