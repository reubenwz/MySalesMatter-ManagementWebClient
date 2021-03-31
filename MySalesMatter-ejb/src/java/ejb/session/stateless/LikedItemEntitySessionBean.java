/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.LikedItemEntity;
import entity.ListingEntity;
import entity.UserEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;
import util.exception.ItemNotLikedException;
import util.exception.LikedItemEntityNotFoundException;
import util.exception.ListingNotFoundException;
import util.exception.UserNotFoundException;

/**
 *
 * @author sylvia
 */
@Stateless
public class LikedItemEntitySessionBean implements LikedItemEntitySessionBeanLocal {

    @PersistenceContext(unitName = "MySalesMatter-ejbPU")
    private EntityManager em;

    @EJB
    private UserEntitySessionBeanLocal userEntitySessionBeanLocal;

    @EJB
    private ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public LikedItemEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public List<LikedItemEntity> getLikedItems(Long userId) throws UserNotFoundException {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.retrieveUserById(userId);

            Query query = em.createQuery("SELECT l FROM LikedItemEntity l WHERE l.user.userId = :inUserId");
            query.setParameter("inUserId", userId);
            List<LikedItemEntity> list = query.getResultList();
            for (LikedItemEntity l : list) {
                l.getListing();
                l.getUser();
            }
            return list;
        } catch (UserNotFoundException ex) {
            throw new UserNotFoundException("User with this id, " + userId + ", does not exist!");
        }

    }

    @Override
    public LikedItemEntity createNewLikedItem(Long userId, Long listingId) throws InputDataValidationException, UserNotFoundException, ListingNotFoundException {
        try {
            LikedItemEntity liked = new LikedItemEntity();
            UserEntity user = userEntitySessionBeanLocal.retrieveUserById(userId);
            liked.setUser(user);
            List<LikedItemEntity> likedItems = getLikedItems(userId);
            boolean alreadyLiked = false;
            if (likedItems != null) {
                for (LikedItemEntity l : likedItems) {
                    if (l.getListing().getListingId().equals(listingId)) {
                        alreadyLiked = true;
                    }
                }
            }

            if (!alreadyLiked) {
                ListingEntity listing = listingEntitySessionBeanLocal.retrieveListingByListingId(listingId);
                liked.setListing(listing);
                listing.setLikes(listing.getLikes() + 1);
                user.getLikedItems().add(liked);
                Set<ConstraintViolation<LikedItemEntity>> constraintViolations = validator.validate(liked);

                if (constraintViolations.isEmpty()) {
                    em.merge(listing);
                    em.persist(liked);
                    em.merge(user);
                    em.flush();
                    return liked;
                } else {
                    throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
                }
            }
        } catch (UserNotFoundException | ListingNotFoundException ex) {
            throw new UserNotFoundException("User does not exists!");
        }

        return null;
    }
    
    @Override
    public void unlikeItem(Long userId, Long listingId) throws UserNotFoundException, ListingNotFoundException, ItemNotLikedException {
        try {
            UserEntity user = userEntitySessionBeanLocal.retrieveUserById(userId);
            List<LikedItemEntity> likedItems = user.getLikedItems();
            boolean found = false;
            LikedItemEntity liked = new LikedItemEntity();
            for (LikedItemEntity l : likedItems) {
                if (l.getListing().getListingId().equals(listingId)) {
                    found = true;
                    liked = l;
                }
            }
            System.out.println("is it found: " + found);
            if (found) {
                ListingEntity listing = listingEntitySessionBeanLocal.retrieveListingByListingId(listingId);
                listing.setLikes(listing.getLikes()-1);
                em.merge(listing);
                user.getLikedItems().remove(liked);
                em.merge(user);
                em.remove(liked);
            }
            

        } catch (UserNotFoundException ex) {
            throw new UserNotFoundException("User does not exists!");
        } catch (ListingNotFoundException ex) {
            throw new ListingNotFoundException("Listing does not exists!");
        }
    }
    
    @Override
    public LikedItemEntity retrieveLikedItemEntityById(Long id) throws LikedItemEntityNotFoundException {
        LikedItemEntity likedItemEntity = em.find(LikedItemEntity.class, id);
        if (likedItemEntity != null) {
            return likedItemEntity;
        } else {
            throw new LikedItemEntityNotFoundException("Liked item not found!");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<LikedItemEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
