package com.cmput301f16t09.unter;

import java.util.ArrayList;

/**
 * Created by Daniel on 2016-10-12.
 */
public class PostList {
    private ArrayList<Post> postList;

    public PostList() {
        this.postList = new ArrayList<Post>();
    }

    public ArrayList<Post> getPosts() {
        return this.postList;
    }

    public void addPost(Post post) {
        postList.add(post);
    }

    public void deletePost(Post post) throws Exception{
        postList.remove(post);
    }

    public Post getPost(int i) {
        return postList.get(i);
    }
}
