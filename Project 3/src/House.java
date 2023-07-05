import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class House extends Entity {

    public House(
            String id,
            Point position,
            List<PImage> images)
    {
        super(id, position, images, 0);
    }

}