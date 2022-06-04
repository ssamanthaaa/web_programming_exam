# What A Trip

* components:
	* DrawableMap.vue -> map on which route and marker can be added, only one route for trip and the marker can be put only on the route or in a range of 100 meters from it. Used in the UpdateTrip.vue page
	* Map.vue -> map on which the trips are displayed in the Dashboard.vue page
	* Header.vue
* views
	* About.vue -> about page where there are the assignment of the exam, the technologies used, a small video tutorial and the future ideas for improvement
	* Dashboard.vue -> home page in which there are a map with all trips displayed on it and also the informations of them are showed in a table. There is the possibility to filter the trips based on their date and also based on a range of date. The table can be reorder based on date, name of the place or transportation. By clicking on the pencil the trip can be updated. 
	* ErrorPage.vue -> page showed when the url written is wrong
	* LoginPage.vue -> login/registration page
	* UpdateTrip.vue -> page for creation or update a trip.
	* Welcome.vue -> welcome page displayed after successfully logging in.
* services (all request are done using axios)
	* UserService.js
		* POST createUser() -> for registering a new user
		* POST authenticateUser() -> for logging in  a user
		* logout() -> for removing all the things saved in local storage
	* TripService.js -> for the requests 
		* GET getAllTrips() for requesting all the trips of a user
		* GET getTrip() for requesting a specific trip of a user
		* POST createTrip() for creating a trip
		* PUT updateTrip() for updating a triip
		* DELETE deleteTrip() for deleting a trip


## Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Lints and fixes files
```
npm run lint
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).
