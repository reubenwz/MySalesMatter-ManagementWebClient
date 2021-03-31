/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.UserEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.DeleteUserException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateUserException;
import util.exception.UserEmailExistsException;
import util.exception.UserNotFoundException;

/**
 *
 * @author Yuki
 */
@Local
public interface UserEntitySessionBeanLocal {
    UserEntity registerUser(UserEntity newUser) throws InputDataValidationException, UnknownPersistenceException, UserEmailExistsException;
    UserEntity userLogin(String email, String password) throws InvalidLoginCredentialException;
    UserEntity retrieveUserByEmail(String email) throws UserNotFoundException;
    UserEntity retrieveUserById(Long id) throws UserNotFoundException;
    List<UserEntity> retrieveAllUsers();
    void updateUser(UserEntity generalUserEntity) throws UserNotFoundException, UpdateUserException, InputDataValidationException;
    void deleteUser(Long userId) throws UserNotFoundException, DeleteUserException;
}
