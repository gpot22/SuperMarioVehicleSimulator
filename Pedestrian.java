import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A Pedestrian that tries to walk across the street
 */
public abstract class Pedestrian extends Animator {
    // general behaviour variables
    protected double speed;
    protected double maxSpeed;
    protected int direction;  // 1 = move down, -1 = move up
    protected boolean awake;
    // Bus variables
    protected Bus targetBus;
    private int waitingForBus;
    
    protected PedHitbox hitbox;
    protected String startImage;
    
    
    // variables serve no purpose at the moment
    public static int totalFallen = 0;
    public static int totalCrossedRoad = 0;
    public abstract String getHead();
    public abstract String getFall();
    public abstract void playHurtSound();
    public abstract void playReviveSound();
    public void act()
    {
        // Koopas can get pushed back by green shells; handle before moving.
        if(this instanceof Koopa) {
            if(((Koopa)this).handlePushing()) {
                return;
            }
        }
        
        movedThisAct = false;  // track whether or not the actor has moved, to know whether to animate walking or not
        if (awake){
            RoadVehicle v = (RoadVehicle)getOneObjectAtOffset(0, (int)(direction * getImage().getHeight()/2 + (int)(direction * speed)), RoadVehicle.class);
            Bus b;
            if (v == null){
                if(this instanceof Mario) {
                    ((Mario)this).moveOrFollowBus();  // special movement method for Mario
                } else {
                    setLocation (getX(), getY() + (int)(speed*direction));  // move if no vehicle ahead
                }
                movedThisAct = speed > 0;
            } else if(v instanceof Bus) {
                b = (Bus)v;
                if (handleBoardBus(b)) {
                    return;  // if boarded bus, do not continue acting
                }
            }
            checkHitGreenShell();
            
            // check if pedestrian has crossed the road; if so, remove
            if (direction == -1 && getY() < 200){
                getWorld().removeObject(this);
            } else if (direction == 1 && getY() > VehicleWorld.WORLD_H-50){
                getWorld().removeObject(this);
            }
        }
    }
    public void addedToWorld(World w) {
        hitbox.init();
    }
    /**
     * Method to cause this Pedestrian to become knocked down - stop moving, turn onto side
     */
    public void knockDown () {
        speed = 0;
        setImage(getFall());
        hitbox.syncToTarget();
        if(awake) {
            playHurtSound();
        }
        awake = false;
    }

    /**
     * Method to allow a downed Pedestrian to be healed
     */
    public void healMe () {
        speed = maxSpeed;
        setImage(startImage);
        hitbox.reset(0, 0, (int)(getImage().getWidth()/1.5), getImage().getHeight());
        if(!awake) {
            playReviveSound();
        }
        awake = true;
    }
    // check if pedestrian gets hit by green shell or fallen koopa
    public boolean checkHitGreenShell() {;
        if(!isAwake()) {
            return false;
        }
        // Pedestrian runs into fallen Koopa
        Koopa p = (Koopa)getOneObjectAtOffset(0, (int)(speed + getImage().getHeight()/2 + 2) * direction, Koopa.class);
        if(hitKoopaShell(p)) {
            return true;
        }
        Koopa p2 = (Koopa)hitbox.getOneIntersectingObject(Koopa.class);  // extra layer of checking , as first offset alone was buggy
        if(hitKoopaShell(p2)) {
            return true;
        }
        // Pedestrian hits GreenShell
        GreenShell g = (GreenShell)getOneObjectAtOffset(0, (int)(speed +getImage().getHeight()/2+ 2)*direction, GreenShell.class);
        if(g != null) {
            int rotation = direction == 1 ? 90 : 270;
            if(this instanceof Koopa) {
                ((Koopa)this).pushKoopaBack(360-(rotation + g.getAngle()%180/4));  // Koopas get pushed back by moving green shells
            } else if(this instanceof Goomba) {  //goombas are weaker than the rest, and get knocked down by green shells
                knockDown();
                return true;
            }
            g.setAngle(rotation + g.getAngle()%180/4);
            return true;
        }
        return false;
    }
    
    public boolean handleBoardBus(Bus b) {
        if(targetBus == null && !b.isMaxCapacity()) {  // target bus if does not already have target & bus is not at max capacity
            targetBus = b;
        }
        if(targetBus == b) {
            if (!b.isMaxCapacity()) {
                b.stop();
                waitForBus();
            } else {
                b.go();  // if bus at max capacity, bus ignores pedestrian
            }
            if(getWaitingForBus() >= 60) { // after 60 acts waiting at bus, passenger boards bus.
                b.addPassenger(this);
                return true;
            }
        }
        return false;
    }
    
    public boolean hitKoopaShell(Koopa k) {
        if(k == null || k.isAwake()) {  // check that koopa is in shell form
            return false;
        }
            
        int rotation = direction == 1 ? 90 : 270;
        if(this instanceof Goomba) {
            k.pushKoopaBack(rotation);  // goombas will only push standing koopa shells slightly
        } else {
            k.pushShell(rotation, speed);
        }
        return true;
    }
    
    protected void fixAvatarDirection() {  // adjust image based on rotation
        if(direction == -1) {
            getImage().mirrorHorizontally();
        }
    }
    // - - - - - - - - - Getters and Setters - - - -  - - - - --  - -
    public void stop() {
        speed = 0;
    }
    public void go() {
        speed = maxSpeed;
    }
    
    public boolean isAwake () {
        return awake;
    }
    public Bus getTargetBus() {
        return targetBus;
    }
    public void setTargetBus(Bus b) {
        targetBus = b;
    }
    
    public void waitForBus() {
        waitingForBus ++;
    }
    public int getWaitingForBus() {
        return waitingForBus;
    }
    public int getDirection() {
        return direction;
    }
    public double getSpeed() {
        return speed;
    }
}
