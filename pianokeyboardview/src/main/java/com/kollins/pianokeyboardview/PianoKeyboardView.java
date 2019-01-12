/*
 * Copyright 2019
 * Kollins Lima (kollins.lima@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.kollins.pianokeyboardview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.kollins.pianokeyboardview.R;

import java.util.ArrayList;

public class PianoKeyboardView extends View {

    private int numberKeys, midiStart;
    private Paint black, pressedColor, white;
    private ArrayList<Key> whites = new ArrayList<>(numberKeys);
    private ArrayList<Key> blacks = new ArrayList<>(numberKeys);
    private int whiteWidth, height;
    private OnKeyChangeListener keyEventListener = null;

    private int touchIndex;
    private float x;
    private float y;
    private Key auxKey;

    public PianoKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.PianoKeyboardView);
        black = new Paint();
        black.setColor(arr.getColor(R.styleable.PianoKeyboardView_blackKeyColor, Color.BLACK));
        white = new Paint();
        white.setColor(arr.getColor(R.styleable.PianoKeyboardView_whiteKeyColor, Color.WHITE));
        pressedColor = new Paint();
        pressedColor.setColor(arr.getColor(R.styleable.PianoKeyboardView_pressedColor, Color.YELLOW));
        pressedColor.setStyle(Paint.Style.FILL);
        numberKeys = arr.getInteger(R.styleable.PianoKeyboardView_numKeys, 15);
        midiStart = arr.getInteger(R.styleable.PianoKeyboardView_midiStart, 60);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        double blackWidth;
        int leftWhite, rightWhite, leftBlack, rightBlack;
        int midiShift = 0;
        RectF key;

        whites.clear();
        blacks.clear();

        whiteWidth = w / numberKeys;
        height = h;

        for (int i = 0, j = 0; i < numberKeys; i++, j = (j + 1) % 7) {
            leftWhite = i * whiteWidth;
            rightWhite = leftWhite + whiteWidth;

            if (i == numberKeys - 1) {
                rightWhite = w;  //Complete the screen
            }

            switch (j) {
                case 1:
                    blackWidth = (3.0f * whiteWidth) / 5.0f;
                    leftBlack = (int) ((i - 1) * whiteWidth + blackWidth);
                    rightBlack = (int) (leftBlack + blackWidth);

                    key = new RectF(leftBlack, 0, rightBlack, 0.67f * height);
                    blacks.add(new Key(key, midiStart + midiShift));
                    midiShift += 1;
                    break;

                case 2:
                    blackWidth = (3.0f * whiteWidth) / 5.0f;
                    rightBlack = (int) ((i + 1) * whiteWidth - blackWidth);
                    leftBlack = (int) (rightBlack - blackWidth);

                    key = new RectF(leftBlack, 0, rightBlack, 0.67f * height);
                    blacks.add(new Key(key, midiStart + midiShift));
                    midiShift += 1;
                    break;

                case 4:
                    blackWidth = (4.0f * whiteWidth) / 7.0f;
                    leftBlack = (int) ((i - 1) * whiteWidth + blackWidth);
                    rightBlack = (int) (leftBlack + blackWidth);

                    key = new RectF(leftBlack, 0, rightBlack, 0.67f * height);
                    blacks.add(new Key(key, midiStart + midiShift));
                    midiShift += 1;
                    break;

                case 5:
                    blackWidth = (4.0f * whiteWidth) / 7.0f;
                    leftBlack = (int) (i * whiteWidth - blackWidth/2);
                    rightBlack = (int) (leftBlack + blackWidth);

                    key = new RectF(leftBlack, 0, rightBlack, 0.67f * height);
                    blacks.add(new Key(key, midiStart + midiShift));
                    midiShift += 1;
                    break;

                case 6:
                    blackWidth = (4.0f * whiteWidth) / 7.0f;
                    rightBlack = (int) ((i + 1) * whiteWidth - blackWidth);
                    leftBlack = (int) (rightBlack - blackWidth);

                    key = new RectF(leftBlack, 0, rightBlack, 0.67f * height);
                    blacks.add(new Key(key, midiStart + midiShift));
                    midiShift += 1;
                    break;
            }

            key = new RectF(leftWhite, 0, rightWhite, h);
            whites.add(new Key(key, midiStart + midiShift));
            midiShift += 1;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Key k : whites) {
            canvas.drawRect(k.keyDesign, k.down ? pressedColor : white);
        }

        for (int i = 1; i < numberKeys; i++) {
            canvas.drawLine(i * whiteWidth, 0, i * whiteWidth, height, black);
        }

        for (Key k : blacks) {
            canvas.drawRect(k.keyDesign, k.down ? pressedColor : black);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getActionMasked();

        if (action == MotionEvent.ACTION_MOVE) {

            ArrayList<Key> releasedKeys = new ArrayList<>(numberKeys);
            ArrayList<Key> pressedKeys = new ArrayList<>(numberKeys);

            //Reset all keys
            for (Key k : blacks) {
                if (k.down) {
                    k.down = false;
                    releasedKeys.add(k);
                }
            }

            for (Key k : whites) {
                if (k.down) {
                    k.down = false;
                    releasedKeys.add(k);
                }
            }

            //Set new keys
            for (int i = 0; i < event.getPointerCount(); i++) {
                x = event.getX(i);
                y = event.getY(i);

                auxKey = keyForCoords(x, y);

                if (auxKey != null) {
                    auxKey.down = true;
                    pressedKeys.add(auxKey);
                    invalidate();
                }
            }

            //Event Listener Released
            for (Key k : releasedKeys){
                if (keyEventListener != null && !pressedKeys.contains(k)){
                    keyEventListener.onKeyReleased(k.midiNote);
                }
            }

            //Event Listener Pressed
            for (Key k : pressedKeys){
                if (keyEventListener != null && !releasedKeys.contains(k)){
                    keyEventListener.onKeyPressed(k.midiNote);
                }
            }

        } else {
            touchIndex = event.getActionIndex();
            x = event.getX(touchIndex);
            y = event.getY(touchIndex);
            auxKey = keyForCoords(x, y);

            if (auxKey != null) {
                if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {
                    auxKey.down = true;
                    if (keyEventListener != null){
                        keyEventListener.onKeyPressed(auxKey.midiNote);
                    }
                } else {
                    auxKey.down = false;
                    if (keyEventListener != null){
                        keyEventListener.onKeyReleased(auxKey.midiNote);
                    }
                }

                invalidate();
            }
        }
        return true;
    }

    private Key keyForCoords(float x, float y) {
        for (Key k : blacks) {
            if (k.keyDesign.contains(x, y)) {
                return k;
            }
        }

        for (Key k : whites) {
            if (k.keyDesign.contains(x, y)) {
                return k;
            }
        }
        return null;
    }

    public int getNumberKeys() {
        return numberKeys;
    }

    public void setNumberKeys(int numberKeys) {
        this.numberKeys = numberKeys;
    }

    public Paint getBlackKeyColor() {
        return black;
    }

    public void setBlackKeyColor(Paint newColor) {
        this.black = newColor;
    }

    public Paint getPressedColor() {
        return pressedColor;
    }

    public void setPressedColor(Paint pressedColor) {
        this.pressedColor = pressedColor;
    }

    public Paint getWhiteKeyColor() {
        return white;
    }

    public void setWhiteKeyColor(Paint newColor) {
        this.white = newColor;
    }

    public int getMidiStart() {
        return midiStart;
    }

    public void setMidiStart(int midiStart) {
        this.midiStart = midiStart;
    }

    public void setKeyEventListener(OnKeyChangeListener keyEventListener) {
        this.keyEventListener = keyEventListener;
    }
}
