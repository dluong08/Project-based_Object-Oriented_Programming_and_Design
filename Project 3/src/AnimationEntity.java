import processing.core.PImage;

import java.util.List;

public abstract class AnimationEntity extends Entity {

    private int animationPeriod;

    public AnimationEntity(String id, Point position, List<PImage> images, int imageIndex, int animationPeriod) {
        super(id, position, images, imageIndex);
        this.animationPeriod = animationPeriod;
    }

    public int getAnimationPeriod() {
        return this.animationPeriod;
    }

    public void nextImage() {
        this.setImageIndex((this.getImageIndex() + 1) % this.getImages().size());
    } //from dudeFull

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                Functions.createAnimationAction(this, 0),
                this.getAnimationPeriod());
    }
}