import bagel.*;

/**
 * Interface for classes where the content needs to be drawn on a ShadowLife simulation
 */
public interface ContentDrawable {
    Font FONT = new Font("res/VeraMono.ttf", 15);
    void drawContent();
}
