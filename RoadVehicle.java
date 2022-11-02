import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
/**
 * Superclass for all Vehicles that follow the road
 * Sprite Credits:
 * https://www.deviantart.com/snowgibbonbasilisk91/art/Super-Mario-Kart-Sprites-w-Customs-403971952
 * 
 * Sound Credits: 
 * https://pixabay.com/sound-effects/search/car-horn/
 */
public abstract class RoadVehicle extends Vehicle
{
    // Variables for turning
    protected TurnFlag activeTurnFlag;
    protected ArrayList<TurnFlag> turnFlagArr;
    protected ArrayList<FlipFlag> usedFlipFlags;
    protected boolean isTurning;  // vehicle is not driving straight / is currently on curve.
    protected boolean leftTurn;
    protected double rotationRate;  // rate at which vehicle must rotate to simulate turning
    // Variables for dealing with another vehicle ahead (lane chaning / slowing down)
    protected int slowCooldown = 0;
    protected int pauseCooldown = 0;
    protected int laneChangeCount = 0;
    protected LaneChangeHitbox laneChangeHitbox;
    protected int myLaneY;
    protected int nextLaneY;
    // General Behaviour
    protected int direction;
    protected int beepCooldown;
    // Slowed by the rain
    protected boolean slowed;  // true when raining
    protected final double slowFactor = Math.random() + 1.5; // how much slower the vehicle gets in the rain
    //  how long a slip last // dont let vehicle slip immediately after it just slipped
    protected int slipCounter, slipCooldown;
    protected boolean heavy;  // if true, vehicle is too heavy and can't slip
    protected boolean waitFixAvatar; // fix avatar after slipping
    protected abstract boolean checkHitPedestrian ();
    
