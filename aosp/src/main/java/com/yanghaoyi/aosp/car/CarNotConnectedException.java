/*
 * Copyright (C) 2015 The Android Open Source Project
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

package com.yanghaoyi.aosp.car;


/**
 * Exception thrown when car is not connected for the API which requires car connection.
 */
public class CarNotConnectedException extends Exception {
    private static final long serialVersionUID = -5629175439268813047L;

    public CarNotConnectedException() {
    }

    public CarNotConnectedException(String name) {
        super(name);
    }

    public CarNotConnectedException(String name, Throwable cause) {
        super(name, cause);
    }

    public CarNotConnectedException(Exception cause) {
        super(cause);
    }
}
