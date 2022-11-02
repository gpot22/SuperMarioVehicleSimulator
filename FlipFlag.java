import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Flag which tells a RoadVehicle to flip its image vertically
 */
public class FlipFlag extends TurnFlag {
    public FlipFlag(int x, int y, int width, int height){//, int radius, boolean leftCurve) {
        super();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        // this.turnRadius = radius;
        // this.leftCurve = leftCurve;
        color = Color.GREEN;
        drawBorder();
    }
}
