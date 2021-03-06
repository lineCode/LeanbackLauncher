package com.rockchips.android.leanbacklauncher.apps;

import android.content.Context;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

public class AppsEntity {
    private AppsDbHelper mDbHelper;
    private HashMap<String, Long> mLastOpened;
    private HashMap<String, Long> mOrder;
    private String mPackageName;

    public AppsEntity(Context context, AppsDbHelper helper, String packageName, long lastOpenTime, long initialOrder) {
        this(context, helper, packageName);
        setLastOpenedTimeStamp(null, lastOpenTime);
        setOrder(null, initialOrder);
    }

    public AppsEntity(Context ctx, AppsDbHelper helper, String packageName) {
        this.mLastOpened = new HashMap();
        this.mOrder = new HashMap();
        this.mDbHelper = helper;
        this.mPackageName = packageName;
    }

    public Set<String> getComponents() {
        return this.mOrder.keySet();
    }

    public void setLastOpenedTimeStamp(String component, long timeStamp) {
        this.mLastOpened.put(component, Long.valueOf(timeStamp));
    }

    public long getLastOpenedTimeStamp(String component) {
        Long lastOpened = (Long) this.mLastOpened.get(component);
        if (lastOpened == null) {
            lastOpened = (Long) this.mLastOpened.get(null);
            if (lastOpened == null) {
                lastOpened = Long.valueOf(0);
            }
        }
        return lastOpened.longValue();
    }

    public long getOrder(String component) {
        Long order = (Long) this.mOrder.get(component);
        if ((order == null || order.longValue() == 0) && this.mOrder.keySet().size() == 1) {
            order = (Long) this.mOrder.values().iterator().next();
            if (order == null) {
                order = Long.valueOf(0);
            }
            setOrder(component, order.longValue());
        }
        if (order != null) {
            return order.longValue();
        }
        return 0;
    }

    public void setOrder(String component, long order) {
        this.mOrder.put(component, Long.valueOf(order));
    }

    public String getKey() {
        return this.mPackageName;
    }

    public synchronized void onAction(int actionType, String component, String group) {
        long time = new Date().getTime();
        if (this.mDbHelper.getMostRecentTimeStamp() >= time) {
            time = this.mDbHelper.getMostRecentTimeStamp() + 1;
        }
        switch (actionType) {
            case android.support.v7.preference.R.styleable.Preference_android_icon /*0*/:
                if (getLastOpenedTimeStamp(component) == 0) {
                    setLastOpenedTimeStamp(component, time);
                    break;
                }
                break;
            case android.support.v7.recyclerview.R.styleable.RecyclerView_android_descendantFocusability /*1*/:
                setLastOpenedTimeStamp(component, time);
                break;
            case android.support.v7.preference.R.styleable.Preference_android_layout /*3*/:
                this.mLastOpened.clear();
                break;
        }
    }
}
