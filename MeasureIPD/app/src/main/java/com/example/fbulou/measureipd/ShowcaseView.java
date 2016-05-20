package com.example.fbulou.measureipd;

import android.graphics.Color;
import android.view.Gravity;

import tourguide.tourguide.ChainTourGuide;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.Sequence;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

public class ShowcaseView {
    static void scv_eyeHolders() {
        ChainTourGuide tourGuide1 = ChainTourGuide.init(ImageActivity.getInstance())
                .setToolTip(new ToolTip()
                        .setDescription("Aim at the center of the left eye pupil in the image by dragging it")
                        .setGravity(Gravity.BOTTOM)
                        .setBackgroundColor(Color.parseColor("#c0392b"))
                )
                .setOverlay(new Overlay()
                        .setBackgroundColor(Color.parseColor("#EE2c3e50"))
                )
                .playLater(ImageActivity.getInstance().mImageViewDragLeftEye);

        ChainTourGuide tourGuide2 = ChainTourGuide.init(ImageActivity.getInstance())
                .setToolTip(new ToolTip()
                        .setDescription("Aim at the center of the right eye pupil in the image by dragging it")
                        .setGravity(Gravity.BOTTOM)
                        .setBackgroundColor(Color.parseColor("#c0392b"))
                )
                .setOverlay(new Overlay()
                        .setBackgroundColor(Color.parseColor("#EE2c3e50"))
                )
                .playLater(ImageActivity.getInstance().mImageViewDragRightEye);

        Sequence sequence = new Sequence.SequenceBuilder()
                .add(tourGuide1, tourGuide2)
                .setDefaultOverlay(new Overlay()
                )
                .setDefaultPointer(null)
                .setContinueMethod(Sequence.ContinueMethod.Overlay)
                .build();

        ChainTourGuide.init(ImageActivity.getInstance()).playInSequence(sequence);
    }

    static void scv_disc() {
        ImageActivity.getInstance().mTourGuideHandlerDisc = TourGuide.init(ImageActivity.getInstance())
                .with(TourGuide.Technique.Click)
                .setToolTip(new ToolTip()
                        .setDescription("Put it on the boundary of the ATM/Debit/Credit card in the image")
                        .setGravity(Gravity.BOTTOM)
                )
                //   .setOverlay(new Overlay())
                .playOn(ImageActivity.getInstance().mImageViewDragDisc);
    }
}
