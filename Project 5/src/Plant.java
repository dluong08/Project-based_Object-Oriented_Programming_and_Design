import processing.core.PImage;

import java.util.List;

public abstract class Plant extends Animated {

    private int actionPeriod;
    private int health;
    private int healthLimit;

    public Plant(String id, Point position, List<PImage> images, int imageIndex,
                 int animationPeriod, int actionPeriod, int health, int healthLimit) {
        super(id, position, images, imageIndex, animationPeriod);
        this.actionPeriod = actionPeriod;
        this.health = health;
        this.healthLimit = healthLimit;
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        if (!this.transform(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    Functions.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }

    public boolean transform( WorldModel world,
                                       EventScheduler scheduler,
                                       ImageStore imageStore)
    {
        if (this.getHealth() <= 0) {
            Entity stump = Functions.createStump(this.getId(),
                    this.getPosition(),
                    imageStore.getImageList(Functions.STUMP_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(stump);

            return true;
        }

        return false;
    }

    protected int getHealth() {
        return health;
    }

    protected void setHealth(int health) { this.health = health; }

    protected int getHealthLimit() { return healthLimit; }

    protected int getActionPeriod() { return actionPeriod; }

    }
