import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Basic type of pedestrian, with no special functionality
 * Sprite Credits:
 * https://www.deviantart.com/asylusgoji91/art/Jeff-The-Goomba-Sprite-sheet-391901949
 * 
 * Audio Credits:
 * "Goomba Happy", "Goomba Sad", https://themushroomkingdom.net/media/mparty8/wav
 */
public class Goomba extends Pedestrian {
    public static final String HEAD = "goombahead.png";
    public static final String FALL = "goombafall.png";
    
    public Goomba(int direction) {
        // choose a random speed
        maxSpeed = Math.random() * 2 + 1;
        speed = maxSpeed;
        
        // set up sprite cycle
        setAnimationCycle("goomba", ".png", 11);
        startImage = "goomba0.png";
        // start as awake 
        awake = true;
        this.direction = direction;
        hitbox = new PedHitbox(this);
        fixAvatarDirection();
    }
    
    public void playHurtSound() {
        GreenfootSound hurtSound = new GreenfootSound("goombahurt.wav");
        hurtSound.setVolume(70);
        hurtSound.play();
    }
    public void playReviveSound() {
        GreenfootSound reviveSound = new GreenfootSound("goombarevive.wav");
        reviveSound.setVolume(70);
        reviveSound.play();
    }
    public void act()
    {
        super.act();
        if(getWorld() == null) {  // stop act if instance has been removed from the world
            return;
        }
        if(imgActCount%(8/(int)maxSpeed) == 0 && awake && movedThisAct) { 
            cycleAvatar();
            fixAvatarDirection();
        }
        imgActCount++;
    }
    public String getHead() {
        return HEAD;
    }
    public String getFall() {
        return FALL;
    }
}
