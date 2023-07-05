import java.util.List;
import java.util.Optional;

public interface Position extends Entity{

    Point nextPosition(WorldModel world, Point desPos);
    boolean moveToFull(WorldModel world, Entity target, EventScheduler scheduler);
}
