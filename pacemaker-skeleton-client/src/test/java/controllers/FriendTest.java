package controllers;

import models.Activity;
import models.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static models.Fixtures.users;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FriendTest {

    //PacemakerAPI pacemaker = new PacemakerAPI("https://evening-tor-94050.herokuapp.com/");
    PacemakerAPI pacemaker = new PacemakerAPI("http://localhost:7000");
    User homer = new User("homer", "simpson", "homer@simpson.com", "secret");

    @Before
    public void setup() {
        pacemaker.deleteUsers();
        homer = pacemaker.createUser(homer.firstname, homer.lastname, homer.email, homer.password);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAddFriend() {
        String emailFriend = "maggie@simpson.com";
        String result = pacemaker.addFriend(homer.id, emailFriend);

    }

    @Test
    public void testListFriend() {
        String emailFriend = "maggie@simpson.com";
        String result = pacemaker.addFriend(homer.id, emailFriend);
        List<String> friends = pacemaker.listFriend(homer.id);
//        System.out.println(friends.get(0));
        assertEquals(emailFriend, friends.get(0));

    }

    @Test
    public void testFriendActivityReport() {
        String emailFriend = homer.email;
        Activity activity = new Activity("run", "fridge", 0.5);
        Activity returnedActivity = pacemaker.createActivity(homer.id, activity.type, activity.location, activity.distance);
        List<Activity> except = new ArrayList<>();
        except.add(returnedActivity);
        List<Activity> result = pacemaker.friendActivityReport(emailFriend);
        assertEquals(except, result);

    }


}
