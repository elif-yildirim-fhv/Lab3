package at.fhv.sysarch.lab3.pipeline.push;

/**
 * Abstrakte Basisklasse für alle Push-Filter.
 * Enthält den Verweis auf den Nachfolger (successor).
 */
public abstract class Push<In, Out> implements IPush<In> {
    protected final IPush<Out> successor;

    public Push(IPush<Out> successor) {
        this.successor = successor;
    }
}
