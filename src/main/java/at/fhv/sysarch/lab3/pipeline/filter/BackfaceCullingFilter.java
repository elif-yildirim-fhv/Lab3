package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Vec3;
import com.hackoeur.jglm.Vec4;

public class BackfaceCullingFilter implements PushFilter {
    private PushFilter successor;

    @Override
    public void setSuccessor(PushFilter successor) {
        this.successor = successor;
    }

    @Override
    public void push(Face f) {
        // Calculate face normal
        Vec4 v1 = f.getV1();
        Vec4 v2 = f.getV2();
        Vec4 v3 = f.getV3();

        Vec3 edge1 = new Vec3(v2.getX() - v1.getX(), v2.getY() - v1.getY(), v2.getZ() - v1.getZ());
        Vec3 edge2 = new Vec3(v3.getX() - v1.getX(), v3.getY() - v1.getY(), v3.getZ() - v1.getZ());
        Vec3 normal = edge1.cross(edge2).getUnitVector();

        // Calculate view vector (from face to camera)
        Vec3 viewVector = new Vec3(-v1.getX(), -v1.getY(), -v1.getZ()).getUnitVector();

        // If dot product is positive, face is visible
        if (normal.dot(viewVector) > 0) {
            successor.push(f);
        }
    }
} 