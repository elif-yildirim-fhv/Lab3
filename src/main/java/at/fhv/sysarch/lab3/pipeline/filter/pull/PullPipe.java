package at.fhv.sysarch.lab3.pipeline.filter.pull;

import java.util.List;

public interface PullPipe<T>{
    List<T> pull();
}
