package src.utils;

import java.awt.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * @author Siqi Zhou
 * student id 903274
 */
public class Shape implements Serializable {


    @Serial
    private static final long serialVersionUID = -528181346306729778L;
    private Point start;
    private Point end;
    private PaintOptionType type;
    private Color color;

    // only used for text inputs
    private String text;


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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
