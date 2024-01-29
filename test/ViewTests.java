import org.junit.Assert;
import org.junit.Test;

import cs3500.reversi.model.HexReversi;
import cs3500.reversi.model.DiscStatus;
import cs3500.reversi.model.Hex;
import cs3500.reversi.model.ReversiModel;

/**
 * Test class for the view of a reversi game.
 */
public class ViewTests {

  @Test
  public void testViewCorrectCoords() {
    ReversiModel model = new HexReversi();
    model.setup();
    ReversiViewMock view = new ReversiViewMock(model);
    ReversiPanelMock.MouseEventsListener listener = null;
    view.panel.transformPixelToHex();
    Assert.assertEquals(view.panel.translateToRoundedHex(0,0), new Hex(0,0,0));
  }

  @Test
  public void testViewCorrectCoords2() {
    ReversiModel model = new HexReversi();
    model.setup();
    ReversiViewMock view = new ReversiViewMock(model);
    ReversiPanelMock.MouseEventsListener listener = null;
    view.panel.transformPixelToHex();
    Assert.assertEquals(view.panel.translateToRoundedHex(-.5,-2.5), new Hex(1,-3,2));
  }

  @Test
  public void testPassOnStart() {
    ReversiModel model = new HexReversi();
    model.setup();
    model.pass(DiscStatus.Black);
    Assert.assertFalse(model.isGameOver());
  }


}
