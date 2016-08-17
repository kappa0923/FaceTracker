/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.gms.samples.vision.face.facetracker;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.google.android.gms.samples.vision.face.facetracker.ui.camera.GraphicOverlay;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;

/**
 * Graphic instance for rendering face position, orientation, and landmarks within an associated
 * graphic overlay view.
 */
class FaceGraphic extends GraphicOverlay.Graphic {
    private static final float FACE_POSITION_RADIUS = 10.0f;
    private static final float ID_TEXT_SIZE = 40.0f;
    private static final float ID_Y_OFFSET = 50.0f;
    private static final float ID_X_OFFSET = -50.0f;
    private static final float BOX_STROKE_WIDTH = 15.0f;

    private static final int COLOR_CHOICES[] = {
        Color.BLUE,
        Color.CYAN,
        Color.GREEN,
        Color.MAGENTA,
        Color.RED,
        Color.WHITE,
        Color.YELLOW
    };
    private static int mCurrentColorIndex = 0;

    private Paint mFacePositionPaint;
    private Paint mIdPaint;
    private Paint mBoxPaint;

    private volatile Face mFace;
    private int mFaceId;
    private float mFaceHappiness;

    FaceGraphic(GraphicOverlay overlay) {
        super(overlay);

        mCurrentColorIndex = (mCurrentColorIndex + 1) % COLOR_CHOICES.length;
        final int alpha = 128;
        final int red = 220;
        final int green = 160;
        final int blue = 240;

        mFacePositionPaint = new Paint();
        mFacePositionPaint.setARGB(alpha, red, green, blue);

        mIdPaint = new Paint();
        mIdPaint.setARGB(alpha, red, green, blue);
        mIdPaint.setTextSize(ID_TEXT_SIZE);

        mBoxPaint = new Paint();
        mBoxPaint.setARGB(alpha, red, green, blue);
        mBoxPaint.setStyle(Paint.Style.STROKE);
        mBoxPaint.setStrokeWidth(BOX_STROKE_WIDTH);
    }

    void setId(int id) {
        mFaceId = id;
    }


    /**
     * Updates the face instance from the detection of the most recent frame.  Invalidates the
     * relevant portions of the overlay to trigger a redraw.
     */
    void updateFace(Face face) {
        mFace = face;
        postInvalidate();
    }

    /**
     * Draws the face annotations for position on the supplied canvas.
     */
    @Override
    public void draw(Canvas canvas) {
        Face face = mFace;
        if (face == null) {
            return;
        }

        float mouseX = 0, mouseY = 0;
        float noseX = 0, noseY = 0;

        for (Landmark landmark : face.getLandmarks()) {
            float cx = translateX(landmark.getPosition().x);
            float cy = translateY(landmark.getPosition().y);
            if (landmark.getType() == Landmark.BOTTOM_MOUTH) {
                mouseX = cx;
                mouseY = cy;
            }
            if (landmark.getType() == Landmark.NOSE_BASE) {
                noseX = cx;
                noseY = cy;
            }
//            canvas.drawCircle(cx, cy, 10, mIdPaint);
        }

        canvas.drawCircle((mouseX + noseX)/2, (mouseY + noseY)/2, (mouseY - noseY)/2, mBoxPaint);
        canvas.drawCircle((mouseX + noseX)/2, (mouseY + noseY)/2, (mouseY - noseY), mBoxPaint);
        canvas.drawCircle((mouseX + noseX)/2, (mouseY + noseY)/2, (int)((mouseY - noseY)*1.5), mBoxPaint);

//        // Draws a circle at the position of the detected face, with the face's track id below.
//        float x = translateX(face.getPosition().x + face.getWidth() / 2);
//        float y = translateY(face.getPosition().y + face.getHeight() / 2);
//        canvas.drawCircle(x, y, FACE_POSITION_RADIUS, mFacePositionPaint);
//        canvas.drawText("id: " + mFaceId, x + ID_X_OFFSET, y + ID_Y_OFFSET, mIdPaint);
//        canvas.drawText("happiness: " + String.format("%.2f", face.getIsSmilingProbability()), x - ID_X_OFFSET, y - ID_Y_OFFSET, mIdPaint);
//        canvas.drawText("right eye: " + String.format("%.2f", face.getIsRightEyeOpenProbability()), x + ID_X_OFFSET * 2, y + ID_Y_OFFSET * 2, mIdPaint);
//        canvas.drawText("left eye: " + String.format("%.2f", face.getIsLeftEyeOpenProbability()), x - ID_X_OFFSET*2, y - ID_Y_OFFSET*2, mIdPaint);
//
//        // Draws a bounding box around the face.
//        float xOffset = scaleX(face.getWidth() / 2.0f);
//        float yOffset = scaleY(face.getHeight() / 2.0f);
//        float left = x - xOffset;
//        float top = y - yOffset;
//        float right = x + xOffset;
//        float bottom = y + yOffset;
//        canvas.drawRect(left, top, right, bottom, mBoxPaint);
    }
}