    public RoadVehicle(VehicleSpawner origin) {
        super(origin);
        turnFlagArr = new ArrayList<TurnFlag>(origin.getTurnFlags());  // this vehicle instance will only be affected by these turn flags
        usedFlipFlags = new ArrayList<FlipFlag>();
        hitbox = new RoadVehicleHitbox(this);
        direction = origin.facesRightward() ? 1 : -1;
        slipCounter = 0;
        slipCooldown = 0;
        beepCooldown = 0;
        myLaneY = origin.getLaneY();
        rotation = origin.facesRightward() ? 0 : 180;
        laneChangeHitbox = null;
    }
    public void act()
    {
        super.act();
        if(getWorld() != null) {
            checkHitPedestrian();
            checkHitGreenShell();
            if(checkHitPuddle() && slipCounter == 0) {
                startSlipping();
            }
        }
    }
    
    
    public void drive() { // drive method which allows for turning at turnFlags
        RoadVehicle ahead = getRoadVehicleAhead(2);
        if(slipCooldown > 0) {
            slipCooldown --;
        }
        if(beepCooldown > 0) {
            beepCooldown --;
        }
        // Slipping
        if(slipCounter != 0 && !heavy && slipCooldown == 0) {
            slipSequence();
        }
        // Fix avatar after slipping on a turn
        if(!isTurning && waitFixAvatar) { 
            waitFixAvatar= false;
            setImage(startImage);
            fixAvatarDirection();
        }
        // - - - handle driving forwards / stopping if vehicle ahead- - -
        if(ahead == null && pauseCooldown == 0) { // nothing ahead & vehicle is not paused
            if(laneChangeHitbox != null) {
                getWorld().removeObject(laneChangeHitbox);
                laneChangeHitbox = null;
            }
            if(slowCooldown > 0) {  // vehicle is slowed for now
                slowCooldown --;
            } else if(!isTurning){  // vehicle moves at default pace if no special conditions met
                if(!slowed) {
                    speed = maxSpeed;
                } else {
                    speed = maxSpeed / slowFactor;
                }
            }
            // if the vehicle has reached the other lane, set rotation back to 0
            if(hitbox.getY() >= getTargetLaneY() && laneChangeDirection() == 1 || hitbox.getY() <= getTargetLaneY() && laneChangeDirection() == -1) {
                setRotation(0);
                laneChangeCount ++; // keep track of how many lane changes have happened, to keep track of what lane the vehicle is on.
            }
            move(speed*direction);
            
        // If Vehicle is ahead and can't change lanes, start pause & slow cooldowns
        } else if(pauseCooldown == 0 && !canChangeLane()) {  // vehicle is ahead, break for 40 acts
            pauseCooldown = 40;
            slowCooldown = 120;  // vehicle will be speed of vehicle ahead for 120 acts after pausing.
            speed = ahead.getSpeed();
        } else if(canChangeLane()) { // LANE CHANGE
            if(beepCooldown == 0) {
                GreenfootSound startSound = new GreenfootSound("carbeep.wav");
                startSound.setVolume(80);
                startSound.play();
                beepCooldown = 40;
            }
            pauseCooldown = 0;
            slowCooldown = 0;
            // rotate vehicle depending on direction of lane change
            if(laneChangeDirection() == 1) {
                if(direction == 1) {
                    setRotation(45);
                } else {
                    setRotation(315);
                }
            } else {
                if (direction == 1) {
                    setRotation(315);
                } else {
                    setRotation(45);
                }
            }
            if(laneChangeHitbox != null) {
                getWorld().removeObject(laneChangeHitbox);
                laneChangeHitbox = null;
            }
        } else {  // break until pauseCooldon == 0
            pauseCooldown --;
        }
        
        // - - - handle turning mechanics - - -
        if(!isTurning) {  // vehicle moving straight
            ArrayList<TurnFlag> touchingTurnFlags = (ArrayList<TurnFlag>)(hitbox.getIntersectingObjects(TurnFlag.class));
            if(turnFlagArr.size() > 0) { // if turns left
                for(TurnFlag f : touchingTurnFlags) {
                    if(doTurn(f)) {  // turn only if turnFlag exists in origin's array
                        isTurning = true;
                        activeTurnFlag = f;
                        speed = Math.min(3, speed); // max speed of 3 when turning
                        leftTurn = !activeTurnFlag.isLeftCurve();
                        break;
                    }
                }
            }
        } else if(ahead == null && pauseCooldown == 0){  // vehicle is moving on curve
            // flip image once, mid-turn
            FlipFlag touchingFlipFlag = (FlipFlag)hitbox.getOneIntersectingObject(FlipFlag.class);
            if(touchingFlipFlag != null && !usedFlipFlags.contains(touchingFlipFlag)) {
                usedFlipFlags.add(touchingFlipFlag);
                getImage().mirrorVertically();
                hitbox.setXOffset(-hitbox.getXOffset());
            }
            // calculate & execute rotation
            if(leftTurn) { // facing right, rotation = 0
                addRotation(-180.0 / (activeTurnFlag.getTurnLength()/speed)); // goes from 0 --> -180(359 --> 180)
                if(rotation <= -180) { // stop at rotation = -180 --> 180
                    isTurning = false;
                    rotation = 180; // readjust to make face exactly leftwards;
                    setRotation(rotation);
                }
            } else {  // facing left, rotation = 180
                addRotation(180.0 / (activeTurnFlag.getTurnLength()/speed));  // goes from 180 --> 360
                if(rotation >= 360) { // stop at rotation = 360 --> 0
                    isTurning = false;
                    rotation = 0;
                    setRotation(rotation);
                }
            }
        }
        
    }
    // turn if TurnFlag is still in array; ensures that vehicle only turns once, at specific flags (i.e. doesnt get unwantingly triggered by other turn flags)
    public boolean doTurn(TurnFlag f) {
        if(turnFlagArr.size() != 0 && turnFlagArr.get(0) == f) {
            turnFlagArr.remove(f);
            return true;
        }
        return false;
    }
    public void addRotation(double x) {
        rotation += x;
        setRotation(rotation);
    }
    public void setRandomImage(String[] arr) {
        int rand = Greenfoot.getRandomNumber(arr.length);
        setImage(arr[rand]);
        startImage = arr[rand];
    }
    // Check collision with puddle
    public boolean checkHitPuddle() {
        Puddle p = (Puddle)hitbox.getOneIntersectingObject(Puddle.class);
        return p != null;
    }
    // Check collision with green shell
    public boolean checkHitGreenShell() {
        double[] offset = calculateOffsetAhead(2);
        GreenShell g = (GreenShell)getOneObjectAtOffset((int)offset[0], (int)offset[1], GreenShell.class);
        if(g != null) {
            g.setAngle(rotation + g.getAngle()%180/4);
            return true;
        }
        return false;
    }
    // Slipping methods
    public void slipSequence() {
        slipCounter++;
        if(slipCounter %2 == 0) {  // rotate image at intervals to simulate slipping (spinning)
            getImage().rotate(30);
        }
        if(slipCounter < 32 && !isTurning) {
            speed = maxSpeed*0.4;  //slow down while slipping
        } else if(slipCounter < 48) {
            speed = 0;
        } else { // stop rotating
            speed = maxSpeed;
            slipCounter = 0;
            slipCooldown = 90;  // cannot slip again for 90 acts
        }
    }
    public boolean isSlipping() {
        return slipCounter != 0;
    }
    public void startSlipping() {
        if(!heavy && slipCooldown <= 0) {
            slipCounter = 1;
            GreenfootSound slipSound = new GreenfootSound("slip.wav");
            slipSound.setVolume(70);
            slipSound.play();
        }
    }
    // Change speed upon raining event
    public void slowMeDown(){
        
        this.speed = maxSpeed / (2.5 + Greenfoot.getRandomNumber(2)*0.5);
        slowed = true;
    }
    public void resumeNormalSpeed(){
        this.speed = maxSpeed;
        slowed = false;
    }
    // - - - - - - Methods related to lane changing - - - - - - 
    public boolean canChangeLane() {
        if(!origin.canChangeLane()) {
            return false;
        }
        VehicleWorld vw = (VehicleWorld)getWorld();
        if(laneChangeHitbox == null) {  // create new laneChangeHitbox to detect if cars exist in other lane
            laneChangeHitbox = new LaneChangeHitbox(this.hitbox, -direction*30, laneChangeDirection()*((int)(vw.getLaneWidth()))+4, 200, vw.getLaneWidth()/2, true, true);
            vw.addObject(laneChangeHitbox, getX(), getY());
        }
        RoadVehicleHitbox v = (RoadVehicleHitbox)laneChangeHitbox.getOneIntersectingObject(RoadVehicleHitbox.class);
        return v == null;  // returns whether vehicle blocking other lane or not
    }
    // get directino of lange change. -1 = down, 1 = up
    public int laneChangeDirection() {
        int up = origin.canChangeLaneUp() ? -1 : 1;  // initial direction of lane change depends on origin (preset)
        int up2 = laneChangeCount % 2 == 1 ? -1 : 1; // if changed lanes an odd number of times, then change direction of lane change
        return up*up2;
    }
    // Get lane Y of the lane that the vehicle is turning towards.
    public int getTargetLaneY() {
        int up = origin.canChangeLaneUp() ? -1 : 1;
        return laneChangeCount % 2 == 1 ? myLaneY : myLaneY + ((VehicleWorld)getWorld()).getLaneWidth()*up;
    }
}
