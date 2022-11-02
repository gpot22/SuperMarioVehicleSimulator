import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Flag indicating a small turn (inner-bit of the semicircle)
 */
public class ShallowTurnFlag extends TurnFlag {
    public ShallowTurnFlag(int x, int y, int width, int height, int radius, boolean leftCurve) {
        super();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.turnRadius = radius;
        this.leftCurve = leftCurve;
        color = Color.RED;
        drawBorder();
    }
}
