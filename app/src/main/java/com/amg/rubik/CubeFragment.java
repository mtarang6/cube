package com.amg.rubik;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class CubeFragment extends AbstractFragment
        implements CubeListener {

    private RubiksCube mCube = null;
    private RubikGLSurfaceView mRubikView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tag = "rubik-game";
        mRootView = inflater.inflate(R.layout.fragment_cube, container, false);
        initUi();
        initializeRubikView();
        return mRootView;
    }

    private void initUi() {
        findViewById(R.id.randomizeButton).setOnClickListener(this);
        findViewById(R.id.solveButton).setOnClickListener(this);
    }

    private void initializeRubikView() {
        ViewGroup view = (ViewGroup)findViewById(R.id.cube_holder);
        mRubikView = new RubikGLSurfaceView(getActivity());
        createCube();
        view.addView(mRubikView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mRubikView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mRubikView.onPause();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.randomizeButton:
                randomizeOnclick();
                break;

            case R.id.solveButton:
                mRubikView.printDebugInfo();
                solve();
                break;
        }
    }

    private void createCube() {
        if (cubeSize() == 3) {
            mCube = new RubiksCube3x3x3();
        } else {
            mCube = new RubiksCube(cubeSize());
        }
        mCube.setListener(this);
        mRubikView.setCube(mCube);
        if (cubeState() != null)
            mCube.restoreColors(cubeState());
    }

    private void resetButtons() {
        Button btn = (Button)findViewById(R.id.randomizeButton);
        btn.setEnabled(true);
        btn.setText(R.string.randomize);
        btn = (Button)findViewById(R.id.solveButton);
        btn.setEnabled(true);
        btn.setText(R.string.solve);
    }

    private void solve() {
        int solveRet = mCube.solve();
        if (solveRet == 0) {
            Button btn = (Button)findViewById(R.id.solveButton);
            btn.setEnabled(false);
            btn = (Button)findViewById(R.id.randomizeButton);
            btn.setEnabled(false);
        }
    }

    private void randomizeOnclick() {
        Button btn = (Button)findViewById(R.id.randomizeButton);
        if (mCube.getState() == RubiksCube.CubeState.IDLE) {
            mRubikView.printDebugInfo();
            mCube.randomize();
            btn.setText(R.string.stop);
            btn = (Button)findViewById(R.id.solveButton);
            btn.setEnabled(false);
        } else if (mCube.getState() == RubiksCube.CubeState.RANDOMIZE) {
            mCube.stopRandomize();
            mRubikView.printDebugInfo();
            btn.setText(R.string.randomize);
            btn = (Button)findViewById(R.id.solveButton);
            btn.setEnabled(true);
        }
    }

    private Toast currentToast;
    public void handleCubeMessage(final String msg) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (currentToast != null) currentToast.cancel();
                currentToast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
                currentToast.show();
            }
        });
    }

    @Override
    public void handleCubeSolved() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button btn = (Button) findViewById(R.id.solveButton);
                btn.setEnabled(true);
                btn = (Button) findViewById(R.id.randomizeButton);
                btn.setEnabled(true);
                mRubikView.printDebugInfo();
            }
        });
    }
}