package at.fhv.sysarch.lab3.pipeline.filter.pull;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.PipelineData;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.stream.Collectors;

public class PullModelColor implements PullFilter<Face, Pair<Face, Color>> {
    private PullPipe<Face> input;
    private final Color color;

    public PullModelColor(PullPipe<Face> input, PipelineData pipelineData) {
        setInput(input);
        this.color = pipelineData.getModelColor();
    }

    @Override
    public void setInput(PullPipe<Face> input) {
        this.input = input;
    }

    @Override
    public List<Pair<Face, Color>> pull() {
        return input.pull().stream()
                .map(face -> new Pair<>(face, color))
                .collect(Collectors.toList());
    }
}
