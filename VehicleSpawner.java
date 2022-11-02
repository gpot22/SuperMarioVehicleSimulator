import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
/**
 * A Spawner object is a place where a Vehicle can spawn. Each spawner is
 * able to check if there is already a Vehicle in the spot to avoid spawning
 * multiple Vehicles on top of each other.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class VehicleSpawner extends Actor {
    public static final Color TRANSPARENT_RED = new Color (255, 0, 0, 128);
    
    public static final int DIST_BETWEEN_CARS = 128;
    
    private GreenfootImage image;
    
    private int height, width;
    private boolean rightward;
    private boolean visible;
    private boolean spawnAny;
    // Attributes passed onto vehicles
    private boolean canChangeLane;
    private boolean changeLaneUp;
    private int laneY;
    private ArrayList<Class<?>> whitelist;
    private ArrayList<TurnFlag> turnFlags; // array of turn flags pertaining to the spawner's lane
    
    public VehicleSpawner (boolean rightward, int laneWidth) {
        
        this(rightward, laneWidth, new ArrayList<Class<?>>(), false, 0);
        this.canChangeLane = false;
    }
    public VehicleSpawner (boolean rightward, int laneWidth, ArrayList<Class<?>> whitelist) {
        
        this(rightward, laneWidth, whitelist, false, 0);
        this.canChangeLane = false;
    }
    public VehicleSpawner (boolean rightward, int laneWidth, boolean changeLaneUp, int laneY) {
        this(rightward, laneWidth, new ArrayList<Class<?>>(), changeLaneUp, laneY);
    }
    // constructor which allows control over which vehicles can spawn form specific vehicle spawners, by passing in a whitelist array
    public VehicleSpawner(boolean rightward, int laneWidth, ArrayList<Class<?>> whitelist, boolean changeLaneUp, int laneY) {
        this.rightward = rightward;
        this.height = (int)(laneWidth * 0.75);
        this.whitelist = whitelist;  // only allow certain types of vehicles to spawn
        this.spawnAny = whitelist.size() == 0; // if whitelist is empty, spawnAny is true
        this.turnFlags = new ArrayList<TurnFlag>();
        this.canChangeLane = true;
        this.changeLaneUp = changeLaneUp;
        this.laneY = laneY;
        width = DIST_BETWEEN_CARS;
        visible = false;
        image = new GreenfootImage (width, height);
        if(visible){
            image.setColor(TRANSPARENT_RED);
            image.fillRect(0, 0, width-1, height - 1);
        }
        setImage(image);
    }
    
    public boolean facesRightward (){
        return rightward;
    }
    
    public boolean isTouchingVehicle () {
        return this.isTouching(Vehicle.class);
    }
    public boolean isTouchingBullet() {
        return this.isTouching(BulletBill.class);
    }
    
    public boolean canSpawnAny() {
        return spawnAny;
    }
    public boolean canSpawn(Class c) {
        return spawnAny || whitelist.contains(c) || whitelist.contains(c.getSuperclass());
    }
    public boolean canChangeLane() {
        return canChangeLane;
    }
    public boolean canChangeLaneUp() {
        return changeLaneUp;
    }
    public int getLaneY() {
        return laneY;
    }
    public void addTurnFlag(TurnFlag f) {
        turnFlags.add(f);
    }
    public ArrayList<TurnFlag> getTurnFlags() {
        return new ArrayList<TurnFlag>(turnFlags);
    }
    // public boolean 
}
