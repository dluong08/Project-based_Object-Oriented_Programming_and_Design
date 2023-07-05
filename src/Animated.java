import processing.core.PImage;
import java.util.List;
import java.util.Optional;

abstract public class Animated extends Actioned {

    public Animated(String id, Point position, List<PImage> images, int resourseLimit, int resourceCount,
                    int actionPeriod, int animationPeriod, int health, int healthLimit)
    {
        super(id,position,images, resourseLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }


    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {

        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                this.getActionPeriod());
    }
}

