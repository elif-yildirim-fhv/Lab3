package at.fhv.sysarch.lab3.pipeline.filter.pull;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.stream.Collectors;

public class PullScreenSpaceTransformation implements PullFilter<Pair<Face, Color>, Pair<Face, Color>> {
    private PullPipe<Pair<Face, Color>> input;
    private final Mat4 viewportTransform;

    public PullScreenSpaceTransformation(PullPipe<Pair<Face, Color>> input, PipelineData pd) {
        setInput(input);
        this.viewportTransform = pd.getViewportTransform();
    }

    @Override
    public void setInput(PullPipe<Pair<Face, Color>> input) {
        this.input = input;
    }

    @Override
    public List<Pair<Face, Color>> pull() {
        return input.pull().stream()
                .map(pair -> {
                    Face face = pair.fst();
                    Color color = pair.snd();

                    Vec4 v1 = divideAndTransform(face.getV1());
                    Vec4 v2 = divideAndTransform(face.getV2());
                    Vec4 v3 = divideAndTransform(face.getV3());

                    Face screenFace = new Face(v1, v2, v3, face);
                    return new Pair<>(screenFace, color);
                })
                .collect(Collectors.toList());
    }

    private Vec4 divideAndTransform(Vec4 vertex) {
        Vec4 divided = new Vec4(
                vertex.getX() / vertex.getW(),
                vertex.getY() / vertex.getW(),
                vertex.getZ() / vertex.getW(),
                1.0f
        );
        return viewportTransform.multiply(divided);
    }
}
