import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
/**
 * The Healer subclass --> my equivalent of Ambulence --> Heals pedestrians by collision or by throwing green mushrooms
 * Sprite Credits:
 * See RoadVehicle superclass
 */
public class Healer extends ItemTosser {
    public Healer(VehicleSpawner origin){
        super (origin); // call the superclass' constructor first
        setRandomImage(new String[]{"daisycar0.png", "birdocar0.png"});
        fixAvatarDirection();
        hitbox.setWidth(getImage().getWidth());
        hitbox.setHeight((int)(getImage().getHeight()/1.8));
        hitbox.setYOffset(getImage().getHeight()/4);
        maxSpeed = 1.5 + ((Math.random() * 30)/5);
        speed = maxSpeed;
        yOffset = getImage().getHeight()/4;
        tossCooldown = 0;
        tossChance = Pedestrian.totalFallen < 15 ? 300 : 90;
        tossChance += Greenfoot.getRandomNumber(60)-30;
        heavy = false;
    }
    public void addedToWorld(World w) {
        super.addedToWorld(w);
        GreenfootSound throwSound = new GreenfootSound("throw.wav");
        throwSound.setVolume(60);
        throwSound.play();
    }
    /**
     * Act - do whatever the Ambulance wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
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

    public boolean checkHitPedestrian () {
        ArrayList<PedHitbox> pArr = (ArrayList<PedHitbox>)(getObjectsInRange((int)(((VehicleWorld)getWorld()).getLaneWidth()/1.7), PedHitbox.class));
        if(pArr.size() > 0) {
            Pedestrian p = (Pedestrian)(pArr.get(0).getTarget());
            if(p instanceof Koopa) {
                ((Koopa)p).pushShell(rotation, speed);
            }
            else if(!p.isAwake()) {
                p.healMe();
            }
        Koopa p2 = (Koopa)hitbox.getOneIntersectingObject(Koopa.class);
        if (p2!= null && !p2.isAwake()) {
            
            ((Koopa)p2).pushShell(rotation, speed);
            return true;
        }
            return true;
        }
        return false;
    }
    public void tossItem() {
        ArrayList<Pedestrian> fallenPeds = (ArrayList<Pedestrian>)(getWorld().getObjects(Pedestrian.class));
        fallenPeds.removeIf(p -> p.isAwake()); // only include fallen pedestrians
        fallenPeds.removeIf(p -> p instanceof Koopa); // do not target Koopas; it is still possible for stray mushrooms to hit them thuouhg
        // allPeds.removeIf(p -> p.isTargetedByMushroom()); // do not include if pedestrian is already being targeted by mushroom
        if(fallenPeds.size() == 0) {
            return;
        }
        int rand = Greenfoot.getRandomNumber(fallenPeds.size());
        Pedestrian ped = fallenPeds.get(rand);
        ReviveMushroom m = new ReviveMushroom(this, ped);
        getWorld().addObject(m, getX(), getY());
    }
}
