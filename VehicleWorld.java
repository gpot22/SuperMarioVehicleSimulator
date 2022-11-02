import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
import java.util.Arrays;
/**
 * Welcome to my Super Mario Vehicle Simulator! 
 * 
 * 
 * Background Image Credits:
 * https://roa-workshop.fandom.com/wiki/Mario_Hill
 * https://www.pinterest.com/pin/428123508326484663/
 * https://wallpapers.com/wallpapers/super-mario-wallpaper-5gqa6t74e1kl7ho9.html
 * Put together / Edited by Caden Chan
 * 
 * @author Caden Chan
 */
public class VehicleWorld extends World
{
    private GreenfootImage background;

    // Color Constants
    public static Color GREY_BORDER = new Color (108, 108, 108);
    public static Color STREET = new Color(80, 50, 30);
    public static Color YELLOW_LINE = new Color (255, 216, 0);
    public static Color SEP_LINE = new Color(120, 90, 70);
    
    // HITBOX VISIBILITY CONTROLLERS (for Mr. Cohen)
    public static final boolean SEE_ROADVEHICLE_HITBOX = false;
    public static final boolean SEE_BULLET_HITBOX = false;
    public static final boolean SEE_PEDESTRIAN_HITBOX = false;
    public static final boolean SEE_LANECHANGE_HITBOX = false;
    public static final boolean SEE_TURN_FLAGS = false;
    // note: projectiles and puddles do not have their own hitboxes
    
    public static final int WORLD_W = 1100, WORLD_H = 900;
    // Instance variables / Objects
    private int middleLaneCount, laneWidth, laneLength, turnCount, spawnerCount, spaceBetweenLanes, spaceBetweenStreets, startX, startY;
    private int[] snkLane1PositionsY, snkLane2PositionsY, strtLanePositionsY;
    private VehicleSpawner[] laneSpawners;
    private VehicleSpawner[] bulletSpawners;
    private TurnFlag[] snkLane1TurnFlags, snkLane2TurnFlags;
    
    // Event flags
    private boolean bulletStormActive;
    private boolean raining;

    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public VehicleWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(WORLD_W, WORLD_H, 1, false); 

        setPaintOrder (Rain.class, Scream.class, Hitbox.class, BulletVictim.class, Bullet.class, Projectile.class, Pedestrian.class, Bus.class, Car.class, Puddle.class, ItemTosser.class, TurnFlag.class);

        // set up background
        background = new GreenfootImage ("my_background.png");
        // background = new GreenfootImage(WORLD_W, WORLD_H);
        // background.setColor(new Color(0, 170, 50));
        // background.fill();
        setBackground (background);
        // Set critical variables
        turnCount = 2;
        middleLaneCount = 4;
        spawnerCount = middleLaneCount + 4;
        laneLength = 600;
        laneWidth = 32;
        spaceBetweenLanes = 2;
        spaceBetweenStreets = 5;
        
        // map position
        startX = 200;
        startY = 260;

        // Init lane spawner objects 
        laneSpawners = new VehicleSpawner[spawnerCount];
        bulletSpawners = new VehicleSpawner[(turnCount+1)*4];
        snkLane1TurnFlags = new TurnFlag[turnCount*2];
        snkLane2TurnFlags = new TurnFlag[turnCount*2];
        
        bulletStormActive = false;
        raining = false;
        
        // Prepare lanes methods - draws the lanes
        snkLane1PositionsY = prepareSnakeLanes(this, background, snkLane1TurnFlags, startX, startY, laneLength, laneWidth, turnCount, spaceBetweenLanes, spaceBetweenStreets);
        strtLanePositionsY = prepareLanes (this, background, laneSpawners, startY+(turnCount+1)*(2*laneWidth+spaceBetweenLanes) +spaceBetweenStreets*turnCount + 15, laneWidth, middleLaneCount, spaceBetweenLanes, true, true);
        snkLane2PositionsY = prepareSnakeLanes(this, background, snkLane2TurnFlags, startX, startY+((turnCount+1)*2+middleLaneCount)*(laneWidth+spaceBetweenLanes)+ spaceBetweenStreets*2 + 30, laneLength, laneWidth, turnCount, spaceBetweenLanes, spaceBetweenStreets);
        
