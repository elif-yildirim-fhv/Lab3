package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.pipeline.filter.pull.PullPipe;

import java.util.List;

public class PullModelSource implements PullPipe<Face> {
    private final List<Face> faces;

    public PullModelSource(final List<Face> faces) {
        this.faces = faces;
    }

    @Override
    public List<Face> pull() {
        return faces;
    }
}
