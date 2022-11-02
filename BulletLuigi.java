import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * BulletLuigi acts like BulletBill, but spawns during a Luigi BulletStorm
 * 
 * Sprite Credits:
 * https://www.spriters-resource.com/snes/smarioworld/sheet/143271/
 * https://www.spriters-resource.com/fullview/143539/
 * Edited by Caden Chan
 */
public class BulletLuigi extends Bullet
{
    private int startX, startY;
    private double speed;
    public static int soundCount = 0;
    public BulletLuigi(String startImage, int startX, int startY, double speed) {
        super(startImage);
        this.startX = startX;
        this.startY = startY;
        this.speed = speed;
        hitbox = new BulletHitbox(this, 0, 10, 42, 27, true, true);
    }
    public void addedToWorld(World w) {
        hitbox.init();
        setRotation(90); // face downwards
        setLocation(startX, startY);
        if(soundCount < 3) {
            GreenfootSound startSound = new GreenfootSound("bulletbilllaunch.wav");
            startSound.setVolume(70);
            startSound.play();
            movingSound = new GreenfootSound("bulletbillmoving.wav");
            movingSound.setVolume(35);
            movingSound.play();
            soundCount ++;
        }
    }
    public void act()
    {
        move(speed);
        // Knock down Pedestrians & Vehicles as you move down
        checkHitPedestrian();
        checkHitVehicle();
        if (getY() > VehicleWorld.WORLD_H + 40){
            if(movingSound != null) {
                movingSound.stop();
            }
            getWorld().removeObject(hitbox);
            getWorld().removeObject(this);
        }
        
    }
}
