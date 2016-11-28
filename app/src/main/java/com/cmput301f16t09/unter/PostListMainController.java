package com.cmput301f16t09.unter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import java.util.concurrent.TimeUnit;

/**
 * The type Post list main controller which handles using the
 * PostList Offline or Online Controllers depending on if
 * WiFi is available or not.
 */
public class PostListMainController {

    private static PostList postListMain = null;
    private static PostList postListQueue = null;
    private static PostList postListUpdate = null;
    private static PostList postListDelete = null;

    /**
     * Gets post list.
     *
     * @param context the context
     * @return the post list
     */
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
            if (WifiReceiver.isNetworkAvailable(context)) {
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

    /**
     * Gets post list queue.
     *
     * @param context the context
     * @return the post list queue
     */
    static public PostList getPostListQueue(Context context) {
        if (postListQueue == null) {
            postListQueue = new PostList();
        }

        PostListOfflineController.loadOfflinePosts("queueOffline", postListQueue, context);
        return postListQueue;
    }

    /**
     * Gets post list update.
     *
     * @param context the context
     * @return the post list update
     */
    static public PostList getPostListUpdate(Context context) {
        if (postListUpdate == null) {
            postListUpdate = new PostList();
        }

        PostListOfflineController.loadOfflinePosts("updateOffline", postListUpdate, context);
        return postListUpdate;
    }

    /**
     * Gets post list delete.
     *
     * @param context the context
     * @return the post list delete
     */
    static public PostList getPostListDelete(Context context) {
        if (postListDelete == null) {
            postListDelete = new PostList();
        }

        PostListOfflineController.loadOfflinePosts("deleteOffline", postListDelete, context);
        return postListDelete;
    }

    /**
     * Clear post list queue.
     *
     * @param context the context
     */
    static public void clearPostListQueue(Context context) {
        postListQueue.getPosts().clear();
        PostListOfflineController.saveOfflinePosts("queueOffline", postListQueue, context);
    }

    /**
     * Clear post list update.
     *
     * @param context the context
     */
    static public void clearPostListUpdate(Context context) {
        postListUpdate.getPosts().clear();
        PostListOfflineController.saveOfflinePosts("updateOffline", postListUpdate, context);
    }

    /**
     * Clear post list delete.
     *
     * @param context the context
     */
    static public void clearPostListDelete(Context context) {
        postListDelete.getPosts().clear();
        PostListOfflineController.saveOfflinePosts("deleteOffline", postListDelete, context);
    }

    /**
     * Add offline post.
     *
     * @param post    the post
     * @param context the context of the activity
     */
    public static void addPost(Post post, Context context) {
        /**
         * Get the post list and add the post to elastic search (if online) and save to offline file
         * @see #getPostList(Context)
         * @see #saveOfflinePosts(Context)
         * @see PostList
         */
        if (WifiReceiver.isNetworkAvailable(context)) {
            PostListOnlineController.AddPostsTask addOnlinePost = new PostListOnlineController.AddPostsTask();
            addOnlinePost.execute(post);
        }

        else {
            getPostListQueue(context).addPost(post);
            PostListOfflineController.saveOfflinePosts("queueOffline", postListQueue, context);
        }

        // Double check this part
        getPostList(context).addPost(post);
        PostListOfflineController.saveOfflinePosts("mainOffline", postListMain, context);
    }

    /**
     * Update posts.
     *
     * @param post    the post
     * @param context the context
     */
    public static void updatePosts(Post post, Context context) {
        if (WifiReceiver.isNetworkAvailable(context)) {
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

    /**
     * Update main offline posts.
     *
     * @param context the context
     */
    public static void updateMainOfflinePosts(Context context) {

        // Get updated information for postListMain, not required for
        postListMain.getPosts();
        PostListOfflineController.saveOfflinePosts("mainOffline", postListMain, context);
//        PostListOfflineController.saveOfflinePosts("queueOffline", postListQueue, context);
    }

    /**
     * Delete posts.
     *
     * @param post    the post
     * @param context the context
     */
    public static void deletePosts(Post post, Context context) {
        try {
            if (WifiReceiver.isNetworkAvailable(context)) {
//                PostListOnlineController.DeletePostsTask upt = new PostListOnlineController.DeletePostsTask();
//                upt.execute(post);
//                upt.get();
                PostListOnlineController.DeletePostsTaskId deletePostsTaskid = new PostListOnlineController.DeletePostsTaskId();
                deletePostsTaskid.execute(post.getId());
                deletePostsTaskid.get();
            }

            else {
                // If postListQueue contains the post, then the post was made offline, so delete from
                // postListMain
                Boolean offlinePostMade = false;

                for (Post p : postListQueue.getPosts()) {
                    if (p.getStartLocation().equals(post.getStartLocation()) &&
                            p.getEndLocation().equals(post.getEndLocation()) &&
                            p.getFare().equals(post.getFare())) {
                        offlinePostMade = true;
                        postListQueue.deletePost(p);
                        break;
                    }
                }
                PostListOfflineController.saveOfflinePosts("queueOffline", postListQueue, context);

                if (!offlinePostMade) {
                    postListDelete.addPost(post);
                    PostListOfflineController.saveOfflinePosts("deleteOffline", postListDelete, context);
                }

                postListMain.deletePost(post);
                PostListOfflineController.saveOfflinePosts("mainOffline", postListMain, context);

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
