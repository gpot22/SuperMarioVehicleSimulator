import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Pedestrians are replaced by BulletVictims when hit by a BulletBill or BulletLuigi
 */
public class BulletVictim extends SuperSmoothMover
{
    private Actor origin;
    private Bullet bullet;
    private double speed;
    public BulletVictim (Actor origin, Bullet bullet){
        this.origin = origin;
        this.bullet = bullet;
        setImage(this.origin.getImage());
        speed = Math.max((int)bullet.getSpeed() * 2, 4);
    }
    public void addedToWorld(World w) {
        setLocation(origin.getX(), origin.getY());
        setRotation(Greenfoot.getRandomNumber(360));
        
    }
    public void act()
    {
        move(speed);
        getImage().rotate(20);  // make it seem like the vehicle has been knocked completely out of control, flying in the air out of the world
    }
    
}
