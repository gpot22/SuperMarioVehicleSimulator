import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
/**
 * Luigi spawns when a bus gets blown up by an exploding BulletBill
 * 
 * Sprite Credits:
 * "Hurt", "Blown Away", https://www.deviantart.com/weakfoggy/art/RPG-Maker-MV-Luigi-Overworld-2-583595164
 */
public class Luigi extends Effect
{
    private int pauseTimer = 40;
    private int angryTimer = 120;
    private int screamTimer = 180;
    private int speed = 2;
    private int stompCount = 0;
    private int stormCount = 3;
    private boolean screamSoundPlayed = false;
    public static Luigi active = null;
    // Luigi Sounds
    public static GreenfootSound[] luigiHurtSounds;
    public static int luigiHurtSoundIndex;
    public static GreenfootSound[] luigiStompSounds;
    public static int luigiStompSoundIndex;
    public void addedToWorld(World w) {
        luigiHurtSoundIndex = SoundManager.playMySound(luigiHurtSounds, luigiHurtSoundIndex, 60);
        
    }
    public static void initSound(){
        luigiHurtSoundIndex = 0;
        luigiHurtSounds = SoundManager.initSound("luigihurt.wav", 6);
        luigiStompSoundIndex = 0;
        luigiStompSounds = SoundManager.initSound("bulletbump_p.wav", 15);
    }
    public void act()
    {
        VehicleWorld vh = (VehicleWorld)getWorld();
        if(pauseTimer > 0) {
            pauseTimer --;
        } else if(angryTimer > 0 && active == this && !vh.isBulletStormActive()) {
            angryTimer --;
            if(stompCount%20 < 10) {
                if(stompCount%20 == 9) {
                    luigiStompSoundIndex = SoundManager.playMySound(luigiStompSounds, luigiStompSoundIndex, 60);
                }
                setRotation(270);
            } else {
                setRotation(90);
            }
            move(speed);
            setRotation(0);
            stompCount ++;
        // Luigi starts screaming
        } else if (screamTimer > 0 && active == this && !((VehicleWorld)getWorld()).isBulletStormActive()) {
            screamTimer --;
            if(screamTimer % 60 == 0) {
                if(!screamSoundPlayed) {
                    Scream.initSound();
                    Scream.playMySound();
                    screamSoundPlayed = true;
                }
                getWorld().addObject(new Scream(600), getX(), getY());
            }
        } else {
            if(getX() < ((VehicleWorld)getWorld()).WORLD_W/2) {
                move(-5);
            } else {
                move(5);
            }
            if(getX() < -20 || getX() > ((VehicleWorld)getWorld()).WORLD_W) {
                if(!vh.isBulletStormActive() && stormCount > 0 && active == this) {
                    vh.spawnBulletStorm();
                    stormCount --;
                }
                if (stormCount == 0){
                    active = null;
                    getWorld().removeObject(this);
                }
            }
        }
    }
    
}
