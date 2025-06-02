package at.fhv.sysarch.lab3.pipeline.filter.push.filter;

import at.fhv.sysarch.lab3.pipeline.filter.push.IPush;
import at.fhv.sysarch.lab3.pipeline.filter.push.Push;
import at.fhv.sysarch.lab3.obj.Face;
import java.util.*;

public class PushDepthSorting extends Push<Face, Face> {
	private final List<Face> faces = new ArrayList<>();

	public PushDepthSorting(IPush<Face> successor) {
		super(successor);
	}

	@Override
	public void push(Face face) {
		if (face == null) {
			flush();
			successor.push(null);
			return;
		}
		faces.add(face);
	}

	private void flush() {
		faces.sort(Comparator.comparingDouble(f -> (f.getV1().getZ() + f.getV2().getZ() + f.getV3().getZ()) / 3));
		for (Face f : faces) {
			successor.push(f);
		}
		faces.clear();
	}
}