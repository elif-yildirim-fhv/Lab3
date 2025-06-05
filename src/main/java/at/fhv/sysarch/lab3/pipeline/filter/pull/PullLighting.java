package at.fhv.sysarch.lab3.pipeline.filter.pull;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import com.hackoeur.jglm.Vec3;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.stream.Collectors;

public class PullLighting implements PullFilter<Pair<Face, Color>, Pair<Face, Color>> {
    private PullPipe<Pair<Face, Color>> input;
    private final Vec3 lightPos;

    public PullLighting(PullPipe<Pair<Face, Color>> input, PipelineData pipelineData) {
        setInput(input);
        this.lightPos = pipelineData.getLightPos();
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
                    Color baseColor = pair.snd();

                    Vec3 faceNormal = averageNormal(face);
                    Vec3 faceCenter = averagePosition(face);
                    Vec3 lightDir = lightPos.subtract(faceCenter).getUnitVector();

                    float intensity = Math.max(0f, faceNormal.getUnitVector().dot(lightDir));
                    Color shaded = baseColor.deriveColor(0, 1.0, intensity, 1.0);

                    return new Pair<>(face, shaded);
                })
                .collect(Collectors.toList());
    }

    private Vec3 averageNormal(Face face) {
        return face.getN1().toVec3()
                .add(face.getN2().toVec3())
                .add(face.getN3().toVec3())
                .scale(1f/3f);
    }

    private Vec3 averagePosition(Face face) {
        return face.getV1().toVec3()
                .add(face.getV2().toVec3())
                .add(face.getV3().toVec3())
                .scale(1f/3f);
    }

}
