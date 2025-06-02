package at.fhv.sysarch.lab3.pipeline.filter.push.filter;

import at.fhv.sysarch.lab3.pipeline.filter.push.IPush;
import at.fhv.sysarch.lab3.pipeline.filter.push.Push;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Mat4;
import com.hackoeur.jglm.Vec4;
import javafx.scene.paint.Color;

public class PushScreenSpaceTransformation extends Push<Pair<Face, Color>, Pair<Face, Color>> {
	private final Mat4 viewportMatrix;

	public PushScreenSpaceTransformation(IPush<Pair<Face, Color>> successor, Mat4 viewportMatrix) {
		super(successor);
		this.viewportMatrix = viewportMatrix;
	}

	@Override
	public void push(Pair<Face, Color> pair) {
		Face f = pair.fst();

		var v1 = f.getV1();
		var v2 = f.getV2();
		var v3 = f.getV3();

		var v1Div = new Vec4(v1.getX() / v1.getW(), v1.getY() / v1.getW(), v1.getZ() / v1.getW(), 1.0f);
		var v2Div = new Vec4(v2.getX() / v2.getW(), v2.getY() / v2.getW(), v2.getZ() / v2.getW(), 1.0f);
		var v3Div = new Vec4(v3.getX() / v3.getW(), v3.getY() / v3.getW(), v3.getZ() / v3.getW(), 1.0f);

		Face transformed = new Face(
				viewportMatrix.multiply(v1Div),
				viewportMatrix.multiply(v2Div),
				viewportMatrix.multiply(v3Div),
				f
		);

		successor.push(new Pair<>(transformed, pair.snd()));
	}

}