import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Hitbox class for RoadVehicles
 */
public class LaneChangeHitbox extends Hitbox {
    public LaneChangeHitbox(Actor target) {
        this(target, 0, 0, target.getImage().getWidth(), target.getImage().getHeight(), true, true, VehicleWorld.SEE_LANECHANGE_HITBOX);
    }
    // Hitbox constructor with all principal components
    public LaneChangeHitbox(Actor target, int xOffset, int yOffset, int width, int height, boolean followTarget, boolean rotateWithTarget) {
        this(target, xOffset, yOffset, width, height, followTarget, rotateWithTarget, VehicleWorld.SEE_LANECHANGE_HITBOX);
    }
    
    // Same as above constructor, but can specify visibility (for testing purposes/closer look at interactions)
    public LaneChangeHitbox(Actor target, int xOffset, int yOffset, int width, int height, boolean followTarget, boolean rotateWithTarget, boolean visible) {
        super(target, xOffset, yOffset, width, height, followTarget, rotateWithTarget, visible);
    }
    public void act()
    {
        super.act();
    }
}
