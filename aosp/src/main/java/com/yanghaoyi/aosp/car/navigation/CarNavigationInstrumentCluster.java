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
package com.yanghaoyi.aosp.car.navigation;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Holds options related to navigation for the car's instrument cluster.
 * 涉及车辆仪表盘的导航信息
 */
public class CarNavigationInstrumentCluster implements Parcelable {

    /**
     * Navigation Next Turn messages contain an image, as well as an enum.
     * 导航转向标信息，可以是图片或枚举
     * */
    public static final int CLUSTER_TYPE_CUSTOM_IMAGES_SUPPORTED = 1;
    /**
     * Navigation Next Turn messages contain only an enum.
     * 只能是枚举类型的转向标信息
     *  */
    public static final int CLUSTER_TYPE_IMAGE_CODES_ONLY = 2;

    /**
     * @hide
     * RetentionPolicy.SOURCE表明当前@IntDef的保留策略，只保留源码中，编译时删除,
     * */
    @Retention(RetentionPolicy.SOURCE)
    /**
     * IntDef本身是个Android中提供的一种注解，用于替代枚举的使用
     * */
    @IntDef({
        CLUSTER_TYPE_CUSTOM_IMAGES_SUPPORTED,
        CLUSTER_TYPE_IMAGE_CODES_ONLY
    })
    public @interface ClusterType {}

    //仪表信息最小更新时间
    private int mMinIntervalMillis;
    //转向标取值类型｛CLUSTER_TYPE_CUSTOM_IMAGES_SUPPORTED / CLUSTER_TYPE_IMAGE_CODES_ONLY｝
    @ClusterType
    private final int mType;
    //如果转向信息为图片枚举类型（CLUSTER_TYPE_CUSTOM_IMAGES_SUPPORTED），图片的像素宽度
    private final int mImageWidth;
    //如果转向信息为图片枚举类型（CLUSTER_TYPE_CUSTOM_IMAGES_SUPPORTED），图片的像素高度
    private final int mImageHeight;
    //如果转向信息为图片枚举类型，图片的素材度（8，16或者32）
    private final int mImageColorDepthBits;
    //额外信息
    private final Bundle mExtra;

    public static final Creator<CarNavigationInstrumentCluster> CREATOR
            = new Creator<CarNavigationInstrumentCluster>() {
        public CarNavigationInstrumentCluster createFromParcel(Parcel in) {
            return new CarNavigationInstrumentCluster(in);
        }

        public CarNavigationInstrumentCluster[] newArray(int size) {
            return new CarNavigationInstrumentCluster[size];
        }
    };

    /**
     * 创建只支持枚举类型
     * */
    public static CarNavigationInstrumentCluster createCluster(int minIntervalMillis) {
        return new CarNavigationInstrumentCluster(minIntervalMillis, CLUSTER_TYPE_IMAGE_CODES_ONLY,
                0, 0, 0);
    }

    /**
     * 创建支持枚举与图片类型
     * */
    public static CarNavigationInstrumentCluster createCustomImageCluster(int minIntervalMs,
            int imageWidth, int imageHeight, int imageColorDepthBits) {
        return new CarNavigationInstrumentCluster(minIntervalMs,
                CLUSTER_TYPE_CUSTOM_IMAGES_SUPPORTED,
                imageWidth, imageHeight, imageColorDepthBits);
    }

    /**
     * Minimum time between instrument cluster updates in milliseconds.
     * 仪表信息最小更新时间
     * */
    public int getMinIntervalMillis() {
        return mMinIntervalMillis;
    }

    /**
     * Type of instrument cluster, can be {@link #CLUSTER_TYPE_CUSTOM_IMAGES_SUPPORTED} or
     * {@link #CLUSTER_TYPE_IMAGE_CODES_ONLY}.
     * 转向标取值类型
     */
    @ClusterType
    public int getType() {
        return mType;
    }

    /**
     *  If instrument cluster is image, width of instrument cluster in pixels.
     *  如果转向信息为图片枚举类型（CLUSTER_TYPE_CUSTOM_IMAGES_SUPPORTED），图片的像素宽度
     * */
    public int getImageWidth() {
        return mImageWidth;
    }

    /**
     * If instrument cluster is image, height of instrument cluster in pixels.
     * 如果转向信息为图片枚举类型（CLUSTER_TYPE_CUSTOM_IMAGES_SUPPORTED），图片的像素高度
     * */
    public int getImageHeight() {
        return mImageHeight;
    }

    /**
     * Contains extra information about instrument cluster.
     * @hide
     * 额外信息
     */
    public Bundle getExtra() { return mExtra; }

    /**
     * If instrument cluster is image, number of bits of colour depth it supports (8, 16, or 32).
     * 如果转向信息为图片枚举类型，图片的素材度（8，16或者32）
     */
    public int getImageColorDepthBits() {
        return mImageColorDepthBits;
    }

    public CarNavigationInstrumentCluster(CarNavigationInstrumentCluster that) {
      this(that.mMinIntervalMillis,
          that.mType,
          that.mImageWidth,
          that.mImageHeight,
          that.mImageColorDepthBits);
    }

    /**
     * Whether cluster support custom image or not.
     * @return
     * 是否提供图片
     */
    public boolean supportsCustomImages() {
      return mType == CLUSTER_TYPE_CUSTOM_IMAGES_SUPPORTED;
    }

    private CarNavigationInstrumentCluster(
            int minIntervalMillis,
            @ClusterType int type,
            int imageWidth,
            int imageHeight,
            int imageColorDepthBits) {
        mMinIntervalMillis = minIntervalMillis;
        mType = type;
        mImageWidth = imageWidth;
        mImageHeight = imageHeight;
        mImageColorDepthBits = imageColorDepthBits;
        mExtra = new Bundle();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mMinIntervalMillis);
        dest.writeInt(mType);
        dest.writeInt(mImageWidth);
        dest.writeInt(mImageHeight);
        dest.writeInt(mImageColorDepthBits);
        dest.writeBundle(mExtra);
    }

    private CarNavigationInstrumentCluster(Parcel in) {
        mMinIntervalMillis = in.readInt();
        mType = in.readInt();
        mImageWidth = in.readInt();
        mImageHeight = in.readInt();
        mImageColorDepthBits = in.readInt();
        mExtra = in.readBundle(getClass().getClassLoader());
    }

    /** Converts to string for debug purpose */
    @Override
    public String toString() {
        return CarNavigationInstrumentCluster.class.getSimpleName() + "{ " +
                "minIntervalMillis: " + mMinIntervalMillis + ", " +
                "type: " + mType + ", " +
                "imageWidth: " + mImageWidth + ", " +
                "imageHeight: " + mImageHeight + ", " +
                "imageColourDepthBits: " + mImageColorDepthBits +
                "extra: " + mExtra + " }";
    }
}
