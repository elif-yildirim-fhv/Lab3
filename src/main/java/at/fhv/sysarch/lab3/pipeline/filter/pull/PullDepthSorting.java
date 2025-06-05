package at.fhv.sysarch.lab3.pipeline.filter.pull;

import at.fhv.sysarch.lab3.obj.Face;

import java.util.List;
import java.util.stream.Collectors;

public class PullDepthSorting implements PullFilter<Face, Face> {
    private PullPipe<Face> input;

    public PullDepthSorting(PullFilter<Face, Face> input) {
      setInput(input);
    }

    @Override
    public void setInput(PullPipe<Face> input) {
        this.input = input;
    }

    @Override
    public List<Face> pull() {
        return input.pull().stream()
                .sorted((f1, f2) -> Double.compare(
                        avarageZ(f2), avarageZ(f1)))
                .collect(Collectors.toList());
    }

    private double avarageZ(Face face) {
        return (face.getV1().getZ() + face.getV2().getZ() + face.getV3().getZ()) / 3.0 ;
    }
}
