package src.constants;

import java.awt.*;

public class Shape {
    private Point start;
    private Point end;
    private PaintOptionType type;


    public Point getStart() {
        return start;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public Point getEnd() {
        return end;
    }

    public void setEnd(Point end) {
        this.end = end;
    }

    public PaintOptionType getType() {
        return type;
    }

    public void setType(PaintOptionType type) {
        this.type = type;
    }
}
