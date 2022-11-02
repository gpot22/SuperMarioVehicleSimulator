import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Superclass for all actors which may (or may not) need to have
 * an animated sprite cycle.
 */
public abstract class Animator extends SuperSmoothMover
{
    private int index, maxIndex;
    protected int imgActCount; // variabled used by subclasses to track frame count to be used for animations (i.e. call cycleAvatar() every 5 acts)
    private String prefix, suffix;
    protected boolean animated = false;
    protected boolean movedThisAct; // regulate animating; if false, dont go to next sprite in cycle
    
    // Set parameters for sprite cycle; based on names of sprite files, which are strategically
    // named to be looped through easily with cycleAvatar(). 
    // Ex: prefix = bulletbill, suffix = a.png, existing images are bulletbill0a.png, bulletbill1a.png, etc.
    public void setAnimationCycle(String prefix, String suffix, int maxIndex) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.maxIndex = maxIndex;
        imgActCount = 0;
        animated = true;
    }
    // switch actor image to next in "list"
    public void cycleAvatar() {
        index = (index+1)%maxIndex;
        String a = prefix + index + suffix;
        setImage(a);
    }
    // Setters
    public void setPrefix(String s) {
        prefix = s;
    }
    public void setSuffix(String s) {
        suffix = s;
    }
}
