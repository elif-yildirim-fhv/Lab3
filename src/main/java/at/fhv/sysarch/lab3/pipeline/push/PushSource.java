package at.fhv.sysarch.lab3.pipeline.push;

import at.fhv.sysarch.lab3.obj.Face;
import java.util.Collection;

/**
 * Die Quelle der Push-Pipeline. Startet den Push-Prozess mit den Faces des Modells.
 */
public class PushSource extends Push<Face, Face> {

    public PushSource(IPush<Face> successor) {
        super(successor);
    }

    /**
     * Startet die Pipeline mit den gegebenen Faces.
     * @param faces Die Liste der Faces des Modells
     */
    public void setSourceData(Collection<Face> faces) {
        for (Face face : faces) {
            successor.push(face);
        }
    }

    @Override
    public void push(Face element) {
        throw new UnsupportedOperationException("PushSource kann keine Elemente von außen empfangen. Nutze setSourceData()!");
    }
}
