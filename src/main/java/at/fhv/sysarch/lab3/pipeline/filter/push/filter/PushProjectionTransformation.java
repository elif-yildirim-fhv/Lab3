package at.fhv.sysarch.lab3.pipeline.filter.push.filter;

import at.fhv.sysarch.lab3.pipeline.filter.push.IPush;
import at.fhv.sysarch.lab3.pipeline.filter.push.Push;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Mat4;
import javafx.scene.paint.Color;

public class PushProjectionTransformation extends Push<Pair<Face, Color>, Pair<Face, Color>> {
	private final Mat4 projectionMatrix;

	public PushProjectionTransformation(IPush<Pair<Face, Color>> successor, Mat4 projectionMatrix) {
		super(successor);
		this.projectionMatrix = projectionMatrix;
	}

	@Override
	public void push(Pair<Face, Color> pair) {
		Face f = pair.fst();
		Face projected = new Face(
				projectionMatrix.multiply(f.getV1()),
				projectionMatrix.multiply(f.getV2()),
				projectionMatrix.multiply(f.getV3()),
				f
		);
		successor.push(new Pair<>(projected, pair.snd()));
	}
}