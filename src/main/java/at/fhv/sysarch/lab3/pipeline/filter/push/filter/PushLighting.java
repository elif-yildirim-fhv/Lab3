package at.fhv.sysarch.lab3.pipeline.filter.push.filter;

import at.fhv.sysarch.lab3.pipeline.filter.push.IPush;
import at.fhv.sysarch.lab3.pipeline.filter.push.Push;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Vec3;
import javafx.scene.paint.Color;

public class PushLighting extends Push<Pair<Face, Color>, Pair<Face, Color>> {
	private final Vec3 lightPos;

	public PushLighting(IPush<Pair<Face, Color>> successor, Vec3 lightPos) {
		super(successor);
		this.lightPos = lightPos;
	}

	@Override
	public void push(Pair<Face, Color> pair) {
		Face f = pair.fst();
		Vec3 v1 = f.getV1().toVec3();
		Vec3 v2 = f.getV2().toVec3();
		Vec3 v3 = f.getV3().toVec3();

		Vec3 normal = v2.subtract(v1).cross(v3.subtract(v1)).getUnitVector();
		Vec3 lightDir = lightPos.subtract(v1).getUnitVector();

		float intensity = Math.max(0, normal.dot(lightDir));
		Color litColor = pair.snd().deriveColor(0, 1, intensity, 1);

		successor.push(new Pair<>(f, litColor));
	}
}