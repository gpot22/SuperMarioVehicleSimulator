import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Superclass for actors that spawn from another actor's actions
 */
public abstract class Projectile extends Animator
{
    protected double x, y;
    protected Actor target;
    protected double speed;
    public abstract boolean checkHit();
    
    public void addedToWorld(World w) {
        setLocation(x, y);
        turnTowards(target);
    }
    public boolean outOfBounds() {
        return getX() > getWorld().getWidth() || getX() < 0 || getY() > getWorld().getHeight() || getY() < 0;
    }
}
