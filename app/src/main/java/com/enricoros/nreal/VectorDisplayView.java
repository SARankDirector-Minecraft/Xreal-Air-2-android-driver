package com.enricoros.nreal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

// Generated by GPT4
public class VectorDisplayView extends View {
  private Paint mPaintVector;
  private Paint mPaintDot;
  private float[] mAcceleration = {0.0f, 0.0f, 0.0f};
  private float mMaxValue = 1.0f;
  private final float mMaxDecayFactor = 0.999f;

  public VectorDisplayView(Context context) {
    super(context);
    init();
  }

  public VectorDisplayView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private void init() {
    mPaintVector = new Paint(Paint.ANTI_ALIAS_FLAG);
    mPaintVector.setColor(Color.WHITE);
    mPaintVector.setStrokeWidth(8);

    mPaintDot = new Paint(Paint.ANTI_ALIAS_FLAG);
    mPaintDot.setColor(Color.RED);
  }

  public void updateAcceleration(float[] acceleration) {
    mAcceleration = acceleration;
    float maxValueInCurrentData = 0.0f;
    for (float value : acceleration)
      maxValueInCurrentData = Math.max(maxValueInCurrentData, Math.abs(value));
    // Update the maximum value for auto-scaling with decay
    mMaxValue = Math.max(maxValueInCurrentData, mMaxValue * mMaxDecayFactor);
    invalidate();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    if (mAcceleration == null || (mAcceleration[0] == 0.0f && mAcceleration[1] == 0.0f && mAcceleration[2] == 0.0f))
      return;

    int centerX = getWidth() / 2;
    int centerY = getHeight() / 2;
    int maxLength = Math.min(centerX, centerY) - 20;

    // Implement a simple 3D projection system
    float x = mAcceleration[0] / mMaxValue * maxLength;
    float y = mAcceleration[2] / mMaxValue * maxLength;
    float z = mAcceleration[1] / mMaxValue * maxLength;

    float fov = 1000;
    float scale = fov / (fov + z);
    float projX = x * scale;
    float projY = y * scale;

    int startX = centerX;
    int startY = centerY;
    int endX = (int) (centerX + projX);
    int endY = (int) (centerY + projY);

    // Draw the 3D-like vector
    canvas.drawLine(startX, startY, endX, endY, mPaintVector);

    // Draw the dot at the tip of the vector
    float baseDotRadius = 20.0f;
    float dotRadius = baseDotRadius * scale;
    canvas.drawCircle(endX, endY, dotRadius, mPaintDot);
  }
}
