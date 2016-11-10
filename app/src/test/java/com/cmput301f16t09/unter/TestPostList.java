package com.cmput301f16t09.unter;

import junit.framework.TestCase;

import org.osmdroid.util.GeoPoint;


public class TestPostList extends TestCase {

    public void testGetPost(){
        PostList postList = new PostList();

        User rider = new User("Kevin", "sandman", "kevin@email.com", "780-431-5274", "password");
        GeoPoint start = new GeoPoint(1.0, 2.0);
        GeoPoint end = new GeoPoint(2.0, 1.0);
        Post newPost = new Post(start, end, "$4", rider);
        postList.addPost(newPost);

        User rider2 = new User("Joe", "MoJoe JoeJoe", "joe@joemail.com", "780-joe-joey", "joeisthebest");
        GeoPoint start2 = new GeoPoint(5.0, 3.0);
        GeoPoint end2 = new GeoPoint(3.0, 5.0);
        Post newPost2 = new Post(start2, end2, "$4", rider2);
        postList.addPost(newPost2);

        assertTrue(postList.getPost(1) == newPost2);
    }

    /**
     * Test get posts.
     */
    public void testGetPosts() {
        PostList postList = new PostList();
        assertTrue(postList.getPosts().size() == 0);

        User rider = new User("Kevin", "sandman", "kevin@email.com", "780-431-5274", "password");
        GeoPoint start = new GeoPoint(1.0, 2.0);
        GeoPoint end = new GeoPoint(2.0, 1.0);
        Post newPost = new Post(start, end, "$4", rider);
        postList.addPost(newPost);

        assertTrue(postList.getPosts().size() == 1);
        assertTrue(postList.getPost(0).equals(newPost));
    }

    /**
     * Test add post.
     */
    public void testAddPost() {

        PostList postList = new PostList();
        assertTrue(postList.getPosts().size() == 0);

        User rider = new User("Kevin", "sandman", "kevin@email.com", "780-431-5274", "password");
        GeoPoint start = new GeoPoint(1.0, 2.0);
        GeoPoint end = new GeoPoint(2.0, 1.0);
        Post newPost = new Post(start, end, "$4", rider);
        postList.addPost(newPost);

        assertTrue(postList.getPosts().size() == 1);
        assertTrue(postList.getPost(0) == newPost);
    }

    /**
     * Test delete post.
     *
     * @throws Exception the exception
     */
    public void testDeletePost() throws Exception {

        PostList postList = new PostList();
        assertTrue(postList.getPosts().size() == 0);

        User rider = new User("Kevin", "sandman", "kevin@email.com", "780-431-5274", "password");
        GeoPoint start = new GeoPoint(1.0, 2.0);
        GeoPoint end = new GeoPoint(2.0, 1.0);
        Post newPost = new Post(start, end, "$4", rider);
        postList.addPost(newPost);

        assertTrue(postList.getPosts().size() == 1);
        assertTrue(postList.getPost(0) == newPost);

        postList.deletePost(newPost);

        assertTrue((postList.getPosts()).isEmpty());

        try {
            postList.deletePost(newPost);
        } catch (Exception e){
            throw new Exception();
        }
    }

    public void testSearchList(){
        PostList postList = new PostList();
        assertTrue(postList.getPosts().size() == 0);

        User rider = new User("Kevin", "sandman", "kevin@email.com", "780-431-5274", "password");
        GeoPoint start = new GeoPoint(1.0, 2.0);
        GeoPoint end = new GeoPoint(2.0, 1.0);
        Post newPost = new Post(start, end, "$4", rider);
        postList.addPost(newPost);

        User rider2 = new User("Joe", "MoJoe JoeJoe", "joe@joemail.com", "780-joe-joey", "joeisthebest");
        GeoPoint start2 = new GeoPoint(5.0, 3.0);
        GeoPoint end2 = new GeoPoint(3.0, 5.0);
        Post newPost2 = new Post(start2, end2, "$4", rider2);
        postList.addPost(newPost2);

        PostList searchResults = new PostList();
        searchResults = postList.searchList("Joe");

        assertTrue((searchResults.getPosts().size() == 1));
        assertTrue(searchResults.getPost(0) == newPost2);

    }
}
