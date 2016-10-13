package com.cmput301f16t09.unter;

import junit.framework.TestCase;

/**
 * Created by Daniel on 2016-10-12.
 */
public class TestDriver extends TestCase {

    public void testUpdatePost() {
        PostList pl = new PostList();
        Rider r = new Rider("Kevin", "sandman", "kevin@email.com", "780-431-5274", "password");
        Post p = new Post("43181 81st", "87132 41st", "100", r);
        pl.addPost(p);
        Driver d = new Driver("Alvin", "urglvin", "alvin@email.com", "780-431-5243", "pass");
        assertTrue(p.getStatus().equals("Pending Offer"));
        assertTrue(p.getDriveOffers().isEmpty());
        assertTrue(pl.getPost(0).getStatus().equals("Pending Offer"));
        d.updatePost(p);
        assertFalse(p.getStatus().equals("Pending Offer"));
        assertTrue(pl.getPost(0).getStatus().equals("Pending Offer"));
        assertTrue(p.getStatus().equals("Pending Approval"));
        assertTrue(pl.getPost(0).getStatus().equals("Pending Approval"));
        assertFalse(p.getDriveOffers().isEmpty());
        assertTrue(p.getDriveOffers().size() == 1);
        assertTrue(p.getDriveOffers().get(0).equals(d.getName()));
    }
}
