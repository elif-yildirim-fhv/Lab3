package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.pipeline.filter.push.Push;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.obj.Face;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import at.fhv.sysarch.lab3.rendering.RenderingMode;

public class PushRenderer extends Push<Pair<Face, Color>, Pair<Face, Color>> {
    private final GraphicsContext gc;
    private final RenderingMode mode;

    public PushRenderer(GraphicsContext gc, RenderingMode mode) {
        super(null);
        this.gc = gc;
        this.mode = mode;
    }

    @Override
    public void push(Pair<Face, Color> pair) {
        Face f = pair.fst();
        Color c = pair.snd();

        gc.setStroke(c);
        gc.setFill(c);

        switch (mode) {
            case POINT -> {
                gc.fillOval(f.getV1().getX(), f.getV1().getY(), 2, 2);
                gc.fillOval(f.getV2().getX(), f.getV2().getY(), 2, 2);
                gc.fillOval(f.getV3().getX(), f.getV3().getY(), 2, 2);
            }
            case WIREFRAME -> {
                gc.strokeLine(f.getV1().getX(), f.getV1().getY(), f.getV2().getX(), f.getV2().getY());
                gc.strokeLine(f.getV2().getX(), f.getV2().getY(), f.getV3().getX(), f.getV3().getY());
                gc.strokeLine(f.getV3().getX(), f.getV3().getY(), f.getV1().getX(), f.getV1().getY());
            }
            case FILLED -> {
                double[] x = {f.getV1().getX(), f.getV2().getX(), f.getV3().getX()};
                double[] y = {f.getV1().getY(), f.getV2().getY(), f.getV3().getY()};
                gc.fillPolygon(x, y, 3);
            }
        }
    }
}
