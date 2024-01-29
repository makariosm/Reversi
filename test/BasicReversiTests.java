import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cs3500.reversi.model.DiscStatus;
import cs3500.reversi.model.Hex;
import cs3500.reversi.model.HexReversi;
import cs3500.reversi.model.ReversiModel;
import cs3500.reversi.view.TextualView;
import cs3500.reversi.view.ReversiTextualView;

/**
 * The class meant to represent all our BasicReversi Model and Textual View tests.
 */
public class BasicReversiTests {

  ReversiModel model;
  ReversiModel model5;
  ReversiModel model7;
  TextualView view7;
  TextualView view5;
  TextualView view;

  @Before
  public void init() {
    model = new HexReversi();
    view = new ReversiTextualView(model);
    model5 = new HexReversi(5);
    view5 = new ReversiTextualView(model5);
    model7 = new HexReversi(7);
    view7 = new ReversiTextualView(model7);
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

  //  @Test(expected = IllegalStateException.class)
  //  public void testPassBeforeGameStart() {
  //    init();
  //    model.pass(DiscStatus.Black);
  //  }

  //  @Test(expected = IllegalStateException.class)
  //  public void testGameOverBeforeGameStart() {
  //    init();
  //    model.isGameOver();
  //  }

  //  @Test(expected = IllegalStateException.class)
  //  public void testBlackScoreBeforeGameStart() {
  //    init();
  //    model.getBlackScore();
  //  }

  //  @Test(expected = IllegalArgumentException.class)
  //  public void testWhiteScoreBeforeGameStart() {
  //    init();
  //    model.getWhiteScore();
  //  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetStatusBeforeGameStart() {
    init();
    model.getStatus(new Hex(0, 0, 0));
  }

  //  @Test(expected = IllegalStateException.class)
  //  public void testGetGridSizeBeforeGameStart() {
  //    init();
  //    model.getGridSize();
  //  }

  //Pass tests

  @Test
  public void testPassChangesNothing() {
    init();
    model.setup();
    model.pass(DiscStatus.Black);
    Assert.assertEquals(model.getStatus(new Hex(0, 0, 0)), DiscStatus.Empty);
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
    model.placeDisc(DiscStatus.White,new Hex(-1, -1, 2));
    model.pass(DiscStatus.Black);
    Assert.assertFalse(model.isGameOver());
  }

  @Test
  public void testMakeTwoMovesInBetweenPassingDoesNotEndGame() {
    init();
    model.setup();
    model.pass(DiscStatus.Black);
    model.placeDisc(DiscStatus.White, new Hex(-2, 1, 1));
    model.placeDisc(DiscStatus.Black,new Hex(2, -1, -1));
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
    model.placeDisc(DiscStatus.Black,new Hex(2, -1, -1));
  }

  @Test
  public void testPassingSwitchesTurnBlackToWhite() {
    init();
    //starts with black score
    model.setup();
    //makes it white turn
    model.pass(DiscStatus.Black);
    //makes move as white then switches to black
    model.placeDisc(DiscStatus.White, new Hex(-2, 1, 1));
    //check scores updated
    Assert.assertEquals(model.getWhiteScore(), 5);
    Assert.assertEquals(model.getBlackScore(), 2);
    //makes move as black
    model.placeDisc(DiscStatus.Black,new Hex(2, -1, -1));
    //check scores updated
    Assert.assertEquals(model.getWhiteScore(), 4);
    Assert.assertEquals(model.getBlackScore(), 4);
    Assert.assertFalse(model.isGameOver());
  }

  @Test
  public void testPassingSwitchesTurnWhiteToBlack() {
    init();
    //starts with black score
    model.setup();
    //makes move as black turn, and switches to white turn
    model.placeDisc(DiscStatus.Black,new Hex(2, -1, -1));
    //check scores updated
    Assert.assertEquals(model.getWhiteScore(), 2);
    Assert.assertEquals(model.getBlackScore(), 5);
    //switches from white to black turn
    model.pass(DiscStatus.White);
    //make move as black and switch to white
    model.placeDisc(DiscStatus.Black,new Hex(-2, 1, 1));
    //check scores updated
    Assert.assertEquals(model.getWhiteScore(), 1);
    Assert.assertEquals(model.getBlackScore(), 7);
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
    model.placeDisc(DiscStatus.Black,new Hex(2, -1, -1));
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
    Assert.assertEquals(model.getWhiteScore(), 3);
    Assert.assertEquals(model.getBlackScore(), 3);
    Assert.assertEquals(model.getWhiteScore(), model.getBlackScore());
  }

