package at.fhv.sysarch.lab3.pipeline.filter.push;

/**
 * Einfache Pipe, die Daten direkt an den nächsten Filter weiterleitet.
 */
public class PushPipe<T> extends Push<T, T> {
    public PushPipe(IPush<T> successor) {
        super(successor);
    }

    @Override
    public void push(T item) {
        successor.push(item);
    }
}