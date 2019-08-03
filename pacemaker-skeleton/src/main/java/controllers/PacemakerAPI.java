package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;

import models.Activity;
import models.Location;
import models.User;


public class PacemakerAPI {

	private Map<String, User> emailIndex = new HashMap<>();
	private Map<String, User> userIndex = new HashMap<>();
	private Map<String, Activity> activitiesIndex = new HashMap<>();

	public PacemakerAPI() {
	}

	public Collection<User> getUsers() {
		return userIndex.values();
	}

	public void deleteUsers() {
		userIndex.clear();
		emailIndex.clear();
	}

	public User createUser(String firstName, String lastName, String email, String password) {
		User user = new User(firstName, lastName, email, password);
		emailIndex.put(email, user);
		userIndex.put(user.id, user);
		return user;
	}

	public Activity createActivity(String id, String type, String location, double distance) {
		Activity activity = null;
		Optional<User> user = Optional.fromNullable(userIndex.get(id));
		if (user.isPresent()) {
			activity = new Activity(type, location, distance);
			user.get().activities.put(activity.id, activity);
			activitiesIndex.put(activity.id, activity);
		}
		return activity;
	}

	public Activity getActivity(String id) {
		return activitiesIndex.get(id);
	}

	public Collection<Activity> getActivities(String id) {
		Collection<Activity> activities = null;
		Optional<User> user = Optional.fromNullable(userIndex.get(id));
		if (user.isPresent()) {
			activities = user.get().activities.values();
		}
		return activities;
	}

	public List<Activity> listActivities(String userId, String sortBy) {
		List<Activity> activities = new ArrayList<>();
		activities.addAll(userIndex.get(userId).activities.values());
		switch (sortBy) {
		case "type":
			activities.sort((a1, a2) -> a1.type.compareTo(a2.type));
			break;
		case "location":
			activities.sort((a1, a2) -> a1.location.compareTo(a2.location));
			break;
		case "distance":
			activities.sort((a1, a2) -> Double.compare(a1.distance, a2.distance));
			break;
		}
		return activities;
	}

	public void deleteActivities(String id) {
		Optional<User> user = Optional.fromNullable(userIndex.get(id));
		if (user.isPresent()) {
			user.get().activities.values().forEach(activity -> activitiesIndex.remove(activity.getId()));
			user.get().activities.clear();
		}
	}

	public void addLocation(String id, double latitude, double longitude) {
		Optional<Activity> activity = Optional.fromNullable(activitiesIndex.get(id));
		if (activity.isPresent()) {
			activity.get().route.add(new Location(latitude, longitude));
		}
	}

	public User getUserByEmail(String email) {
		return emailIndex.get(email);
	}

	public User getUser(String id) {
		return userIndex.get(id);
	}

	public User deleteUser(String id) {
		User user = userIndex.remove(id);
		return emailIndex.remove(user.email);
	}
}
