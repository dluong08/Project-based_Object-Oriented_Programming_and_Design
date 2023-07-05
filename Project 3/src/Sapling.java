import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Sapling extends Position {

    public Sapling(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod,
            int health,
            int healthLimit)
    {
        super(id, position, images, 0, animationPeriod, actionPeriod, health, healthLimit);
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        this.setHealth(this.getHealth() + 1);
        super.executeActivity(world, imageStore, scheduler);
    }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                Functions.createActivityAction(this, world, imageStore),
                this.getActionPeriod());
        super.scheduleActions(scheduler, world, imageStore);
    }

    public boolean transform( WorldModel world,
                              EventScheduler scheduler,
                              ImageStore imageStore)
    {
        super.transform(world, scheduler, imageStore);
        if (this.getHealth() >= this.getHealthLimit())
        {
            Entity tree = Functions.createTree("tree_" + this.getId(),
                    this.getPosition(),
                    Functions.getNumFromRange(Functions.TREE_ACTION_MAX, Functions.TREE_ACTION_MIN),
                    Functions.getNumFromRange(Functions.TREE_ANIMATION_MAX, Functions.TREE_ANIMATION_MIN),
                    Functions.getNumFromRange(Functions.TREE_HEALTH_MAX, Functions.TREE_HEALTH_MIN),
                    imageStore.getImageList(Functions.TREE_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(tree);
            ((Position)tree).scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

}