import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

    public class Obstacle extends Animated {

        public Obstacle(
                String id,
                Point position,
                List<PImage> images,
                int animationPeriod) {
            super(id, position, images, 0, animationPeriod);
        }

    }