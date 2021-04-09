/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.LikedItemEntity;
import entity.UserEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
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
@Stateless
public class UserEntitySessionBean implements UserEntitySessionBeanLocal {

    @PersistenceContext(unitName = "MySalesMatter-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    @EJB
    private ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;

    @EJB
    private SalesTransactionEntitySessionBeanLocal transationEntitySessionBeanLocal;

    @EJB
    private OfferEntitySessionBeanLocal offerEntitySessionBeanLocal;

    public UserEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public UserEntity registerUser(UserEntity newUser) throws InputDataValidationException, UnknownPersistenceException, UserEmailExistsException {
        try {
            Set<ConstraintViolation<UserEntity>> constraintViolations = validator.validate(newUser);

            if (constraintViolations.isEmpty()) {
                em.persist(newUser);
                em.flush();

                return newUser;
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new UserEmailExistsException();
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    @Override
    public UserEntity userLogin(String email, String password) throws InvalidLoginCredentialException {
        try {
            UserEntity user = retrieveUserByEmail(email);

            if (user.getPassword().equals(password)) {
                user.getTransactions().size();
                return user;
            } else {
                throw new InvalidLoginCredentialException("Email does not exist or invalid password!");
            }
        } catch (UserNotFoundException ex) {
            throw new InvalidLoginCredentialException("Email does not exist or invalid password!");
        }
    }

    @Override
    public UserEntity retrieveUserByEmail(String email) throws UserNotFoundException {
        Query query = em.createQuery("SELECT u FROM UserEntity u WHERE u.email =:inEmail");
        query.setParameter("inEmail", email);

        try {
            return (UserEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new UserNotFoundException("User with email:" + email + " does not exist");
        }
    }

    @Override
    public UserEntity retrieveUserById(Long id) throws UserNotFoundException {
        UserEntity user = em.find(UserEntity.class, id);
        if (user != null) {
            user.getTransactions().size();
            //user.getConversationsAsOfferee().size();
            //user.getConversationsAsOfferer().size();
            user.getLikedItems().size();
            user.getListings().size();
            user.getOffers().size();
            user.getReviews().size();
            return user;
        } else {
            throw new UserNotFoundException("User id " + id + " does not exists");
        }
    }

    @Override
    public List<UserEntity> retrieveAllUsers() {
        Query query = em.createQuery("SELECT u FROM UserEntity u");

        return query.getResultList();
    }

    @Override
    public void updateUser(UserEntity userEntity) throws UserNotFoundException, UpdateUserException, InputDataValidationException {
        if (userEntity != null && userEntity.getUserId() != null) {
            Set<ConstraintViolation<UserEntity>> constraintViolations = validator.validate(userEntity);

            if (constraintViolations.isEmpty()) {
                UserEntity generalUserToUpdate = retrieveUserById(userEntity.getUserId());

                if (generalUserToUpdate.getUsername().equals(userEntity.getUsername())) {
                    generalUserToUpdate.setName(userEntity.getName());
                } else {
                    throw new UpdateUserException("Username of user record to be updated does not match the existing record");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new UserNotFoundException("User ID not provided for user to be updated");
        }
    }

    @Override
    public void deleteUser(Long userId) throws UserNotFoundException, DeleteUserException {
        UserEntity userToRemove = retrieveUserById(userId);

        if (userToRemove.getTransactions().isEmpty()) {
            em.remove(userToRemove);
        } else {
            throw new DeleteUserException("User ID " + userId + " is associated with existing transaction(s) and cannot be deleted!");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<UserEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
