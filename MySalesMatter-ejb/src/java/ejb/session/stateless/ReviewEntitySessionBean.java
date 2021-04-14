/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ListingEntity;
import entity.ReviewEntity;
import entity.SalesTransactionEntity;
import entity.UserEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CreateNewReviewException;
import util.exception.InputDataValidationException;
import util.exception.ListingNotFoundException;
import util.exception.ReviewNotFoundException;
import util.exception.SalesTransactionNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateReviewException;
import util.exception.UserNotFoundException;

/**
 *
 * @author reuben
 */
@Stateless
public class ReviewEntitySessionBean implements ReviewEntitySessionBeanLocal {

    @PersistenceContext(unitName = "MySalesMatter-ejbPU")
    private EntityManager entityManager;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    @EJB
    UserEntitySessionBeanLocal userEntitySessionBeanLocal;

    @EJB
    ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;
    
    @EJB
    SalesTransactionEntitySessionBeanLocal salesTransactionEntitySessionBeanLocal;

    public ReviewEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public ReviewEntity createNewReviewEntity(ReviewEntity newReviewEntity, Long reviewerId, Long listingId, Long salesId) throws UnknownPersistenceException, InputDataValidationException, CreateNewReviewException, UserNotFoundException, ListingNotFoundException, SalesTransactionNotFoundException {
        Set<ConstraintViolation<ReviewEntity>> constraintViolations = validator.validate(newReviewEntity);

        if (constraintViolations.isEmpty()) {
            try {
                ListingEntity lisitng = listingEntitySessionBeanLocal.retrieveListingByListingId(listingId);
                UserEntity user = userEntitySessionBeanLocal.retrieveUserById(reviewerId);
                SalesTransactionEntity s = salesTransactionEntitySessionBeanLocal.retrieveTransactionById(salesId);
                s.setReviewed(true);
                newReviewEntity.setReviewer(user);
                newReviewEntity.setListing(lisitng);
                user.getReviews().add(newReviewEntity);
                lisitng.getReviews().add(newReviewEntity);
                entityManager.persist(newReviewEntity);
                entityManager.merge(user);
                entityManager.merge(lisitng);
                entityManager.flush();
                return newReviewEntity;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    throw new UnknownPersistenceException(ex.getMessage());
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }

            } catch (UserNotFoundException | ListingNotFoundException | SalesTransactionNotFoundException ex) {
                throw new CreateNewReviewException("An error has occurred while creating the new offer: " + ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    // For getting reviews written
    @Override
    public List<ReviewEntity> getReviewsByUserId(Long userId) throws UserNotFoundException {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.retrieveUserById(userId);

            Query query = entityManager.createQuery("SELECT l FROM ReviewEntity l WHERE l.reviewer.userId = :inUserId");
            query.setParameter("inUserId", userId);
            List<ReviewEntity> list = query.getResultList();
            for (ReviewEntity l : list) {
                l.getListing();
                l.getReviewer();
            }
            return list;
        } catch (UserNotFoundException ex) {
            throw new UserNotFoundException("User with this id, " + userId + ", does not exist!");
        }

    }

    // For getting reviews received
    @Override
    public List<ReviewEntity> getReviewsReceivedByUserId(Long userId) throws UserNotFoundException {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.retrieveUserById(userId);

            List<ListingEntity> listings = listingEntitySessionBeanLocal.retrieveListingsByUser(userId);
            List<ReviewEntity> reviews = new ArrayList<>();

            if (!listings.isEmpty()) {
                for (ListingEntity l : listings) {
                    for (ReviewEntity r: l.getReviews()) {
                        r.getListing();
                        r.getReviewer();
                        reviews.add(r);
                    }
                }
                return reviews;
            }
            return reviews;
        } catch (UserNotFoundException ex) {
            throw new UserNotFoundException("User with this id, " + userId + ", does not exist!");
        }

    }

    @Override
    public List<ReviewEntity> retrieveAllReviews() {
        Query query = entityManager.createQuery("SELECT r FROM ReviewEntity r ORDER BY r.reviewId ASC");
        List<ReviewEntity> reviewEntities = query.getResultList();

        return reviewEntities;
    }

    @Override
    public ReviewEntity retrieveReviewByReviewId(Long reviewId) throws ReviewNotFoundException {
        ReviewEntity reviewEntity = entityManager.find(ReviewEntity.class, reviewId);

        if (reviewEntity != null) {
            return reviewEntity;
        } else {
            throw new ReviewNotFoundException("Review ID " + reviewId + " does not exist!");
        }
    }

    @Override
    public void updateReview(Long reviewId, int starRating, String desc, String pictPath) throws InputDataValidationException, ReviewNotFoundException, UpdateReviewException {
        try {
            ReviewEntity reviewEntityToUpdate = retrieveReviewByReviewId(reviewId);

            reviewEntityToUpdate.setStarRating(starRating);
            reviewEntityToUpdate.setDescription(desc);
            reviewEntityToUpdate.setPicturePaths(pictPath);
        } catch (Exception ex) {
            throw new ReviewNotFoundException("Review ID not provided for review to be updated");
        }
    }

    @Override
    public void deleteReview(Long reviewId) throws ReviewNotFoundException {
        ReviewEntity reviewEntityToRemove = retrieveReviewByReviewId(reviewId);
        UserEntity user = reviewEntityToRemove.getReviewer();
        ListingEntity listing = reviewEntityToRemove.getListing();
        user.getReviews().remove(reviewEntityToRemove);
        listing.getReviews().remove(reviewEntityToRemove);
        entityManager.merge(user);
        entityManager.merge(listing);
        entityManager.remove(reviewEntityToRemove);

    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ReviewEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
