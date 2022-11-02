import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Contains methods used to initialize and play sounds
 */
public class SoundManager extends Actor
{
    public static GreenfootSound[] initSound(String soundName, int numOfDupes){
        GreenfootSound[] sounds = new GreenfootSound[numOfDupes]; // lots of simultaneous explosioning!
        for (int i = 0; i < sounds.length; i++){
            sounds[i] = new GreenfootSound(soundName);
        }   
        return sounds;
    }
    public static int playMySound(GreenfootSound[] sounds, int currentIndex, int volume) {
        currentIndex++;
        sounds[currentIndex].setVolume(volume);
        sounds[currentIndex].play();
        if (currentIndex >= sounds.length){
            currentIndex = 0;
        }
        return currentIndex;
    }
}
