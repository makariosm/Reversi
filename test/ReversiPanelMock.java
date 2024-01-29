import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.BasicStroke;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.Objects;
import cs3500.reversi.model.Hex;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;
import cs3500.reversi.model.ReadOnlyReversi;

/**
 * Mock for a reversi panel to test the view class.
 */
public class ReversiPanelMock extends JPanel {
  private final ReadOnlyReversi model;

  private Hex currHexSelection;

  protected boolean alreadySelected;


  /**
   * Constructs a mock for a reversi panel. Used to mock view and reveal functionality in tests.
   * @param model the read only model used to create the panel and its contents.
   */
  public ReversiPanelMock(ReadOnlyReversi model) {
    this.model = Objects.requireNonNull(model);
    ReversiPanelMock.MouseEventsListener listener = new ReversiPanelMock.MouseEventsListener();
    this.addMouseListener(listener);
    this.addMouseMotionListener(listener);

    this.alreadySelected = false;
    this.currHexSelection = new Hex(model.getGridSize(), model.getGridSize(),
            -2 * model.getGridSize());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Dimension getPreferredSize() {
    return new Dimension(750, 750);
  }

  private Dimension getPreferredLogicalSize() {
    return new Dimension(model.getGridSize(), model.getGridSize());
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g.create();
    g2d.setColor(Color.darkGray);
    Rectangle bounds = this.getBounds();
    g2d.fill(bounds);
    g2d.transform(transformHexToPixel());
    int coord = (int) Math.floor(model.getGridSize() / 2);
    for (int q = -coord; q <= coord; q++) {
      for (int r = -coord; r <= coord; r++) {
        for (int s = -coord; s <= coord; s++) {
          if (q + r + s == 0) {
            this.drawHex(g2d, q, r, this.currHexSelection.equals(new Hex(q, r, s))
                    && !this.alreadySelected);
          }
        }
      }
    }
  }

  protected void drawHex(Graphics2D g2d, double q, double r, boolean selected) {
    this.drawHexagon(g2d, q, r, selected);
    switch (model.getStatus(new Hex((int) q, (int) r, (int) (-q - r)))) {
      case White:
        this.drawCircle(g2d, Color.WHITE, q, r);
        break;
      case Black:
        this.drawCircle(g2d, Color.BLACK, q, r);
        break;
      default:
        break;
    }
  }

  protected void drawCircle(Graphics2D g2d, Color color, double q, double r) {
    AffineTransform oldTransform = g2d.getTransform();
    q = q / 2;
    r = r / 2;
    g2d.translate(q, r);
    g2d.setColor(color);
    q = q + r;
    if (r > 0) {
      r = (r - 2 * (Math.abs(r) * 0.1374));
    } else if (r < 0) {
      r = (r + 2 * (Math.abs(r) * 0.1374));
    }
    Shape circle = new Ellipse2D.Double(
            q - 0.3,     // left
            r - 0.3,     // top
            0.6,  // width
            0.6); // height
    g2d.fill(circle);
    g2d.setTransform(oldTransform);
  }

  protected void drawHexagon(Graphics2D g2d, double x, double y, boolean selected) {
    AffineTransform oldTransform = g2d.getTransform();
    x = x / 2;
    y = y / 2;
    g2d.translate(x, y);
    x = x + y;
    if (y > 0) {
      y = (y - 2 * (Math.abs(y) * 0.1374));
    } else if (y < 0) {
      y = (y + 2 * (Math.abs(y) * 0.1374));
    }
    Path2D hexagonInProgress = new Path2D.Double();
    hexagonInProgress.moveTo(x, y - 0.577);
    hexagonInProgress.lineTo(x + 0.5, y - 0.2887);
    hexagonInProgress.lineTo(x + 0.5, y + 0.2887);
    hexagonInProgress.lineTo(x, y + 0.577);
    hexagonInProgress.lineTo(x - 0.5, y + 0.2887);
    hexagonInProgress.lineTo(x - 0.5, y - 0.2887);
    hexagonInProgress.lineTo(x, y - 0.577);
    if (!selected) {
      g2d.setColor(Color.GRAY);
    } else {
      g2d.setColor(Color.CYAN);
    }
    g2d.fill(hexagonInProgress);
    g2d.setColor(Color.BLACK);
    g2d.setStroke(new BasicStroke(0.025F));
    g2d.draw(hexagonInProgress);
    g2d.setTransform(oldTransform);
  }


  private AffineTransform transformHexToPixel() {
    AffineTransform ret = new AffineTransform();
    Dimension preferred = getPreferredLogicalSize();
    ret.translate(getWidth() / 2., getHeight() / 2.);
    ret.scale(getWidth() / preferred.getWidth(), getHeight() / preferred.getHeight());

    return ret;
  }

  /**
   * Transforms a pixel coordinate to a hex coordinate.
   * @return an Affine of the transformed coordinates.
   */
  public AffineTransform transformPixelToHex() {
    AffineTransform ret = new AffineTransform();
    Dimension preferred = getPreferredLogicalSize();
    ret.scale(preferred.getWidth() / getWidth(), preferred.getHeight() / getHeight());
    ret.translate(-getWidth() / 2., -getHeight() / 2.);

    return ret;
  }

  /**
   * After transforming to the wanted coordinate system, it returns coordinates as floats, but.
   * model only works with integers, so it rounds to the correct integer.
   *
   * @param x q coordinate of the given hex
   * @param y r coordinate of the given hex
   * @return final rounded hex with integer q,r,s coordinates
   */
  public Hex translateToRoundedHex(double x, double y) {
    double q = ((double) 20 / 11) * model.getGridSize() * (Math.sqrt(3) / 3 * x - 1. / 3 * y) /
            getPreferredLogicalSize().width;
    double r = ((double) 20 / 11) * model.getGridSize() * (2. / 3 * y)
            / getPreferredLogicalSize().height;

    double s = -q - r;

    int qi = (int) Math.round(q);
    int ri = (int) Math.round(r);
    int si = (int) Math.round(s);
    double q_diff = Math.abs(qi - q);
    double r_diff = Math.abs(ri - r);
    double s_diff = Math.abs(si - s);
    if (q_diff > r_diff && q_diff > s_diff) {
      qi = -ri - si;
    } else if (r_diff > s_diff) {
      ri = -qi - si;
    } else {
      si = -qi - ri;
    }

    if (q < 0 && Math.floor(q) == -Math.ceil((double) model.getGridSize() / 2)) {
      qi = (int) Math.ceil(q);
      ri = (int) Math.round(r);
      si = -qi - ri;
    } else if (Math.ceil(q) == Math.ceil((double) model.getGridSize() / 2)) {
      qi = (int) Math.floor(q);
      ri = (int) Math.round(r);
      si = -qi - ri;
    } else if (r < 0 && qi + ri == -Math.ceil((double) model.getGridSize() / 2)) {
      ri = (int) Math.ceil(r);
      qi = (int) Math.round(q);
      si = -qi - ri;
    } else if (qi + ri == Math.ceil((double) model.getGridSize() / 2)) {
      ri = (int) Math.floor(r);
      qi = (int) Math.round(q);
      si = -qi - ri;
    }

    return new Hex(qi, ri, si);
  }

  /**
   * Gets the hex with rounded coordinates using rounded hex coordinates.
   *
   * @param x q coordinate of the given hex
   * @param y r coordinate of the given hex
   * @return final rounded hex with integer q,r,s coordinates
   */
  public Hex getRoundedHex(double x, double y) {
    double q = ((double) 20 / 11) * model.getGridSize() * (Math.sqrt(3) / 3 * x - 1. / 3 * y)
            / getPreferredLogicalSize().width;
    double r = ((double) 20 / 11) * model.getGridSize() * (2. / 3 * y)
            / getPreferredLogicalSize().height;

    double s = -q - r;


    int qi = (int) Math.round(q);
    int ri = (int) Math.round(r);
    int si = (int) Math.round(s);
    double q_diff = Math.abs(qi - q);
    double r_diff = Math.abs(ri - r);
    double s_diff = Math.abs(si - s);
    if (q_diff > r_diff && q_diff > s_diff) {
      qi = -ri - si;
    } else if (r_diff > s_diff) {
      ri = -qi - si;
    } else {
      si = -qi - ri;
    }

    if (q < 0 && Math.floor(q) == -Math.ceil((double) model.getGridSize() / 2)) {
      qi = (int) Math.ceil(q);
      ri = (int) Math.round(r);
      si = -qi - ri;
    } else if (Math.ceil(q) == Math.ceil((double) model.getGridSize() / 2)) {
      qi = (int) Math.floor(q);
      ri = (int) Math.round(r);
      si = -qi - ri;
    } else if (r < 0 && qi + ri == -Math.ceil((double) model.getGridSize() / 2)) {
      ri = (int) Math.ceil(r);
      qi = (int) Math.round(q);
      si = -qi - ri;
    } else if (qi + ri == Math.ceil((double) model.getGridSize() / 2)) {
      ri = (int) Math.floor(r);
      qi = (int) Math.round(q);
      si = -qi - ri;
    }

    Hex selectedHex = translateToRoundedHex(x, y);
    System.out.println("(" + selectedHex.getQ() + "," + selectedHex.getR() + ")");

    try {
      model.getStatus(new Hex(selectedHex.getQ(), selectedHex.getR(), selectedHex.getS()));
    } catch (IllegalArgumentException exception) {
      if (Math.ceil(r) == Math.ceil((double) model.getGridSize() / 2)) {
        ri = ri - 1;
        qi = qi + 1;
        si = -qi - ri;
      } else if (Math.floor(r) == -Math.ceil((double) model.getGridSize() / 2)) {
        ri = ri + 1;
        qi = qi - 1;
        si = -qi - ri;
      } else {
        return new Hex(qi, ri, si);
      }
    }
    return new Hex(qi, ri, si);
  }

  /**
   * Class used to listen for mouse events, specificallu where a mouse clicked.
   */
  public class MouseEventsListener extends MouseInputAdapter {

    public MouseEventsListener() {
      // not needed
    }


    @Override
    public void mouseClicked(MouseEvent e) {

      // This point is measured in actual physical pixels
      Point physicalP = e.getPoint();
      // For us to figure out which hex it belongs to, we need to transform it
      // into logical coordinates
      Point2D logicalP = transformPixelToHex().transform(physicalP, null);
      Point2D logicalPUpdated = new Point2D.Double(logicalP.getX(), logicalP.getY());


      Hex selectedHex = getRoundedHex(logicalPUpdated.getX(), logicalPUpdated.getY());

      try {
        model.getStatus(new Hex(selectedHex.getQ(), selectedHex.getR(), selectedHex.getS()));
      } catch (IllegalArgumentException exception) {
        return;
      }

      if (ReversiPanelMock.this.currHexSelection.equals(selectedHex)) {
        ReversiPanelMock.this.alreadySelected = !alreadySelected;
      } else {
        ReversiPanelMock.this.alreadySelected = false;
      }

      ReversiPanelMock.this.currHexSelection = selectedHex;

      ReversiPanelMock.this.repaint();
    }
  }

}

