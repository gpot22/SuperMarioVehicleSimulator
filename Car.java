import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The Car subclass
 * Sprite Credits:
 * See RoadVehicle Class
 */
public class Car extends RoadVehicle {
    
    public Car(VehicleSpawner origin) {
        super(origin); // call the superclass' constructor
        setRandomImage(new String[]{"toadcar0.png", "toadettecar0.png"});
        
        hitbox.setWidth(getImage().getWidth());
        hitbox.setHeight((int)(getImage().getHeight()/1.8));
        hitbox.setYOffset(getImage().getHeight()/4);
        
        maxSpeed = 1.5 + ((Math.random() * 30)/5);
        speed = maxSpeed;
        fixAvatarDirection();
        yOffset = getImage().getHeight()/4;
        heavy = false;
    }

    public void act()
    {
        super.act();
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
    
}
