package at.fhv.sysarch.lab3.pipeline.filter;

import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Vec3;
import com.hackoeur.jglm.Vec4;
import javafx.scene.paint.Color;

public class LightingFilter implements PushFilter {
    private PushFilter successor;
    private final Vec3 lightPos;

    public LightingFilter(PushFilter successor, Vec3 lightPos) {
        this.successor = successor;
        this.lightPos = lightPos;
    }

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

        // Calculate light direction
        Vec3 lightDir = lightPos.subtract(new Vec3(v1.getX(), v1.getY(), v1.getZ())).getUnitVector();

        // Calculate diffuse lighting
        float intensity = Math.max(0.0f, normal.dot(lightDir));
        
        // Apply lighting to face color
        Color faceColor = f.getColor();
        Color litColor = faceColor.deriveColor(0, 1, intensity, 1);
        
        // Create new face with lit color
        Face litFace = new Face(v1, v2, v3, f);
        litFace.setColor(litColor);
        
        successor.push(litFace);
    }
}
