package at.fhv.sysarch.lab3.pipeline.filter.pull;

import at.fhv.sysarch.lab3.obj.Face;

import java.util.List;
import java.util.stream.Collectors;

public class PullBackfaceCulling implements PullFilter<Face, Face> {
    private PullPipe<Face> input;

    public PullBackfaceCulling(PullFilter<Face, Face> input) {
        setInput(input);
    }

    @Override
    public void setInput(PullPipe<Face> input) {
        this.input = input;
    }

    @Override
    public List<Face> pull() {
        return input.pull().stream()
                .filter(face -> face.getN1().toVec3().dot(face.getV1().toVec3()) < 0)
                .collect(Collectors.toList());
    }
}
