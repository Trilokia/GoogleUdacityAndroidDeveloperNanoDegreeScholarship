package com.udacity.gradle.builditbigger;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class EndPointAsyncTaskTest {
   @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);
    @Test
    public void testDoInBackground() {
        EndpointAsyncTask endpointsAsyncTask = new EndpointAsyncTask();
        try {
            String result = endpointsAsyncTask.get(30, SECONDS);
            assertNotNull(result);
            assertTrue(result.length() > 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }


