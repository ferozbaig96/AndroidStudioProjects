package com.example.fbulou.showcaseviewtutorial;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    Button b1, b2, b3, b4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        b1 = (Button) findViewById(R.id.ButtonOne);
        b2 = (Button) findViewById(R.id.ButtonTwo);
        b3 = (Button) findViewById(R.id.ButtonThree);
        b4 = (Button) findViewById(R.id.ButtonFour);

        showSCV(R.id.ButtonFour, "Content Title", "This is highlighting the button. Are you getting it my friend? If yes, gimme a high five bro. Otherwise, high five on thy face bro!", 0, 0);
    }

    ViewTarget target;
    RelativeLayout.LayoutParams lps;
    int margin;
    ShowcaseView sv;

    void showSCV(int viewId, String title, String text, int rule1, int rule2) {
        rule1 = rule1 != 0 ? rule1 : RelativeLayout.ALIGN_PARENT_BOTTOM;
        rule2 = rule2 != 0 ? rule2 : RelativeLayout.ALIGN_PARENT_RIGHT;

        target = new ViewTarget(viewId, this);

        lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(rule1);
        lps.addRule(rule2);
        margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
        lps.setMargins(margin, margin, margin, margin);


        sv = new ShowcaseView.Builder(this)
                .withMaterialShowcase()
                .setTarget(target)
                .setContentTitle(title)
                .setContentText(text)
                //  .withNewStyleShowcase()
                .setStyle(R.style.ShowcaseView_Light)
                //  .singleShot(R.id.ButtonOne)             //to show one time only
                .hideOnTouchOutside()
                //  .doNotBlockTouches()

                .build();
        sv.setButtonPosition(lps);


    }

}
