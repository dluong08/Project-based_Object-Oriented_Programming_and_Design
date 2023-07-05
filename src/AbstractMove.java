import java.util.Optional;
import java.util.LinkedList;
import processing.core.PImage;
import java.util.List;

public abstract class AbstractMove extends AbstractAnimation implements Position {
    public AbstractMove(String id, Point position,
                        List<PImage> images, int resourceLimit,
                        int resourceCount, int actionPeriod,
                        int animationPeriod, int repeatCount) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, repeatCount);
    }

    public Point nextPosition( WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - this.getPosition().x);
        Point newPos = new Point(this.getPosition().x + horiz,
                this.getPosition().y);

        if (horiz == 0 || world.isOccupied( newPos))
        {
            int vert = Integer.signum(destPos.y - this.getPosition().y);
            newPos = new Point(this.getPosition().x,
                    this.getPosition().y + vert);

            if (vert == 0 || world.isOccupied( newPos))
            {
                newPos = this.getPosition();
            }
        }

        return newPos;
    }

    public boolean moveToFull(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (Point.adjacent(this.getPosition(), target.getPosition())) {
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