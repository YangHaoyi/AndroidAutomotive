/*
 * Copyright (C) 2017 The Android Open Source Project
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
package com.yanghaoyi.androidautomotive;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Rect;
import android.hardware.display.DisplayManager.DisplayListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;

import com.yanghaoyi.aosp.car.CarNotConnectedException;
import com.yanghaoyi.aosp.car.cluster.ClusterActivityState;
import com.yanghaoyi.aosp.car.cluster.renderer.InstrumentClusterRenderingService;
import com.yanghaoyi.aosp.car.cluster.renderer.NavigationRenderer;
import com.yanghaoyi.aosp.car.navigation.CarNavigationInstrumentCluster;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static java.lang.Integer.parseInt;

/**
 * Dummy implementation of {@link SampleClusterServiceImpl} to log all interaction.
 */
public class SampleClusterServiceImpl extends InstrumentClusterRenderingService {

    private static final String TAG = SampleClusterServiceImpl.class.getSimpleName();

    private Listener mListener;
    private final Binder mLocalBinder = new LocalBinder();
    static final String LOCAL_BINDING_ACTION = "local";

    private ClusterDisplayProvider mDisplayProvider;

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind, intent: " + intent);
        return (LOCAL_BINDING_ACTION.equals(intent.getAction()))
                ? mLocalBinder : super.onBind(intent);
        //super的onBind方法回调用getNavigationRenderer对mRendererBinder进行赋值操作
        // mRendererBinder = new RendererBinder(getNavigationRenderer());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");

        mDisplayProvider = new ClusterDisplayProvider(this,
                new DisplayListener() {
                    @Override
                    public void onDisplayAdded(int displayId) {
                        Log.i(TAG, "Cluster display found, displayId: " + displayId);
                        doClusterDisplayConnected(displayId);
                    }

                    @Override
                    public void onDisplayRemoved(int displayId) {
                        Log.w(TAG, "Cluster display has been removed");
                    }

                    @Override
                    public void onDisplayChanged(int displayId) {

                    }
                });
    }

    private void doClusterDisplayConnected(int displayId) {
        ActivityOptions options = ActivityOptions.makeBasic();
        options.setLaunchDisplayId(displayId);
        Intent intent = new Intent(this, MainClusterActivity.class);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent, options.toBundle());
    }

    @Override
    protected void onKeyEvent(KeyEvent keyEvent) {
        Log.i(TAG, "onKeyEvent, keyEvent: " + keyEvent + ", listener: " + mListener);
        if (mListener != null) {
            mListener.onKeyEvent(keyEvent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "onDestroy");
    }

    void registerListener(Listener listener) {
        mListener = listener;
    }

    void unregisterListener() {
        mListener = null;
    }

    //初始化CarNavigationInstrumentCluster仪表转向标信息
    @Override
    protected NavigationRenderer getNavigationRenderer() {
        NavigationRenderer navigationRenderer = new NavigationRenderer() {
            @Override
            public CarNavigationInstrumentCluster getNavigationProperties() {
                Log.i(TAG, "getNavigationProperties");
                //创建只支持枚举类型的仪表信息 更新时间1000毫秒
                CarNavigationInstrumentCluster config =
                        CarNavigationInstrumentCluster.createCluster(1000);
                Log.i(TAG, "getNavigationProperties, returns: " + config);
                return config;
            }

            @Override
            public void onEvent(int eventType, Bundle bundle) {
                StringBuilder bundleSummary = new StringBuilder();
                for (String key : bundle.keySet()) {
                    bundleSummary.append(key);
                    bundleSummary.append("=");
                    bundleSummary.append(bundle.get(key));
                    bundleSummary.append(" ");
                }
                Log.i(TAG, "onEvent(" + eventType + ", " + bundleSummary + ")");
            }
        };

        Log.i(TAG, "createNavigationRenderer, returns: " + navigationRenderer);
        return navigationRenderer;
    }

    class LocalBinder extends Binder {
        SampleClusterServiceImpl getService() {
            // Return this instance of LocalService so clients can call public methods
            return SampleClusterServiceImpl.this;
        }
    }

    interface Listener {
        void onKeyEvent(KeyEvent event);
    }

    @Override
    protected void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        if (args != null && args.length > 0) {
            execShellCommand(args);
        } else {

            if (args == null || args.length == 0) {
                writer.println("* dump " + getClass().getCanonicalName() + " *");
                writer.println("DisplayProvider: " + mDisplayProvider);
            }
        }
    }

//    private void emulateKeyEvent(int keyCode) {
//        Log.i(TAG, "emulateKeyEvent, keyCode: " + keyCode);
//        long downTime = SystemClock.uptimeMillis();
//        long eventTime = SystemClock.uptimeMillis();
//        KeyEvent event = obtainKeyEvent(keyCode, downTime, eventTime, KeyEvent.ACTION_DOWN);
//        onKeyEvent(event);
//
//        eventTime = SystemClock.uptimeMillis();
//        event = obtainKeyEvent(keyCode, downTime, eventTime, KeyEvent.ACTION_UP);
//        onKeyEvent(event);
//    }

//    private KeyEvent obtainKeyEvent(int keyCode, long downTime, long eventTime, int action) {
//        int scanCode = 0;
//        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
//            scanCode = 108;
//        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
//            scanCode = 106;
//        }
//        return KeyEvent.obtain(
//                    downTime,
//                    eventTime,
//                    action,
//                    keyCode,
//                    0 /* repeat */,
//                    0 /* meta state */,
//                    0 /* deviceId*/,
//                    scanCode /* scancode */,
//                    KeyEvent.FLAG_FROM_SYSTEM /* flags */,
//                    InputDevice.SOURCE_KEYBOARD,
//                    null /* characters */);
//    }

    private void execShellCommand(String[] args) {
        Log.i(TAG, "execShellCommand, args: " + Arrays.toString(args));

        String command = args[0];

        switch (command) {
            case "injectKey": {
                if (args.length > 1) {
//                    emulateKeyEvent(parseInt(args[1]));
                } else {
                    Log.i(TAG, "Not enough arguments");
                }
                break;
            }
            case "destroyOverlayDisplay": {
                Settings.Global.putString(getContentResolver(),
                        "overlay_display_devices", "");
                break;
            }

            case "createOverlayDisplay": {
                if (args.length > 1) {
                    Settings.Global.putString(getContentResolver(),
                            "overlay_display_devices", args[1]);
                } else {
                    Log.i(TAG, "Not enough arguments, expected 2");
                }
                break;
            }

            case "setUnobscuredArea": {
                if (args.length > 5) {
                    Rect unobscuredArea = new Rect(parseInt(args[2]), parseInt(args[3]),
                            parseInt(args[4]), parseInt(args[5]));
                    try {
                        setClusterActivityState(args[1],
                                ClusterActivityState.create(true, unobscuredArea).toBundle());
                    } catch (CarNotConnectedException e) {
                        Log.i(TAG, "Failed to set activity state.", e);
                    }
                } else {
                    Log.i(TAG, "wrong format, expected: category left top right bottom");
                }
            }
        }
    }

}
