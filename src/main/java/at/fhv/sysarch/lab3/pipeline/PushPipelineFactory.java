package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.animation.AnimationRenderer;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.filter.push.PushPipe;
import at.fhv.sysarch.lab3.pipeline.filter.push.PushSource;
import at.fhv.sysarch.lab3.pipeline.filter.push.filter.*;
import com.hackoeur.jglm.Matrices;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;

public class PushPipelineFactory {

    public static AnimationTimer createPipeline(PipelineData pd) {
        // TODO: push from the source (model)
        PushRenderer pushRenderer = new PushRenderer(pd.getGraphicsContext(), pd.getRenderingMode());

        // TODO: improve connecting filters, add pipes...
        PushPipe<Pair<Face, Color>> toRenderer = new PushPipe<>(pushRenderer);
        PushScreenSpaceTransformation screenSpaceFilter = new PushScreenSpaceTransformation(toRenderer, pd.getViewportTransform());
        PushPipe<Pair<Face, Color>> toScreenSpace = new PushPipe<>(screenSpaceFilter);
        PushProjectionTransformation projectionFilter = new PushProjectionTransformation(toScreenSpace, pd.getProjTransform());

        PushPipe<Pair<Face, Color>> toProjectionOrLighting;

        if (pd.isPerformLighting()) {
            PushPipe<Pair<Face, Color>> toProjection = new PushPipe<>(projectionFilter);
            PushLighting lightingFilter = new PushLighting(toProjection, pd.getLightPos().getUnitVector());
            toProjectionOrLighting = new PushPipe<>(lightingFilter);
        } else {
            toProjectionOrLighting = new PushPipe<>(projectionFilter);
        }

        PushModelColor modelColorFilter = new PushModelColor(toProjectionOrLighting, pd.getModelColor());

        PushPipe<Face> toModelColor = new PushPipe<>(modelColorFilter);
        PushDepthSorting depthSortingFilter = new PushDepthSorting(toModelColor);

        PushPipe<Face> toDepthSorting = new PushPipe<>(depthSortingFilter);
        PushBackfaceCulling backfaceCullingFilter = new PushBackfaceCulling(toDepthSorting);

        PushPipe<Face> toBackfaceCulling = new PushPipe<>(backfaceCullingFilter);
        PushModelViewTransformation pushModelViewTransformation = new PushModelViewTransformation(toBackfaceCulling, pd.getViewTransform(), pd.getModelTranslation());

        // push from the source (model)
        PushPipe<Face> toModelView = new PushPipe<>(pushModelViewTransformation);
        PushSource sourceModel = new PushSource(toModelView);

        // returning an animation renderer which handles clearing of the
        // viewport and computation of the praction
        return new AnimationRenderer(pd) {
            // TODO rotation variable goes in here
            float totalRotation = 0;

            // Test funktion
            // private int counter = 0;
            // private double startPos = Math.random()*1000;

            /** This method is called for every frame from the JavaFX Animation
             * system (using an AnimationTimer, see AnimationRenderer).
             * @param fraction the time which has passed since the last render call in a fraction of a second
             * @param model    the model to render
             */
            @Override
            protected void render(float fraction, Model model) {

               // Test funktion
               // pd.getGraphicsContext().setStroke(Color.WHITE);
               // pd.getGraphicsContext().strokeLine(startPos+counter,startPos+ counter,startPos+counter+100,startPos+counter+100);
               // counter++;

                // TODO: use generic parameters for passing objects

                // TODO compute rotation in radians
                totalRotation += fraction;
                double rad = totalRotation % (2 * Math.PI);

                // TODO create new model rotation matrix using pd.modelRotAxis
                var rotationMatrix = Matrices.rotate((float) rad, pd.getModelRotAxis());

                // TODO compute updated model-view tranformation
                pushModelViewTransformation.updateRotationMatrix(rotationMatrix);

                // TODO update model-view filter

                // TODO trigger rendering of the pipeline
                sourceModel.setSourceData(model.getFaces());
            }
        };
    }
}
