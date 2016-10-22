package com.cmput301f16t09.unter;

import junit.framework.TestCase;

import java.util.ArrayList;

public class TestPostListOnlineController extends TestCase{

    public void testRetrievePosts {
        try {
            PostList postlist = PostListOnlineController.getPostList();
        }
        catch (Exception e) {
            fail("Couldn't obtain from elastic server.");
        }
    }

    public void testaddPost {
        Post new_post = new Post(new Location("1234", "1234"), new Location("1234", "1234"), "50");
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

    public void testdeletePost {
        Post new_post = new Post(new Location("1234", "1234"), new Location("1234", "1234"), "50");
        try {
            PostListOnlineController.AddPostsTask addPostsTask = new PostListOnlineController.AddPostsTask();
            addPostsTask.execute(new_post);
        }
        catch (Exception e) {
            fail("Couldn't add post");
        }
        try {
            PostList postlist = PostListOnlineController.getPostList();
            PostListOnlineController.DeletePostsTask deletePostsTask = new PostListOnlineController.DeletePostsTask();
            deletePostsTask.execute(new_post);
            assertFalse(postlist.getPosts().contains(new_post));
        }
        catch (Exception e) {
            fail("Couldn't delete post or obtain from server");
        }
    }

    public void testsearchPost {
        Post new_post = new Post(new Location("1234", "1234"), new Location("1234", "1234"), "50");
        Post new_post2 = new Post(new Location("1234", "1234"), new Location("1234", "1234"), "55");
        Post new_post3 = new Post(new Location("1234", "1234"), new Location("1234", "1234"), "60");
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
