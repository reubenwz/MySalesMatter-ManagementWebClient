/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.ListingEntitySessionBeanLocal;
import ejb.session.stateless.ReviewEntitySessionBeanLocal;
import entity.ReviewEntity;
import entity.UserEntity;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.CreateNewReviewException;
import util.exception.InputDataValidationException;
import util.exception.ListingNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UserNotFoundException;

/**
 *
 * @author sylvia
 */
@Named(value = "addReviewManagedBean")
@SessionScoped
public class AddReviewManagedBean implements Serializable {

    private UserEntity user;
    private int starRating;
    private String description;
    private Long listingIdToView;
    private Long salesTransactionIdToView;
    private boolean reviewExist;
   
    
    @EJB
    private ReviewEntitySessionBeanLocal reviewEntitySessionBeanLocal;
    @EJB
    private ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;
    
    
    public AddReviewManagedBean() {
        reviewExist = false;
    }
    
    @PostConstruct
    public void postConstruct() {

        setUser((UserEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser"));
        try
        {
            if(listingIdToView == null)
            {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No listing has been selected", null));
            }
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void addReview(ActionEvent event) {


        try {
            Long userId = user.getUserId();
            ReviewEntity review = new ReviewEntity();
            review.setDescription(description);
            review.setReviewer(user);
            review.setStarRating(starRating);
            review.setListing(listingEntitySessionBeanLocal.retrieveListingByListingId(listingIdToView));
            review = reviewEntitySessionBeanLocal.createNewReviewEntity(review, userId, listingIdToView);
            reviewExist = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Review added successfully (Review ID: " + review.getReviewId() + ")", null));
        } catch (UserNotFoundException | UnknownPersistenceException | CreateNewReviewException | ListingNotFoundException | InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new Listing: " + ex.getMessage(), null));
        }
    }
    
    public boolean reviewDoesNotExist(Long listingId) {
        List<ReviewEntity> reviews = user.getReviews();
        if (!reviews.isEmpty()) {
            for (ReviewEntity r : reviews) {
                if (r.getListing().getListingId().equals(listingId)) {
                    return false;
                }
            }
        }
        return true;   
    }
    
    public void setId(ActionEvent event) {
        listingIdToView = (Long) event.getComponent().getAttributes().get("listingId");        
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public int getStarRating() {
        return starRating;
    }

    public void setStarRating(int starRating) {
        this.starRating = starRating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public Long getListingIdToView() {
        return listingIdToView;
    }

    public void setListingIdToView(Long listingIdToView) {
        this.listingIdToView = listingIdToView;
    }

    public Long getSalesTransactionIdToView() {
        return salesTransactionIdToView;
    }

    public void setSalesTransactionIdToView(Long salesTransactionIdToView) {
        this.salesTransactionIdToView = salesTransactionIdToView;
    }

    public boolean isReviewExist() {
        return reviewExist;
    }

    public void setReviewExist(boolean reviewExist) {
        this.reviewExist = reviewExist;
    }
    
}
