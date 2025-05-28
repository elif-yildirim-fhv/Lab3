package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;

public class ModelViewFilter implements PushFilter {
    private PushFilter successor;
    private Mat4 viewTransform;
    private Mat4 rotationMatrix;
    private Mat4 modelTransform;

    public ModelViewFilter(Mat4 viewTransform) {
        this.viewTransform = viewTransform;
        this.rotationMatrix = new Mat4(1); // Identity matrix
        this.modelTransform = new Mat4(1); // Identity matrix
    }

    public void updateRotationMatrix(Mat4 rotationMatrix) {
        this.rotationMatrix = rotationMatrix;
        // Combine rotation and model transformation
        this.modelTransform = rotationMatrix;
    }

    @Override
    public void setSuccessor(PushFilter successor) {
        this.successor = successor;
    }

    @Override
    public void push(Face f) {
        // First apply model transformation (rotation + translation)
        Vec4 v1 = modelTransform.multiply(f.getV1());
        Vec4 v2 = modelTransform.multiply(f.getV2());
        Vec4 v3 = modelTransform.multiply(f.getV3());

        // Then transform to view space
        v1 = viewTransform.multiply(v1);
        v2 = viewTransform.multiply(v2);
        v3 = viewTransform.multiply(v3);

        // Create new face with transformed vertices
        Face transformedFace = new Face(v1, v2, v3, f);
        successor.push(transformedFace);
    }
} 