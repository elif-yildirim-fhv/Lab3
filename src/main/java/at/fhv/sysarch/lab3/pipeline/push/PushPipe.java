package at.fhv.sysarch.lab3.pipeline.push;

/**
 * Einfache Pipe, die Daten direkt an den nächsten Filter weiterleitet.
 */
public class PushPipe<E> extends Push<E, E> {

    public PushPipe(IPush<E> successor) {
        super(successor);
    }

    @Override
    public void push(E data) {
        successor.push(data);
    }
}
