package com.cmput301f16t09.unter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class PostListMainController {

    private static PostList postListMain = null;
    private static PostList postListQueue = new PostList();
    private static PostList postListUpdate = new PostList();

    static public PostList getPostList(Context context) {
        /**
         * Create a new list if the list has not been initialized
         * @see PostList
         */
        if (postListMain == null) {
            postListMain = new PostList();
        }

        /**
         * Try to fetch posts from elasticsearch server using PostListOnlineController. Also saves
         * posts to offline file (real_offline_posts.sav)
         * @see PostListOnlineController
         * @see #saveOfflinePosts(Context)
         */
        try {
            if (isNetworkAvailable(context)) {
                PostListOnlineController.GetPostsTask onlinePosts = new PostListOnlineController.GetPostsTask();
                onlinePosts.execute("");
                // App crashes when using postListMain.getposts().clear(), or if there is no list clearing statement
                postListMain = new PostList();
                postListMain.setPostList(onlinePosts.get(1000, TimeUnit.MILLISECONDS));
                PostListOfflineController.saveOfflinePosts("mainOffline", postListMain, context);
            }
            else {
                PostListOfflineController.loadOfflinePosts("mainOffline", postListMain, context);
            }
        }

        /**
         * If fetching from elasticsearch fails, read data from file (offline behaviour)
         * @param e
         * @see #loadOfflinePosts(Context)
         */
        //
        catch (Exception e) {
            // Add To Queue FILE
            PostListOfflineController.loadOfflinePosts("mainOffline", postListMain, context);
            Toast.makeText(context, "Cannot load posts from elasticsearch", Toast.LENGTH_SHORT).show();
            Log.i("Error", "Loading failed");
        }
        return postListMain;
    }

    static public PostList getPostListQueue() {
        return postListQueue;
    }

    static public PostList getPostListUpdate() {
        return postListUpdate;
    }

    static public void clearPostListQueue() {
        postListQueue.getPosts().clear();
    }

    static public void clearPostListUpdate() {
        postListUpdate.getPosts().clear();
    }

    /**
     * Add offline post.
     *
     * @param post the post
     * @param context     the context of the activity
     */
    public static void addPost(Post post, Context context) {
        /**
         * Get the post list and add the post to elastic search (if online) and save to offline file
         * @see #getPostList(Context)
         * @see #saveOfflinePosts(Context)
         * @see PostList
         */
        if (isNetworkAvailable(context)) {
            PostListOnlineController.AddPostsTask addOnlinePost = new PostListOnlineController.AddPostsTask();
            addOnlinePost.execute(post);
        }

        else {
            PostListOfflineController.loadOfflinePosts("queueOffline", postListQueue, context);
            postListQueue.addPost(post);
            PostListOfflineController.saveOfflinePosts("queueOffline", postListQueue, context);
        }

        // Double check this part
        getPostList(context).addPost(post);
        PostListOfflineController.saveOfflinePosts("mainOffline", postListMain, context);
    }

    public static void updatePosts(Post post, Context context) {
        if (isNetworkAvailable(context)) {
            try {
                PostListOnlineController.UpdatePostsTask updatePostsTask = new PostListOnlineController.UpdatePostsTask();
                updatePostsTask.execute(post);
                updatePostsTask.get();
            }

            catch (Exception e) {
                e.printStackTrace();
            }
        }

        else {
            PostListOfflineController.loadOfflinePosts("updateOffline", postListUpdate, context);
            postListUpdate.addPost(post);
            PostListOfflineController.saveOfflinePosts("updateOffline", postListUpdate, context);
        }
//        postListMain.updatePost(post);
        PostListOfflineController.saveOfflinePosts("mainOffline", postListMain, context);
        // Also update main list.
    }

    public static void updateOfflinePosts(Context context) {

        // Get updated information for postListMain, not required for
        postListMain.getPosts();
        PostListOfflineController.saveOfflinePosts("mainOffline", postListMain, context);
//        PostListOfflineController.saveOfflinePosts("queueOffline", postListQueue, context);
    }

    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
