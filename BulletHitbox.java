import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Hitbox for the Bullet class
 */
public class BulletHitbox extends Hitbox
{
    public BulletHitbox(Actor target) {
        this(target, 0, 0, target.getImage().getWidth(), target.getImage().getHeight(), true, true, VehicleWorld.SEE_BULLET_HITBOX);
    }
    // Hitbox constructor with all principal components
    public BulletHitbox(Actor target, int xOffset, int yOffset, int width, int height, boolean followTarget, boolean rotateWithTarget) {
        this(target, xOffset, yOffset, width, height, followTarget, rotateWithTarget, VehicleWorld.SEE_BULLET_HITBOX);
    }
    
    // Same as above constructor, but can specify visibility (for testing purposes/closer look at interactions)
    public BulletHitbox(Actor target, int xOffset, int yOffset, int width, int height, boolean followTarget, boolean rotateWithTarget, boolean visible) {
        super(target, xOffset, yOffset, width, height, followTarget, rotateWithTarget, visible);
    }
    public void act()
    {
        super.act();
    }
    public boolean checkHitPedestrian () {
        Pedestrian p = (Pedestrian)getOneIntersectingObject(Pedestrian.class);
        if(p != null && (p.isAwake() || target instanceof BulletLuigi)) {
            BulletVictim b = new BulletVictim(p, (Bullet)(this.target));
            getWorld().addObject(b, p.getX(), p.getY());
            getWorld().removeObject(p);
            GreenfootSound bumpSound = new GreenfootSound("bulletbump_p.wav");
            bumpSound.setVolume(70);
            bumpSound.play();
            return true;
        }
        return false;
    }
    public boolean checkHitVehicle() {
        RoadVehicleHitbox vb = (RoadVehicleHitbox)getOneIntersectingObject(RoadVehicleHitbox.class);
        if(vb != null) {
            RoadVehicle v = (RoadVehicle)vb.getTarget();
            if(v instanceof Bus) {
                return false;
            }
            BulletVictim b = new BulletVictim(v, (Bullet)(this.target));
            GreenfootSound bumpSound = new GreenfootSound("bulletbump_v.wav");
            bumpSound.setVolume(70);
            bumpSound.play();
            v.removeHitbox();
            getWorld().addObject(b, v.getX(), v.getY());
            getWorld().removeObject(v);
            return true;
        }
        return false;
    }
}
