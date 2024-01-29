import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import cs3500.reversi.model.DiscStatus;
import cs3500.reversi.model.Hex;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.model.Square;
import cs3500.reversi.model.SquareReversi;

/**
 * Constructs tests for a square reversi model game, using textual views .
 */
public class SquareReversiTests {

  ReversiModel model;
  ReversiModel model4;

  @Before
  public void init() {
    model = new SquareReversi();
    model4 = new SquareReversi(4);
  }

  //Start game tests

  @Test
  public void testStartGameBasic() {
    init();
    model.setup();
    Assert.assertFalse(model.isGameOver());
  }

  @Test(expected = IllegalStateException.class)
  public void testStartGameWhenGameAlreadyStarted() {
    init();
    model.setup();
    model.setup();
  }

  //all methods cannot be called before game has been started
  @Test(expected = IllegalArgumentException.class)
  public void testPlaceDiscBeforeGameStart() {
    init();
    model.placeDisc(DiscStatus.Black, new Hex(0, 0, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetStatusBeforeGameStart() {
    init();
    model.getStatus(new Hex(0, 0, 0));
  }

  //Pass tests

  @Test
  public void testPassChangesNothing() {
    init();
    model.setup();
    model.pass(DiscStatus.Black);
    Assert.assertEquals(model.getStatus(new Square(3, 3)), DiscStatus.Black);
  }

  @Test
  public void testPassTwiceInARowEndsGame() {
    init();
    model.setup();
    model.pass(DiscStatus.Black);
    model.pass(DiscStatus.White);
    Assert.assertTrue(model.isGameOver());
  }

  @Test
  public void testMakeMoveInBetweenPassingDoesNotEndGame() {
    init();
    model.setup();
    model.pass(DiscStatus.Black);
    model.placeDisc(DiscStatus.White, new Square(2, 3));
    model.pass(DiscStatus.Black);
    Assert.assertFalse(model.isGameOver());
  }

  @Test
  public void testMakeTwoMovesInBetweenPassingDoesNotEndGame() {
    init();
    model.setup();
    model.pass(DiscStatus.Black);
    model.placeDisc(DiscStatus.White, new Square(2, 3));
    model.placeDisc(DiscStatus.Black, new Square(4, 2));
    model.pass(DiscStatus.White);
    Assert.assertFalse(model.isGameOver());
  }

  @Test(expected = IllegalStateException.class)
  public void testCannotMakeMoveAfterPassingTwice() {
    init();
    model.setup();
    model.pass(DiscStatus.Black);
    model.pass(DiscStatus.White);
    Assert.assertTrue(model.isGameOver());
    model.placeDisc(DiscStatus.Black, new Square(5, 3));
  }

  @Test
  public void testPassingSwitchesTurnBlackToWhite() {
    init();
    //starts with black score
    model.setup();
    //makes it white turn
    model.pass(DiscStatus.Black);
    //makes move as white then switches to black
    Assert.assertFalse(model.isBlackTurn());
    model.placeDisc(DiscStatus.White, new Square(2, 3));
    //check scores updated
    Assert.assertEquals(model.getWhiteScore(), 4);
    Assert.assertEquals(model.getBlackScore(), 1);
    Assert.assertFalse(model.isGameOver());
  }

  @Test
  public void testPassingSwitchesTurnWhiteToBlack() {
    init();
    //starts with black score
    model.setup();
    //makes move as black turn, and switches to white turn
    model.placeDisc(DiscStatus.Black, new Square(5, 3));
    //check scores updated
    Assert.assertEquals(model.getWhiteScore(), 1);
    Assert.assertEquals(model.getBlackScore(), 4);
    //switches from white to black turn
    model.pass(DiscStatus.White);
    //make move as black and switch to white
    Assert.assertTrue(model.isBlackTurn());
    model.placeDisc(DiscStatus.Black, new Square(3, 5));
    //check scores updated
    Assert.assertEquals(model.getWhiteScore(), 0);
    Assert.assertEquals(model.getBlackScore(), 6);
    Assert.assertFalse(model.isGameOver());
  }

  //isGameOver tests
  @Test
  public void testGameOverAfterOnePass() {
    init();
    model.setup();
    model.pass(DiscStatus.Black);
    Assert.assertFalse(model.isGameOver());
  }

  @Test
  public void testGameOverFalseAfterNormalMove() {
    init();
    model.setup();
    model.placeDisc(DiscStatus.Black, new Square(5, 3));
    Assert.assertFalse(model.isGameOver());
  }

  @Test
  public void testGameOverAfterGameIsOver() {
    init();
    model.setup();
    model.pass(DiscStatus.Black);
    model.pass(DiscStatus.White);
    Assert.assertTrue(model.isGameOver());
    model.isGameOver();
  }

  //scoring tests
  @Test
  public void testCheckBlackAndWhiteScoreAfterStart() {
    init();
    model.setup();
    Assert.assertEquals(model.getWhiteScore(), 2);
    Assert.assertEquals(model.getBlackScore(), 2);
    Assert.assertEquals(model.getWhiteScore(), model.getBlackScore());
  }

  @Test
  public void testBlackScoreIncreasesAfterBlackMove() {
    init();
    model.setup();
    Assert.assertEquals(model.getBlackScore(), 2);
    model.placeDisc(DiscStatus.Black, new Square(5, 3));
    Assert.assertEquals(model.getBlackScore(), 4);
  }

  @Test
  public void testWhiteScoreDecreasesAfterBlackMove() {
    init();
    model.setup();
    Assert.assertEquals(model.getWhiteScore(), 2);
    model.placeDisc(DiscStatus.Black, new Square(5, 3));
    Assert.assertEquals(model.getWhiteScore(), 1);
  }

  @Test
  public void testWhiteScoreIncreasesAfterWhiteMove() {
    init();
    model.setup();
    Assert.assertEquals(model.getWhiteScore(), 2);
    model.pass(DiscStatus.Black);
    Assert.assertEquals(model.getWhiteScore(), 2);
    model.placeDisc(DiscStatus.White, new Square(2, 3));
    Assert.assertEquals(model.getWhiteScore(), 4);
  }


  @Test
  public void testBlackScoreDecreasesAfterWhiteMove() {
    init();
    model.setup();
    Assert.assertEquals(model.getBlackScore(), 2);
    model.pass(DiscStatus.Black);
    Assert.assertEquals(model.getBlackScore(), 2);
    model.placeDisc(DiscStatus.White, new Square(2, 3));
    Assert.assertEquals(model.getBlackScore(), 1);
  }

  @Test
  public void testScoresStayTheSameAfterPass() {
    init();
    model.setup();
    Assert.assertEquals(model.getBlackScore(), 2);
    Assert.assertEquals(model.getWhiteScore(), 2);
    model.pass(DiscStatus.Black);
    Assert.assertEquals(model.getBlackScore(), 2);
    Assert.assertEquals(model.getWhiteScore(), 2);
    model.placeDisc(DiscStatus.White, new Square(2, 3));
    Assert.assertEquals(model.getBlackScore(), 1);
    Assert.assertEquals(model.getWhiteScore(), 4);
    model.pass(DiscStatus.Black);
    Assert.assertEquals(model.getBlackScore(), 1);
    Assert.assertEquals(model.getWhiteScore(), 4);
  }

  //getStatus tests
  @Test
  public void testGetStatusBlackUpdates() {
    init();
    model.setup();
    Assert.assertEquals(model.getStatus(new Square(4, 3)), DiscStatus.White);
    Assert.assertEquals(model.getStatus(new Square(3, 3)), DiscStatus.Black);
    model.placeDisc(DiscStatus.Black, new Square(5, 3));
    Assert.assertEquals(model.getStatus(new Square(4, 3)), DiscStatus.Black);
    Assert.assertEquals(model.getStatus(new Square(5, 3)), DiscStatus.Black);
    //the white switched to black as it should have
    Assert.assertEquals(model.getStatus(new Square(4, 3)), DiscStatus.Black);
  }

  @Test
  public void testGetStatusWhiteUpdates() {
    init();
    model.setup();
    model.pass(DiscStatus.Black);
    Assert.assertEquals(model.getStatus(new Square(4, 3)), DiscStatus.White);
    Assert.assertEquals(model.getStatus(new Square(3, 3)), DiscStatus.Black);
    model.placeDisc(DiscStatus.White, new Square(2, 3));
    Assert.assertEquals(model.getStatus(new Square(3, 3)), DiscStatus.White);
    //the black switched to white as it should have
    Assert.assertEquals(model.getStatus(new Square(2, 3)), DiscStatus.White);
    Assert.assertEquals(model.getStatus(new Square(3, 3)), DiscStatus.White);
  }

  @Test
  public void testGridSizeCannotChange() {
    init();
    model.setup();
    int gridSize = model.getGridSize();
    Assert.assertEquals(model.getGridSize(), 8);
    Assert.assertEquals(gridSize, 8);
    gridSize = 12;
    Assert.assertEquals(model.getGridSize(), 8);
    Assert.assertEquals(gridSize, 12);
  }

  @Test
  public void testGridSizeGetCorrectGridSizeForDifferentGrids() {
    init();
    ReversiModel reversi = new SquareReversi(6);
    reversi.setup();
    Assert.assertEquals(reversi.getGridSize(), 6);
    ReversiModel reversi2 = new SquareReversi(8);
    reversi2.setup();
    Assert.assertEquals(reversi2.getGridSize(), 8);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCannotMakeOddNumberedGrids() {
    init();
    ReversiModel reversi = new SquareReversi(5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCannotMakeGridsSmallerThan2() {
    init();
    ReversiModel reversi = new SquareReversi(1);
  }

  //placeDisc tests
  @Test
  public void testPlaceDiscToTheRight() {
    init();
    model.setup();
    model.placeDisc(DiscStatus.Black, new Square(5, 3));
    Assert.assertEquals(model.getStatus(new Square(5, 3)), DiscStatus.Black);
    Assert.assertEquals(model.getStatus(new Square(4, 3)), DiscStatus.Black);
    Assert.assertEquals(model.getStatus(new Square(3, 3)), DiscStatus.Black);
  }

  @Test
  public void testPlaceDiscToTheBottomLeft() {
    init();
    model.setup();
    model.placeDisc(DiscStatus.Black, new Square(2, 4));
    model.placeDisc(DiscStatus.White, new Square(2, 5));
    Assert.assertEquals(model.getStatus(new Square(2, 5)), DiscStatus.White);
    Assert.assertEquals(model.getStatus(new Square(3, 4)), DiscStatus.White);
    Assert.assertEquals(model.getStatus(new Square(4, 3)), DiscStatus.White);
  }

  @Test
  public void testPlaceDiscToTheLeft() {
    init();
    model.setup();
    model.pass(DiscStatus.Black);
    model.placeDisc(DiscStatus.White, new Square(2, 3));
    Assert.assertEquals(model.getStatus(new Square(2, 3)), DiscStatus.White);
    Assert.assertEquals(model.getStatus(new Square(3, 3)), DiscStatus.White);
    Assert.assertEquals(model.getStatus(new Square(4, 3)), DiscStatus.White);
  }

  @Test
  public void testPlaceDiscToTheBottomRight() {
    init();
    model.setup();
    model.pass(DiscStatus.Black);
    model.placeDisc(DiscStatus.White, new Square(5, 4));
    model.placeDisc(DiscStatus.Black, new Square(5, 5));
    Assert.assertEquals(model.getStatus(new Square(5, 5)), DiscStatus.Black);
    Assert.assertEquals(model.getStatus(new Square(4, 4)), DiscStatus.Black);
    Assert.assertEquals(model.getStatus(new Square(3, 3)), DiscStatus.Black);
  }

  @Test
  public void testPlaceDiscToTheTopLeft() {
    init();
    model.setup();
    model.pass(DiscStatus.Black);
    model.placeDisc(DiscStatus.White, new Square(2, 3));
    model.placeDisc(DiscStatus.Black, new Square(2, 2));
    Assert.assertEquals(model.getStatus(new Square(2, 2)), DiscStatus.Black);
    Assert.assertEquals(model.getStatus(new Square(3, 3)), DiscStatus.Black);
    Assert.assertEquals(model.getStatus(new Square(4, 4)), DiscStatus.Black);
  }

  @Test
  public void testPlaceDiscToTheTopRight() {
    init();
    model.setup();
    model.placeDisc(DiscStatus.Black, new Square(5, 3));
    model.placeDisc(DiscStatus.White, new Square(5, 2));
    Assert.assertEquals(model.getStatus(new Square(5, 2)), DiscStatus.White);
    Assert.assertEquals(model.getStatus(new Square(4, 3)), DiscStatus.White);
    Assert.assertEquals(model.getStatus(new Square(3, 4)), DiscStatus.White);
  }

  //placeDisc Fails
  @Test(expected = IllegalStateException.class)
  public void testPlaceDiscNextToSameColor() {
    init();
    model.setup();
    model.placeDisc(DiscStatus.Black, new Square(2, 3));
    Assert.assertFalse(model.isGameOver());
  }


  @Test
  public void testPlaceDiscFlipsAllNecessary() {
    init();
    model.setup();
    model.placeDisc(DiscStatus.Black, new Square(4, 2));
    model.placeDisc(DiscStatus.White, new Square(5, 2));
    model.pass(DiscStatus.Black);
    model.placeDisc(DiscStatus.White, new Square(3, 2));
    Assert.assertEquals(model.getStatus(new Square(3, 4)), DiscStatus.White);
    Assert.assertEquals(model.getStatus(new Square(3, 3)), DiscStatus.White);
    Assert.assertEquals(model.getStatus(new Square(3, 2)), DiscStatus.White);
    Assert.assertEquals(model.getStatus(new Square(4, 2)), DiscStatus.White);
    Assert.assertEquals(model.getStatus(new Square(5, 2)), DiscStatus.White);
    Assert.assertFalse(model.isGameOver());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlaceDiscOffGridNotAllowed() {
    init();
    model4.setup();
    model4.placeDisc(DiscStatus.Black, new Square(4, 4));
    Assert.assertFalse(model4.isGameOver());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlaceDiscOutOfOrder() {
    init();
    model.setup();
    model.placeDisc(DiscStatus.White, new Square(2, 3));
    Assert.assertFalse(model.isGameOver());
  }

}
