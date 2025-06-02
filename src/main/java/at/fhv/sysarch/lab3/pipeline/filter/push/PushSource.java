package at.fhv.sysarch.lab3.pipeline.filter.push;

import at.fhv.sysarch.lab3.obj.Face;

import java.util.List;
import java.util.Queue;
import java.util.LinkedList;

public class PushSource implements IPush<Face> {
    private final Queue<Face> dataQueue = new LinkedList<>();
    private IPush<Face> successor;

    public PushSource(IPush<Face> successor) {
        this.successor = successor;
    }

    public void setSourceData(List<Face> faces) {
        dataQueue.clear();
        dataQueue.addAll(faces);
    }

    public void run() {
        while (!dataQueue.isEmpty()) {
            successor.push(dataQueue.poll());
        }
    }

    @Override
    public void push(Face item) {
        throw new UnsupportedOperationException("PushSource: push() nicht erlaubt, nur run() nutzen!");
    }
}