package cs3500.reversi.provider;

//import java.util.HashMap;
//import java.util.Map;
//
//import cs3500.reversi.controller.ReversiController;
//import cs3500.reversi.model.BasicReversi;
//import cs3500.reversi.provider.model.MutableReversiModel;
//import cs3500.reversi.provider.model.ROReversiModel;
//import cs3500.reversi.provider.player.Player;
//import cs3500.reversi.provider.strategy.infallible.AvoidCornerAdjacencyMaxScore;
//import cs3500.reversi.provider.strategy.infallible.CaptureMostPieces;
//import cs3500.reversi.provider.strategy.infallible.CherryPickerCMSOptimizer;
//import cs3500.reversi.provider.strategy.infallible.OptimizeCornerStratMaxScore;
//import cs3500.reversi.provider.strategy.infallible.PlayCornersMaxScore;
//import cs3500.reversi.provider.view.graphical.GraphicalFrameView;
//import cs3500.reversi.provider.view.graphical.JFrameView;

/**
 * Main class for provider's implementation of reversi.
 */
public final class Reversi {

  //  public static void main(String[] args) {
  //    // initialize model
  //    MutableReversiModel model = new BasicReversi(5);
  //
  //    // initialize views
  //    GraphicalFrameView viewPlayer1 = new JFrameView(model);
  //    GraphicalFrameView viewPlayer2 = new JFrameView(model);
  //
  //    // initialize player options
  //    Map<String, Player> playerOptions = getPlayerOptions(model);
  //
  //    // initialize players
  //    Player player1 = getPlayer(playerOptions, args, 0,
  //            playerOptions.get("CaptureMostPieces"));
  //    Player player2 = getPlayer(playerOptions, args, 1,
  //            playerOptions.get("CaptureMostPieces"));
  //
  //    // initialize controllers
  //    ReversiController controller1 = new ReversiController(model, player1, viewPlayer1);
  //    ReversiController controller2 = new ReversiController(model, player2, viewPlayer2);
  //
  //    // start the game
  //    model.startGame();
  //  }
  //
  //    /**
  //     * Gets player options that can be used to initialize players. Each player option .
  //     * maps a player String to the actual {@link Player} object.
  //     *
  //     * @param model a read-only Reversi model
  //     * @return the player options
  //     * @throws IllegalArgumentException if the given model is <code>null</code>
  //     */
  //  private static Map<String, Player> getPlayerOptions(ROReversiModel model)
  //          throws IllegalArgumentException {
  //    if (model == null) {
  //      throw new IllegalArgumentException("Model cannot be null.");
  //    }
  //
  //    Map<String, Player> playerOptions = new HashMap<>();
  //
  //    playerOptions.put("Human", new HumanPlayer());
  //    playerOptions.put("CaptureMostPieces", new AIPlayer(new CaptureMostPieces(), model));
  //    playerOptions.put("PlayCornersMaxScore", new AIPlayer(new PlayCornersMaxScore(), model));
  //    playerOptions.put("AvoidCornerAdjacencyMaxScore",
  //            new AIPlayer(new AvoidCornerAdjacencyMaxScore(), model));
  //    playerOptions.put("OptimizeCornerStratMaxScore",
  //            new AIPlayer(new OptimizeCornerStratMaxScore(), model));
  //    playerOptions.put("CherryPickerCMSOptimizer",
  //            new AIPlayer(new CherryPickerCMSOptimizer(), model));
  //
  //    return playerOptions;
  //  }
  //
  //    /**
  //     * Gets a specific player based on the given arguments. The player options that
  //     * map a String.
  //     * representation of a player to the actual player is given. A list of arguments consisting
  //     * of String representations of players is given.
  //     * The index that decides which String representation
  //     * of a player to focus on in the given list of arguments is given.
  //     * This specific String
  //     * representation of a player will be searched for in the given player options to
  //     * return the
  //     * actual player it corresponds to. If no such player cannot be found for
  //     * whatever reason, a
  //     * default value will be returned instead.
  //     *
  //     * @param playerOptions player options that map a String representation
  //     *                      of a player to the actual
  //     *                      player
  //     * @param args list of arguments consisting of String representations of players
  //     * @param index the index at which the chosen String representation of a
  //     *              player is located in the
  //     *              given list of arguments
  //     * @param defaultValue the default player to return
  //     * @return a specific player based on the given arguments, or the default
  //     * player if no such
  //     *         specific player is found
  //     * @throws IllegalArgumentException if the given player options is <code>null</code>
  //     *                                  OR the given
  //     *                                  list of arguments is <code>null</code> OR the
  //     *                                  given default
  //     *                                  value to return is <code>null</code>
  //     */
  //  private static Player getPlayer(Map<String, Player> playerOptions, String[] args, int index,
  //                                  Player defaultValue) throws IllegalArgumentException {
  //    if (playerOptions == null) {
  //      throw new IllegalArgumentException("Player options cannot be null.");
  //    } else if (args == null) {
  //      throw new IllegalArgumentException("Given arguments cannot be null.");
  //    } else if (defaultValue == null) {
  //      throw new IllegalArgumentException("The default value cannot be null.");
  //    } else if (index < 0 || index >= args.length) {
  //      return defaultValue;
  //    }
  //
  //    Player player = playerOptions.get(args[index]);
  //    return player == null ? defaultValue : player;
  //  }
}
