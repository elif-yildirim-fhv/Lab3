package at.fhv.sysarch.lab3.pipeline.push;

/**
 * Interface für alle Push-Filter und Pipes.
 * Definiert die push()-Methode, um Daten an den nächsten Filter weiterzugeben.
 */
public interface IPush<T> {
    void push(T element);
}
