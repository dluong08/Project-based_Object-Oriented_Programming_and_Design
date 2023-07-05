import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Dude_Not_Full extends Execute {

    private int resourceLimit;
    private int resourceCount;

    public Dude_Not_Full(
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int actionPeriod,
            int animationPeriod)
    {
        super(id, position, images, 0, animationPeriod, actionPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> target =
                world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(Tree.class, Sapling.class)));

        if (!target.isPresent() || !this.moveTo(world,
                target.get(),
                scheduler)
                || !this.transform(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    Functions.createActivityAction(this, world, imageStore),
                    this.getActionPeriod());
        }
    }

    public Point nextPosition(
            WorldModel world, Point destPos)
    {
        /*
        int horiz = Integer.signum(destPos.getX() - this.getPosition().getY());
        Point newPos = new Point(this.getPosition().getX() + horiz, this.getPosition().getY());

        if (horiz == 0 || world.isOccupied(newPos) && !(world.getOccupancyCell(newPos).getClass() == Stump.class)) {
            int vert = Integer.signum(destPos.getY() - this.getPosition().getY());
            newPos = new Point(this.getPosition().getX(), this.getPosition().getY() + vert);

            if (vert == 0 || world.isOccupied(newPos) &&  !(world.getOccupancyCell(newPos).getClass() == Stump.class)) {
                newPos = this.getPosition();
            }
        }

        return newPos;

         */

        Predicate<Point> canPassThrough = (p) -> (world.withinBounds(p) && world.getOccupancyCell(p) == null &&
        !world.isOccupied(p)) || (world.withinBounds(p) && world.getOccupancyCell(p).getClass() == Stump.class);
        BiPredicate<Point, Point> withinReach = (p1, p2) -> Functions.adjacent(p1, p2);

        AStarPathingStrategy strat = new AStarPathingStrategy();
        List<Point> path = strat.computePath(this.getPosition(), destPos, canPassThrough, withinReach,
                strat.CARDINAL_NEIGHBORS);

        return path != null && path.size() != 0 ? path.get(0) : this.getPosition();
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

    public boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore) {
        if (this.resourceCount >= this.resourceLimit) {
            Entity miner = Functions.createDudeFull(this.getId(),
                    this.getPosition(), this.getActionPeriod(),
                    this.getAnimationPeriod(),
                    this.resourceLimit,
                    this.getImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            ((Dude_Full)miner).scheduleActions(scheduler, world, imageStore); // changed from animated to dudeFull

            return true;
        }

        return false;
    }

    public void transformToFollower(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore) {

        List<PImage> images = imageStore.getImageList("follower");

            Entity miner = Functions.createFollower(Functions.FOLLOWER_KEY,
                    this.getPosition(), this.getActionPeriod(),
                    this.getAnimationPeriod(),
                    this.resourceLimit,
                    images);

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            ((Follower)miner).scheduleActions(scheduler, world, imageStore);
    }

    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (Functions.adjacent(this.getPosition(), target.getPosition())) {
            this.resourceCount += 1;
            ((Plant)target).setHealth(((Plant)target).getHealth() - 1);
            return true;
        }
        else {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }


}
