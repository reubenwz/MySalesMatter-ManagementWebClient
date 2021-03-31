/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ListingEntity;
import entity.ReviewEntity;
import entity.UserEntity;
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
import util.exception.ReviewNotFoundException;
import util.exception.UpdateReviewException;

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
    
    
    public ReviewEntitySessionBean()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public ReviewEntity createNewReviewEntity(ReviewEntity newReviewEntity, Long reviewerId, Long listingId) throws InputDataValidationException, CreateNewReviewException
    {
        Set<ConstraintViolation<ReviewEntity>>constraintViolations = validator.validate(newReviewEntity);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                ListingEntity lisitng = listingEntitySessionBeanLocal.retrieveListingByListingId(listingId);
                UserEntity user = userEntitySessionBeanLocal.retrieveUserById(reviewerId);
                user.getReviews().add(newReviewEntity);
                lisitng.getReviews().add(newReviewEntity);
                entityManager.merge(user);
                entityManager.merge(lisitng);
                entityManager.persist(newReviewEntity);
                entityManager.flush();

                return newReviewEntity;
            }
            catch(PersistenceException ex)
            {                               
                throw new CreateNewReviewException("An unexpected error has occurred: " + ex.getMessage());              
            }
            catch(Exception ex)
            {                
                throw new CreateNewReviewException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public List<ReviewEntity> retrieveAllReviews()
    {
        Query query = entityManager.createQuery("SELECT r FROM ReviewEntity r ORDER BY r.reviewId ASC");
        List<ReviewEntity> reviewEntities = query.getResultList();
   
        return reviewEntities;
    }
    
    @Override
    public ReviewEntity retrieveReviewByReviewId(Long reviewId) throws ReviewNotFoundException
    {
        ReviewEntity reviewEntity = entityManager.find(ReviewEntity.class, reviewId);
        
        if(reviewEntity != null)
        {
            return reviewEntity;
        }
        else
        {
            throw new ReviewNotFoundException("Review ID " + reviewId + " does not exist!");
        }               
    }
    
    @Override
    public void updateReview(ReviewEntity reviewEntity) throws InputDataValidationException, ReviewNotFoundException, UpdateReviewException
    {
        Set<ConstraintViolation<ReviewEntity>>constraintViolations = validator.validate(reviewEntity);
        
        if(constraintViolations.isEmpty())
        {
            if(reviewEntity.getReviewId()!= null)
            {
                ReviewEntity reviewEntityToUpdate = retrieveReviewByReviewId(reviewEntity.getReviewId());
                
                reviewEntityToUpdate.setStarRating(reviewEntity.getStarRating()); 
                reviewEntityToUpdate.setDescripion(reviewEntity.getDescripion()); 
                reviewEntityToUpdate.setPicturePaths(reviewEntity.getPicturePaths());
            }
            else
            {
                throw new ReviewNotFoundException("Review ID not provided for review to be updated");
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public void deleteReview(Long reviewId) throws ReviewNotFoundException
    {
        ReviewEntity reviewEntityToRemove = retrieveReviewByReviewId(reviewId);
        UserEntity user = reviewEntityToRemove.getReviewer();
        ListingEntity listing = reviewEntityToRemove.getListing();
        user.getReviews().remove(reviewEntityToRemove);
        listing.getReviews().remove(reviewEntityToRemove);
        entityManager.merge(user);
        entityManager.merge(listing);
        entityManager.remove(reviewEntityToRemove);
                
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ReviewEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
