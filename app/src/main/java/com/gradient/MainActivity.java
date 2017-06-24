package com.gradient;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.gradient.view.GradientTextView;
import com.gradient.view.Orientation;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnLeftToRight;
    Button btnLeftToRightFormNone;
    Button btnRightToLeft;
    Button btnRightToLeftFromNone;
    Button btnInnerToOuter;
    Button btnCustomGradient;
    Button btnStaticInitGradient;
    GradientTextView gradientTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        initView();
    }

    private void initView() {
        btnLeftToRight = (Button) findViewById(R.id.left_to_right);
        btnLeftToRightFormNone = (Button) findViewById(R.id.left_to_right_form_none);
        btnRightToLeft = (Button) findViewById(R.id.right_to_left);
        btnRightToLeftFromNone = (Button) findViewById(R.id.right_to_left_from_none);
        btnInnerToOuter = (Button) findViewById(R.id.inner_to_outer);
        btnCustomGradient = (Button) findViewById(R.id.custom_gradient);
        btnStaticInitGradient = (Button) findViewById(R.id.static_init_gradient);
        gradientTextView = (GradientTextView) findViewById(R.id.gradientTextView);

        btnLeftToRight.setOnClickListener(this);
        btnLeftToRightFormNone.setOnClickListener(this);
        btnRightToLeft.setOnClickListener(this);
        btnRightToLeftFromNone.setOnClickListener(this);
        btnInnerToOuter.setOnClickListener(this);
        btnCustomGradient.setOnClickListener(this);
        btnStaticInitGradient.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.left_to_right:
                gradientTextView.start(Orientation.LEFT_TO_RIGHT,1000);
                break;
            case R.id.left_to_right_form_none:
                gradientTextView.start(Orientation.LEFT_TO_RIGHT_FORM_NONE,1000);
                break;
            case R.id.right_to_left:
                gradientTextView.start(Orientation.RIGHT_TO_LEFT,1000);
                break;
            case R.id.right_to_left_from_none:
                gradientTextView.start(Orientation.RIGHT_TO_LEFT_FROM_NONE,1000);
                break;
            case R.id.inner_to_outer:
                gradientTextView.start(Orientation.INNER_TO_OUTER,1000);
                break;
            case R.id.custom_gradient:
                gradientTextView.setOrientation(Orientation.LEFT_TO_RIGHT);
                ValueAnimator animator = ValueAnimator.ofFloat(new float[]{0.0F, 1.0F});
                animator.setDuration(2000);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = ((Float)animation.getAnimatedValue()).floatValue();
                        gradientTextView.setCurrentProgress(value);
                    }
                });
                animator.start();
                break;
            case R.id.static_init_gradient:
                gradientTextView.setOrientation(Orientation.LEFT_TO_RIGHT);
                gradientTextView.setCurrentProgress(0.5f);
                break;
            default:
                break;
        }
    }
}
