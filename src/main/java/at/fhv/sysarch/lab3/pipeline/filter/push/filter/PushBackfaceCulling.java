package at.fhv.sysarch.lab3.pipeline.filter.push.filter;

import at.fhv.sysarch.lab3.pipeline.filter.push.IPush;
import at.fhv.sysarch.lab3.pipeline.filter.push.Push;
import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Vec3;

public class PushBackfaceCulling extends Push<Face, Face> {
	public PushBackfaceCulling(IPush<Face> successor) {
		super(successor);
	}

	@Override
	public void push(Face face) {
		if (face == null) { // Dummy-Ende-Signal für Flush (optional)
			successor.push(null);
			return;
		}

		Vec3 v1 = face.getV1().toVec3();
		Vec3 v2 = face.getV2().toVec3();
		Vec3 v3 = face.getV3().toVec3();

		Vec3 normal = v2.subtract(v1).cross(v3.subtract(v1)).getUnitVector();
		Vec3 viewVector = new Vec3(-v1.getX(), -v1.getY(), -v1.getZ()).getUnitVector();

		if (normal.dot(viewVector) > 0) {
			successor.push(face);
		}
	}

}