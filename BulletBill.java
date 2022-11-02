import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * BulletBill knocks Pedestrians and Vehicles off of the map. After a certain number of collisions, it explodes
 * 
 * Sprite Credits:
 * https://www.spriters-resource.com/fullview/143539/
 */
public class BulletBill extends Bullet {
    protected VehicleSpawner origin;
    protected boolean moving;

    private int explodeCount;
    
    public BulletBill(VehicleSpawner origin) {
        super(origin, "bullet/bulletbill0a.png");
        setAnimationCycle("bullet/bulletbill", "a.png", 8);
        if(rotation == 180) {
            hitbox = new BulletHitbox(this, -12, 2, 42, 27, true, true);
        } else {
            hitbox = new BulletHitbox(this, 12, 2, 42, 27, true, true);
        }
        maxSpeed = 5;
        explodeCount = 90;
        speed = maxSpeed;
        yOffset = Greenfoot.getRandomNumber(30)-15;
    }
    public void act()
    {
        if(speed <= 2) {
            setSuffix("d.png");
            if(explosionSequence()) {
                return;
            }
        } else if (speed <= 3) {
            setSuffix("c.png");
        } else if(speed <= 4) {
            setSuffix("b.png");
        }
        super.act();
        if(getWorld() == null) {
            return;
        }
        if(imgActCount%10 == 0) {
            cycleAvatar();
            fixAvatarDirection();
        }
        imgActCount++;
        if(checkHitPedestrian()) {
            speed -= 1;
        }
        if(checkHitVehicle()) {
            speed -= 2;
        }
        speed = Math.max(0, speed);
    }
    
    public boolean explosionSequence() {
        explodeCount --;
        if(explodeCount == 0) {
            int explosionSize = Greenfoot.getRandomNumber(40) + 120;
            Explosion e = new Explosion(explosionSize);
            e.initSound();
            getWorld().addObject(e, getX(), getY());
            movingSound.stop();
            getWorld().removeObject(hitbox);
            getWorld().removeObject(this);
            return true;
        }
        return false;
    }
    public boolean checkHitPedestrian() {
        return ((BulletHitbox)hitbox).checkHitPedestrian();
    }
    public boolean checkHitVehicle() {
        return ((BulletHitbox)hitbox).checkHitVehicle();
    }
    public boolean facesRightward() {
        return origin.facesRightward();
    }
    public void setSpeed(int x) {
        speed = x;
    }
}
