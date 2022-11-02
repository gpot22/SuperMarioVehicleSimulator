import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
/**
 * The Bus subclass
 * Sprite Credits:
 * https://www.subpng.com/png-ua8x87/
 * https://www.deviantart.com/snowgibbonbasilisk91/art/Super-Mario-Kart-Sprites-w-Customs-403971952
 * Edited by Caden Chan
 */
public class Bus extends RoadVehicle {
    private int passengerCount;
    private final int maxPassengers = 4;
    private int[] passengers;
    public Bus(VehicleSpawner origin){
        super (origin); // call the superclass' constructor first
        
        //Set up values for Bus
        maxSpeed = 1.5 + ((Math.random() * 10)/5);
        speed = maxSpeed;
        // because the Bus graphic is tall, offset it a bit up (this may result in some collision check issues)
        yOffset = 15;
        hitbox.setHeight(hitbox.getHeight()/2);
        hitbox.setYOffset(hitbox.getHeight()/2);
        hitbox.setWidth(hitbox.getWidth()-16);
       
        heavy = true;
        passengerCount = 0;
        passengers = new int[maxPassengers];
    }

    /**
     * Act - do whatever the Bus wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // super.act();
        if(speed > 0) {
            drive();
        }
        checkHitPedestrian();
        if (checkEdge()){
            getWorld().removeObject(hitbox);
            getWorld().removeObject(this);
        }
    }

    public boolean checkHitPedestrian () {
        go();
        if (isMaxCapacity()) {
            return false;
        }
        Pedestrian p = (Pedestrian)getOneObjectAtOffset((int)(speed + getImage().getWidth()/2), 0, Pedestrian.class);
        if (p != null){
            if(!p.isAwake()) {
                if(p instanceof Koopa) {
                ((Koopa)p).pushShell(rotation, speed);
                return true;
            }
                return false;
            }
            if(p.getTargetBus() == null) {
                p.setTargetBus(this);
            }
            if(p.getTargetBus() == this) {
                p.waitForBus();
                stop();
                p.stop();
                if(p.getWaitingForBus() == 60) {
                    addPassenger(p);
                }
            }
        }
        return false;
        
    }
    
    public void stop() {
        speed = 0;
    }
    public void go() {
        speed = maxSpeed;
    }
    public void addPassenger(Pedestrian p) {
        if(passengerCount >= passengers.length) {
            return;
        }
        drawPassenger((p.getHead()));
        go();
        
        if(p instanceof Toad) {
            passengers[passengerCount] = 1;
        } else if(p instanceof Goomba) {
            passengers[passengerCount] = 2;
        } else if(p instanceof Koopa) {
            passengers[passengerCount] = 3;
        } else if(p instanceof Mario) {
            passengers[passengerCount] = 4;
        }
        passengerCount ++;
        GreenfootSound addPassengerSound = new GreenfootSound("boardbus.wav");
        addPassengerSound.setVolume(75);
        addPassengerSound.play();
        getWorld().removeObject(p);
    }
    public void drawPassenger(String img) {
        drawPassenger(img, passengerCount);
    }
    public void drawPassenger(String img, int x) {
        GreenfootImage head = new GreenfootImage(img);
        if(direction == 1) { // facing right
            getImage().drawImage(head, 4 + 20*passengerCount, 12);
        } else if(direction == -1) { // facing left
            head.mirrorHorizontally();
            getImage().drawImage(head, 98 - 20*passengerCount, 12);
        }
    }
    public boolean isMaxCapacity() {
        return passengerCount >= maxPassengers;
    }
    public void spawnPassengers() {
        // spawn Luigi
        Luigi.initSound();
        Luigi l = new Luigi();
        if(Luigi.active == null) {  // Luigi only activates event if not already active
            Luigi.active = l;
        }
        if (rotation == 0) {  // set Luigi location
            getWorld().addObject(l, getX(), getY());
        } else {
            getWorld().addObject(l, getX()+getImage().getWidth()-20, getY());
        }
        // spawn other passengers
        int spawnX, spawnY, randDirection;
        
        for(int p: passengers) { // 1 = toad, 2 = goomba, 3 = koopa
            spawnX = Greenfoot.getRandomNumber(getImage().getWidth()) + getX()-getImage().getWidth()/2;
            spawnY = Greenfoot.getRandomNumber(getImage().getHeight()) + getY()-getImage().getHeight()/2;
            randDirection = Greenfoot.getRandomNumber(2) == 0 ? 1 : -1;
            if(p == 1) {
                getWorld().addObject(new Toad(randDirection), spawnX, spawnY);
            } else if(p == 2) {
                getWorld().addObject(new Goomba(randDirection), spawnX, spawnY);
            } else if(p == 3) {
                getWorld().addObject(new Koopa(randDirection), spawnX, spawnY);
            } else if(p == 4) {
                getWorld().addObject(new Mario(randDirection), spawnX, spawnY);
            }
        }
        // int spawnX = Greenfoot.getRandomNumber(getImage().getWidth()) + getX()-getImage().getWidth()/2;
        // int spawnY = Greenfoot.getRandomNumber(getImage().getHeight()) + getY()-getImage().getHeight()/2;
    }
    public void bulletStormCheat() {
        Luigi.initSound();
        spawnPassengers();
        getWorld().removeObject(this);
    }
}
