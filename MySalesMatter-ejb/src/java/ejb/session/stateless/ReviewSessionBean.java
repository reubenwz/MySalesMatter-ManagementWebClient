/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ListingEntity;
import entity.ReviewEntity;
import entity.UserEntity;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.UserNotFoundException;

/**
 *
 * @author sylvia
 */
@Stateless
public class ReviewSessionBean implements ReviewSessionBeanLocal {

    @PersistenceContext(unitName = "MySalesMatter-ejbPU")
    private EntityManager em;

    @EJB
    private UserEntitySessionBeanLocal userEntitySessionBeanLocal;
    
    @Override
    public List<ReviewEntity> getReviewsReceived(Long userId) throws UserNotFoundException {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.retrieveUserById(userId);
            List<ListingEntity> listings = userEntity.getListings();

            if (!listings.isEmpty()) {
                List<ReviewEntity> allReviews = new ArrayList<>();
               for (ListingEntity l: listings) {
                   for (ReviewEntity r: l.getReviews()) {
                       allReviews.add(r);
                   }
               }
                return allReviews;
            } else {
                return null;
            }
        } catch (UserNotFoundException ex) {
            throw new UserNotFoundException("User with this id, " + userId + ", does not exist!");
        }
    }
    
    @Override
    public List<ReviewEntity> getReviewsWritten(Long userId) throws UserNotFoundException {
        try {
            UserEntity userEntity = userEntitySessionBeanLocal.retrieveUserById(userId);

            if (!userEntity.getReviews().isEmpty()) {
                userEntity.getReviews().size();
                return userEntity.getReviews();
            } else {
                return null;
            }
        } catch (UserNotFoundException ex) {
            throw new UserNotFoundException("User with this id, " + userId + ", does not exist!");
        }
    }
}
