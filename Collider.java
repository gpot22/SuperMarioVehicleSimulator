import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Superclass for actors dedicated to handle collisions --> custom, more accurate collision detections
 */
public abstract class Collider extends SuperSmoothMover {
    protected Actor target;
    protected int xOffset, yOffset;
    protected int width, height;
    protected boolean rotateWithTarget;
    protected boolean followTarget;
    protected boolean visible;
    protected boolean active;
    protected Color color;
    
    protected abstract void drawBorder();
    
    public void follow() {  // collider follows movement of actor
        if(target != null && target.getWorld() != null) {
            setLocation(target.getX() + xOffset, target.getY() + yOffset); 
            if(rotateWithTarget) {
                setRotation(target.getRotation());
            }
        }
    }
    
    public void init() {
        target.getWorld().addObject(this, target.getX() + xOffset, target.getY() + yOffset);
    }
    
    public boolean getActive() {
        return active;
    }
    
    public Actor getTarget() {
        return target;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public int getRight() {
        return getX() + (int)(getWidth()/2);
    }
    public int getLeft() {
        return getX() - (int)(getWidth()/2);
    }
    public int getTop() {
        return getY() -(int)(getHeight()/2);
    }
    public int getBottom() {
        return getY() + (int) (getHeight()/2);
    }
}
