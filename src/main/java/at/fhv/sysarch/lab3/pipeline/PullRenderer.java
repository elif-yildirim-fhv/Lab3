package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.data.Pair;

import java.awt.*;

public class PullRenderer<T extends Pair<Face, Color>> extends Pull<T, Pair<Face, Color>> {
    private final PiplineData pd;

    public PullRenderer(PiplineData pd, IPull<T> source ) {
        super(source);
        this.pd = pd;
    }
    @Override
    public Pair<Face, Color> pull() {
        throw new IllegalArgumentException(("PullRender is a sink class of the pull structure, therefore it cannot be pulled"));

    }

    @Override
    public boolean hasNext() {
        throw new IllegalCallerException("PullRenderer is a sink class of the pull structure, therefore it cannot operate has next");


    }
     public void doRender() {
        var gc = pd.getGraphicsContext();

        while (source.hasNext()) {
            Pair<Face, Color> pair = source.pull();
            Face face = pair.fst();
            Color color = pair.snd();

            gc.setStroke(color);
            gc.setFill(color);

            var x = new double[]{face.getV1().getX(), face.getV2().getX(), face.getV3().getX()};
            var y = new double[]{face.getV1().getY(), face.getV2().getY(), face.getV3().getY()};

            switch (pd.getRenderingMode()) {
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
}
