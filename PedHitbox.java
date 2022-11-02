import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Hitbox for the Pedestrian class
 */
public class PedHitbox extends Hitbox
{
    public PedHitbox(Actor target) {
        this(target, 0, 0, target.getImage().getWidth(), target.getImage().getHeight(), true, true, VehicleWorld.SEE_PEDESTRIAN_HITBOX);
    }
    // Hitbox constructor with all principal components
    public PedHitbox(Actor target, int xOffset, int yOffset, int width, int height, boolean followTarget, boolean rotateWithTarget) {
        this(target, xOffset, yOffset, width, height, followTarget, rotateWithTarget, VehicleWorld.SEE_PEDESTRIAN_HITBOX);
    }
    
    // Same as above constructor, but can specify visibility (for testing purposes/closer look at interactions)
    public PedHitbox(Actor target, int xOffset, int yOffset, int width, int height, boolean followTarget, boolean rotateWithTarget, boolean visible) {
        super(target, xOffset, yOffset, width, height, followTarget, rotateWithTarget, visible);
    }
    public void act()
    {
        super.act();
    }
}
