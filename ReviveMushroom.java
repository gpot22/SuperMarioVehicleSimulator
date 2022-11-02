import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * ReviveMushrooms are thrown by Healers to heal fallen Pedestrians
 * 
 * Sprite Credits:
 * https://www.deviantart.com/epochflipnote/art/Sprite-Green-mushroom-682377414
 */
public class ReviveMushroom extends Projectile
{
    private int rotateImage;
    public ReviveMushroom(Healer origin, Pedestrian target) {
        this.target = target;
        this.x = origin.getX();
        this.y = origin.getY();
        speed = 4;
        rotateImage = 0;
    }
    public void addedToWorld(World w) {
        setLocation(x, y);
        turnTowards(target);
    }
    public void act()
    {
        move(speed);
        // rotate image (visual effect, does not affect actor itself)
        if(rotateImage % 10 == 0) {
            getImage().rotate(60);
        }
        rotateImage++;
        // remove if out of bounds
        if(outOfBounds()) {
            getWorld().removeObject(this);
            return;
        }
        // check if hits pedestrian; ignores vehicles
        checkHit();
    }
    public boolean checkHit() {
        Pedestrian p = (Pedestrian)getOneIntersectingObject(Pedestrian.class);
        if(p == null || p.isAwake()) {
            return false;
        }
        p.healMe();
        getWorld().removeObject(this);
        GreenfootSound reviveSound = new GreenfootSound("1up.wav");
        reviveSound.setVolume(60);
        reviveSound.play();
        return true;
    }
    
}
