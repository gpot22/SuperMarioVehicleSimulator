import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Flag indicating a bigger turn (outer-bit of the semi-circle)
 */
public class WideTurnFlag extends TurnFlag {
    public WideTurnFlag(int x, int y, int width, int height, int radius, boolean leftCurve) {
        super();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.turnRadius = radius;
        this.leftCurve = leftCurve;
        color = Color.BLUE;
        drawBorder();
    }
}
