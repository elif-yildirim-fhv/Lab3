package at.fhv.sysarch.lab3.pipeline.filter.pull;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import com.hackoeur.jglm.Mat4;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.stream.Collectors;

public class PullProjectionTransformation implements PullFilter<Pair<Face, Color>, Pair<Face, Color>> {
    private PullPipe<Pair<Face, Color>> input;
    private final Mat4 projMatrix;

    public PullProjectionTransformation(PullPipe<Pair<Face, Color>> input, PipelineData pd) {
        setInput(input);
        this.projMatrix = pd.getProjTransform();
    }

    @Override
    public List<Pair<Face, Color>> pull() {
        return input.pull().stream()
                .map(pair -> {
                    Face face = pair.fst();
                    Color color = pair.snd();
                    Face projected = new Face(
                            projMatrix.multiply(face.getV1()),
                            projMatrix.multiply(face.getV2()),
                            projMatrix.multiply(face.getV3()),
                            face.getN1(), face.getN2(), face.getN3()
                    );
                    return new Pair<>(projected, color);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void setInput(PullPipe<Pair<Face, Color>> input) {
        this.input = input;
    }
}