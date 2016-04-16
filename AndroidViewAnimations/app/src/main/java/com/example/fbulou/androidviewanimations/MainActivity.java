package com.example.fbulou.androidviewanimations;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/*https://github.com/daimajia/AndroidViewAnimations#slide


        In Gradle :

        dependencies {
        compile 'com.nineoldandroids:library:2.4.0'
        compile 'com.daimajia.easing:library:1.0.1@aar'
        compile 'com.daimajia.androidanimations:library:1.1.3@aar'
        }

        YoYo.with(Techniques.Tada)
        .duration(700)
        .playOn(findViewById(R.id.edit_area));
        */

public class MainActivity extends AppCompatActivity {

    Spinner mSpinner;
    EditText mEdittext;
    String[] mAnimationListArray = {
            /* feature ATTENTION*/
            "Flash", "Pulse", "RubberBand", "Shake", "Swing", "Wobble", "Bounce", "Tada", "StandUp", "Wave",

            /*Special*/
            "Hinge", "RollIn", "RollOut", "Landing", "TakingOff", "DropOut",

            /*Bounce*/
            "BounceIn", "BounceInDown", "BounceInLeft", "BounceInRight", "BounceInUp",

            /*Fade*/
            "FadeIn", "FadeInUp", "FadeInDown", "FadeInLeft", "FadeInRight", "FadeOut", "FadeOutDown", "FadeOutLeft", "FadeOutRight", "FadeOutUp",

            /*Flip*/
            "FlipInX", "FlipOutX", "FlipOutY",

            /*Rotate*/
            "RotateIn", "RotateInDownLeft", "RotateInDownRight", "RotateInUpLeft", "RotateInUpRight", "RotateOut",
            "RotateOutDownLeft", "RotateOutDownRight", "RotateOutUpLeft", "RotateOutUpRight",

            /*Slide*/
            "SlideInLeft", "SlideInRight", "SlideInUp", "SlideInDown", "SlideOutLeft", "SlideOutRight", "SlideOutUp", "SlideOutDown",
            /*Zoom*/

            "ZoomIn", "ZoomInDown", "ZoomInLeft", "ZoomInRight", "ZoomInUp", "ZoomOut", "ZoomOutDown", "ZoomOutLeft", "ZoomOutRight", "ZoomOutUp"
    };

    List<Techniques> mAnimations;
    TextView mTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSpinner = (Spinner) findViewById(R.id.mSpinner);
        mTextview = (TextView) findViewById(R.id.mTextview);
        mEdittext= (EditText) findViewById(R.id.mEdittext);

        mAnimations = new ArrayList<>(Arrays.asList(
                Techniques.Flash, Techniques.Pulse, Techniques.RubberBand, Techniques.Shake, Techniques.Swing, Techniques.Wobble, Techniques.Bounce, Techniques.Tada, Techniques.StandUp, Techniques.Wave,

                Techniques.Hinge, Techniques.RollIn, Techniques.RollOut, Techniques.Landing, Techniques.TakingOff, Techniques.DropOut,

                Techniques.BounceIn, Techniques.BounceInDown, Techniques.BounceInLeft, Techniques.BounceInRight, Techniques.BounceInUp,

                Techniques.FadeIn, Techniques.FadeInUp, Techniques.FadeInDown, Techniques.FadeInLeft, Techniques.FadeInRight, Techniques.FadeOut, Techniques.FadeOutDown, Techniques.FadeOutLeft, Techniques.FadeOutRight, Techniques.FadeOutUp,

                Techniques.FlipInX, Techniques.FlipOutX, Techniques.FlipOutY,

                Techniques.RotateIn, Techniques.RotateInDownLeft, Techniques.RotateInDownRight, Techniques.RotateInUpLeft, Techniques.RotateInUpRight, Techniques.RotateOut, Techniques.RotateOutDownLeft, Techniques.RotateOutDownRight, Techniques.RotateOutUpLeft, Techniques.RotateOutUpRight,

                Techniques.SlideInLeft, Techniques.SlideInRight, Techniques.SlideInUp, Techniques.SlideInDown, Techniques.SlideOutLeft, Techniques.SlideOutRight, Techniques.SlideOutUp, Techniques.SlideOutDown,

                Techniques.ZoomIn, Techniques.ZoomInDown, Techniques.ZoomInLeft, Techniques.ZoomInRight, Techniques.ZoomInUp, Techniques.ZoomOut, Techniques.ZoomOutDown, Techniques.ZoomOutLeft, Techniques.ZoomOutRight, Techniques.ZoomOutUp
        ));

        setMySpinner();

        addListenerToMySpinner();
    }

    private void addListenerToMySpinner() {
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                mTextview.setText(mAnimationListArray[position]);
                String temp=mEdittext.getText().toString();
                int mDuration;

                if(temp.length() ==0)
                    mDuration=3000;
                else
                    mDuration=Integer.parseInt(temp);

                YoYo.with(mAnimations.get(position))
                        .duration(mDuration)
                        .playOn(mTextview);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setMySpinner() {
        List<String> mAnimationList = new ArrayList<>(Arrays.asList(mAnimationListArray));

        ArrayAdapter<String> mSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mAnimationList);

        mSpinner.setAdapter(mSpinnerAdapter);
    }


}
