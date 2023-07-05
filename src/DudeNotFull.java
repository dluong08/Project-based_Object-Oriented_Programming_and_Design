import java.util.*;
import processing.core.PImage;

public final class DudeNotFull extends AbstractDude{
    public String id;
    public Point position;
    public List<PImage> images;
    public int imageIndex;
    public int resourceLimit;
    public int resourceCount;
    public int actionPeriod;
    public int animationPeriod;
    public int health;
    public int healthLimit;

    public DudeNotFull(
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int actionPeriod,
            int animationPeriod,
            int health,
            int healthLimit) {
        super(id, position, images, resourceLimit, resourceLimit, actionPeriod, animationPeriod, 0);

    }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                this.actionPeriod);
        scheduler.scheduleEvent(this,
                Animation.createAnimationAction(this, 0),
                this.getAnimationPeriod());
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> target =
                world.findNearest(this.position, new ArrayList<>(Arrays.asList(Tree.class, Sapling.class)));

        if (!target.isPresent() || !this.moveToFull(world,
                target.get(),
                scheduler)
                || !transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    Activity.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }

    public boolean moveToFull(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (Point.adjacent(this.position, target.getPosition())) {
            this.resourceCount += 1;
            return true;
        }
        else {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!this.position.equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant( nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity( this, nextPos);
            }
            return false;
        }
    }

    public Point nextPosition( WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz,
                this.position.y);

        if (horiz == 0 || world.isOccupied( newPos))
        {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x,
                    this.position.y + vert);

            if (vert == 0 || world.isOccupied( newPos))
            {
                newPos = this.position;
            }
        }

        return newPos;
    }

    public boolean transformNotFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (this.resourceCount >= this.resourceLimit) {
            DudeFull miner = WorldModel.createDudeFull(this.id,
                    this.position, this.actionPeriod,
                    this.animationPeriod,
                    this.resourceLimit,
                    this.images);

            world.removeEntity(this);
            scheduler.unscheduleAllEvents( this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public int getActionPeriod() { return actionPeriod; }
    public int getAnimationPeriod() { return animationPeriod; }
    public int getResourceLimit() { return resourceLimit; }
    public void nextImage() { imageIndex = (imageIndex + 1) % images.size(); }
    public Point getPosition() { return position; }
    public void setPosition(Point p) { this.position = p; }
    public List<PImage> getImages() { return images; }
    public String getId() { return id; }
    public int getImageIndex() { return imageIndex; }
    public int getHealth() {return this.health;}
}