  @Test
  public void testBlackScoreIncreasesAfterBlackMove() {
    init();
    model.setup();
    Assert.assertEquals(model.getBlackScore(), 3);
    model.placeDisc(DiscStatus.Black,new Hex(2, -1, -1));
    Assert.assertEquals(model.getBlackScore(), 5);
  }

  @Test
  public void testWhiteScoreDecreasesAfterBlackMove() {
    init();
    model.setup();
    Assert.assertEquals(model.getWhiteScore(), 3);
    model.placeDisc(DiscStatus.Black,new Hex(2, -1, -1));
    Assert.assertEquals(model.getWhiteScore(), 2);
  }

  @Test
  public void testWhiteScoreIncreasesAfterWhiteMove() {
    init();
    model.setup();
    Assert.assertEquals(model.getWhiteScore(), 3);
    model.placeDisc(DiscStatus.Black,new Hex(2, -1, -1));
    Assert.assertEquals(model.getWhiteScore(), 2);
    model.placeDisc(DiscStatus.White, new Hex(-2, 1, 1));
    Assert.assertEquals(model.getWhiteScore(), 4);
  }


  @Test
  public void testBlackScoreDecreasesAfterWhiteMove() {
    init();
    model.setup();
    Assert.assertEquals(model.getBlackScore(), 3);
    model.placeDisc(DiscStatus.Black,new Hex(2, -1, -1));
    Assert.assertEquals(model.getBlackScore(), 5);
    model.placeDisc(DiscStatus.White, new Hex(-2, 1, 1));
    Assert.assertEquals(model.getBlackScore(), 4);
  }

  @Test
  public void testScoresStayTheSameAfterPass() {
    init();
    model.setup();
    Assert.assertEquals(model.getBlackScore(), 3);
    Assert.assertEquals(model.getWhiteScore(), 3);
    model.pass(DiscStatus.Black);
    Assert.assertEquals(model.getBlackScore(), 3);
    Assert.assertEquals(model.getWhiteScore(), 3);
    model.placeDisc(DiscStatus.White, new Hex(-2, 1, 1));
    Assert.assertEquals(model.getBlackScore(), 2);
    Assert.assertEquals(model.getWhiteScore(), 5);
    model.pass(DiscStatus.Black);
    Assert.assertEquals(model.getBlackScore(), 2);
    Assert.assertEquals(model.getWhiteScore(), 5);
  }

  //getStatus tests
  @Test
  public void testGetStatusBlackUpdates() {
    init();
    model.setup();
    Assert.assertEquals(model.getStatus(new Hex(1, -1, 0)), DiscStatus.White);
    Assert.assertEquals(model.getStatus(new Hex(0, -1, 1)), DiscStatus.Black);
    model.placeDisc(DiscStatus.Black,new Hex(2, -1, -1));
    Assert.assertEquals(model.getStatus(new Hex(2, -1, -1)), DiscStatus.Black);
    Assert.assertEquals(model.getStatus(new Hex(0, -1, 1)), DiscStatus.Black);
    //the white switched to black as it should have
    Assert.assertEquals(model.getStatus(new Hex(1, -1, 0)), DiscStatus.Black);
  }

  @Test
  public void testGetStatusWhiteUpdates() {
    init();
    model.setup();
    model.pass(DiscStatus.Black);
    Assert.assertEquals(model.getStatus(new Hex(1, -1, 0)), DiscStatus.White);
    Assert.assertEquals(model.getStatus(new Hex(0, -1, 1)), DiscStatus.Black);
    model.placeDisc(DiscStatus.White, new Hex(-1, -1, 2));
    Assert.assertEquals(model.getStatus(new Hex(-1, -1, 2)), DiscStatus.White);
    //the black switched to white as it should have
    Assert.assertEquals(model.getStatus(new Hex(0, -1, 1)), DiscStatus.White);
    Assert.assertEquals(model.getStatus(new Hex(1, -1, 0)), DiscStatus.White);
  }

  @Test
  public void testGridSizeCannotChange() {
    init();
    model.setup();
    int gridSize = model.getGridSize();
    Assert.assertEquals(model.getGridSize(), 11);
    Assert.assertEquals(gridSize, 11);
    gridSize = 12;
    Assert.assertEquals(model.getGridSize(), 11);
    Assert.assertEquals(gridSize, 12);
  }

