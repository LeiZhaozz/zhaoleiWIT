package controllers;

//---------------------------------------------------------------------------
//This class:
//  1. Declares and starts the Javalin server listening on local host 7000 (i.e. app).
//  2. Starts our PacemakerRestService (i.e. service). 
//  3. Finally, defines a series of end-points on the server, for our service.  
//     Each end point contains the URL mapped to the service method call.  The
//     endpoints are handled in the order they are defined in.
//
//---------------------------------------------------------------------------

import io.javalin.Javalin;

public class RestMain {

	public static void main(String[] args) throws Exception {
		Javalin app = Javalin.create();
		app.port(getAssignedPort());
		app.start();
		PacemakerRestService service = new PacemakerRestService();
		configRoutes(app, service);
	}

	public static void configRoutes(Javalin app, PacemakerRestService service) {
		//------------------------------------------------------------------------
		// https://javalin.io/documentation#getting-started
		// Javalin has a three main handler types: before-, endpoint-, 
		// and after-handlers.  These handlers require three parts:
		//	     - A verb, ex: before, get, post, put, delete, after
		//	     - A path, ex: /, /hello-world
		//	     - A handler implementation ctx -> { ... }
		//------------------------------------------------------------------------

		app.get("/users", ctx -> {
			service.listUsers(ctx);
		});

		app.post("/users", ctx -> {
			service.createUser(ctx);
		});

		app.get("/users/:id", ctx -> {
			service.listUser(ctx);
		});

		app.get("/users/:id/activities", ctx -> {
			service.getActivities(ctx);
		});

		app.post("/users/:id/activities", ctx -> {
			service.createActivity(ctx);
		});	    

		app.get("/users/:id/activities/:activityid", ctx -> {
			service.getActivity(ctx);
		});

		app.get("/users/:id/activities/:activityid/locations", ctx -> {
			service.getActivityLocations(ctx);
		});

		app.post("/users/:id/activities/:activityid/locations", ctx -> {
			service.addLocation(ctx);
		});

		app.delete("/users", ctx -> {
			service.deleteUsers(ctx);
		});

		app.delete("/users/:id", ctx -> {
			service.deleteUser(ctx);
		});

		app.delete("/users/:id/activities", ctx -> {
			service.deleteActivities(ctx);
		});
	}

	private static int getAssignedPort() {
		ProcessBuilder processBuilder = new ProcessBuilder();
		if (processBuilder.environment().get("PORT") != null) {
			return Integer.parseInt(processBuilder.environment().get("PORT"));
		}
		return 7000;
	}


}