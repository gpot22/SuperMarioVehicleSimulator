import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
/**
 * The Hitbox class manages all collisions between entities in the world.
 * 
 */
public class Hitbox extends Collider {
    // Most basic Hitbox constructor. Creates hitbox with exact dimensions & position of target actor
    public Hitbox(Actor target) {
        this(target, 0, 0, target.getImage().getWidth(), target.getImage().getHeight(), true, true, false);
    }
    // Hitbox constructor with all principal components
    public Hitbox(Actor target, int xOffset, int yOffset, int width, int height, boolean followTarget, boolean rotateWithTarget) {
        this(target, xOffset, yOffset, width, height, followTarget, rotateWithTarget, false);
    }
    
    // Same as above constructor, but can specify visibility (for testing purposes/closer look at interactions)
    public Hitbox(Actor target, int xOffset, int yOffset, int width, int height, boolean followTarget, boolean rotateWithTarget, boolean visible) {
        // super(target, xOffset, yOffset, followTarget, rotateWithTarget);
        this.target = target;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.followTarget = followTarget;
        this.rotateWithTarget = rotateWithTarget;
        this.width = width;
        this.height = height;
        this.visible = visible;
        this.color = Color.CYAN;
        drawBorder();
    }
    /**
     * Act - do whatever the Hitbox wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        if(followTarget) {
            follow();
        }
        if(target.getWorld() == null) {
            getWorld().removeObject(this);
        }
    }
    protected void drawBorder() {
        GreenfootImage border = new GreenfootImage(width, height);
        if(visible) {  // draw hitbox if visible
            border.setColor(color);
            border.drawRect(0, 0, width-1, height-1);
        }
        setImage(border);
    }
    public void setWidth(int width) {
        this.width = width;
        drawBorder();
    }
    
    public void setHeight(int height) {
        this.height = height;
        drawBorder();
    }
    public void setXOffset(int xOffset) {
        this.xOffset = xOffset;
    }
    public void setYOffset(int yOffset) {
        this.yOffset = yOffset;
    }
    
    
    
    public int getXOffset() {
        return xOffset;
    }
    
    public void syncToTarget() {
        this.width = target.getImage().getWidth();
        this.height = target.getImage().getHeight();
        drawBorder();
    }
    public void reset(int xOffset, int yOffset, int width, int height) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.width = width;
        this.height = height;
        drawBorder();
    }
    
    // change Actor collision detection methods to public
    
    public <A> ArrayList<A> getIntersectingObjects(Class<A> c) {
        return (ArrayList<A>)super.getIntersectingObjects(c);
    }
    
    public Actor getOneIntersectingObject(Class c) {
        return super.getOneIntersectingObject(c);
    }
    
    public Actor getOneObjectAtOffset(int xOffset, int yOffset, Class c) {
        return super.getOneObjectAtOffset(xOffset, yOffset, c);
    }
}
