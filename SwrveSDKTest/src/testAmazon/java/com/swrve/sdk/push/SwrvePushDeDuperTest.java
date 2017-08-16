package com.swrve.sdk.push;

import android.content.Context;
import android.util.Log;

import com.swrve.sdk.SwrveLogger;
import com.swrve.sdk.SwrveSDK;
import com.swrve.sdk.SwrveTestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk=21)
public class SwrvePushDeDuperTest {

    @Before
    public void setUp() throws Exception {
        SwrveSDK.createInstance(RuntimeEnvironment.application, 1, "apiKey");
        SwrveLogger.setLogLevel(Log.VERBOSE);
        ShadowLog.stream = System.out;
    }

    @After
    public void tearDown() throws Exception {
        SwrveTestUtils.removeSwrveSDKSingletonInstance();
    }

    @Test
    public void testNotificationCache() throws Exception {
        Context context = RuntimeEnvironment.application.getApplicationContext();
        SwrvePushDeDuper deDuper = new SwrvePushDeDuper(context);
        LinkedList<String> idCache = deDuper.getRecentNotificationIdCache(context);

        //Start empty
        assertEquals(0, idCache.size());

        //Update with negative size cache. Clamps cache size to 0.
        deDuper.updateRecentNotificationIdCache(idCache, "Don't store", -1, context);

        //Should still be size 0
        idCache = deDuper.getRecentNotificationIdCache(context);
        assertEquals(0, idCache.size());

        //Update with zero size cache.
        deDuper.updateRecentNotificationIdCache(idCache, "Don't store", 0, context);

        //Should still be size 0
        idCache = deDuper.getRecentNotificationIdCache(context);
        assertEquals(0, idCache.size());

        //Update with positive cache.
        deDuper.updateRecentNotificationIdCache(idCache, "MyFirstId", 1, context);

        //Expect size of 1, with "MyFirstId"
        idCache = deDuper.getRecentNotificationIdCache(context);
        assertEquals(1, idCache.size());
        assertEquals("MyFirstId", idCache.get(0));

        deDuper.updateRecentNotificationIdCache(idCache, "MySecondId", 1, context);
        idCache = deDuper.getRecentNotificationIdCache(context);
        assertEquals(1, idCache.size());
        assertEquals("MySecondId", idCache.get(0));

        deDuper.updateRecentNotificationIdCache(idCache, "MyThirdId", 2, context);
        idCache = deDuper.getRecentNotificationIdCache(context);
        assertEquals(2, idCache.size());
        assertEquals("MySecondId", idCache.get(0));
        assertEquals("MyThirdId", idCache.get(1));

        //Empty on next update
        deDuper.updateRecentNotificationIdCache(idCache, "MyThirdId", 0, context);
        idCache = deDuper.getRecentNotificationIdCache(context);
        assertEquals(0, idCache.size());

        //No change if cache is bigger
        idCache.add("1");
        idCache.add("2");
        idCache.add("3");
        idCache.add("4");
        deDuper.updateRecentNotificationIdCache(idCache, "5", 10, context);
        idCache = deDuper.getRecentNotificationIdCache(context);
        assertEquals(5, idCache.size());

        //Check cache is reduced correctly
        deDuper.updateRecentNotificationIdCache(idCache, "6", 2, context);
        idCache = deDuper.getRecentNotificationIdCache(context);
        assertEquals(2, idCache.size());
        assertEquals("5", idCache.get(0));
        assertEquals("6", idCache.get(1));

        //Check cache is empty with -1 value
        deDuper.updateRecentNotificationIdCache(idCache, "7", -1, context);
        idCache = deDuper.getRecentNotificationIdCache(context);
        assertEquals(0, idCache.size());
    }
}

