# web_programming_exam
## What A Trip

Dependencies for the REST web service are:
* Jersey2 (Glassfish)
* (Jackson) JAX-RS
* Jose4j JWT implementation

# 
* src/main/java
	* it/units/rest
		* database
			* SQLConnection.java -> for connecting to te mySQL database
			* dao
				* SqlTripDAO.java -> method for getting trip information from the db
				* SqlUserDAO.java -> method for getting the user information from the db
				* TripDAO.java -> interface implemented by SqlTripDAO.java
				* UserDAO.java -> interface implemented by SqlUserDAO.java
		* exception
			* TripNotFoundException.java
			* UserNotFoundException.java
			* UserExistingException.java
		* filter
			* AuthenticationFilter.java -> class called for every request to the server
		* model
		* restapi
			* TripRestService.java -> all methods here require authentication 
			* UserRestService.java -> authentication/creation/deletion of user 
* webapp/ -> folder on which the  webapp is loaded 