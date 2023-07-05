

import processing.core.PImage;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.lang.reflect.Executable;
import java.util.List;
import java.util.Optional;

abstract public class Dude extends Moved {

    protected int resourceLimit;
    protected int resourceCount;


    public Dude(String id, Point position, List<PImage> images, int resourseLimit, int resourceCount,
                    int actionPeriod, int animationPeriod, int health, int healthLimit)
    {
        super(id,position,images, resourseLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }

    public int getResourceLimit(){return resourceLimit;}
    public int getResourceCount(){return resourceCount;}
    abstract public void execute(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
}