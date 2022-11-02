import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Superclass for Bullets (the mario kind)
 * 
 * Audio Credits:
 * https://www.mariomayhem.com/downloads/sounds/new_super_mario_bros_wii_sound_effects.php
 */
public abstract class Bullet extends Vehicle {
    protected VehicleSpawner origin;
    protected boolean moving;
    // Spawn bullet without VehicleSpawner
    protected GreenfootSound movingSound;
    public Bullet(String startImage) {
        super(null);
        setImage(startImage);
    }
    public Bullet(VehicleSpawner origin, String startImage) {
        super(origin, startImage);
        this.origin = origin;
        if(rotation == 180) {
            hitbox = new BulletHitbox(this, -12, 2, 42, 27, true, true);
        } else {
            hitbox = new BulletHitbox(this, 12, 2, 42, 27, true, true);
        }
    }
    public void addedToWorld(World w) {
        super.addedToWorld(w);
        GreenfootSound startSound = new GreenfootSound("bulletbilllaunch.wav");
        startSound.setVolume(80);
        startSound.play();
        movingSound = new GreenfootSound("bulletbillmoving.wav");
        movingSound.setVolume(35);
        movingSound.play();
    }
    public void act()
    {
        super.act();
    }
    // check collisions using methods from BulletHitbox
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
    public GreenfootSound getMovingSound() {
        return movingSound;
    }
}
