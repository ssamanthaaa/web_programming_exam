package it.units.rest.database.dao;


import java.util.List;

import it.units.rest.exception.UserExistingException;
import it.units.rest.exception.UserNotFoundException;
import it.units.rest.model.User;
import it.units.rest.model.UserSecurity;

public interface UserDAO {
	public boolean createUser( UserSecurity user ) throws UserExistingException;
	
	public Integer getUserIdByUsername( String username ) throws UserNotFoundException;
	public Integer getUserIdByEmail(String email) throws UserNotFoundException;
	public String getUserUsernameById( Integer id ) throws UserNotFoundException;
	public User getUser( Integer id ) throws UserNotFoundException;
	
	public List<User> getAllUsers();
	
	public UserSecurity getUserAuthentication( Integer id ) throws UserNotFoundException;
	public boolean setUserAuthentication( UserSecurity user ) throws UserNotFoundException;
	
	public boolean updateUser( User user ) throws UserNotFoundException;
	public boolean deleteUser( Integer id ) throws UserNotFoundException;
}