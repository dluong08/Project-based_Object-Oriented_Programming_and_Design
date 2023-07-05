import java.util.*;
import processing.core.PImage;

public final class Stump implements AnimationEntity {
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

    public static final String STUMP_KEY = "stump";

    public static final int TREE_ANIMATION_MAX = 600;
    public static final int TREE_ANIMATION_MIN = 50;
    public static final int TREE_ACTION_MAX = 1400;
    public static final int TREE_ACTION_MIN = 1000;
    public static final int TREE_HEALTH_MAX = 3;
    public static final int TREE_HEALTH_MIN = 1;

    public Stump(
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int actionPeriod,
            int animationPeriod,
            int health,
            int healthLimit) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        this.health = health;
        this.healthLimit = healthLimit;
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