/*
 * Copyright (C) 2016 The Android Open Source Project
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
package com.yanghaoyi.aosp.car.cluster.renderer;

import android.os.Bundle;
import android.support.annotation.UiThread;

import com.yanghaoyi.aosp.car.navigation.CarNavigationInstrumentCluster;

/**
 * Contains methods specified for Navigation App renderer in instrument cluster.
 * 为导航应用提供渲染仪表的方法
 * @hide
 */
@UiThread
public abstract class NavigationRenderer {
    /**
     * Returns properties of instrument cluster for navigation.
     * 返回用于仪表的导航信息
     */
    abstract public CarNavigationInstrumentCluster getNavigationProperties();

    /**
     * Called when an event is fired to change the navigation state.
     * 在导航状态改变的时候调用
     */
    abstract public void onEvent(int eventType, Bundle bundle);
}