  @Test
  public void testGridSizeGetCorrectGridSizeForDifferentGrids() {
    init();
    ReversiModel reversi = new HexReversi(7);
    reversi.setup();
    Assert.assertEquals(reversi.getGridSize(), 7);
    ReversiModel reversi2 = new HexReversi(9);
    reversi2.setup();
    Assert.assertEquals(reversi2.getGridSize(), 9);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCannotMakeEvenNumberedGrids() {
    init();
    ReversiModel reversi = new HexReversi(4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCannotMakeGridsSmallerThan3() {
    init();
    ReversiModel reversi = new HexReversi(1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCannotMakeGridsSmallerThan3AndEven() {
    init();
    ReversiModel reversi = new HexReversi(2);
  }

  //placeDisc tests
  @Test
  public void testPlaceDiscToTheRight() {
    init();
    model5.setup();
    model5.placeDisc(DiscStatus.Black,new Hex(2, -1, -1));
    Assert.assertEquals(model5.getStatus(new Hex(2, -1, -1)), DiscStatus.Black);
    Assert.assertEquals(model5.getStatus(new Hex(1, -1, 0)), DiscStatus.Black);
    Assert.assertEquals(model5.getStatus(new Hex(0, -1, 1)), DiscStatus.Black);
  }

  @Test
  public void testPlaceDiscToTheBottomLeft() {
    init();
    model5.setup();
    model5.placeDisc(DiscStatus.Black,new Hex(-2, 1, 1));
    Assert.assertEquals(model5.getStatus(new Hex(-2, 1, 1)), DiscStatus.Black);
    Assert.assertEquals(model5.getStatus(new Hex(-1, 0, 1)), DiscStatus.Black);
    Assert.assertEquals(model5.getStatus(new Hex(0, -1, 1)), DiscStatus.Black);
  }

  @Test
  public void testPlaceDiscToTheLeft() {
    init();
    model5.setup();
    model5.pass(DiscStatus.Black);
    model5.placeDisc(DiscStatus.White, new Hex(-1, -1, 2));
    Assert.assertEquals(model5.getStatus(new Hex(-1, -1, 2)), DiscStatus.White);
    Assert.assertEquals(model5.getStatus(new Hex(0, -1, 1)), DiscStatus.White);
    Assert.assertEquals(model5.getStatus(new Hex(1, -1, 0)), DiscStatus.White);
  }

  @Test
  public void testPlaceDiscToTheBottomRight() {
    init();
    model5.setup();
    model5.pass(DiscStatus.Black);
    model5.placeDisc(DiscStatus.White, new Hex(1, 1, -2));
    Assert.assertEquals(model5.getStatus(new Hex(1, 1, -2)), DiscStatus.White);
    Assert.assertEquals(model5.getStatus(new Hex(1, 0, -1)), DiscStatus.White);
    Assert.assertEquals(model5.getStatus(new Hex(1, -1, 0)), DiscStatus.White);
  }

  @Test
  public void testPlaceDiscToTheTopLeft() {
    init();
    model5.setup();
    model5.placeDisc(DiscStatus.Black,new Hex(-1, -1, 2));
    Assert.assertEquals(model5.getStatus(new Hex(-1, -1, 2)), DiscStatus.Black);
    Assert.assertEquals(model5.getStatus(new Hex(-1, 0, 1)), DiscStatus.Black);
    Assert.assertEquals(model5.getStatus(new Hex(-1, 1, 0)), DiscStatus.Black);
  }

  @Test
  public void testPlaceDiscToTheTopRight() {
    init();
    model5.setup();
    model5.pass(DiscStatus.Black);
    model5.placeDisc(DiscStatus.White, new Hex(2, -1, -1));
    Assert.assertEquals(model5.getStatus(new Hex(2, -1, -1)), DiscStatus.White);
    Assert.assertEquals(model5.getStatus(new Hex(1, 0, -1)), DiscStatus.White);
    Assert.assertEquals(model5.getStatus(new Hex(0, 1, -1)), DiscStatus.White);
  }

  //placeDisc Fails
  @Test(expected = IllegalStateException.class)
  public void testPlaceDiscDirectLineSpaceInTheMiddle() {
    init();
    model5.setup();
    model5.pass(DiscStatus.Black);
    model5.placeDisc(DiscStatus.White, new Hex(2, 0, -2));
    Assert.assertFalse(model5.isGameOver());
  }

  @Test(expected = IllegalStateException.class)
  public void testPlaceDiscNoAdjacentOppositeWhiteMove() {
    init();
    model5.setup();
    model5.pass(DiscStatus.Black);
    model5.placeDisc(DiscStatus.White, new Hex(0, 2, -2));
    Assert.assertFalse(model5.isGameOver());
  }

  @Test(expected = IllegalStateException.class)
  public void testPlaceDiscNoAdjacentOppositeBlackMove() {
    init();
    model5.setup();
    model5.placeDisc(DiscStatus.Black,new Hex(0, -2, 2));
    Assert.assertFalse(model5.isGameOver());
  }

  @Test(expected = IllegalStateException.class)
  public void testAdjacentExistsButNoDirectLineBlackMove() {
    init();
    model5.setup();
    model5.placeDisc(DiscStatus.Black,new Hex(0, 0, 0));
    Assert.assertFalse(model5.isGameOver());
  }

  @Test(expected = IllegalStateException.class)
  public void testAdjacentExistsButNoDirectLineWhiteMove() {
    init();
    model5.setup();
    model5.pass(DiscStatus.Black);
    model5.placeDisc(DiscStatus.White,new Hex(0, 0, 0));
    Assert.assertFalse(model5.isGameOver());
  }

  @Test(expected = IllegalStateException.class)
  public void testDirectLineLeadsOffGridBlackMove() {
    init();
    model5.setup();
    model5.pass(DiscStatus.Black);
    model5.placeDisc(DiscStatus.White, new Hex(-2, 1, 1));
    model5.placeDisc(DiscStatus.Black, new Hex(1, 1, -2));
    Assert.assertFalse(model5.isGameOver());
  }

  @Test(expected = IllegalStateException.class)
  public void testDirectLineLeadsOffGridWhiteMove() {
    init();
    model5.setup();
    model5.placeDisc(DiscStatus.Black, new Hex(2, -1, -1));
    model5.placeDisc(DiscStatus.White, new Hex(-1, -1, 2));
    Assert.assertFalse(model5.isGameOver());
  }

  @Test(expected = IllegalStateException.class)
  public void testCannotPlaceDiscOnNonEmptyCell() {
    init();
    model5.setup();
    model5.placeDisc(DiscStatus.Black, new Hex(0, 1, -1));
    Assert.assertFalse(model5.isGameOver());
  }

  @Test(expected = IllegalStateException.class)
  public void testCannotPlaceDiscOnNonEmptyCellEvenWhenValidMove() {
    init();
    model5.setup();
    model5.pass(DiscStatus.Black);
    model5.placeDisc(DiscStatus.White, new Hex(-1, -1, 2));
    model5.placeDisc(DiscStatus.Black, new Hex(-1, -1, 2));
    Assert.assertFalse(model5.isGameOver());
  }

  @Test
  public void testAllNecessaryDiscsFlip() {
    init();
    model7.setup();
    model7.placeDisc(DiscStatus.Black, new Hex(-2, 1, 1));
    model7.placeDisc(DiscStatus.White, new Hex(-3, 1, 2));
    model7.pass(DiscStatus.Black);
    model7.placeDisc(DiscStatus.White,  new Hex(-1, -1, 2));
    Assert.assertEquals(view7.toString(), "   _ _ _ _ \n" +
            "  _ _ _ _ _ \n" +
            " _ O O O _ _ \n" +
            "_ _ O _ X _ _ \n" +
            " O O O O _ _ \n" +
            "  _ _ _ _ _ \n" +
            "   _ _ _ _ \n");
    Assert.assertFalse(model7.isGameOver());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNotValidHexCoordinates() {
    init();
    model5.setup();
    model5.placeDisc(DiscStatus.Black, new Hex(1, 1, 1));
    Assert.assertFalse(model5.isGameOver());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidHexCoordinatesButNotOnGrid() {
    init();
    model5.setup();
    model5.placeDisc(DiscStatus.Black, new Hex(-3, 1, 2));
    Assert.assertFalse(model5.isGameOver());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidHexCoordinatesValidMoveNotOnGrid() {
    model7.setup();
    model7.placeDisc(DiscStatus.Black, new Hex(-2, 1, 1));
    model7.placeDisc(DiscStatus.White, new Hex(-3, 1, 2));
    model7.pass(DiscStatus.Black);
    model7.placeDisc(DiscStatus.White, new Hex(-1, -1, 2));
    model7.placeDisc(DiscStatus.Black, new Hex(-1, 2, -1));
    model7.placeDisc(DiscStatus.White, new Hex(-4, 1, 3));
    Assert.assertFalse(model7.isGameOver());
  }

  @Test
  public void testPossibleMovesToBeMadeOnStart() {
    init();
    model5.setup();
    Assert.assertTrue(model5.anyPossibleMoves());
    model5.placeDisc(DiscStatus.Black, new Hex(2, -1, -1));
    Assert.assertTrue(model5.anyPossibleMoves());
    model5.placeDisc(DiscStatus.White, new Hex(1, -2, 1));
    Assert.assertTrue(model5.anyPossibleMoves());
    model5.placeDisc(DiscStatus.Black, new Hex(-1, 2, -1));
    model5.pass(DiscStatus.White);
    model5.placeDisc(DiscStatus.Black, new Hex(-1, -1, 2));
    Assert.assertEquals(model5.getWhiteScore(), 1);
    Assert.assertEquals(model5.getBlackScore(), 9);
    model5.pass(DiscStatus.White);
    model5.pass(DiscStatus.Black);
    Assert.assertTrue(model5.isGameOver());
  }


  @Test
  public void testStartingToString() {
    init();
    model.setup();
    Assert.assertEquals(view.toString(),
            "     _ _ _ _ _ _ \n" +
                    "    _ _ _ _ _ _ _ \n" +
                    "   _ _ _ _ _ _ _ _ \n" +
                    "  _ _ _ _ _ _ _ _ _ \n" +
                    " _ _ _ _ X O _ _ _ _ \n" +
                    "_ _ _ _ O _ X _ _ _ _ \n" +
                    " _ _ _ _ X O _ _ _ _ \n" +
                    "  _ _ _ _ _ _ _ _ _ \n" +
                    "   _ _ _ _ _ _ _ _ \n" +
                    "    _ _ _ _ _ _ _ \n" +
                    "     _ _ _ _ _ _ \n");

  }

  @Test
  public void testStartingToStringDifferentGridSize() {
    init();
    model5.setup();
    Assert.assertEquals(view5.toString(), "  _ _ _ \n" +
            " _ X O _ \n" +
            "_ O _ X _ \n" +
            " _ X O _ \n" +
            "  _ _ _ \n");
  }

  @Test
  public void testStartingToStringDifferentGridSize7() {
    init();
    model7.setup();
    Assert.assertEquals(view7.toString(), "   _ _ _ _ \n" +
            "  _ _ _ _ _ \n" +
            " _ _ X O _ _ \n" +
            "_ _ O _ X _ _ \n" +
            " _ _ X O _ _ \n" +
            "  _ _ _ _ _ \n" +
            "   _ _ _ _ \n");
  }

  @Test
  public void testInitialPlaceDisc() {
    init();
    model.setup();
    model.placeDisc(DiscStatus.Black, new Hex(2, -1, -1));
    Assert.assertEquals(view.toString(), "     _ _ _ _ _ _ \n" +
            "    _ _ _ _ _ _ _ \n" +
            "   _ _ _ _ _ _ _ _ \n" +
            "  _ _ _ _ _ _ _ _ _ \n" +
            " _ _ _ _ X X X _ _ _ \n" +
            "_ _ _ _ O _ X _ _ _ _ \n" +
            " _ _ _ _ X O _ _ _ _ \n" +
            "  _ _ _ _ _ _ _ _ _ \n" +
            "   _ _ _ _ _ _ _ _ \n" +
            "    _ _ _ _ _ _ _ \n" +
            "     _ _ _ _ _ _ \n");
  }

  @Test(expected = IllegalStateException.class)
  public void testIllegalMoveSpaceBetween() {
    init();
    model.setup();
    model.placeDisc(DiscStatus.Black, new Hex(0, 2, -2));
    Assert.assertEquals(view.toString(), "     _ _ _ _ _ _ \n" +
            "    _ _ _ _ _ _ _ \n" +
            "   _ _ _ _ _ _ _ _ \n" +
            "  _ _ _ _ _ _ _ _ _ \n" +
            " _ _ _ _ X O _ _ _ _ \n" +
            "_ _ _ _ O _ X _ _ _ _ \n" +
            " _ _ _ _ X O _ _ _ _ \n" +
            "  _ _ _ _ _ _ _ _ _ \n" +
            "   _ _ _ _ _ _ _ _ \n" +
            "    _ _ _ _ _ _ _ \n" +
            "     _ _ _ _ _ _ \n");
  }



}
