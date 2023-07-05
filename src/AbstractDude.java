import processing.core.PImage;
import java.util.List;

public abstract class AbstractDude extends AbstractMove {
    public AbstractDude(String id, Point position,
                        List<PImage> images, int resourceLimit,
                        int resourceCount, int actionPeriod,
                        int animationPeriod, int repeatCount) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, 0);
    }

    public void transformFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore) {
        Entity miner = WorldModel.createDudeNotFull(this.getId(),
                this.getPosition(), this.getActionPeriod(),
                this.getAnimationPeriod(),
                this.getresourceLimit(),
                this.getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        ((Move) miner).scheduleActions(scheduler, world, imageStore);
    }
}