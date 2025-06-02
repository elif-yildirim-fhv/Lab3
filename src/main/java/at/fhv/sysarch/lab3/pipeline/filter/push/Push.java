package at.fhv.sysarch.lab3.pipeline.filter.push;

/**
 * Abstrakte Basisklasse für alle Push-Filter.
 * Enthält den Verweis auf den Nachfolger (successor).
 */
public abstract class Push<I, O> implements IPush<I> {
    protected final IPush<O> successor;

    public Push(IPush<O> successor) {
        this.successor = successor;
    }
}