        // Create Vehicle Spawners for Snake Lanes & Bullets        
        for(int i=0;i<2;i++ ) {  // spawners for snkLane1
            VehicleSpawner vs = new VehicleSpawner(true, laneWidth, new ArrayList<Class<?>>(Arrays.asList(Healer.class, Car.class, EnemyCar.class)));
            // attribute flags to corresponding vehicle spawner in same lane
            vs.addTurnFlag(snkLane1TurnFlags[3-i]);  // first turnFlag that vehicle will pass by
            vs.addTurnFlag(snkLane1TurnFlags[i]);  // second turnFlag that vehicle will pass by
            laneSpawners[i+4] = vs; 

            addObject(laneSpawners[i+4], 0, snkLane1PositionsY[snkLane1PositionsY.length-1-i]);
        }
        for(int i=0;i<2;i++) {  // spawners for snkLane2
            VehicleSpawner vs = new VehicleSpawner(true, laneWidth, new ArrayList<Class<?>>(Arrays.asList(Car.class, Healer.class, EnemyCar.class)));
            // attribute flags to corresponding vehicle spawner in same lane
            
            vs.addTurnFlag(snkLane2TurnFlags[3-i]);  // first turnFlag that vehicle will pass by
            vs.addTurnFlag(snkLane2TurnFlags[i]);  // second turnFlag that vehicle will pass by
            laneSpawners[i+6] = vs;
            addObject(laneSpawners[i+6], 0, snkLane2PositionsY[snkLane2PositionsY.length-1-i]);

        }
        // bullet spawners;
        for(int i=0;i<6;i++) {
            boolean rightward = (i/2)%2 != 0;
            VehicleSpawner vs = new VehicleSpawner(rightward, laneWidth, new ArrayList<Class<?>>(Arrays.asList(BulletBill.class)));
            bulletSpawners[i] = vs;
            if(rightward) {
                addObject(bulletSpawners[i], 0, snkLane1PositionsY[i]);
            } else {
                addObject(bulletSpawners[i], getWidth(), snkLane1PositionsY[i]);
            }
        }
        for(int i=0;i<6;i++) {
            boolean rightward = (i/2)%2 != 0;
            VehicleSpawner vs = new VehicleSpawner(rightward, laneWidth, new ArrayList<Class<?>>(Arrays.asList(BulletBill.class)));
            bulletSpawners[i+6] = vs;
            if(rightward) {
                addObject(bulletSpawners[i+6], 0, snkLane2PositionsY[i]);
            } else {
                addObject(bulletSpawners[i+6], getWidth(), snkLane2PositionsY[i]);
            }
        }
        
    }
    public void act () {
        spawn();
    }
    public void started() {
        // restart longer sounds after pausing simulation
        super.started();
        ArrayList<Bullet> bullets = (ArrayList<Bullet>)getObjects(Bullet.class);
        for(Bullet b: bullets) {
            if(b.getMovingSound()!= null) {
                b.getMovingSound().play();
            }
        }
        ArrayList<Rain> rainArr = (ArrayList<Rain>)getObjects(Rain.class);
        if(rainArr.size() != 0) {
            if(rainArr.get(0).getRainSound() != null) {
                rainArr.get(0).getRainSound().play();
            }
        }
    }
    public void stopped() {
        // stop longer sounds when simulation is paused
        super.stopped();
        ArrayList<Bullet> bullets = (ArrayList<Bullet>)getObjects(Bullet.class);
        for(Bullet b: bullets) {
            if(b.getMovingSound()!= null) {
                b.getMovingSound().stop();
            }
        }
        ArrayList<Rain> rainArr = (ArrayList<Rain>)getObjects(Rain.class);
        if(rainArr.size() != 0) {
            if(rainArr.get(0).getRainSound() != null) {
                rainArr.get(0).getRainSound().stop();
            }
        }
        
    }

    private void spawn () {
        // Chance to spawn a vehicle
        if (Greenfoot.getRandomNumber (40) == 0){
            int lane = Greenfoot.getRandomNumber(laneSpawners.length);
            VehicleSpawner spawner = laneSpawners[lane];
            if (!spawner.isTouchingVehicle()){
                int vehicleType = Greenfoot.getRandomNumber(100);
                if (vehicleType < 20 && spawner.canSpawn(Car.class)){
                    addObject(new Car(spawner), 0, 0);
                } else if (vehicleType < 40 && spawner.canSpawn(Bus.class)){
                    addObject(new Bus(spawner), 0, 0);
                } else if (vehicleType < 75 && spawner.canSpawn(Healer.class)){
                    addObject(new Healer(spawner), 0, 0);
                } else if(spawner.canSpawn(EnemyCar.class)) {
                    addObject(new EnemyCar(spawner), 0, 0);
                } 
            }
        }

        // Chance to spawn a Pedestrian
        if (Greenfoot.getRandomNumber (30) == 0){
            int direction = Greenfoot.getRandomNumber(2) == 0 ? 1 : -1; // 1 = spawn at top
            int xSpawnLocation = Greenfoot.getRandomNumber (WORLD_W-200) + 100; // random between 99 and 699, so not near edges
            int ySpawnLocation = direction == 1 ? 230 : getHeight()-20;
            int pedType = Greenfoot.getRandomNumber(100);
            if(pedType < 35) {
                addObject(new Koopa(direction), xSpawnLocation, ySpawnLocation);
            } else if(pedType < 65) {
                addObject(new Toad(direction), xSpawnLocation, ySpawnLocation);
            } else if(pedType < 90) {
                addObject(new Goomba(direction), xSpawnLocation, ySpawnLocation);
            } else {
                addObject(new Mario(direction), xSpawnLocation, ySpawnLocation);
            }
        }
        // Chance to spawn bullet bill
        if(Greenfoot.getRandomNumber(200) == 0) {
            int lane = Greenfoot.getRandomNumber(bulletSpawners.length);
            VehicleSpawner spawner = bulletSpawners[lane];
            if(!spawner.isTouchingVehicle() && !spawner.isTouchingBullet()) {
                addObject(new BulletBill(spawner), 0, 0);
            }
        }
        // rain
        if (Greenfoot.getRandomNumber (500) == 0 && !raining && !puddlesLeft()){
            addObject(new Rain(240), WORLD_W/2, WORLD_H/2);
        }
    }
    
    // Draw lanes; designed based on 'int[] prepareLanes()' from Vehicle Simulator Starter Code by Mr. Cohen
    public static int[] prepareSnakeLanes(World world, GreenfootImage target, TurnFlag[] turnFlags, int startX, int startY, int laneLength, int laneWidth, int turns, int laneSpacing, int streetSpacing ) {
        // lane variables
        int lanes = turns*2 + 2; // number of lanes
        int streets = lanes/2;  // number of streets; one street = two adjacent lanes
        int[] lanePositions = new int[lanes];
        int heightOffset = laneWidth/2;
        int laneX, adjustedLength;
        
        // turn/curve variables
        boolean leftCurve;
        int curveX, offsetter;
        int curveRadius = laneWidth*2 + laneSpacing + streetSpacing/2;
        laneLength -= curveRadius*2; // Include space taken by curves in given laneLength
        int whiteLineRadius = curveRadius-laneWidth;  // draw white semi-circle for lane separating line
        int smallRadius = curveRadius-laneWidth-laneSpacing;  // cover up white semi-circle (^^) to mimick line
        
        // draw lanes
        for(int i=0;i<lanes;i++) {
            int street = i/2;  // street number
                            // y offset      account for space btwn lanes           account for space btwn streets      save as center y-val of lane
            lanePositions[i] = startY + (i-i/2)*(laneWidth+laneSpacing) + (i/2)*(laneWidth+streetSpacing) + heightOffset;            
            // draw street body
            target.setColor(STREET);
            if(street == 0 && (streets)%2==0) { // top street and even total streets, street starts from left of world
                laneX = 0;
                adjustedLength = laneLength + startX + curveRadius;
            } else if(street == 0) {  // top street and odd total streets, street starts from right of world
                laneX = startX+curveRadius;
                adjustedLength = target.getWidth()-startX-curveRadius;
            } else if(street == streets - 1) {  // bottom street always start from left
                laneX = 0;
                adjustedLength = laneLength + startX + curveRadius;
            } else {  // all other streets
                laneX = startX+curveRadius;
                adjustedLength = laneLength;
            }
            target.fillRect(laneX, lanePositions[i]-heightOffset, adjustedLength, laneWidth);
            
            // white-line lane spacing
            if(i>0 && i%2 == 1) {
                target.setColor(SEP_LINE);
                target.fillRect(laneX, lanePositions[i]-heightOffset-laneSpacing, adjustedLength, laneSpacing);
            }
            // turns/curves
            if(street != streets-1 && i%2==0) { // dont draw turn at end of last street & only need to draw every two lanes
                // calculate variables for curve
                leftCurve = street%2 == turns%2;  // curve is/isnt left-semi-circle 
                GreenfootImage curveImage = new GreenfootImage(curveRadius, curveRadius*2);
                offsetter = leftCurve ? curveRadius : 0;  // offsets each layered semi circle to create final semi-circular road
                curveX = leftCurve ? startX : startX + laneLength + curveRadius; // get x val of curve location in world
                // draw curves
                drawLaneCurve(curveImage, offsetter-curveRadius, 0, curveRadius, leftCurve, STREET);  // draw base
                drawLaneCurve(curveImage, offsetter-whiteLineRadius, laneWidth, whiteLineRadius, leftCurve, SEP_LINE);  // white space for line
                drawLaneCurve(curveImage, offsetter-smallRadius, laneWidth+laneSpacing, smallRadius, leftCurve, STREET);  // cover non-line white space
                target.drawImage(curveImage, curveX, lanePositions[i]-heightOffset);
                
                // create TurnFlag objects --> indicates to vehicles when/how they should turn to follow path
                offsetter = leftCurve ? laneWidth/4 : -laneWidth/4;
                ShallowTurnFlag shallowFlag = new ShallowTurnFlag(curveX+laneWidth+laneSpacing+offsetter, lanePositions[i]+laneWidth*2, laneWidth/2, laneWidth/2, smallRadius-laneWidth/2, leftCurve);
                WideTurnFlag wideFlag = new WideTurnFlag(curveX+laneWidth+laneSpacing-offsetter, lanePositions[i]+laneWidth*3+laneSpacing*2, laneWidth, laneWidth, curveRadius-laneWidth/2, leftCurve);
                // flip flag flips vehicle's image
                FlipFlag flipFlag = new FlipFlag(curveX+curveRadius/2, lanePositions[i]+laneWidth, laneWidth*2+laneSpacing, 4);
                world.addObject(flipFlag, flipFlag.getX(), flipFlag.getY());
                turnFlags[i] = shallowFlag;
                turnFlags[i+1] = wideFlag;
            }
        }
        for(TurnFlag f : turnFlags) {  // add turnFlags to world
            world.addObject(f, f.getX(), f.getY());
        }        
        return lanePositions;
    }
    // Draw semi-circle to mimic a turn/curve in the lane
    private static void drawLaneCurve (GreenfootImage target, int x, int y, int radius, boolean leftCurve, Color color) { 
        GreenfootImage curveImage = new GreenfootImage(radius, radius*2);
        curveImage.setColor(color);
        if(leftCurve) {
            curveImage.fillOval(0, 0, radius*2, radius*2);  // shows left half of semi-circle
            target.drawImage(curveImage, x, y);
        } else {
            curveImage.fillOval(-radius, 0, radius*2, radius*2); // show right half of semi-circle by shifting circle "radius" pixels to the left
            target.drawImage(curveImage, x+radius, y);
        }
    }
    // Method by Mr. Cohen
    public static int[] prepareLanes (World world, GreenfootImage target, VehicleSpawner[] spawners, int startY, int heightPerLane, int lanes, int spacing, boolean twoWay, boolean centreSplit){
        // Declare an array to store the y values as I calculate them
        int[] lanePositions = new int[lanes];
        int heightOffset = heightPerLane / 2;

        // Main Loop to Calculate Positions and draw lanes
        for (int i = 0; i < lanes; i++){
            // calculate the position for the lane
            lanePositions[i] = startY + spacing + (i * (heightPerLane+spacing)) + heightOffset ;

            // draw lane
            target.setColor(STREET); 
            // the lane body
            target.fillRect (0, lanePositions[i] - heightOffset, target.getWidth(), heightPerLane);
            // the lane spacing - where the white or yellow lines will get drawn
            target.fillRect(0, lanePositions[i] + heightOffset, target.getWidth(), spacing);

            // Place spawners and draw lines depending on whether its 2 way and centre split
            if (twoWay && centreSplit){
                // first half of the lanes go rightward (no option for left-hand drive, sorry UK students .. ?)
                if ( i < lanes / 2){
                    spawners[i] = new VehicleSpawner(false, heightPerLane, i%2 == 1, lanePositions[i]);
                    world.addObject(spawners[i], target.getWidth(), lanePositions[i]);
                } else { // second half of the lanes go leftward
                    // System.out.println("hi");
                    spawners[i] = new VehicleSpawner(true, heightPerLane, i%2 == 1, lanePositions[i]);
                    world.addObject(spawners[i], 0, lanePositions[i]);
                }

                // draw yellow lines if middle 
                if (i == lanes / 2){
                    target.setColor(YELLOW_LINE);
                    target.fillRect(0, lanePositions[i] - heightOffset - spacing, target.getWidth(), spacing);

                } else if (i > 0){ // draw white lines if not first lane
                    // for (int j = 0; j < target.getWidth(); j += 120){
                    target.setColor (SEP_LINE);
                    target.fillRect(i, lanePositions[i] - heightOffset - spacing, target.getWidth(), spacing);
                        // target.fillRect (j, lanePositions[i] - heightOffset - spacing, 60, spacing);
                    // }
                } 

            } else if (twoWay){ // not center split
                if ( i % 2 == 0){
                    spawners[i] = new VehicleSpawner(false, heightPerLane, true, lanePositions[i]);
                    world.addObject(spawners[i], target.getWidth(), lanePositions[i]);
                } else {
                    spawners[i] = new VehicleSpawner(true, heightPerLane, true, lanePositions[i]);
                    world.addObject(spawners[i], 0, lanePositions[i]);
                }

                // draw Grey Border if between two "Streets"
                if (i > 0){ // but not in first position
                    if (i % 2 == 0){
                        target.setColor(GREY_BORDER);
                        target.fillRect(0, lanePositions[i] - heightOffset - spacing, target.getWidth(), spacing);

                    } else { // draw lines
                        // for (int j = 0; j < target.getWidth(); j += 120){
                        target.setColor (SEP_LINE);
                        target.fillRect(i, lanePositions[i] - heightOffset - spacing, target.getWidth(), spacing);
                        // target.fillRect (j, lanePositions[i] - heightOffset - spacing, 60, spacing);
                        // }
                    } 
                }
            } else { // One way traffic
                spawners[i] = new VehicleSpawner(true, heightPerLane, true, lanePositions[i]);
                world.addObject(spawners[i], 0, lanePositions[i]);
                if (i > 0){
                    for (int j = 0; j < target.getWidth(); j += 120){
                        target.setColor (Color.WHITE);
                        target.fillRect (j, lanePositions[i] - heightOffset - spacing, 60, spacing);
                    }
                }
            }
        }
        return lanePositions;
    }
    public int getLaneWidth() {
        return laneWidth;
    }
    public int getLaneLength() {
        return laneLength;
    }
    // - - - - - - - - World Event Methods - - - - -  - - - - -  - - 
    public boolean isBulletStormActive() {
        ArrayList<BulletLuigi> bulletLuigiArr = (ArrayList<BulletLuigi>)getObjects(BulletLuigi.class);
        return bulletLuigiArr.size() != 0;
    }
    public void spawnBulletStorm() {  //called by Luigi to start bullet storm.
        // x shift every 40-50, y anywhere between -100 & -20, speed anywhere from 5 to 12
        bulletStormActive = true;
        int spawnX = 60;
        int spawnY;
        int speed;
        ArrayList<BulletLuigi> bulletLuigiArr = new ArrayList<BulletLuigi>();
        while(spawnX < WORLD_W - 60) {
            spawnX += 40 + Greenfoot.getRandomNumber(11);
            spawnY = -100 + Greenfoot.getRandomNumber(81);
            speed = 5 + Greenfoot.getRandomNumber(8);
            bulletLuigiArr.add(new BulletLuigi("luigibullet0.png", spawnX, spawnY, speed));
        }
        for(BulletLuigi b : bulletLuigiArr) {
            addObject(b, 0, 0);
        }
    }
    public void rain() {
        raining = true;
    }
    public void stopRain() {
        raining = false;
    }
    public boolean isRaining() {
        return raining;
    }
    public boolean puddlesLeft() {
        ArrayList<Puddle> puddles= (ArrayList<Puddle>)getObjects(Puddle.class);
        return puddles.size() > 0;
    }
    public int[] getLaneYs() {  // called by spawnPuddle() to choose random location on random lane
        int total = snkLane1PositionsY.length + snkLane2PositionsY.length + strtLanePositionsY.length;
        int[] allLanePositionsY = new int[total];
        System.arraycopy(snkLane1PositionsY, 0, allLanePositionsY, 0, snkLane1PositionsY.length);
        System.arraycopy(snkLane2PositionsY, 0, allLanePositionsY, snkLane1PositionsY.length, snkLane2PositionsY.length);
        System.arraycopy(strtLanePositionsY, 0, allLanePositionsY, snkLane1PositionsY.length + snkLane2PositionsY.length, strtLanePositionsY.length);

        return allLanePositionsY;
    }
    public void spawnPuddle() {  // called by rain class at random intervals
        int x, y;
        int[] laneYPositions = getLaneYs();
        x = Greenfoot.getRandomNumber(laneLength) + startX;
        y = laneYPositions[Greenfoot.getRandomNumber(laneYPositions.length)];
        addObject(new Puddle(420), x, y);
    }
}
