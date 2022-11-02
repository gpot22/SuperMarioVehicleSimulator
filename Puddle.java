import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Puddles cause non-heavy RoadVehiles to slip
 * 
 * Sprite Credits:
 * https://www.istockphoto.com/illustrations/puddle-reflection-sky
 */
public class Puddle extends Effect
{
    private int duration;
    public Puddle(int duration) {
        image = new GreenfootImage("puddle" + Greenfoot.getRandomNumber(3) + ".png");
        setImage(image);
        this.duration = duration;
    }
    public void act()
    {
        if (duration == 0){
            getWorld().removeObject(this);
        } else if (duration <= 90){
            fade (duration, 90);
        }
        duration--;
    }
}
