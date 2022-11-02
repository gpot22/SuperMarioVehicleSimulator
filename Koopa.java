import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Type of Pedestrian which turns into a GreenShell when knocked by a Vehicle or, in certain cases, another Pedestrian
 * 
 * Sprite Credits:
 * https://www.spriters-resource.com/snes/smarioworld/sheet/143271/
 * 
 * Audio Credits:
 * "Koopa Happy", "Koopa Sad", https://themushroomkingdom.net/media/mparty8/wav
 */
public class Koopa extends Pedestrian {
    public static final String HEAD = "koopahead.png";
    public static final String FALL = "greenshell0.png";
    private int pushCooldown;
    private double pushAngle;
    
    public Koopa(int direction) {
        // choose a random speed
        maxSpeed = Math.random() * 2 + 1;
        speed = maxSpeed;
        // sprite cycle setup
        setAnimationCycle("koopa", ".png", 2);
        startImage = "koopa0.png";
        // when pushCooldown > 0, trigger pushing sequence in Pedestrian act()
        pushCooldown = 0;
        // start as awake 
        awake = true;
        this.direction = direction;
        hitbox = new PedHitbox(this, 0, 0, (int)(getImage().getWidth()/1.5), getImage().getHeight(), true, true);
    }
    public void addedToWorld(World w) {
        super.addedToWorld(w);
        
    }
    public void playHurtSound() {
        GreenfootSound hurtSound = new GreenfootSound("koopahurt.wav");
        hurtSound.setVolume(70);
        hurtSound.play();
    }
    public void playReviveSound() {
        GreenfootSound reviveSound = new GreenfootSound("kooparevive.wav");
        reviveSound.setVolume(70);
        reviveSound.play();
    }
    public void act()
    {
        super.act();
        if(getWorld() == null) {  // stop act if instance has been removed from the world
            return;
        }
        // act every (10/maxSpeed) acts  // only animate if awake & if actor moved this act & if not being pushed
        if(imgActCount%(10/(int)maxSpeed) == 0 && awake && movedThisAct && pushCooldown == 0) { 
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
    public void pushShell(double a, double s) {  // instead of getting knocked over, koopas turn into GreenShells and get pushed.
        GreenShell g = new GreenShell(this);
        getWorld().addObject(g, getX(), getY());
        g.setAngle(a);
        g.setSpeed(s);
        getWorld().removeObject(this);
    }
    public void pushKoopaBack(double a) {
        pushCooldown = 40; // setting a pushCooldown > 0 will trigger pushing mechanism in Pedestrian's act() method
        pushAngle = a;
        if (isAwake()) {
            setImage("koopapushed.png");  // special sprite for when Koopa is being pushed
        }
        
    }
    public boolean handlePushing() {
        if (pushCooldown == 0) {
            return false;
        }
        setLocation(getX() + (int)(3*Math.cos(pushAngle)), getY() + (int)(3*Math.sin(pushAngle)));  // move at angle of pushAngle
        pushCooldown --;
        if(pushCooldown == 0 && isAwake()) { // end of pushing
            setImage(startImage);  // reset koopa image
        }
        return true;
    }
}
