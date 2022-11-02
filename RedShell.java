import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
/**
 * Type of Projectile which is shot by EnemyCars. RedShell knocks down Pedestrians (other than Koopas, which get pushed back)
 * and cause Vehicles to "slip"
 * 
 * Sprite Credits:
 * https://www.spriters-resource.com/snes/smarioworld/sheet/143271/
 * 
 * Audio Credits:
 * https://www.sounds-resource.com/wii_u/mariokart8/sound/3864/
 */
public class RedShell extends Projectile
{
    private EnemyCar origin;
    private int saveRotation;
    public RedShell(EnemyCar origin, Pedestrian target) {
        this.target = target;
        this.origin = origin;
        this.x = origin.getX();
        this.y = origin.getY();
        speed = Greenfoot.getRandomNumber(3) + 4;
        setAnimationCycle("redshell", ".png", 4);
        saveRotation = 0;
    }
    public void addedToWorld(World w) {
        super.addedToWorld(w);
        GreenfootSound throwSound = new GreenfootSound("throw.wav");
        throwSound.setVolume(60);
        throwSound.play();
    }
    public void act()
    {
        
        if(target.getWorld() != null && ((Pedestrian)target).isAwake()) {
            turnTowards(target);
            saveRotation = getRotation();  // preserve direction of shell, for when/if target is removed from world
        } else {  
            setRotation(saveRotation); // rotate to where the target was, right before it got removed from world/became awake
        }
        move(speed);
        setRotation(0); // maintains sprite rotation, while still moving at an angle --> try to implement for GreenShell?
        // sprite cycle
        if(imgActCount%6 == 0) {
            cycleAvatar();
        }
        imgActCount++;
        // remove if out of bounds
        if(outOfBounds()) {
            getWorld().removeObject(this);
            return;
        }
        // check if hit vehicle or pedestrian
        checkHit();

    }
    public boolean checkHitPedestrian() {
        Pedestrian p = (Pedestrian)getOneIntersectingObject(Pedestrian.class);
        if(p == null || !p.isAwake()) {  // ignore if pedestrian is not awake
            return false;
        }
        if(p instanceof Koopa) {
            ((Koopa)p).pushKoopaBack(360-saveRotation); // Koopa's shell protects them, only gets pushed back
        } else {
            p.knockDown();  // all pedestrians other than Koopa get knocked down when hit by red shell
        }
        getWorld().removeObject(this);
        return true;
    }
    public boolean checkHitVehicle() {
        ArrayList<RoadVehicle> touchingVehicles = (ArrayList<RoadVehicle>)getIntersectingObjects(RoadVehicle.class);
        touchingVehicles.removeIf(v -> v == origin);  // work-around to avoid RedShell detecting the vehicle that shot it as a target
        if(touchingVehicles.size() == 0) {
            return false;
        }
        RoadVehicle v = touchingVehicles.get(0);
        if(!v.isSlipping()) {  // play slipping animation for vehicle that gets hit by red shell
            v.startSlipping();
        }
        getWorld().removeObject(this);
        return true;
    }
    public boolean checkHit() {
        boolean temp = checkHitVehicle();
        if(!temp) { // if not hit vehicle, check for pedestrians
            return checkHitPedestrian();
        }
        return true;
    }
}
