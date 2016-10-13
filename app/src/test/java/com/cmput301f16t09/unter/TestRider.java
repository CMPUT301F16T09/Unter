package com.cmput301f16t09.unter;

import junit.framework.TestCase;


public class TestRider extends TestCase {

    /**
     * Test create post.
     */
    public void testCreatePost() {
        PostList pl = new PostList();
        Rider r = new Rider("Kevin", "sandman", "kevin@email.com", "780-431-5274", "password");
        Post p = new Post("43181 81st", "87132 41st", "100", r);
        pl.addPost(p);

        assertTrue(p.getStatus().equals("Pending Offer"));
        assertTrue(pl.getPost(0).getStatus().equals("Pending Offer"));
    }
}
