package comp208;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.animation.ModelAnimator;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.AnimationData;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    private ArFragment arFragment;
    private ModelRenderable water1Renderable;
    private ModelRenderable water2Renderable;
    private ModelRenderable water3Renderable;
    private ModelRenderable water4Renderable;
    private ModelRenderable water5Renderable;
    private ModelRenderable coldFrontRenderable;
    private ModelRenderable warmFrontRenderable;
    private FloatingActionButton infoButton;
    private FloatingActionButton deleteButton;
    private FloatingActionButton menuButton;
    private FloatingActionButton chooseButton;
    private FloatingActionButton playButton;

    private AnchorNode anchorNode;
    private Anchor anchor;
    private boolean isOnclickMenu = false;
    private boolean setFlag = false;
    private boolean menuFlag = false;
    private boolean infoFlag = false;
    private AnimatorSet deleteButtonAnimation;
    private AnimatorSet changeButtonAnimation;
    private ConstraintLayout layout;
    private Model Model;
    private ModelAnimator animator;
    private int nextAnimation;
    private boolean hasBegun=false;
    private Context hintText;
    private Context infoWarning;
    private Button btn1;
    private TextSwitcher textSwitcher;
    private  TextView introText;
    private int curStr;
    private String[] strs1=new String[]{"The water cycle model","Water transport cycle is a phenomenon of the earth", "Firstly, water will be evaporate by sunlight",
            "Cloud then help to transport water from sea to mainland", "Raining weather make water back to ground and converge into river",
            "Finally, river flow into sea again"};
    private String[] strs2=new String[]{"The warm front model","Warm front always cause raining day"};
    private String[] strs3=new String[]{"The cold front model","Cold front always cause windy weather"};
    private int count, flag1, flag2;
    private ModelRenderable temp;
    private int choice;


    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    // CompletableFuture requires api level 24
    // FutureReturnValueIgnored is not valid
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!checkIsSupportedDeviceOrFinish(this))
            return;

        setContentView(R.layout.activity_ux);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        textSwitcher = (TextSwitcher) findViewById(R.id.textSwitcher);
        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView tv = new TextView(MainActivity.this);
                tv.setTextSize(20);
                tv.setTextColor(Color.WHITE);
                return tv;
            }
        });

        ModelRenderable.builder()
                .setSource(this, R.raw.b1)
                .build()
                .thenAccept(renderable -> water1Renderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        ModelRenderable.builder()
                .setSource(this, R.raw.b2)
                .build()
                .thenAccept(renderable -> water2Renderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        ModelRenderable.builder()
                .setSource(this, R.raw.b3)
                .build()
                .thenAccept(renderable -> water3Renderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        ModelRenderable.builder()
                .setSource(this, R.raw.b4)
                .build()
                .thenAccept(renderable -> water4Renderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        ModelRenderable.builder()
                .setSource(this, R.raw.b5)
                .build()
                .thenAccept(renderable -> water5Renderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        ModelRenderable.builder()
                .setSource(this, R.raw.warm)
                .build()
                .thenAccept(renderable -> warmFrontRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        ModelRenderable.builder()
                .setSource(this, R.raw.cold)
                .build()
                .thenAccept(renderable -> coldFrontRenderable = renderable)
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

        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setEnabled(true);
        deleteButton.setOnClickListener(this::setDeleteButton);

        menuButton = findViewById(R.id.menuButton);
        menuButton.setEnabled(true);
        menuButton.setOnClickListener(this::setMenuButton);

        chooseButton = findViewById(R.id.chooseButton);
        chooseButton.setEnabled(true);
        chooseButton.setOnClickListener(this::setChooseButton);

        playButton = findViewById(R.id.animationButton);
        playButton.setEnabled(true);
        playButton.setOnClickListener(this::onPlayAnimation);

        setButtonAnimation();

        introText = findViewById(R.id.textView);

        hintText = this;
        btn1=(Button)findViewById(R.id.button2);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast t =Toast.makeText(hintText,"Tap the buttons at the bottom right corner:\n" +
                        " 1.Choose model\n 2.Delete the model\n 3.Get Explanation for the model",Toast.LENGTH_LONG);
                t.show();
            }
        });

        infoWarning = this;
        infoButton = findViewById(R.id.infoButton);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setFlag != true){
                    Toast t =Toast.makeText(infoWarning,"Please deploy a model first.",Toast.LENGTH_SHORT);
                    t.show();
                    return;
                }
                if (infoFlag == true){
                    //introText = findViewById(R.id.textView);
                    introText.setText("");
                    introText.setBackground(null);
                    infoFlag = false;
                    return;
                }

                if (choice == 0){
                    //introText=findViewById(R.id.textView);
                    introText.setBackgroundColor(Color.GRAY);
                    introText.setText("The water cycle describes how water evaporates from the surface of the earth, rises into the atmosphere, cools and condenses into rain or snow in clouds, and falls again to the surface as precipitation. \n\n" +
                            "The water falling on land collects in rivers and lakes, soil, and porous layers of rock, and much of it flows back into the oceans, where it will once more evaporate.\n\n " +
                            "The cycling of water in and out of the atmosphere is a significant aspect of the weather patterns on Earth.\n\n\n"+
                            "https://pmm.nasa.gov/education/water-cycle");
                    introText.setAutoLinkMask(Linkify.ALL);
                    introText.setMovementMethod(LinkMovementMethod.getInstance());
                    infoFlag = true;

                }
                if (choice == 1){
                    introText.setBackgroundColor(Color.GRAY);
                    introText.setText("A warm weather front is defined as the changeover region where a warm air mass is replacing a cold air mass. Warm fronts usually move from southwest to northeast and the air behind a warm front is warmer and moister than the air ahead of it. " +
                            "When a warm front passes, the air becomes noticeably warmer and more humid than it was before.\n\n" +
                            "On a weather forecast map, a warm front is represented by a solid line with red semicircles pointing towards the colder air and in the direction of movement. \n\n\n"+
                            "https://en.wikipedia.org/wiki/Warm_front");
                    introText.setAutoLinkMask(Linkify.ALL);
                    introText.setMovementMethod(LinkMovementMethod.getInstance());
                    infoFlag = true;
                }
                if (choice == 2){
                    introText.setBackgroundColor(Color.GRAY);
                    introText.setText("A cold weather front is defined as the changeover region where a cold air mass is replacing a warmer air mass. Cold weather fronts usually move from northwest to southeast.\n\n" +
                            "The air behind a cold front is colder and drier than the air in front.\n\n When a cold front passes through, temperatures can drop more than 15 degrees within an hour.\n\n\n" +
                            "https://en.wikipedia.org/wiki/Cold_front");
                    introText.setAutoLinkMask(Linkify.ALL);
                    introText.setMovementMethod(LinkMovementMethod.getInstance());
                    infoFlag = true;
                }
            }
        });

        choice = 0;
    }

    public void next(View source){
    }

    public void onClickMenuButton(View v){
        switch ( (v.getId())){
            case R.id.deleteButton:
                if (isOnclickMenu){
                }
                break;
            case R.id.infoButton:
                break;
        }
    }

    private void setButtonAnimation(){
        changeButtonAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.animation);
    }

    private void setChooseButton(View none){
        chooseModel();
    }

    private void setDeleteButton (View none){
        if (setFlag != true){
            Toast t =Toast.makeText(this,"Please deploy a model first.",Toast.LENGTH_SHORT);
            t.show();
            return;
        }
        textSwitcher.setText("");
        introText.setText("");
        introText.setBackground(null);
        infoFlag = false;
        while (arFragment.getArSceneView().getScene().getChildren().size()>2) {
            arFragment.getArSceneView().getScene().removeChild(arFragment.getArSceneView().getScene().getChildren().get(2));
        }
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
            changeButtonAnimation.start();
            menuFlag = true;
        }
    }

    private void onPlanedTap(HitResult hitResult, Plane unusedPlane, MotionEvent unusedMotionEvent){
        hasBegun = true;
        if (setFlag == true)
            return;

        count = 0;
        flag1 = flag2 = 0;
        //choice = 0;
        anchor = hitResult.createAnchor();
        anchorNode = new AnchorNode(anchor);
        anchorNode.setParent(arFragment.getArSceneView().getScene());

        switch (choice){
            case 0:
                temp = water1Renderable;
                deployModel("Water Model", water1Renderable, anchorNode);
                textSwitcher.setText(strs1[0]);
                break;
            case 1:
                temp = warmFrontRenderable;
                deployModel("Warm Front Model", warmFrontRenderable, anchorNode);
                textSwitcher.setText(strs2[0]);
                break;
            case 2:
                temp = coldFrontRenderable;
                deployModel("Cold Front Model", coldFrontRenderable, anchorNode);
                textSwitcher.setText(strs3[0]);
                break;
        }
        setFlag = true;
    }

    private void onPlayAnimation(View unusedView) {
        if (setFlag != true){
            Toast t =Toast.makeText(this,"Please deploy a model fistly.",Toast.LENGTH_SHORT);
            t.show();
            return;
        }
        if (animator != null && animator.isRunning())
            return;

        if (choice == 0) {
            if (flag1 != flag2) {
                flag1++;
                nextModel();
            }
        }else if(choice == 1){
            textSwitcher.setText(strs2[1]);
        }else if(choice == 2){
            textSwitcher.setText(strs3[1]);
        }

        if (animator == null || !animator.isRunning()) {
            if (choice ==0 && flag2 == 0)
                textSwitcher.setText(strs1[1]);
            AnimationData data = temp.getAnimationData(nextAnimation);
            animator = new ModelAnimator(data, temp);
            animator.start();
            Log.d(
                    TAG,
                    String.format(
                            "Starting animation %s - %d ms long", data.getName(), data.getDurationMs()));
            count++;
            flag2++;
        }
    }

    public void deployModel(String modelName, ModelRenderable modelRenderable, AnchorNode anchorNode){
        Model = new Model(modelName, this, modelRenderable, arFragment, anchorNode);
        Model.setParent(arFragment.getArSceneView().getScene());
        Model.setLocalPosition(new Vector3(0.0f, 0.0f, 0.0f));
    }

    public void chooseModel(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this).setTitle("Choose a model");
        final String[] models={"Water Model","Warm Front Model", "Cold Front Model"};
        builder.setItems(models, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                introText.setText("");
                introText.setBackground(null);
                infoFlag = false;
                if (setFlag != true){
                    switch (which){
                        case 0:
                            choice = 0;
                            break;
                        case 1:
                            choice = 1;
                            break;
                        case 2:
                            choice = 2;
                            break;
                    }
                }else {
                    switch (which) {
                        case 0:
                            introText = findViewById(R.id.textView);
                            introText.setText("");
                            textSwitcher.setText(strs1[0]);
                            Model.changeRenderable(water1Renderable, "Water Model");
                            temp = water1Renderable;
                            choice = 0;
                            count = flag1 = flag2 = 0;
                            break;
                        case 1:
                            introText = findViewById(R.id.textView);
                            introText.setText("");
                            textSwitcher.setText(strs2[0]);
                            Model.changeRenderable(warmFrontRenderable, "Warm Front Model");
                            temp = warmFrontRenderable;
                            choice = 1;
                            flag1 = flag2 = 0;
                            break;
                        case 2:
                            introText = findViewById(R.id.textView);
                            introText.setText("");
                            textSwitcher.setText(strs3[0]);
                            Model.changeRenderable(coldFrontRenderable, "Cold Front Model");
                            temp = coldFrontRenderable;
                            choice = 2;
                            flag1 = flag2 = 0;
                            break;
                    }
                }
            }
        });
        builder.show();
    }

    public void nextModel(){
        switch (count){
            case 1:
                introText=findViewById(R.id.textView);
                introText.setText("");
                textSwitcher.setText(strs1[2]);
                Model.changeRenderable(water5Renderable, "Evaporation");
                temp = water5Renderable;
                break;
            case 2:
                introText=findViewById(R.id.textView);
                introText.setText("");
                textSwitcher.setText(strs1[3]);
                Model.changeRenderable(water4Renderable, "Condensation");
                temp = water4Renderable;
                break;
            case 3:
                introText=findViewById(R.id.textView);
                introText.setText("");
                textSwitcher.setText(strs1[4]);
                Model.changeRenderable(water3Renderable, "Precipitation");
                temp = water3Renderable;
                break;
            case 4:
                introText=findViewById(R.id.textView);
                introText.setText("");
                textSwitcher.setText(strs1[5]);
                Model.changeRenderable(water2Renderable, "Run-Off");
                temp = water2Renderable;
                count = 0;
                break;
        }

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


