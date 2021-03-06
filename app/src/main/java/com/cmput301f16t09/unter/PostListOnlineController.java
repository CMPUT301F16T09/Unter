package com.cmput301f16t09.unter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * The PostList Online controller for elastic search.
 */
public class PostListOnlineController {

    //Static variables for JestDroidClient and holding posts.
    private static JestDroidClient client;
    private static ArrayList<Post> arrayPostList = null;
    private static PostList postList = new PostList();

    /**
     * Gets post list from elastic search.
     *
     * @return the postList
     */
    static public PostList getPostList() {
        if (arrayPostList == null) {

            // GetsPostsTask to obtain posts from elasticsearch
            PostListOnlineController.GetPostsTask getPostsTask = new PostListOnlineController.GetPostsTask();
            getPostsTask.execute("");
            try {
                arrayPostList = getPostsTask.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            postList.setPostList(arrayPostList);
        }
        return postList;
    }

    /**
     * The type Search post lists task.
     * Searches for a matching type and keyword.
     */
    public static class SearchPostListsTask extends AsyncTask<String, Void, ArrayList<Post>> {
        @Override
        protected ArrayList<Post> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<Post> posts = new ArrayList<Post>();

            //Just list top 10000 posts with type and keyword
            String search_string = "{\"from\": 0, \"size\": 10000, \"query\": {\"match_phrase\": {\"" + search_parameters[0] + "\": \"" + search_parameters[1] + "\"}}}";

            //Add Indexing and type
            Search search = new Search.Builder(search_string)
                    .addIndex("t09")
                    .addType("post")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    ArrayList<Post> foundPosts = (ArrayList<Post>) result.getSourceAsObjectList(Post.class);
                    posts.addAll(foundPosts);
                }
                else {
                    Log.i("Error", "The search query failed to find any posts that matched.");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return posts;
        }
    }

    /**
     * The type get all posts from elastic search.
     */
    public static class GetPostsTask extends AsyncTask<String, Void, ArrayList<Post>> {
        @Override
        protected ArrayList<Post> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<Post> posts = new ArrayList<Post>();

