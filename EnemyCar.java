import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
/**
 * The EnemyCar subclass, knocks down pedestrians by throwing red shells or running over them
 * 
 * Sprite Credits:
 * See RoadVehicle Class
 */
public class EnemyCar extends ItemTosser {
    public EnemyCar(VehicleSpawner origin) {
        super(origin); // call the superclass' constructor
        setRandomImage(new String[]{"goombacar0.png", "wariocar0.png"});
        fixAvatarDirection();
        // hitbox.syncToTarget();
        hitbox.setWidth(getImage().getWidth());
        hitbox.setHeight((int)(getImage().getHeight()/1.8));
        hitbox.setYOffset(getImage().getHeight()/4);
        
        maxSpeed = 1.5 + ((Math.random() * 30)/5);
        speed = maxSpeed;
        yOffset = getImage().getHeight()/4;
        tossCooldown = 0;
        tossChance = 100;
        heavy = false;
    }

    public void act()
    {
        super.act();
        if (tossCooldown != 0) {
            tossCooldown --;
        }
        else if(Greenfoot.getRandomNumber(tossChance) == 0) {
            if(getWorld() != null) {
                tossItem();
                tossCooldown = 60;
            }
        } 
    }
    
    
    /**
     * When a Car hit's a Pedestrian, it should knock it over
     */
    public boolean checkHitPedestrian () {
        double[] offset = calculateOffsetAhead(2);
        PedHitbox ph = (PedHitbox)getOneObjectAtOffset((int)offset[0], (int)offset[1], PedHitbox.class);
        
        if (ph != null){
            Pedestrian p = (Pedestrian)ph.getTarget();
            if(p instanceof Koopa) {
                ((Koopa)p).pushShell(rotation, speed);
                return true;
            }
            p.knockDown();
            return true;
        }
        Koopa p2 = (Koopa)hitbox.getOneIntersectingObject(Koopa.class);
        if (p2!= null && !p2.isAwake()) {
            
            ((Koopa)p2).pushShell(rotation, speed);
            return true;
        }
        return false;
    }
    
    public void tossItem() {
        ArrayList<Pedestrian> movingPeds = (ArrayList<Pedestrian>)(getWorld().getObjects(Pedestrian.class));
        movingPeds.removeIf(p -> !p.isAwake()); // only include awake pedestrians
        // allPeds.removeIf(p -> p.isTargetedByMushroom()); // do not include if pedestrian is already being targeted by mushroom
        if(movingPeds.size() == 0) {
            return;
        }
        int rand = Greenfoot.getRandomNumber(movingPeds.size());
        Pedestrian ped = movingPeds.get(rand);
        RedShell m = new RedShell(this, ped);
        getWorld().addObject(m, getX(), getY());
    }
}
