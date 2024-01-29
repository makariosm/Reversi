import org.junit.Assert;
import org.junit.Test;

import cs3500.reversi.model.Hex;
import cs3500.reversi.model.HexReversi;
import cs3500.reversi.model.DiscStatus;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.model.Square;
import cs3500.reversi.model.SquareReversi;
import cs3500.reversi.strategies.AvoidNextToCornerStrategy;
import cs3500.reversi.strategies.CaptureCornersStrategy;
import cs3500.reversi.strategies.CaptureMostStrategy;
import cs3500.reversi.strategies.HexStrategy;
import cs3500.reversi.view.ReversiTextualView;
import cs3500.reversi.view.SquareReversiTextualView;
import cs3500.reversi.view.TextualView;

/**
 * Tests that test the functionality of the strategies utilized by AI players .
 * in a basic reversi game.
 */
public class StrategyTests {

  @Test
  public void testCaptureMostBasic() throws IllegalAccessException {
    ReversiModel model = new HexReversi();
    TextualView view = new ReversiTextualView(model);
    HexStrategy chooseMost = new CaptureMostStrategy();
    model.setup();
    model.placeDisc(DiscStatus.Black, new Hex(2,-1,-1));
    Assert.assertEquals(chooseMost.chooseHex(model, DiscStatus.White).getQ(), 3);
    Assert.assertEquals(chooseMost.chooseHex(model, DiscStatus.White).getR(), -2);
    Assert.assertEquals(chooseMost.chooseHex(model, DiscStatus.White).getS(), -1);
    model.placeDisc(DiscStatus.White,new Hex( 3,-2,-1));
  }

  @Test
  public void testAvoidCorners() throws IllegalAccessException {
    ReversiModel model = new HexReversi(7);
    TextualView view = new ReversiTextualView(model);
    HexStrategy chooseMost = new AvoidNextToCornerStrategy();
    model.setup();
    Assert.assertEquals(chooseMost.chooseHex(model, DiscStatus.White).getQ(), 1);
    Assert.assertEquals(chooseMost.chooseHex(model, DiscStatus.White).getR(), -2);
    Assert.assertEquals(chooseMost.chooseHex(model, DiscStatus.White).getS(), 1);
    model.placeDisc(DiscStatus.Black, new Hex(1,-2,1));
  }

  @Test
  public void testCaptureCornersMock() throws IllegalAccessException {

    StringBuilder log = new StringBuilder();
    MockHexReversi mock = new MockHexReversi(log);
    HexStrategy chooseCorners = new CaptureCornersStrategy();

    String corner1 = "Checking if move is legal at q = 0 r = -5 s = 5, ";
    String corner2 = "Checking if move is legal at q = 5 r = -5 s = 0, ";
    String corner3 = "Checking if move is legal at q = 0 r = 5 s = -5, ";
    String corner4 = "Checking if move is legal at q = -5 r = 5 s = 0, ";

    mock.setup();
    chooseCorners.chooseHex(mock, DiscStatus.White);
    Assert.assertTrue(log.toString().contains(corner1));
    Assert.assertTrue(log.toString().contains(corner2));
    Assert.assertTrue(log.toString().contains(corner3));
    Assert.assertTrue(log.toString().contains(corner4));
  }

  @Test
  public void testAvoidCornersMock() throws IllegalAccessException {

    StringBuilder log = new StringBuilder();
    MockHexReversi mock = new MockHexReversi(log);
    HexStrategy avoidCorners = new AvoidNextToCornerStrategy();

    String rightOfCorner1 = "Checking if move is legal at q = 0 r = -4 s = 4, ";
    String leftOfCorner1 = "Checking if move is legal at q = -1 r = -4 s = 5, ";
    String underCorner1 = "Checking if move is legal at q = 0 r = -4 s = 4, ";

    mock.setup();
    avoidCorners.chooseHex(mock, DiscStatus.White);

    Assert.assertFalse(log.toString().contains(rightOfCorner1));
    Assert.assertFalse(log.toString().contains(leftOfCorner1));
    Assert.assertFalse(log.toString().contains(underCorner1));
  }



