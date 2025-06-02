package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.animation.AnimationRenderer;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.filter.push.*;
import at.fhv.sysarch.lab3.pipeline.filter.push.filter.*;
import com.hackoeur.jglm.Matrices;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;

public class PushPipelineFactory {

    public static AnimationTimer createPipeline(PipelineData pd) {
        // 7. Renderer (Sink) - übernimmt das Zeichnen auf dem Canvas
        PushRenderer renderer = new PushRenderer(pd.getGraphicsContext(), pd.getRenderingMode());
        PushPipe<Pair<Face, Color>> toRenderer = new PushPipe<>(renderer);

        // 6. Screen Space Transformation (2D-Projektion + Viewport Mapping)
        PushScreenSpaceTransformation screenSpaceFilter = new PushScreenSpaceTransformation(toRenderer, pd.getViewportTransform());
        PushPipe<Pair<Face, Color>> toScreenSpace = new PushPipe<>(screenSpaceFilter);

        // 5. Projection Transformation (3D ➔ Clipping Space)
        PushProjectionTransformation projectionFilter = new PushProjectionTransformation(toScreenSpace, pd.getProjTransform());
        PushPipe<Pair<Face, Color>> toProjection;

        // 4. Lighting (optional, Flat Shading)
        if (pd.isPerformLighting()) {
            PushLighting lightingFilter = new PushLighting(projectionFilter, pd.getLightPos().getUnitVector());
            toProjection = new PushPipe<>(lightingFilter);
        } else {
            toProjection = new PushPipe<>(projectionFilter);
        }

        // 3. Model Color (Farbe des Teapots)
        PushModelColor modelColorFilter = new PushModelColor(toProjection, pd.getModelColor());
        PushPipe<Face> toModelColor = new PushPipe<>(modelColorFilter);

        // 2. Depth Sorting (Painters Algorithm, sortiert Faces nach Z-Tiefe)
        PushDepthSorting depthSortingFilter = new PushDepthSorting(toModelColor);
        PushPipe<Face> toDepthSorting = new PushPipe<>(depthSortingFilter);

        // 1. Backface Culling (versteckt unsichtbare Faces)
        PushBackfaceCulling backfaceCullingFilter = new PushBackfaceCulling(toDepthSorting);
        PushPipe<Face> toBackfaceCulling = new PushPipe<>(backfaceCullingFilter);

        // 0. Model-View Transformation (Rotation um Y-Achse + Translation)
        PushModelViewTransformation modelViewFilter = new PushModelViewTransformation(toBackfaceCulling, pd.getViewTransform(), pd.getModelTranslation());
        PushPipe<Face> toModelView = new PushPipe<>(modelViewFilter);

        // Source: Modell-Daten als Startpunkt der Pipeline
        PushSource source = new PushSource(toModelView);

        // Animation Renderer für kontinuierliche Rotation & Animation
        return new AnimationRenderer(pd) {
            float totalRotation = 0; // Rotationswinkel in Radiant

            @Override
            protected void render(float fraction, Model model) {
                // Framerate-unabhängige Rotation
                totalRotation += fraction;
                double radians = totalRotation % (2 * Math.PI);

                // Neue Rotationsmatrix berechnen
                var rotationMatrix = Matrices.rotate((float) radians, pd.getModelRotAxis());

                // ModelView-Filter mit neuer Rotation updaten
                modelViewFilter.updateRotationMatrix(rotationMatrix);

                // Faces an Source pushen (Batchweise Übergabe aller Faces pro Frame)
                source.setSourceData(model.getFaces());
            }
        };
    }
}
