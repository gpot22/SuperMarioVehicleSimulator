import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
/**
 * Rain World Event --> slows down vehicles and begin the spawning of Puddles
 * 
 * Sprite Credits:
 * https://parspng.com/en/images/png-rainfall/
 * Edited by Caden Chan
 * 
 * Audio Credits:
 * https://www.zapsplat.com/sound-effect-category/rain/
 */
public class Rain extends Effect
{
    private int duration;
    private String imageStr;
    private ArrayList<Puddle> puddles;
    private GreenfootSound rainSound;
    public Rain (int duration){
        this.duration = duration;
        imageStr = "rain.png";
        puddles = new ArrayList<Puddle>();
    }

    public void addedToWorld (World w){
        image = drawRain (w.getWidth(), w.getHeight(), imageStr);
        setImage(image);
        ArrayList<RoadVehicle> vehicles = (ArrayList<RoadVehicle>) w.getObjects(RoadVehicle.class);
        for (RoadVehicle v : vehicles){
            v.slowMeDown();
        }
        rainSound = new GreenfootSound("rain.wav");
        rainSound.setVolume(60);
        rainSound.play();
        VehicleWorld vw = (VehicleWorld)w;
        vw.rain();
    }

    public void act()
    {
        if (duration == 0){
            ArrayList<RoadVehicle> vehicles = (ArrayList<RoadVehicle>) getWorld().getObjects(RoadVehicle.class);
            for (RoadVehicle v : vehicles){
                v.resumeNormalSpeed();
            }
            // Tell the world to stop raining
            VehicleWorld vw = (VehicleWorld)getWorld();
            vw.stopRain();
            vw.removeObject(this);
            rainSound.stop();
            return;
        } else if (duration <= 120){
            fade (duration, 120);
        }
        if(Greenfoot.getRandomNumber(20) == 0) {
            ((VehicleWorld)getWorld()).spawnPuddle();
        }
        duration--;
    }
    public static GreenfootImage drawRain(int width, int height, String imageStr) {
        GreenfootImage rainImage = new GreenfootImage(width, height);
        rainImage.setColor(new Color(195, 228, 248));
        // rainImage.setTransparency(20);
        // rainImage.fill();
        rainImage.drawImage(new GreenfootImage (imageStr), 0, 100);
        return rainImage;
    }
    public GreenfootSound getRainSound() {
        return rainSound;
    }
    
}
