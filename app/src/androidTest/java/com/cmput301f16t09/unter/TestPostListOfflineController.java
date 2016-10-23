package com.cmput301f16t09.unter;

import android.app.Application;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;

import junit.framework.TestCase;

import java.util.ArrayList;
import android.app.Application;
import android.test.ApplicationTestCase;

/**
 * The type Test post list offline controller.
 */
public class TestPostListOfflineController extends ApplicationTestCase<Application> {
    /**
     * Instantiates a new Test post list offline controller.
     */
    public TestPostListOfflineController() {
        super(Application.class);
    }

    /**
     * Test offline save.
     */
    public void testOfflineSave() {

        PostListOfflineController ploc = new PostListOfflineController();
        MockContext mc = new MockContext();

        Location startLocation = new Location("53.52676", "-113.52715");
        Location endLocation = new Location("53.54565", "-113.49026");
        String fare = "$10.50";

        ploc.getPostList();
        ploc.addOfflinePost(startLocation, endLocation, fare, mc);

        PostList pl = new PostList();
        pl = ploc.loadOfflinePosts(mc);
        assertFalse(pl.getPost(0).equals(ploc.getPostList().getPost(0)));
    }
}