            //Add Indexing and type
            Search search = new Search.Builder(search_parameters[0])
                    .addIndex("t09")
                    .addType("post")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    ArrayList<Post> foundPosts = (ArrayList<Post>) result.getSourceAsObjectList(Post.class);
                    posts.addAll(foundPosts);
                }
                else {
                    Log.i("Error", "The search query failed to find any posts that matched.");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return posts;
        }
    }

    /**
     * The type Add posts task to elastic search.
     */
    public static class AddPostsTask extends AsyncTask<Post, Void, Void> {
        @Override
        protected Void doInBackground(Post... posts) {
            verifySettings();

            for (Post post: posts) {
                //Add Indexing and type
                Index index = new Index.Builder(post).index("t09").type("post").build();

                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        //Adds a id if successfully added to elastic search
                        post.setId(result.getId());
                    }
                    else {
                        Log.i("Error", "Elastic search was not able to add the post.");
                    }
                }
                catch (Exception e) {
                    Log.i("Uhoh", "We failed to add a user to elastic search!");
                    e.printStackTrace();
                }
            }

            return null;
        }
    }

    /**
     * The type Update posts task.
     */
    public static class UpdatePostsTask extends AsyncTask<Post, Void, Void> {
        @Override
        protected Void doInBackground(Post... posts) {
            verifySettings();

            for (Post post: posts) {
                //Add Indexing, type and id of post
                Index index = new Index.Builder(post).index("t09").type("post").id(post.getId()).build();

                try {
                    DocumentResult result = client.execute(index);
                    if (!result.isSucceeded()) {
                        Log.i("Error", "Elastic search was not able to update the post.");
                    }
                }
                catch (Exception e) {
                    Log.i("Uhoh", "We failed to add a user to elastic search!");
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    /**
     * The type Delete posts task from elastic search.
     */
    public static class DeletePostsTask extends AsyncTask<Post, Void, Void> {

        @Override
        protected Void doInBackground(Post... posts) {
            verifySettings();

            for (Post post: posts) {
                // Adds index, type and id of post to be deleted.
                Delete index = new Delete.Builder(post.getId()).index("t09").type("post").build();

                try {
                    DocumentResult result = client.execute(index);
                    if (!result.isSucceeded()) {
                        Log.i("Error", "Elastic search was not able to delete the post.");
                    }
                }
                catch (Exception e) {
                    Log.i("Uhoh", "We failed to delete the post from elastic search!");
                    e.printStackTrace();
                }
            }
            return null;
        }
    }


    /**
     * The type Delete posts task from elastic search.
     */
    public static class DeletePostsTaskId extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... posts) {
            verifySettings();

            for (String post: posts) {
                // Adds index, type and id of post to be deleted.
                Delete index = new Delete.Builder(post).index("t09").type("post").build();

                try {
                    DocumentResult result = client.execute(index);
                    if (!result.isSucceeded()) {
                        Log.i("Error", "Elastic search was not able to delete the post.");
                    }
                }
                catch (Exception e) {
                    Log.i("Uhoh", "We failed to delete the post from elastic search!");
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    /**
     * The type Search keyword task.
     */
    public static class SearchKeywordTask extends AsyncTask<String, Void, ArrayList<Post>> {

        @Override
        protected ArrayList<Post> doInBackground(String... search_parameters) {
            verifySettings();
            ArrayList<Post> posts = new ArrayList<Post>();
            ArrayList<String> uniqueposts = new ArrayList<String>();
            ArrayList<String> search_string = new ArrayList<>();
            String search_details = "{\"from\": 0, \"size\": 10000, \"query\": {\"multi_match\": {\"query\": \"" + search_parameters[0] + "\", \"type\": \"best_fields\", \"fields\": [\"user\", \"startAddress\", \"startLocation\", \"endAddress\", \"endLocation\"]}}}";
            search_string.add(search_details);
            String search_location = "{\"from\": 0, \"size\": 10000, \"query\": {\"multi_match\": {\"query\": \"" + search_parameters[0] + "\", \"type\": \"best_fields\", \"fields\": [\"startAddress.mLatitude\", \"startAddress.mLongitude\", \"endLocation.mLatitude\", \"endLocation.mLongitude\", \"fare\"]}}}";
            search_string.add(search_location);

            for (int i = 0; i < 2; i++) {
                //Add Indexing and type{"from": 0, "size": 10000, "query": {"multi_match": {"query": "home", "type": "best_fields", "fields": ["driver_OfferList","startAddress", "startLocation", "endAddress", "endLocation", "fare"]}}}
                Search search = new Search.Builder(search_string.get(i))
                        .addIndex("t09")
                        .addType("post")
                        .build();
                try {
                    SearchResult result = client.execute(search);

                    if (result.isSucceeded()) {
                        ArrayList<Post> foundPosts = (ArrayList<Post>) result.getSourceAsObjectList(Post.class);

                        for (Post p : foundPosts) {
                            if (!uniqueposts.contains(p.getId())) {
                                uniqueposts.add(p.getId());
                                posts.add(p);
                            }
                        }
                    } else {
                        Log.i("Error", "The search query failed to find any posts that matched.");
                    }
                } catch (Exception e) {
                    Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
                }
            }
            return posts;
        }
    }

    /**
     * The type Search post lists range task.
     */
    public static class SearchPostListsRangeTask extends AsyncTask<String, Void, ArrayList<Post>> {

        @Override

        protected ArrayList<Post> doInBackground(String... search_parameters) {
            verifySettings();
            ArrayList<Post> posts = new ArrayList<Post>();

            //Just list top 10000 posts with type and range for greater than or equals
            //and less than or equals
            String search_string = "{\"from\": 0, \"size\": 10000, \"query\": {\"range\": " +
                                    "{\"" + search_parameters[0] + "\": {\"gte\":" + search_parameters[1] + ", \"lte\":" +
                                        search_parameters[2] + "}}}}";
            //Add Indexing and type
            Search search = new Search.Builder(search_string)
                    .addIndex("t09")
                    .addType("post")
                    .build();
            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    ArrayList<Post> foundPosts = (ArrayList<Post>) result.getSourceAsObjectList(Post.class);
                    posts.addAll(foundPosts);
                }
                else {
                    Log.i("Error", "The search query failed to find any posts that matched.");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            return posts;
        }
    }

    public static class ProcessOfflineData extends AsyncTask<Object, Void, Post> {
        Geocoder coder;

        @Override
        protected Post doInBackground(Object... offlineParameters) {
            verifySettings();
            Context context = (Context) offlineParameters[0];
            coder = new Geocoder(context, Locale.getDefault());
            Post post = (Post) offlineParameters[1];
            return reverseGeocodeForOfflinePost(post, context);

        }

        public Post reverseGeocodeForOfflinePost(Post post, Context context){

            Post onlinePost = post;

            try {
                List<Address> start = coder.getFromLocation(post.getStartLocation().getLatitude(),
                        post.getStartLocation().getLongitude(), 1);

                List<Address> end = coder.getFromLocation(post.getEndLocation().getLatitude(),
                        post.getEndLocation().getLongitude(), 1);

                onlinePost.setStartAddress(start.get(0).getAddressLine(0) + ", " + start.get(0).getAddressLine(1));
                onlinePost.setEndAddress(end.get(0).getAddressLine(0) + ", " + end.get(0).getAddressLine(1));

                onlinePost.setFareKM(onlinePost.getFare()/(distanceCalculator(onlinePost.getStartLocation(), onlinePost.getEndLocation(), context)/1000));
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("IOException", "IOException was caught");
            }

            return onlinePost;
        }

        double distanceCalculator(GeoPoint startp, GeoPoint endp, Context context) {

            double distance = 0.0;
            ArrayList<GeoPoint> wayPoints = new ArrayList<>();
            wayPoints.add(startp);
            wayPoints.add(endp);
            RoadManager roadManager = new OSRMRoadManager(context);

            Road road = roadManager.getRoad(wayPoints);
            Polyline roadPolyline = RoadManager.buildRoadOverlay(road);
            List<GeoPoint> roadSegment = roadPolyline.getPoints();

            for (int j = 0; j < roadSegment.size() - 1; j += 1) {
                GeoPoint geoStart = roadSegment.get(j);
                GeoPoint geoEnd = roadSegment.get(j + 1);

                Location start = new Location("start");
                start.setLatitude(geoStart.getLatitude());
                start.setLongitude(geoStart.getLongitude());

                Location end = new Location("end");
                end.setLatitude(geoEnd.getLatitude());
                end.setLongitude(geoEnd.getLongitude());

                distance += start.distanceTo(end);
            }

            return distance;
        }
    }

    /**
     * Verifies settings of the elastic search server
     */
    private static void verifySettings() {
        // if the client hasn't been initialized then we should make it!
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}
