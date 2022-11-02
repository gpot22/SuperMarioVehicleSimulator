import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
/**
 * Type of pedestrian which has a chance to follow a Bus
 * 
 * Sprite Credits:
 * https://www.deviantart.com/hartflip0218/art/Custom-Mario-sprite-sheet-ver-1-768101045
 * Audio Credits:
 * "Hurt", "Celebrating", https://themushroomkingdom.net/media/mparty8/wav
 */
public class Mario extends Pedestrian {
    public static final String HEAD = "mariohead.png";
    public static final String FALL = "mariofall.png";
    private ArrayList<Bus> buses;
    private Bus myTargetBus;
    private boolean followBus;
    public Mario(int direction) {
        // choose a random speed
        maxSpeed = Math.random() * 2 + 1;
        speed = maxSpeed;
        setAnimationCycle("mario", ".png", 7);
        startImage = "mario0.png";
        // start as awake 
        awake = true;
        myTargetBus = null;
        followBus = Greenfoot.getRandomNumber(4) == 0;
        this.direction = direction;
        hitbox = new PedHitbox(this, 0, 0, (int)(getImage().getWidth()/1.5), getImage().getHeight(), true, true);
        fixAvatarDirection();
    }
    public void playHurtSound() {
        GreenfootSound hurtSound = new GreenfootSound("mariohurt.wav");
        hurtSound.setVolume(70);
        hurtSound.play();
    }
    public void playReviveSound() {
        GreenfootSound reviveSound = new GreenfootSound("mariorevive.wav");
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
        if(!followBus) {
            return;
        }
        if(myTargetBus != null) {
            if(myTargetBus.getWorld() == null) {
                myTargetBus = null;
            }
        }
        if(myTargetBus == null || myTargetBus.isMaxCapacity())  {
            buses = (ArrayList<Bus>)getWorld().getObjects(Bus.class);
            buses.removeIf(n -> n.isMaxCapacity());
            if(buses.size() > 0) {
                myTargetBus = buses.get(Greenfoot.getRandomNumber(buses.size()));
            }
        }
    }
    public String getHead() {
        return HEAD;
    }
    public String getFall() {
        return FALL;
    }
    
    public void moveOrFollowBus() {
        if(myTargetBus == null || !followBus) {
            setLocation (getX(), getY() + (int)(speed*direction));
        } else if(myTargetBus.getWorld() != null && followBus){
            if(myTargetBus.getRotation() == 0) {
                turnTowards(myTargetBus.getX()+myTargetBus.getImage().getWidth()/2, myTargetBus.getY());
            } else {
                turnTowards(myTargetBus.getX()-myTargetBus.getImage().getWidth()/2, myTargetBus.getY());
            } 
            move(speed*1.5);
            setRotation(0);
        }
    }
}
