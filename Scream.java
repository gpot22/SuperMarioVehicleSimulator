import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Scream class to give the visual effect of Luigi screaming
 * Written by Caden Chan, based on Mr. Cohen's Explosion effect
 */
public class Scream extends Effect
{
    private GreenfootImage screamImage;

    private int radius;
    private int transparency;
    private int speed;
    private int steps;
    private int maxSize;
    
    public static GreenfootSound[] luigiScreamSounds;
    public static int luigiScreamSoundIndex;
    public Scream (int maxSize)
    {
        // Create image to manage this graphic
        screamImage = new GreenfootImage (maxSize, maxSize);
        transparency = 255;

        // Dynamic way to set speed so small explosions don't
        // disappear to fast and large explosions don't linger
        speed = Math.max((int)Math.sqrt(maxSize), 1);
        // Set starting Radius
        radius = 3;
        steps = (maxSize) / (speed);
        this.maxSize = maxSize;

        // Method to actually draw the circle
        redraw();
        // Set this Actor's graphic as the image I just created
        this.setImage(screamImage);
    }

    public void addedToWorld (World w){
        
    }
    public static void playMySound() {
        luigiScreamSoundIndex = SoundManager.playMySound(luigiScreamSounds, luigiScreamSoundIndex, 60);
    }
    public static void initSound() {
        luigiScreamSoundIndex = 0;
        luigiScreamSounds = SoundManager.initSound("luigiscream.wav", 3);
    }

    /**
     * Act method gets called by Greenfoot every frame. In this class, this method
     * will serve to increase the size each act until maxSize is reached, at which
     * point the object will remove itself from existence.
     */
    public void act() 
    {
        redraw();   // redraw the circle at its new size

        if (radius + speed <= maxSize)  // If the explosion hasn't yet hit its max
            radius += speed;            // size, keep growing

        else{ 
            getWorld().removeObject(this);
        }
        
    }    

    /**
     * redraw() method is a private method called by this object each act
     * in order to redraw the graphic
     */
    private void redraw()
    {
        
        // reduce transparency, but ensure it doesn't fall below zero - that would cause
        // a crash
        transparency = Math.max(transparency-(255/steps), 0);


        // update transparency
        screamImage.setColor (Color.WHITE);
        // redraw image
        screamImage.drawOval ((maxSize - radius)/2, (maxSize - radius)/2, radius, radius);
        
        screamImage.setTransparency(transparency);
    }

}
