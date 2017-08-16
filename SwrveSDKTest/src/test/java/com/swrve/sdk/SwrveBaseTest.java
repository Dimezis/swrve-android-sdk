package com.swrve.sdk;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.util.Log;

import com.swrve.sdk.rest.IRESTClient;
import com.swrve.sdk.rest.IRESTResponseListener;
import com.swrve.sdk.rest.RESTResponse;
import com.swrve.sdk.test.BuildConfig;
import com.swrve.sdk.test.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowLog;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public abstract class SwrveBaseTest {

    protected ShadowApplication shadowApplication;
    protected Activity mActivity;
    protected ShadowActivity mShadowActivity;

    @Before
    public void setUp() throws Exception {
        SwrveLogger.setLogLevel(Log.VERBOSE);
        ShadowLog.stream = System.out;
        shadowApplication = Shadows.shadowOf(RuntimeEnvironment.application);
        mActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();
        mShadowActivity = Shadows.shadowOf(mActivity);
    }

    @After
    public void tearDown() throws Exception {
        // empty
    }
}
