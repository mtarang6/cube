package com.amg.rubik.graphics;

import android.opengl.GLES20;
import android.util.Log;

import com.amg.rubik.cube.RubiksCube;
import com.amg.rubik.cube.Square;

/**
 * Created by amar on 30/11/15.
 */
public class RubikRenderer extends GLRenderer {

    private static final String tag = "rubik-renderer";
    RubiksCube mCube;

    public RubikRenderer() {
        mCube = null;
    }

    public void setCube(RubiksCube cube) {
        this.mCube = cube;
    }

    @Override
    public void onCreate(int width, int height, boolean contextLost) {
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1f);
    }

    boolean highlightFlag = false;
    Square highlightPoint;
    float mSize = 0.02f;

    public void clearHighlight() {
        highlightFlag = false;
    }

    public void setHighlightPoint(Point3D point, Axis axis) {
        // TODO: only front face is implemented now
        Point3D[] corners = new Point3D[4];

        switch (axis) {
            case X_AXIS:
                corners[0] = new Point3D(point.getX(), point.getY() + mSize, point.getZ() + mSize);
                corners[1] = new Point3D(point.getX(), point.getY() - mSize, point.getZ() + mSize);
                corners[2] = new Point3D(point.getX(), point.getY() - mSize, point.getZ() - mSize);
                corners[3] = new Point3D(point.getX(), point.getY() + mSize, point.getZ() - mSize);
                break;
            case Y_AXIS:
                corners[0] = new Point3D(point.getX() - mSize, point.getY(), point.getZ() - mSize);
                corners[1] = new Point3D(point.getX() - mSize, point.getY(), point.getZ() + mSize);
                corners[2] = new Point3D(point.getX() + mSize, point.getY(), point.getZ() + mSize);
                corners[3] = new Point3D(point.getX() + mSize, point.getY(), point.getZ() - mSize);
                break;
            case Z_AXIS:
                corners[0] = new Point3D(point.getX() - mSize, point.getY() + mSize, point.getZ());
                corners[1] = new Point3D(point.getX() - mSize, point.getY() - mSize, point.getZ());
                corners[2] = new Point3D(point.getX() + mSize, point.getY() - mSize, point.getZ());
                corners[3] = new Point3D(point.getX() + mSize, point.getY() + mSize, point.getZ());
                break;
        }
        this.highlightPoint = new Square(corners, Square.HIGHLIGHT_COLOR);
        highlightFlag = true;
    }

    @Override
    public void onDrawFrame(boolean firstDraw) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT
                | GLES20.GL_DEPTH_BUFFER_BIT);
        if (mCube == null) {
            Log.w(tag, "no cube set");
            return;
        }

        Square.startDrawing();
        mCube.draw(mMVPMatrix);
        if (highlightFlag) {
            highlightPoint.draw(mMVPMatrix);
        }
        Square.finishDrawing();
    }
}
