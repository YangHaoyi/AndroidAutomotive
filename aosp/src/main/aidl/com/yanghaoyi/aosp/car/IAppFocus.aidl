package com.yanghaoyi.aosp.car;

import com.yanghaoyi.aosp.car.IAppFocusListener;
import com.yanghaoyi.aosp.car.IAppFocusOwnershipCallback;

interface IAppFocus {
    void registerFocusListener(IAppFocusListener callback, int appType) = 0;
    void unregisterFocusListener(IAppFocusListener callback, int appType) = 1;
    int[] getActiveAppTypes() = 2;
    /** callback used as a token */
    boolean isOwningFocus(IAppFocusOwnershipCallback callback, int appType) = 3;
    /** callback used as a token */
    int requestAppFocus(IAppFocusOwnershipCallback callback, int appType) = 4;
    /** callback used as a token */
    void abandonAppFocus(IAppFocusOwnershipCallback callback, int appType) = 5;
}