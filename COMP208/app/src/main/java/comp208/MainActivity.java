package comp208;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import android.support.design.widget.FloatingActionButton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    private ArFragment arFragment;
    private ModelRenderable andyRenderable;
    private ModelRenderable testRenderable;
    private TransformableNode model;
    private FloatingActionButton changeButton;
    private FloatingActionButton deleteButton;
    private FloatingActionButton menuButton;
    private AnchorNode anchorNode;
    private Anchor anchor;
    private int flag = 0;
    private boolean isOnclickMenu = false;
    private boolean setFlag = false;
    private boolean menuFlag = false;
    private AnimatorSet deleteButtonAnimation;
    private AnimatorSet changeButtonAnimation;
    private ConstraintLayout layout;

    private Model waterModel;

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    // CompletableFuture requires api level 24
    // FutureReturnValueIgnored is not valid
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }

        setContentView(R.layout.activity_ux);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        //CompletableFuture<ViewRenderable> solarControlsStage =
        //        ViewRenderable.builder().setView(this, R.layout.solar_controls).build();



        // When you build a Renderable, Sceneform loads its resources in the background while returning
        // a CompletableFuture. Call thenAccept(), handle(), or check isDone() before calling get().
        ModelRenderable.builder()
                .setSource(this, R.raw.fixed1)
                .build()
                .thenAccept(renderable -> andyRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        ModelRenderable.builder()
                .setSource(this, R.raw.test)
                .build()
                .thenAccept(renderable -> testRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });



        layout = (ConstraintLayout)findViewById(R.id.ConstraintLayout);
        layout.setVisibility(View.GONE);


        arFragment.setOnTapArPlaneListener(this::onPlanedTap);

        changeButton = findViewById(R.id.changeButton);
        changeButton.setEnabled(true);
        changeButton.setOnClickListener(this::setChangeButton);

        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setEnabled(true);
        deleteButton.setOnClickListener(this::setDeleteButton);

        menuButton = findViewById(R.id.menuButton);
        menuButton.setEnabled(true);
        menuButton.setOnClickListener(this::setMenuButton);


        setButtonAnimation();
    }

    public void onClickMenuButton(View v){
        switch ( (v.getId())){
            case R.id.deleteButton:
                if (isOnclickMenu){


                }
                break;

            case R.id.changeButton:

                break;


        }
    }

    private void setButtonAnimation(){
        changeButtonAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.animation);
    }



    private void setChangeButton (View none){
        waterModel.showInfo();
        /*
        if (setFlag == false)
            return;
        flag = flag +1;
        if ((flag%2)==0){
            model.setRenderable(andyRenderable);
            model.select();
        }else{
            model.setRenderable(testRenderable);
            model.select();
        }
        */
    }

    private void setDeleteButton (View none){
        if (setFlag == false)
            return;
        arFragment.getArSceneView().getScene().removeChild(arFragment.getArSceneView().getScene().getChildren().get(2));
        setFlag = false;
    }

    private void setMenuButton (View none){
        if (menuFlag == true){
            layout.setVisibility(View.GONE);
            menuFlag = false;
        }
        else {
            layout.setVisibility(View.VISIBLE);
            changeButtonAnimation.setTarget(layout);
            //changeButtonAnimation.setStartDelay(200);
            changeButtonAnimation.start();
            menuFlag = true;
        }
    }

    private void onPlanedTap(HitResult hitResult, Plane unusedPlane, MotionEvent unusedMotionEvent){
        if (setFlag == true)
            return;
        anchor = hitResult.createAnchor();
        anchorNode = new AnchorNode(anchor);
        anchorNode.setParent(arFragment.getArSceneView().getScene());

        waterModel = new Model("Water Model", this,andyRenderable, arFragment, anchorNode);
        waterModel.setParent(arFragment.getArSceneView().getScene());
        waterModel.setLocalPosition(new Vector3(0.0f, 0.0f, 0.0f));

        /*
        model = new TransformableNode(arFragment.getTransformationSystem());
        model.setParent(anchorNode);
        model.setRenderable(andyRenderable);
        model.select();
        setFlag = true;
        */
        setFlag = true;
    }

    /**
     * Returns false and displays an error message if Sceneform can not run, true if Sceneform can run
     * on this device.
     *
     * <p>Sceneform requires Android N on the device as well as OpenGL 3.0 capabilities.
     *
     * <p>Finishes the activity if Sceneform can not run
     */
    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }
}
