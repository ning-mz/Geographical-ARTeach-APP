package comp208;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.TextView;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;

public class Model extends Node implements Node.OnTapListener {
    private static final float INFO_CARD_Y_POS_COEFF = 2f;

    private final String modelName;
    private final Context context;
    private Node infoCard;
    private final ModelRenderable modelRenderable;
    private TransformableNode modelNode;
    private final ArFragment arFragment;
    private final AnchorNode anchorNode;

    public Model(String modelName,
                 Context context,
                 ModelRenderable modelRenderable,
                 ArFragment arFragment,
                 AnchorNode anchorNode){
        this.modelName = modelName;
        this.context = context;
        this.modelRenderable = modelRenderable;
        this.arFragment = arFragment;
        this.anchorNode = anchorNode;
        setOnTapListener(this);
    }

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    public void onActivate() {

        if (getScene() == null) {
            throw new IllegalStateException("Scene is null!");
        }

        if (infoCard == null) {
            infoCard = new Node();
            infoCard.setParent(anchorNode);
            infoCard.setEnabled(true);
            infoCard.setLocalPosition(new Vector3(0.0f, 1.0f, 0.0f));

            ViewRenderable.builder()
                    .setView(context, R.layout.model_card_view)
                    .build()
                    .thenAccept(
                            (renderable) -> {
                                infoCard.setRenderable(renderable);
                                TextView textView = (TextView) renderable.getView();
                                textView.setText(modelName);
                            })
                    .exceptionally(
                            (throwable) -> {
                                throw new AssertionError("Could not load plane card view.", throwable);
                            });
        }
        modelNode = new TransformableNode(arFragment.getTransformationSystem());
        modelNode.setParent(anchorNode);
        modelNode.setRenderable(modelRenderable);
        modelNode.select();
    }

    @Override
    public void onUpdate(FrameTime frameTime) {
        if (infoCard == null) {
            return;
        }
        // Typically, getScene() will never return null because onUpdate() is only called when the node
        // is in the scene.
        // However, if onUpdate is called explicitly or if the node is removed from the scene on a
        // different thread during onUpdate, then getScene may be null.
        if (getScene() == null) {
            return;
        }
        Vector3 cameraPosition = getScene().getCamera().getWorldPosition();
        Vector3 cardPosition = infoCard.getWorldPosition();
        Vector3 direction = Vector3.subtract(cameraPosition, cardPosition);
        Quaternion lookRotation = Quaternion.lookRotation(direction, Vector3.up());
        infoCard.setWorldRotation(lookRotation);
    }

    @Override
    public void onTap(HitTestResult hitTestResult, MotionEvent motionEvent) {
        if (infoCard == null)
            return;
        infoCard.setEnabled(!infoCard.isEnabled());
    }

    public void showInfo(){
        if (infoCard == null)
            return;
        infoCard.setEnabled(!infoCard.isEnabled());
    }
}
