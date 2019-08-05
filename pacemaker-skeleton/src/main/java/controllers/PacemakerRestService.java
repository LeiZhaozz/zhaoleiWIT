package controllers;

import com.google.gson.Gson;
import io.javalin.http.Context;
import kotlin.jvm.internal.Intrinsics;
import models.Activity;
import models.Location;
import models.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static models.Fixtures.users;

public class PacemakerRestService {

	PacemakerAPI pacemaker = new PacemakerAPI();

	PacemakerRestService() {
		users.forEach(
				user -> pacemaker.createUser(user.firstname, user.lastname, user.email, user.password));
	}

	public void listUsers(Context ctx) {
		// serialize object and set as result
        ctxJSON(pacemaker.getUsers(), ctx);
		System.out.println("list users requested");
	}

	public void createUser(Context ctx) {
		// convert json body to object 
		User user = ctx.bodyAsClass(User.class);
		User newUser = pacemaker
				.createUser(user.firstname, user.lastname, user.email, user.password);
		// serialize object and set as result
        ctxJSON(newUser, ctx);
	}

	public void listUser(Context ctx) {
		// get a path-parameter
		String id = ctx.pathParam("id");
		// serialize object and set as result
        ctxJSON(pacemaker.getUser(id), ctx);
	}

	public void getActivities(Context ctx) {
		String id = ctx.pathParam("id");    // get a path-parameter
		User user = pacemaker.getUser(id);
		if (user != null) {
            ctxJSON(user.activities.values(), ctx);  // serialize object and set as result
		} else {
			ctx.status(404);   // set response status
		}
	}

	public void createActivity(Context ctx) {
		String id = ctx.pathParam("id");   // get a path-parameter
		User user = pacemaker.getUser(id);
		if (user != null) {
			// convert json body to object 
			Activity activity = ctx.bodyAsClass(Activity.class);
			Activity newActivity = pacemaker
                    .createActivity(id, activity.type, activity.location, activity.distance);
			// serialize object and set as result
            //there is bug of Javalin about json  when the type of  Properties of Object is Object and null;
            //because the null will be handled as "".when covert json to Object,
            // there will be  an error because "" is not an object
            //JavalinJson.toJson(obj) is too weak, this method was changed by Gson here.
            ctxJSON(newActivity, ctx);

		} else {
			ctx.status(404);  // set response status (not found)
		}
	}

	public void getActivity(Context ctx) {
  	    String id = ctx.pathParam("activityid");  
		Activity activity = pacemaker.getActivity(id);
		if (activity != null) {
            ctxJSON(activity, ctx);
		} else {
			ctx.status(404);
		}
	}	

	public void deleteActivities(Context ctx) {
		String id = ctx.pathParam("id");
		pacemaker.deleteActivities(id);
        ctxJSON(204, ctx);
	}

	public void getActivityLocations(Context ctx) {
		String id = ctx.pathParam("activityid");
		Activity activity = pacemaker.getActivity(id);
		if (activity != null) {
            ctxJSON(activity.route, ctx);
		} else {
			ctx.status(404);
		}
	}

	public void addLocation(Context ctx) {
		String id = ctx.pathParam("activityid");
		Activity activity = pacemaker.getActivity(id);
		if (activity != null) {
			Location location = ctx.bodyAsClass(Location.class);
			activity.route.add(location);
            ctxJSON(location, ctx);
		} else {
			ctx.status(404);
		}
	}

	public void deleteUser(Context ctx) {
		String id = ctx.pathParam("id");
        ctxJSON(pacemaker.deleteUser(id), ctx);
	}

	public void deleteUsers(Context ctx) {
		pacemaker.deleteUsers();
        ctxJSON("204", ctx);   //Successfully fulfilled request & no content to display
    }

    public void userLogin(Context ctx) {
        // convert json body to object
        User user = ctx.bodyAsClass(User.class);
        Collection<User> users = pacemaker.getUsers();
        users.stream().forEach((userOne) -> {
            if (user.email.equals(userOne.email)) {
                if (user.password.equals(userOne.password)) {
                    ctxJSON(userOne, ctx);

                } else {
                    ctxJSON(null, ctx);
                }
            }

        });

    }

    public void addFriend(Context ctx) {
        String id = ctx.pathParam("id");   // get a path-parameter
        String emailFriend = ctx.pathParam("emailFriend");
        String friend = pacemaker.addFriend(id, emailFriend);
        ctxJSON("add friend:" + friend + " successfully ", ctx);
    }

    public void listFriend(Context ctx) {
        String id = ctx.pathParam("id");   // get a path-parameter
        List<String> friends = pacemaker.listFriend(id);
        ctxJSON(friends, ctx);
    }

    public void getActivitiesByEmail(Context ctx) {
        String email = ctx.pathParam("email");    // get a path-parameter
        List<Activity> activities = pacemaker.getActivitiesByEmail(email);
        ctxJSON(activities, ctx);  // serialize object and set as result
    }

    private void ctxJSON(Object object, Context ctx) {
        Intrinsics.checkParameterIsNotNull(object, "obj");
        Gson gson = new Gson();
        String result = gson.toJson(object);
        ctx.contentType("application/json").result(result);
    }


}