import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
/**
 * This is the superclass for Vehicles.
 * 
 */
public abstract class Vehicle extends Animator {
    // Vehicle behaviour variables
    protected double maxSpeed;
    protected double speed;
    protected double rotation;
    protected boolean moving;
    
    protected int yOffset;
    protected String startImage;
    // Related actors
    protected VehicleSpawner origin;
    protected Hitbox hitbox;
    
    
    protected abstract boolean checkHitPedestrian ();

    public Vehicle (VehicleSpawner origin) {  // no start image
        this(origin, ""); 
    }
    // work-around for not being able to manually access images in folders within "images" folder in greenfoot file (ex: accessing images in "bullet" folder) 
    public Vehicle (VehicleSpawner origin, String startImage) {
        if(!startImage.isEmpty()) {
            setImage(startImage);
        }
        this.startImage = startImage;
        this.origin = origin;
        moving = true;
        if(origin != null) {
            fixAvatarDirection();
        }
        
    }

    public void addedToWorld (World w){
        //         calculation for direction; rotation = 180, then direction = -1. rotation = 0, direction = 1.
        //          direction = -(rotation-90)/90
        setLocation (origin.getX() - (-(rotation-90)/90 * 100), origin.getY() - yOffset);
        hitbox.init(); // add hitbox to world
    }
    
    public void act() {
        drive();
        
        if (checkEdge()){
            removeHitbox();
            getWorld().removeObject(this);
        }
    }


    /**
     * A method used by all Vehicles to check if they are at the edge
     */
    protected boolean checkEdge() {
        if (rotation == 0){
            if (getX() > getWorld().getWidth() + getImage().getWidth()){
                return true;
            }
        } else {
            if (getX() < - getImage().getWidth()){
                return true;
            }
        }
        return false;
    }

    protected void fixAvatarDirection() {  // adjust image based on rotation
        if(origin.facesRightward()) {
            rotation = 0;
        } else {
            rotation = 180;
            getImage().mirrorHorizontally();
        }
    }
    /**
     * Method that deals with movement. Speed can be set by individual subclasses in their constructors
     */
    public void drive() // basic drive function for vehicles that never need to turn --> will typically be overridden to check for other actors
    {
        move (speed * -(rotation-90)/90);
    }   
    /**
    /* calculate point at offset distance, dependant on actor's rotation (i.e. allows for diagonal offset if needed)
     * returns double array with {xOffset, yOffset}.
     */
    public double[] calculateOffsetAhead(double offset) {  
        double[] offsetArr = new double[2];
        double hyp = speed + getImage().getWidth()/2 + offset;
        double r = Math.min(360.0, rotation); // temp rotation variable
        // trig calculations for offset
        offsetArr[0] = hyp*Math.cos(r*Math.PI/180);  // xOffset
        offsetArr[1] = hyp*Math.sin(r*Math.PI/180);  // yOffset
        return offsetArr;
    }
    
    public RoadVehicle getRoadVehicleAhead(double offset) {
        double[] offsetArr = calculateOffsetAhead(offset);
        //                                                                  xOffset             yOffset
        RoadVehicleHitbox vh =(RoadVehicleHitbox) hitbox.getOneObjectAtOffset((int)offsetArr[0], (int)offsetArr[1], RoadVehicleHitbox.class);
        if(vh != null && vh.getTarget() != null) {  // get owner Vehicle of VehicleHitbox
            return (RoadVehicle)vh.getTarget();
        }
        return null;
    }
    // Getters    
    public double getRotationVal() {
        return rotation;
    }
    
    public double getSpeed(){
        return speed;
    }
    // Remove hitbox from world
    public void removeHitbox() {
        getWorld().removeObject(hitbox);
    }
    
}

