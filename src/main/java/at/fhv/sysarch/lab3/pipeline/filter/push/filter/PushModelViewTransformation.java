package at.fhv.sysarch.lab3.pipeline.filter.push.filter;

import at.fhv.sysarch.lab3.pipeline.filter.push.IPush;
import at.fhv.sysarch.lab3.pipeline.filter.push.Push;
import at.fhv.sysarch.lab3.obj.Face;
import com.hackoeur.jglm.Mat4;

public class PushModelViewTransformation extends Push<Face, Face> {
	private Mat4 modelViewMatrix;

	public PushModelViewTransformation(IPush<Face> successor, Mat4 viewMatrix, Mat4 modelMatrix) {
		super(successor);
		this.modelViewMatrix = viewMatrix.multiply(modelMatrix);
	}

	public void updateRotationMatrix(Mat4 rotationMatrix) {
		this.modelViewMatrix = rotationMatrix.multiply(this.modelViewMatrix);
	}

	@Override
	public void push(Face face) {
		Face transformed = new Face(
				modelViewMatrix.multiply(face.getV1()),
				modelViewMatrix.multiply(face.getV2()),
				modelViewMatrix.multiply(face.getV3()),
				face
		);
		successor.push(transformed);
	}
}