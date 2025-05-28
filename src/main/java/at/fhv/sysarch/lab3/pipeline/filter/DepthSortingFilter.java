package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.obj.Face;
import java.util.ArrayList;
import java.util.List;

public class DepthSortingFilter implements PushFilter {
    private PushFilter successor;
    private List<Face> faces = new ArrayList<>();

    @Override
    public void setSuccessor(PushFilter successor) {
        this.successor = successor;
    }

    @Override
    public void push(Face f) {
        faces.add(f);
    }

    public void flush() {
        // Sort faces by average z-coordinate (back to front)
        faces.sort((f1, f2) -> {
            float z1 = (f1.getV1().getZ() + f1.getV2().getZ() + f1.getV3().getZ()) / 3;
            float z2 = (f2.getV1().getZ() + f2.getV2().getZ() + f2.getV3().getZ()) / 3;
            return Float.compare(z1, z2);
        });

        // Push sorted faces to successor
        for (Face f : faces) {
            successor.push(f);
        }
        faces.clear();
    }
} 