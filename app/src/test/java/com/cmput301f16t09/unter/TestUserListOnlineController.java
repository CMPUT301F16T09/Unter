//package com.cmput301f16t09.unter;
//
//import android.app.Activity;
//import android.test.ActivityInstrumentationTestCase2;
//import android.util.Log;
//import android.widget.EditText;
//
//
//import java.util.ArrayList;
//import com.robotium.solo.Solo;
//
//public class TestUserListOnlineController extends ActivityInstrumentationTestCase2<MainGUIActivity> {
//
////    public void testRetrieveUsers() {
////        try {
////            UserListOnlineController.GetUsersTask getUsersTask = new UserListOnlineController.GetUsersTask();
////            getUsersTask.execute("");
////            ArrayList<User> userlist = getUsersTask.get();
////        }
////        catch (Exception e) {
////            fail("Couldn't obtain from elastic server.");
////        }
////    }
//    private Solo solo;
//
//    public TestUserListOnlineController() {
//        super(com.cmput301f16t09.unter.MainGUIActivity.class);
//    }
//
//    public void testStart() throws Exception {
//        Activity activity = getActivity();
//    }
//
//    public void setUp() throws Exception{
//        Log.d("TAG1", "setUp()");
//        solo = new Solo(getInstrumentation(),getActivity());
//    }
//
//    public void testCreateUser() {
//        solo.assertCurrentActivity("Wrong Activity", MainGUIActivity.class);
//        solo.clickOnButton("Create\\nNew User");
//        solo.assertCurrentActivity("Wrong Activity", CreateNewUserUIActivity.class);
//        solo.enterText((EditText) solo.getView(R.id.editTextSignUpUsername), "Harambe");
//        solo.enterText((EditText) solo.getView(R.id.editTextSignUpName), "Harry Potter");
//        solo.enterText((EditText) solo.getView(R.id.editTextSignUpPhoneNumber), "780-123-4567");
//        solo.enterText((EditText) solo.getView(R.id.editTextSignUpEmail), "one_ring_to_rule_them@all.ca");
//        solo.enterText((EditText) solo.getView(R.id.editTextSignUpPassword), "dumbledore");
//        solo.enterText((EditText) solo.getView(R.id.editTextSignUpConfirmPassword), "dumbledore");
//        solo.clickOnButton("C o n f i r m");
//    }
////    public void testaddUser() {
////        User new_user = new User("Henry", "Popcorn_chicken", "KFC_lover@barnyard.com", "123-456-7890", "password");
////        try {
////            UserListOnlineController.AddUsersTask addUserTask = new UserListOnlineController.AddUsersTask();
////            addUserTask.execute(new_user);
////        }
////        catch (Exception e) {
////            fail("Couldn't add user");
////        }
////        try {
////            UserListOnlineController.GetUsersTask getUsersTask = new UserListOnlineController.GetUsersTask();
////            getUsersTask.execute("");
////            ArrayList<User> userlist = getUsersTask.get();
////            assertTrue(userlist.contains(new_user));
////        }
////        catch (Exception e) {
////            fail("Couldn't obtain from elastic server.");
////        }
////    }
//
////    public void testdeleteUser() {
////        User new_user = new User("Henry", "Popcorn_chicken", "KFC_lover@barnyard.com", "123-456-7890", "password");
////        try {
////            UserListOnlineController.AddUsersTask addUserTask = new UserListOnlineController.AddUsersTask();
////            addUserTask.execute(new_user);
////        }
////        catch (Exception e) {
////            fail("Couldn't add user");
////        }
////        try {
////            UserList userlist = UserListOnlineController.getUserList();
////            UserListOnlineController.DeleteUsersTask deleteUsersTask = new UserListOnlineController.DeleteUsersTask();
////            deleteUsersTask.execute(new_user);
////            assertFalse(userlist.getUserList().contains(new_user));
////        }
////        catch (Exception e) {
////            fail("Couldn't delete user or obtain from server");
////        }
////    }
//
////    public void testsearchUser() {
////        User new_user = new User("Henry", "Popcorn_chicken", "KFC_lover@barnyard.com", "123-456-7890", "password");
////        User new_user2 = new User("Henry2", "Popcorn_chicken", "KFC_lover@barnyard.com", "123-456-7890", "password");
////        User new_user3 = new User("Aaron", "Popcorn_chicken", "KFC_lover@barnyard.com", "123-456-7890", "password");
////        try {
////            UserListOnlineController.SearchUserListsTask searchUserListsTask = new UserListOnlineController.SearchUserListsTask();
////            searchUserListsTask.execute("Henry");
////            ArrayList<User> userlist = searchUserListsTask.get();
////            assertTrue(userlist.contains(new_user));
////            assertTrue(userlist.contains(new_user2));
////            assertFalse(userlist.contains(new_user3));
////        } catch (Exception e) {
////            fail("Couldn't complete search");
////        }
////    }
//
//    @Override
//    public void tearDown() throws Exception{
//        solo.finishOpenedActivities();
//    }
//}
