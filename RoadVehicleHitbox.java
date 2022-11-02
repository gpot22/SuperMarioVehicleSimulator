import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Hitbox class for RoadVehicles
 */
public class RoadVehicleHitbox extends Hitbox {
    public RoadVehicleHitbox(Actor target) {
        this(target, 0, 0, target.getImage().getWidth(), target.getImage().getHeight(), true, true, VehicleWorld.SEE_ROADVEHICLE_HITBOX);
    }
    // Hitbox constructor with all principal components
    public RoadVehicleHitbox(Actor target, int xOffset, int yOffset, int width, int height, boolean followTarget, boolean rotateWithTarget) {
        this(target, xOffset, yOffset, width, height, followTarget, rotateWithTarget, VehicleWorld.SEE_ROADVEHICLE_HITBOX);
    }
    
    // Same as above constructor, but can specify visibility (for testing purposes/closer look at interactions)
    public RoadVehicleHitbox(Actor target, int xOffset, int yOffset, int width, int height, boolean followTarget, boolean rotateWithTarget, boolean visible) {
        super(target, xOffset, yOffset, width, height, followTarget, rotateWithTarget, visible);
    }
    public void act()
    {
        super.act();
    }
}
