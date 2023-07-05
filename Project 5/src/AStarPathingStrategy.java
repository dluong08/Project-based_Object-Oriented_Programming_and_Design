import org.w3c.dom.Node;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;


class AStarPathingStrategy
        implements PathingStrategy
{


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {

        List<Point> path = new LinkedList<Point>();
        PriorityQueue<Node> openSet = new PriorityQueue<Node>(1, Comparator.comparingInt(Node::getF));
        HashMap<Point, Node> closedSet = new HashMap<Point, Node>();
        HashMap<Point, Node> look = new HashMap<Point, Node>();
        Node startNode = new Node(heuristic(start, end), heuristic(start, end), 0, start, null);
        openSet.add(startNode);
        look.put(start, startNode);
        Node curr = startNode;

        while(!openSet.isEmpty() && !withinReach.test(curr.getPos(), end)) {
            curr = openSet.remove();
            look.remove(curr.getPos());
            closedSet.put(curr.getPos(), curr);
            List<Point> neighbors = potentialNeighbors.apply(curr.getPos()).filter(canPassThrough).collect(Collectors.toList());

            for (Point neighbor : neighbors) {
                if (!closedSet.containsKey(neighbor)) {
                    Node node = new Node((curr.getPrev() == null ? 1 : curr.getPrev().getG() + 1),
                            heuristic(neighbor, end),
                            (curr.getPrev() == null ? 1 : curr.getPrev().getG() + 1)
                            + heuristic(neighbor, end),
                            neighbor, curr);
                    if (openSet.contains(node)) {
                        if (node.getG() < look.get(node.getPos()).getG()) {
                            openSet.remove(node);
                            openSet.add(node);
                            look.remove(node.getPos());
                            look.put(node.getPos(), node);
                        }
                    }
                    else {
                        openSet.add(node);
                        look.put(node.getPos(), node);
                    }
                }
            }
            curr = openSet.peek();
        }

        if (curr != null) {
            while (curr.getPrev() != null) {
                path.add(0, curr.getPos());
                curr = curr.getPrev();
            }
        }

        return path;
    }

    public int heuristic(Point pos, Point end) // h
    {
        return Math.abs(pos.getX() - end.getX()) + Math.abs(pos.getY() - end.getY());
    }

    class Node {
        private int g;
        private int h;
        private int f;
        private Point pos;
        private Node prev; //previous node


        public Node (int g, int h, int f, Point pos, Node prev) {
            this.g = g;
            this.h = h;
            this.f = f;
            this.pos = pos;
            this.prev = prev;
        }

        public boolean containsPoint(Point p) { return (this.pos == p); }

        public int getG() { return g; }
        public void setG(int g) { this.g = g; }
        public int getH() { return h; }
        public void setH(int h) { this.h = h; }
        public int getF() { return f; }
        public void setF(int f) { this.f = f; }
        public Point getPos() { return pos; }
        public void setPos(Point pos) { this.pos = pos; }
        public Node getPrev() { return prev; }
        public void setG(Node prev) { this.prev = prev; }
    }
}
