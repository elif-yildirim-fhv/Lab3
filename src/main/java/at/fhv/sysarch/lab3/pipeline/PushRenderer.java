package at.fhv.sysarch.lab3.pipeline;


import at.fhv.sysarch.lab3.obj.Face;

import at.fhv.sysarch.lab3.pipeline.data.Pair;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PushRenderer implements PushPipe<Pair<Face, Color>> {
    private final GraphicsContext gc;
    private final PipelineData.RenderingMode renderingMode;

    public PushRenderer(GraphicsContext gc, PipelineData.RenderingMode renderingMode) {
        this.gc = gc;
        this.renderingMode = renderingMode;
    }

    @Override
    public void push(Pair<Face, Color> data) {
        Face face = data.fst();
        Color color = data.snd();

        gc.setStroke(color);
        gc.setFill(color);

        var x = new double[]{face.getV1().getX(), face.getV2().getX(), face.getV3().getX()};
        var y = new double[]{face.getV1().getY(), face.getV2().getY(), face.getV3().getY()};

        switch (renderingMode) {
            case POINT -> {
                for (int i = 0; i < 3; i++) {
                    gc.strokeLine(x[i], y[i], x[i]+1, y[i]+1);
                }
            }
            case WIREFRAME -> gc.strokePolygon(x, y, 3);
            case FILLED, SHADED -> gc.fillPolygon(x, y, 3);
        }
    }
}
