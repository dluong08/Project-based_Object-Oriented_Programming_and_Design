public class Animation implements Action {

    private Entity entity;
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public Animation(
            Entity entity,
            WorldModel world,
            ImageStore imageStore,
            int repeatCount)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler) {
        ((AnimationEntity)this.entity).nextImage();

        if (this.repeatCount != 1) {
            scheduler.scheduleEvent(this.entity,
                    Functions.createAnimationAction(this.entity,
                            Math.max(this.repeatCount - 1,
                                    0)),
                    ((AnimationEntity)this.entity).getAnimationPeriod());
        }
    }

}