/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ReviewEntity;
import java.util.List;
import javax.ejb.Local;
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
@Local
public interface ReviewEntitySessionBeanLocal {
    public ReviewEntity createNewReviewEntity(ReviewEntity newReviewEntity, Long reviewerId, Long listingId, Long salesId) throws UnknownPersistenceException, InputDataValidationException, CreateNewReviewException, UserNotFoundException, ListingNotFoundException, SalesTransactionNotFoundException;
    public List<ReviewEntity> retrieveAllReviews();
    public ReviewEntity retrieveReviewByReviewId(Long reviewId) throws ReviewNotFoundException;
    public void deleteReview(Long reviewId) throws ReviewNotFoundException;
    public List<ReviewEntity> getReviewsByUserId(Long userId) throws UserNotFoundException;
    public void updateReview(Long reviewId, int starRating, String desc, String pictPath) throws InputDataValidationException, ReviewNotFoundException, UpdateReviewException;

    public List<ReviewEntity> getReviewsReceivedByUserId(Long userId) throws UserNotFoundException;
}
