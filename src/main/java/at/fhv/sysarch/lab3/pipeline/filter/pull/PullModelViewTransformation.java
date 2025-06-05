package at.fhv.sysarch.lab3.pipeline.filter.pull;


import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import com.hackoeur.jglm.Mat4;

import java.util.List;
import java.util.stream.Collectors;

public class PullModelViewTransformation implements PullFilter<Face, Face> {
    private PullPipe<Face> input;
    private final Mat4 modelView;

    public PullModelViewTransformation(PullPipe<Face> input, PipelineData pipelineData) {
        setInput(input);
        this.modelView = pipelineData.getViewTransform().multiply(pipelineData.getModelTranslation());
    }

    @Override
    public void setInput(PullPipe<Face> input) {
        this.input = input;
    }

    @Override
    public List<Face> pull() {
        return input.pull().stream()
                .map(face -> new Face(
                        modelView.multiply(face.getV1()),
                        modelView.multiply(face.getV2()),
                        modelView.multiply(face.getV3()),
                        modelView.multiply(face.getN1()),
                        modelView.multiply(face.getN2()),
                        modelView.multiply(face.getN3())
                ))
                .collect(Collectors.toList());
    }

}
