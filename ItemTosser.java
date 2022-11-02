import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class ItemTosser here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class ItemTosser extends RoadVehicle
{
    protected int tossChance;
    protected int tossCooldown;
    
    protected abstract boolean checkHitPedestrian ();
    protected abstract void tossItem();
    public ItemTosser(VehicleSpawner origin) {
        super(origin);
    }
    public void act()
    {
        super.act();
    }
}
