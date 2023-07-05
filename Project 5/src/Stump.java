import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Stump extends Entity {

    private String id;
    private Point position;
    private List<PImage> images;

    public Stump(
            String id,
            Point position,
            List<PImage> images)
    {
        super(id, position, images, 0);
    }

}
