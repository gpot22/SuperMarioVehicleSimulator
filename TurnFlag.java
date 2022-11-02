import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Colliders which tell a vehicle how to behave at a curve on the road
 */
// Tells a vehicle when to turn; Vehicle turns upon colliding a turn flag that pertains to it.
public abstract class TurnFlag extends Collider {
    protected int turnRadius;
    protected boolean leftCurve;
    protected int x, y;
    protected int laneWidth;
    public TurnFlag() {
        this.visible = VehicleWorld.SEE_TURN_FLAGS;
    }
    public void drawBorder() {
        GreenfootImage border = new GreenfootImage(width, height);
        if(visible) {
            border.setColor(color);
            border.drawRect(0, 0, width-1, height-1);
        }
        setImage(border);
    }
    public double getTurnLength() { // circumference of half-circle --> turning path
        return (double)turnRadius * Math.PI;
    }
    public boolean isLeftCurve() {
        return leftCurve;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    
    
}