  // Check if Strategy 1 actually checked all possible locations for potential moves.
  @Test
  public void testCaptureMostBasicMock() throws IllegalAccessException {
    StringBuilder log = new StringBuilder();
    MockHexReversi mock = new MockHexReversi(log);
    HexStrategy chooseMost = new CaptureMostStrategy();

    String result = "Checking all possible q = 2 r = 0 s = -2";
    String corner1 = "Checking all possible q = 0 r = -5 s = 5";
    String corner2 = "Checking all possible q = 5 r = -5 s = 0";
    String corner3 = "Checking all possible q = 0 r = 5 s = -5";
    String corner4 = "Checking all possible q = -5 r = 5 s = 0";
    String rightOfCorner1 = "Checking all possible q = 0 r = -4 s = 4";
    String leftOfCorner1 = "Checking all possible q = -1 r = -4 s = 5";
    String underCorner1 = "Checking all possible q = 0 r = -4 s = 4";
    mock.setup();

    chooseMost.chooseHex(mock, DiscStatus.Black);
    Assert.assertFalse(log.toString().contains(rightOfCorner1));
    Assert.assertFalse(log.toString().contains(leftOfCorner1));
    Assert.assertFalse(log.toString().contains(underCorner1));
    Assert.assertTrue(log.toString().contains(corner1));
    Assert.assertTrue(log.toString().contains(corner2));
    Assert.assertTrue(log.toString().contains(corner3));
    Assert.assertTrue(log.toString().contains(corner4));
  }

  @Test
  public void testCaptureMostChosenHex() throws IllegalAccessException {
    StringBuilder log = new StringBuilder();
    MockHexReversi mock = new MockHexReversi(log);
    MockCaptureMostStrategy chooseMost = new MockCaptureMostStrategy(log);

    String result = "Choosing hex at q = 2, r = -1, s = -1 with return score of 2";
    mock.setup();
    chooseMost.chooseHex(mock, DiscStatus.Black);
    Assert.assertTrue(log.toString().contains(result));
  }

  @Test
  public void squareTestCaptureMostBasic() throws IllegalAccessException {
    ReversiModel model = new SquareReversi();
    TextualView view = new SquareReversiTextualView(model);
    HexStrategy chooseMost = new CaptureMostStrategy();
    model.setup();
    model.placeDisc(DiscStatus.Black, new Square(5,3));
    Assert.assertEquals(chooseMost.chooseHex(model, DiscStatus.White).getX(), 5);
    Assert.assertEquals(chooseMost.chooseHex(model, DiscStatus.White).getY(), 2);
    model.placeDisc(DiscStatus.White, new Square( 5, 2));
  }

  @Test
  public void squareTestAvoidCorners() throws IllegalAccessException {
    ReversiModel model = new SquareReversi();
    TextualView view = new SquareReversiTextualView(model);
    HexStrategy chooseMost = new AvoidNextToCornerStrategy();
    model.setup();
    Assert.assertEquals(chooseMost.chooseHex(model, DiscStatus.White).getX(), 4);
    Assert.assertEquals(chooseMost.chooseHex(model, DiscStatus.White).getY(), 2);
    model.placeDisc(DiscStatus.Black, new Square(4,2));
  }

  @Test
  public void squareTestCaptureCornersMock() throws IllegalAccessException {

    StringBuilder log = new StringBuilder();
    MockSquareReversi mock = new MockSquareReversi(log);
    HexStrategy chooseCorners = new CaptureCornersStrategy();

    String corner1 = "Checking if move is legal at x = 0, y = 1";
    String corner2 = "Checking if move is legal at x = 0, y = 2";
    String corner3 = "Checking if move is legal at x = 0, y = 3";
    String corner4 = "Checking if move is legal at x = 0, y = 4";

    mock.setup();
    chooseCorners.chooseHex(mock, DiscStatus.White);
    Assert.assertTrue(log.toString().contains(corner1));
    Assert.assertTrue(log.toString().contains(corner2));
    Assert.assertTrue(log.toString().contains(corner3));
    Assert.assertTrue(log.toString().contains(corner4));
  }

  @Test
  public void squareTestAvoidCornersMock() throws IllegalAccessException {

    StringBuilder log = new StringBuilder();
    MockSquareReversi mock = new MockSquareReversi(log);
    HexStrategy avoidCorners = new AvoidNextToCornerStrategy();

    String rightOfCorner1 = "Checking if move is legal at x = 0, y = 1";
    String leftOfCorner1 = "Checking if move is legal at x = 0, y = 1";
    String underCorner1 = "Checking if move is legal at x = 0, y = 1";

    mock.setup();
    avoidCorners.chooseHex(mock, DiscStatus.White);

    Assert.assertFalse(log.toString().contains(rightOfCorner1));
    Assert.assertFalse(log.toString().contains(leftOfCorner1));
    Assert.assertFalse(log.toString().contains(underCorner1));
  }

  @Test
  public void squareTestCaptureMostChosenHex() throws IllegalAccessException {
    StringBuilder log = new StringBuilder();
    MockSquareReversi mock = new MockSquareReversi(log);
    MockCaptureMostStrategy chooseMost = new MockCaptureMostStrategy(log);

    String result = "Choosing square at x = 2, y = 4 with return score of 2";
    mock.setup();
    chooseMost.chooseHex(mock, DiscStatus.Black);
    Assert.assertTrue(log.toString().contains(result));
  }

}
