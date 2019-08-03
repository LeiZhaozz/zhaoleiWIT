package controllers;

import io.javalin.Context;
import models.Activity;
import models.Location;
import models.User;
import static models.Fixtures.users;

public class PacemakerRestService {

	PacemakerAPI pacemaker = new PacemakerAPI();

	PacemakerRestService() {
		users.forEach(
				user -> pacemaker.createUser(user.firstname, user.lastname, user.email, user.password));
	}

	public void listUsers(Context ctx) {
		// serialize object and set as result
		ctx.json(pacemaker.getUsers());
		System.out.println("list users requested");
	}

	public void createUser(Context ctx) {
		// convert json body to object 
		User user = ctx.bodyAsClass(User.class);
		User newUser = pacemaker
				.createUser(user.firstname, user.lastname, user.email, user.password);
		// serialize object and set as result
		ctx.json(newUser);
	}

	public void listUser(Context ctx) {
		// get a path-parameter
		String id = ctx.pathParam("id");
		// serialize object and set as result
		ctx.json(pacemaker.getUser(id));
	}

	public void getActivities(Context ctx) {
		String id = ctx.pathParam("id");    // get a path-parameter
		User user = pacemaker.getUser(id);
		if (user != null) {
			ctx.json(user.activities.values());  // serialize object and set as result
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
			ctx.json(newActivity);
		} else {
			ctx.status(404);  // set response status (not found)
		}
	}

	public void getActivity(Context ctx) {
  	    String id = ctx.pathParam("activityid");  
		Activity activity = pacemaker.getActivity(id);
		if (activity != null) {
			ctx.json(activity);
		} else {
			ctx.status(404);
		}
	}	

	public void deleteActivities(Context ctx) {
		String id = ctx.pathParam("id");
		pacemaker.deleteActivities(id);
		ctx.json(204);
	}

	public void getActivityLocations(Context ctx) {
		String id = ctx.pathParam("activityid");
		Activity activity = pacemaker.getActivity(id);
		if (activity != null) {
			ctx.json(activity.route);
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
			ctx.json(location);
		} else {
			ctx.status(404);
		}
	}

	public void deleteUser(Context ctx) {
		String id = ctx.pathParam("id");
		ctx.json(pacemaker.deleteUser(id));
	}

	public void deleteUsers(Context ctx) {
		pacemaker.deleteUsers();
		ctx.json(204);   //Successfully fulfilled request & no content to display
	}
}