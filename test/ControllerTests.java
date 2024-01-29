import org.junit.Assert;
import org.junit.Test;

import cs3500.reversi.model.BasicPlayer;
import cs3500.reversi.model.Player;
import cs3500.reversi.model.HexReversi;
import cs3500.reversi.model.DiscStatus;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.model.SquareReversi;
import cs3500.reversi.strategies.CaptureMostStrategy;
import cs3500.reversi.strategies.HumanStrategy;
import cs3500.reversi.view.BasicReversiView;
import cs3500.reversi.view.ReversiView;
import cs3500.reversi.view.SquareReversiView;

/**
 * Tests that the controller's functionality is correct, including utilizing its listeners well.
 */
public class ControllerTests {

  @Test
  public void testSwitchPlayer() {
    StringBuilder log = new StringBuilder();
    ReversiModel model = new HexReversi();
    Player player = new BasicPlayer(model, DiscStatus.Black,
            new CaptureMostStrategy());
    ReversiView view = new BasicReversiView(model);
    MockController mockController = new MockController(log, model, player, view);

    String resultBlack = "The current turn is black";
    String resultWhite = "The current turn is white";

    Assert.assertTrue(log.toString().contains(resultBlack));
    Assert.assertTrue(log.toString().contains(resultWhite));
  }

  @Test
  public void testGameEnd() {
    StringBuilder log = new StringBuilder();
    ReversiModel model = new HexReversi();
    Player player = new BasicPlayer(model, DiscStatus.Black,
            new CaptureMostStrategy());
    ReversiView view = new BasicReversiView(model);
    MockController mockController = new MockController(log, model, player, view);

    model.setup();
    model.startGame();
    model.pass(DiscStatus.Black);
    model.pass(DiscStatus.White);

    String result = "You tied";
    String wrongResult = "You Lost!";

    Assert.assertTrue(log.toString().contains(result));
    Assert.assertFalse(log.toString().contains(wrongResult));
  }

  @Test
  public void squareTestSwitchPlayer() {
    StringBuilder log = new StringBuilder();
    ReversiModel model = new SquareReversi();
    Player player = new BasicPlayer(model, DiscStatus.Black,
            new CaptureMostStrategy());
    SquareReversiView view = new SquareReversiView(model);
    MockController mockController = new MockController(log, model, player, view);

    String resultBlack = "The current turn is black";
    String resultWhite = "The current turn is white";

    Assert.assertTrue(log.toString().contains(resultBlack));
    Assert.assertTrue(log.toString().contains(resultWhite));
  }

  @Test
  public void squareTestGameEnd() {
    StringBuilder log = new StringBuilder();
    SquareReversi model = new SquareReversi();
    MockSquareReversiView view = new MockSquareReversiView(model);
    Player player = new BasicPlayer(model, DiscStatus.Black,
            new HumanStrategy(view));
    MockController mockController = new MockController(log, model, player, view);

    model.setup();
    model.startGame();
    model.pass(DiscStatus.Black);
    model.pass(DiscStatus.White);

    String result = "You tied";
    String wrongResult = "You Lost!";

    Assert.assertTrue(log.toString().contains(result));
    Assert.assertFalse(log.toString().contains(wrongResult));
  }

}
