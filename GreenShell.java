import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
/**
 * Type of Projectile which spawns from GreenShell. GreenShell gets knocked around by Vehicles and Pedestrians. Goombas get knocked down
 * 
 * Sprite Credits:
 * https://www.spriters-resource.com/snes/smarioworld/sheet/143271/
 * Audio Credits:
 * https://www.sounds-resource.com/wii_u/mariokart8/sound/3864/
 */
public class GreenShell extends Projectile
{
    private Koopa origin;
    private double angle;
    Hitbox lastHitbox;
    public GreenShell(Koopa origin) {
        this.target = target;
        this.x = origin.getX();
        this.y = origin.getY();
        speed = 4;
        this.angle = origin.direction == 1 ? 90 : 270;
        
        setAnimationCycle("greenshell", ".png", 4);
        lastHitbox = null; // track the last hitbox that Greenshell hit --> work-around for glitch where shell would stay bouncing around withinin the same vehicle

    }
    public void addedToWorld(World w) {
        setLocation(x, y);
        checkHit();
    }
    
    public void act()
    {
        moveAtAngle();  // mostly functional movement method, which moves actor at an angle of this.angle
        // cycle sprite
        if(imgActCount%6 == 0) {
            cycleAvatar();
        }
        imgActCount++;
        // remove if out of bounds
        if(outOfBounds()) {
            getWorld().removeObject(this);
            return;
        }
        // check if GreenShell hit vehicle or pedestrian
        checkHit();
    }
    public boolean checkHitVehicle() {
        RoadVehicleHitbox vh = (RoadVehicleHitbox)getOneObjectAtOffset((int)nextX(), (int)nextY(), RoadVehicleHitbox.class);
        if(vh == null || vh == lastHitbox) {
            return false;
        }
        lastHitbox = vh;
        // check above & below --> side collisions are handled in RoadVehicle
        if(getY() < vh.getY()-vh.getHeight()/2) {
            setAngle(270-angle);
        } else {
            setAngle(270+angle);
        }
        return true;
        
    }
    public boolean checkHitPedestrian() {
        PedHitbox ph = (PedHitbox)getOneObjectAtOffset((int)nextX(), (int)nextY(), PedHitbox.class);
        if(ph == null || ph == lastHitbox) {
            return false;
        }
        Pedestrian p = (Pedestrian)ph.getTarget();
        if(!p.isAwake()){
            return false;
        }
        lastHitbox = ph;
        if(p instanceof Goomba) { // Goombas are weakest --> get knocked down
            p.knockDown();
        } else {
            if(p instanceof Koopa) {  // Koopas knock the GreenShell, but also get knocked back themselves
                ((Koopa)p).pushKoopaBack(angle);
            }
            if(getX() < ph.getX()-ph.getWidth()/2) {  // change angle based on collision
                setAngle(270-angle);
            } else {
                setAngle(270+angle);
            }
            
        }
        return true;
    }
    public boolean checkHit() {
        boolean temp = checkHitVehicle();
        if(!temp) { // if not hit vehicle, check for pedestrians
            if (!checkHitPedestrian()) {
                return false;
            }
        }
        GreenfootSound greenshell = new GreenfootSound("shell2.wav");
        greenshell.setVolume(60);
        greenshell.play();
        return true;
    }
    // Getters & Setters
    public void setAngle(double a) {
        angle = a >= 0 ? a : 360+a;
    }
    public double getAngle() {
        return angle;
    }
    public void setSpeed(double s) {
        speed = Math.max(4, s+0.7);
    }
    // Movement Methods
    public double nextX() {  // positive = right (angle 0-90 or 270-360), negative = left (angle 90-180 or 180-270);
        return speed * Math.cos(Math.toRadians(angle));
    }
    public double nextY() {  // positive = down (angle 0-90 or 90-180), negative = up (angle 180-270 or 270-360)
        return speed * Math.sin(Math.toRadians(angle));
    }
    public void moveAtAngle() {
        setLocation(getX() + nextX(), getY() + nextY());
    }
}
