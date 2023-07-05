import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Fairy extends Move {

    public Fairy (
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod)
    {
        super(id, position, images, 0, animationPeriod, actionPeriod);
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> fairyTarget =
                world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(Stump.class)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();

            if (this.moveTo(world, fairyTarget.get(), scheduler)) {
                Position sapling = (Position) Functions.createSapling("sapling_" + this.getId(), tgtPos,
                        imageStore.getImageList(Functions.SAPLING_KEY));

                world.addEntity(sapling);
                sapling.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent( this,
                Functions.createActivityAction(this, world, imageStore),
                this.getActionPeriod());
    }

    public Point nextPosition(
            WorldModel world, Point destPos)
    {

        int horiz = Integer.signum(destPos.getX() - this.getPosition().getX());
        Point newPos = new Point(this.getPosition().getX() + horiz, this.getPosition().getY());
        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.getY() - this.getPosition().getY());
            newPos = new Point(this.getPosition().getX(), this.getPosition().getY() + vert);
            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = this.getPosition();
            }
        }
        return newPos;

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

    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        return false;
    }

    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (Functions.adjacent(this.getPosition(), target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
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