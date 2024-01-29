import cs3500.reversi.model.BasicPlayer;
import cs3500.reversi.controller.ReversiController;
import cs3500.reversi.model.DiscStatus;
import cs3500.reversi.model.HexReversi;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.model.SquareReversi;
import cs3500.reversi.provider.strategy.infallible.AvoidCornerAdjacencyMaxScore;
import cs3500.reversi.provider.strategy.infallible.CaptureMostPieces;
import cs3500.reversi.provider.strategy.infallible.OptimizeCornerStratMaxScore;
import cs3500.reversi.provider.strategy.infallible.PlayCornersMaxScore;
import cs3500.reversi.strategies.AvoidNextToCornerStrategy;
import cs3500.reversi.strategies.CaptureCornersStrategy;
import cs3500.reversi.strategies.CaptureMostStrategy;
import cs3500.reversi.strategies.CombinedStrategy;
import cs3500.reversi.strategies.HexStrategy;
import cs3500.reversi.strategies.HumanStrategy;
import cs3500.reversi.strategies.StrategyAdapter;
import cs3500.reversi.view.BasicReversiView;
import cs3500.reversi.view.ReversiView;
import cs3500.reversi.view.SquareReversiView;

/**
 * The entry level class to start a game of Reversi.
 */
public class ReversiGame {

  /**
   * The entry level main method that uses the command line arguments to start the Reversi game.
   * @param args the array of arguments passed in from the command line
   */
  public static void main(String[] args) {
    wrongCommandLine(args);

    ReversiModel model = setModel(args[0]);
    //    MutableReversiModel theirModel = new ReversiModelAdapter(model);

    model.setup();

    ReversiView ourView = setView(args[0], model);
    ReversiView ourView2 = setView(args[0], model);

    //    GraphicalFrameView theirView = new JFrameView(theirModel);

    BasicPlayer player1 =
            new BasicPlayer(model, DiscStatus.Black, setStrategy(args[1], ourView));
    BasicPlayer player2 =
            new BasicPlayer(model, DiscStatus.White, setStrategy(args[2], ourView2));

    ReversiController controllerPlayer1 = new ReversiController(model, player1, ourView);
    ReversiController controllerPlayer2 = new ReversiController(model, player2, ourView2);
    model.startGame();

  }

  private static ReversiView setView(String arg, ReversiModel model) {
    switch (arg) {
      case "square":
        return new SquareReversiView(model);
      case "hex":
        return new BasicReversiView(model);
      default:
        throw new IllegalArgumentException("cannot handle this type of reversi game");
    }
  }

  private static ReversiModel setModel(String arg) {
    switch (arg) {
      case "square":
        return new SquareReversi();
      case "hex":
        return new HexReversi();
      default:
        throw new IllegalArgumentException("cannot handle this type of reversi game");
    }
  }

  private static void wrongCommandLine(String[] args) {
    if (args.length < 3) {
      throw new IllegalArgumentException("Not enough arguments provided");
    } else if (args.length > 3) {
      throw new IllegalArgumentException("Too many arguments provided");
    }
  }

  private static HexStrategy setStrategy(String string, ReversiView view) {
    switch (string) {
      case "human":
        return new HumanStrategy(view);
      case "strategy1":
        return new AvoidNextToCornerStrategy();
      case "strategy2":
        return new CaptureCornersStrategy();
      case "strategy3":
        return new CaptureMostStrategy();
      case "strategy4":
        return new CombinedStrategy();

      case "providerHuman":
        return null;
      case "providerStrategy1":
        return new StrategyAdapter(new CaptureMostPieces());
      case "providerStrategy2":
        return new StrategyAdapter(new AvoidCornerAdjacencyMaxScore());
      case "providerStrategy3":
        return new StrategyAdapter(new OptimizeCornerStratMaxScore());
      case "providerStrategy4":
        return new StrategyAdapter(new PlayCornersMaxScore());
      default:
        throw new IllegalArgumentException("Cannot handle this type of player");
    }
  }

}
