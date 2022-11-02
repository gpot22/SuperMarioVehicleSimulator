import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Effect here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Effect extends Actor
{
    protected GreenfootImage image;
    
    protected void fade (int timeLeft, int fadeTime) {
        double percent = timeLeft / (double)fadeTime;
        // Transparency 0 -- 255
        int newTransparency = (int)(percent * 255);
        image.setTransparency(newTransparency);
    }
}
