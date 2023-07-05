import processing.core.PImage;
import java.util.List;

abstract public class Actioned extends Entity{

    public Actioned(String id, Point position, List<PImage> images, int resourseLimit, int resourceCount,
                    int actionPeriod, int animationPeriod, int health, int healthLimit)
    {
        super(id,position,images, resourseLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                this.getActionPeriod());
        EventScheduler.scheduleEvent(this,
                Animation.createAnimationAction(this, 0),
                this.getAnimationPeriod());
    }


}
