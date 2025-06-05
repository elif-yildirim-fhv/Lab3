package at.fhv.sysarch.lab3.pipeline;

import at.fhv.sysarch.lab3.animation.AnimationRenderer;
import at.fhv.sysarch.lab3.obj.Face;
import at.fhv.sysarch.lab3.obj.Model;
import at.fhv.sysarch.lab3.pipeline.data.Pair;
import at.fhv.sysarch.lab3.pipeline.filter.PullModelSource;
import at.fhv.sysarch.lab3.pipeline.filter.pull.*;
import com.hackoeur.jglm.Matrices;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;

import java.util.List;

public class PullPipelineFactory {
    public static AnimationTimer createPipeline(PipelineData pd) {
        // TODO: pull from the source (model)
        PullPipe<Face> source = new PullModelSource((List<Face>) pd.getModel());

        // TODO 1. perform model-view transformation from model to VIEW SPACE coordinates
        PullFilter<Face, Face> modelView = new PullModelViewTransformation(source, pd);

        // TODO 2. perform backface culling in VIEW SPACE
        PullFilter<Face, Face> culled = new PullBackfaceCulling(modelView);

        // TODO 3. perform depth sorting in VIEW SPACE
        PullFilter<Face, Face> sorted = new PullDepthSorting(culled);

        // TODO 4. add coloring (space unimportant)
        PullFilter<Face, Pair<Face, Color>> colored = new PullModelColor(sorted, pd);

        PullPipe<Pair<Face, Color>> lit;
        // lighting can be switched on/off
        if (pd.isPerformLighting()) {
            // 4a. TODO perform lighting in VIEW SPACE
            lit = new PullLighting(colored, pd);

            // 5. TODO perform projection transformation on VIEW SPACE coordinates
        } else {
            // 5. TODO perform projection transformation
            lit = colored;
        }

        PullFilter<Pair<Face, Color>, Pair<Face, Color>> projected = new PullProjectionTransformation(lit, pd);

        // TODO 6. perform perspective division to screen coordinates
        PullFilter<Pair<Face, Color>, Pair<Face, Color>> screen = new PullScreenSpaceTransformation(projected, pd);

        // TODO 7. feed into the sink (renderer)
        PullRenderer<Pair<Face, Color>> renderer = new PullRenderer<>(pd, screen);

        return new AnimationRenderer(pd) {
            // TODO rotation variable goes in here
            float totalRotation = 0;

            /** This method is called for every frame from the JavaFX Animation
             * system (using an AnimationTimer, see AnimationRenderer).
             * @param fraction the time which has passed since the last render call in a fraction of a second
             * @param model    the model to render
             */
            @Override
            protected void render(float fraction, Model model) {
                // TODO compute rotation in radians
                totalRotation += fraction;
                double rad = totalRotation % (2 * Math.PI);

                // TODO create new model rotation matrix using pd.getModelRotAxis and Matrices.rotate
                var rotationMatrix = Matrices.rotate((float) rad, pd.getModelRotAxis());

                // TODO compute updated model-view tranformation
                pullModelViewTransformationFilter.updateRotationMatrix(rotationMatrix);

                // TODO update model-view filter

                // TODO trigger rendering of the pipeline
                pullRenderer.doRender();
            }
        };
    }
}
