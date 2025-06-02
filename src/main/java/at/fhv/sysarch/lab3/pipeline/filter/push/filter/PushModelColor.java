package at.fhv.sysarch.lab3.pipeline.filter.push.filter;

import at.fhv.sysarch.lab3.pipeline.filter.push.IPush;
import at.fhv.sysarch.lab3.pipeline.filter.push.Push;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.obj.Face;
import javafx.scene.paint.Color;

public class PushModelColor extends Push<Face, Pair<Face, Color>> {
	private final Color modelColor;

	public PushModelColor(IPush<Pair<Face, Color>> successor, Color color) {
		super(successor);
		this.modelColor = color;
	}

	@Override
	public void push(Face face) {
		successor.push(new Pair<>(face, modelColor));
	}
}