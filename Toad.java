import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Basic type of Pedestrian, differing from Goombas with its ability to knock back GreenShell
 * 
 * Sprite Credits: 
 * https://www.deviantart.com/thecynicalpoet/art/MLSS-Toad-Sprites-203747832
 * Audio Credits:
 * "Hurt", "Here I Go", https://themushroomkingdom.net/media/mparty8/wav
 */
public class Toad extends Pedestrian {
    public static final String HEAD = "toadhead.png";
    public static final String FALL = "toadfall.png";
    public Toad(int direction) {
        // choose a random speed
        maxSpeed = Math.random() * 2 + 1;
        speed = maxSpeed;
        
        //set up sprite cycle
        setAnimationCycle("toad", ".png", 5);
        startImage = "toad0.png";
        
        // start as awake 
        awake = true;
        this.direction = direction;
        hitbox = new PedHitbox(this, 0, 0, (int)(getImage().getWidth()/1.5), getImage().getHeight(), true, true);
        fixAvatarDirection();
    }
    
    public void playHurtSound() {
        GreenfootSound hurtSound = new GreenfootSound("toadhurt.wav");
        hurtSound.setVolume(70);
        hurtSound.play();
    }
    public void playReviveSound() {
        GreenfootSound reviveSound = new GreenfootSound("toadrevive.wav");
        reviveSound.setVolume(70);
        reviveSound.play();
    }
    
    public void act()
    {
        super.act();
        if(getWorld() == null) {  // stop act if instance has been removed from the world
            return;
        }
        if(imgActCount%(10/(int)maxSpeed) == 0 && awake && movedThisAct) { 
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
