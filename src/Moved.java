import processing.core.PImage;
import java.util.List;
import java.util.Optional;
import java.util.List;

public class Moved extends Actioned {

    public Moved(String id, Point position, List<PImage> images, int resourseLimit, int resourceCount,
                    int actionPeriod, int animationPeriod, int health, int healthLimit)
    {
        super(id,position,images, resourseLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }

    public Point nextPosition(
            WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - this.getPosition().x);
        Point newPos = new Point(this.getPosition().x + horiz, this.getPosition().y);

        if (horiz == 0 || WorldModel.isOccupied(world, newPos) && WorldModel.getOccupancyCell(world, newPos).getClass() != Stump.class) {
            int vert = Integer.signum(destPos.y - this.getPosition().y);
            newPos = new Point(this.getPosition().x, this.getPosition().y + vert);

            if (vert == 0 || WorldModel.isOccupied(world, newPos) &&  WorldModel.getOccupancyCell(world, newPos).getClass() != Stump.class) {
                newPos = this.getPosition();
            }
        }

        return newPos;
    }
}
