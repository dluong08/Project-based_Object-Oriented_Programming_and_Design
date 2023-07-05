import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public abstract class Execute extends Animated {

    private int actionPeriod;

    public Execute(String id, Point position, List<PImage> images, int imageIndex,
                   int animationPeriod, int actionPeriod) {
        super(id, position, images, imageIndex, animationPeriod);
        this.actionPeriod = actionPeriod;
    }

    abstract void executeActivity(WorldModel world,
                         ImageStore imageStore,
                         EventScheduler scheduler);

    public abstract boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler);

    abstract Point nextPosition(WorldModel world, Point destPos);

    abstract boolean transform(WorldModel world,
                      EventScheduler scheduler,
                      ImageStore imageStore);

    protected int getActionPeriod() { return this.actionPeriod; }

}
