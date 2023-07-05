import java.util.*;

import processing.core.PImage;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public abstract class Entity
{
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;

    public Entity(String id, Point position, List<PImage> images, int imageIndex) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = imageIndex;
    }

    protected List<PImage> getImages() {
        return images;
    }

    protected int getImageIndex() {
        return imageIndex;
    }

    protected String getId() {
        return id;
    }

    protected Point getPosition() {
        return position;
    }

    protected void setPosition(Point position) {
        this.position = position;
    }

    protected void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }
}