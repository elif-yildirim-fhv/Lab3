package at.fhv.sysarch.lab3.pipeline.filter.pull;

public interface PullFilter<I,O> extends PullPipe<O> {
    void setInput(PullPipe<I> input);
}
