package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;

public class ProjectionFilter implements PushFilter {
    private PushFilter successor;
    private final Mat4 projectionMatrix;

    public ProjectionFilter(PushFilter successor, Mat4 projectionMatrix) {
        this.successor = successor;
        this.projectionMatrix = projectionMatrix;
    }

    @Override
    public void setSuccessor(PushFilter successor) {
        this.successor = successor;
    }

    @Override
    public void push(Face f) {
        // Apply projection transformation
        Vec4 v1 = projectionMatrix.multiply(f.getV1());
        Vec4 v2 = projectionMatrix.multiply(f.getV2());
        Vec4 v3 = projectionMatrix.multiply(f.getV3());

        // Create new face with projected vertices
        Face projectedFace = new Face(v1, v2, v3, f);
        successor.push(projectedFace);
    }
}
