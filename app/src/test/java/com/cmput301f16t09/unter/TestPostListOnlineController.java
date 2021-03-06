package com.cmput301f16t09.unter;

import junit.framework.TestCase;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

/**
 * The type Test post list online controller.
 */
public class TestPostListOnlineController extends TestCase{

    /**
     * Test retrieve posts.
     */
    public void testRetrievePosts() {
        try {
            PostList postlist = PostListOnlineController.getPostList();
        }
        catch (Exception e) {
            fail("Couldn't obtain from elastic server.");
        }
    }

    /**
     * Testadd post.
     */
    public void testaddPost() {
        User newUsr = new User("Kelly", "JellYKeLly", "DaNiEl@EmAil.com", "780-653-1241", "password", "");
        Post new_post = new Post(new GeoPoint(1234.0, 1234.0), new GeoPoint(1234.0, 1234.0), "Corona Station", "University of Alberta", 50.0, 2.4, newUsr.getUsername());
        try {
            PostListOnlineController.AddPostsTask addPostsTask = new PostListOnlineController.AddPostsTask();
            addPostsTask.execute(new_post);
        }
        catch (Exception e) {
            fail("Couldn't add post");
        }
        try {
            PostList postlist = PostListOnlineController.getPostList();
            assertTrue(postlist.getPosts().contains(postlist));
        }
        catch (Exception e) {
            fail("Couldn't obtain from elastic server.");
        }
    }

    /**
     * Testdelete post.
     */
    public void testdeletePost() {
        User newUsr = new User("Kelly", "JellYKeLly", "DaNiEl@EmAil.com", "780-653-1241", "password", "");
        Post new_post = new Post(new GeoPoint(1234.0, 1234.0), new GeoPoint(1234.0, 1234.0), "Corona Station", "University of Alberta", 50.0, 2.4, newUsr.getUsername());

        try {
            PostListOnlineController.AddPostsTask addPostsTask = new PostListOnlineController.AddPostsTask();
            addPostsTask.execute(new_post);
        }
        catch (Exception e) {
            fail("Couldn't add post");
        }
        try {
            PostList postlist = PostListOnlineController.getPostList();
            //PostListOnlineController.DeletePostsTask deletePostsTask = new PostListOnlineController.DeletePostsTask();
            //deletePostsTask.execute(new_post);
            assertFalse(postlist.getPosts().contains(new_post));
        }
        catch (Exception e) {
            fail("Couldn't delete post or obtain from server");
        }
    }

    /**
     * Testsearch post.
     */
    public void testsearchPost() {
        User newUsr = new User("Kelly", "JellYKeLly", "DaNiEl@EmAil.com", "780-653-1241", "password", "");
        User newUsr2 = new User("Kelly", "JellY", "DaNiEl@EmAil.com", "780-653-1241", "password", "");
        User newUsr3 = new User("Kelly", "KeLly", "DaNiEl@EmAil.com", "780-653-1241", "password", "");
        Post new_post = new Post(new GeoPoint(1234.0, 1234.0), new GeoPoint(1234.0, 1234.0), "Corona Station", "University of Alberta", 50.0, 2.4, newUsr.getUsername());
        Post new_post2 = new Post(new GeoPoint(567.0, 432.0), new GeoPoint(3145.0, 4123.0), "Capilano Mall", "Whyte Ave", 121.0, 4.4, newUsr2.getUsername());
        Post new_post3 = new Post(new GeoPoint(1421.0, 624.0), new GeoPoint(3412.0, 8912.0), "Southgate Mall", "Belvedere", 122.0, 6.2, newUsr3.getUsername());

        try {
            PostListOnlineController.SearchPostListsTask searchPostListsTask = new PostListOnlineController.SearchPostListsTask();
            searchPostListsTask.execute("50");
            ArrayList<Post> postlist = searchPostListsTask.get();
            assertTrue(postlist.contains(new_post));
            searchPostListsTask.execute("55");
            postlist = searchPostListsTask.get();
            assertTrue(postlist.contains(new_post2));
            searchPostListsTask.execute("60");
            postlist = searchPostListsTask.get();
            assertTrue(postlist.contains(new_post3));
        } catch (Exception e) {
            fail("Couldn't complete search");
        }
    }
}
