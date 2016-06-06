package com.coska.beacon.model;

import android.os.Build;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import com.coska.beacon.BaseTest;
import com.coska.beacon.BuildConfig;
import com.coska.beacon.service.TaskService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;


@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,
        manifest = Config.NONE,
        sdk = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class TaskServiceTest extends BaseTest {

    private static final String TAG = "com.coska.beacon.model";

    @Before
    public void setUp() {
        Log.i(TAG,"TaskServiceTest-setUp()");

    }

    @Test
    public void testTaskService1() {
        Log.i(TAG,"testTaskService1");

//        Intent intent = new Intent(getSystemContext(), MyIntentService.class);
//        super.startService(intent);
//        assertNotNull(getService());
//
//        Context ctx = new Context();
//        TaskService.startService(, );
    }


    @After
    public void tearDown() {
    }
}