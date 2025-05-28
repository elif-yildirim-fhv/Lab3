package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;

public class ScreenSpaceFilter implements PushFilter {
    private PushFilter successor;
    private final Mat4 viewportMatrix;

    public ScreenSpaceFilter(PushFilter successor, Mat4 viewportMatrix) {
        this.successor = successor;
        this.viewportMatrix = viewportMatrix;
    }

    @Override
    public void setSuccessor(PushFilter successor) {
        this.successor = successor;
    }

    @Override
    public void push(Face f) {
        // Apply viewport transformation
        Vec4 v1 = viewportMatrix.multiply(f.getV1());
        Vec4 v2 = viewportMatrix.multiply(f.getV2());
        Vec4 v3 = viewportMatrix.multiply(f.getV3());

        // Perform perspective division
        v1 = new Vec4(v1.getX() / v1.getW(), v1.getY() / v1.getW(), v1.getZ() / v1.getW(), 1.0f);
        v2 = new Vec4(v2.getX() / v2.getW(), v2.getY() / v2.getW(), v2.getZ() / v2.getW(), 1.0f);
        v3 = new Vec4(v3.getX() / v3.getW(), v3.getY() / v3.getW(), v3.getZ() / v3.getW(), 1.0f);

        // Create new face with screen space coordinates
        Face screenFace = new Face(v1, v2, v3, f);
        successor.push(screenFace);
    }
}
