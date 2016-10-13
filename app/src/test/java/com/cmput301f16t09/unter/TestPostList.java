package com.cmput301f16t09.unter;

import junit.framework.TestCase;


public class TestPostList extends TestCase {

    /**
     * Test get posts.
     */
    public void testGetPosts() {
        PostList postList = new PostList();
        Rider rider = new Rider("Kevin", "sandman", "kevin@email.com", "780-431-5274", "password");
        assertTrue(postList.getPosts().size() == 0);

        Post newPost = new Post("12345 67St", "54321 76St", "55", rider);
        postList.addPost(newPost);

        assertTrue(postList.getPosts().size() == 1);
        assertTrue(postList.getPost(0).equals(newPost));
    }

    /**
     * Test add post.
     */
    public void testAddPost() {
        PostList postList = new PostList();
        Rider rider = new Rider("Kevin", "sandman", "kevin@email.com", "780-431-5274", "password");
        assertTrue(postList.getPosts().size() == 0);

        Post newPost = new Post("12345 67St", "54321 76St", "55", rider);
        postList.addPost(newPost);
        assertTrue(postList.getPosts().size() == 1);
    }

    /**
     * Test delete post.
     *
     * @throws Exception the exception
     */
    public void testDeletePost() throws Exception {
        PostList postList = new PostList();
        Rider rider = new Rider("Kevin", "sandman", "kevin@email.com", "780-431-5274", "password");
        assertTrue(postList.getPosts().size() == 0);

        Post newPost = new Post("12345 67St", "54321 76St", "55", rider);
        postList.addPost(newPost);
        assertTrue(postList.getPosts().size() == 1);

        try {
            postList.deletePost(newPost);
        } catch (Exception e){
            throw new Exception();
        }
    }
}
