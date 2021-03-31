/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.ListingEntity;
import entity.ReviewEntity;
import entity.UserEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author sylvia
 */
@Named(value = "reviewsManagedBean")
@ViewScoped
public class ReviewsManagedBean implements Serializable {

    private List<ReviewEntity> reviewsWritten;
    private List<ReviewEntity> reviewsReceived;
    private UserEntity currentUser;
    
    public ReviewsManagedBean() {
        reviewsWritten = new ArrayList<>();
        reviewsReceived = new ArrayList<>();
        currentUser = new UserEntity();
    }
    
    @PostConstruct
    public void postConstruct() {
        currentUser = (UserEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
        reviewsWritten = currentUser.getReviews();
        List<ListingEntity> listings = currentUser.getListings();
        for (ListingEntity l : listings) {
            for (ReviewEntity r : l.getReviews()) {
                reviewsReceived.add(r);
            }
        }
    }

    public void viewReviewDetails(ActionEvent event) throws IOException {
        Long reviewIdToView = (Long) event.getComponent().getAttributes().get("reviewId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("reviewIdToView", reviewIdToView);
        //FacesContext.getCurrentInstance().getExternalContext().redirect("viewProductDetails.xhtml");
    }

    public List<ReviewEntity> getReviewsWritten() {
        return reviewsWritten;
    }

    public void setReviewsWritten(List<ReviewEntity> reviewsWritten) {
        this.reviewsWritten = reviewsWritten;
    }

    public UserEntity getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserEntity currentUser) {
        this.currentUser = currentUser;
    }

    public List<ReviewEntity> getReviewsReceived() {
        return reviewsReceived;
    }

    public void setReviewsReceived(List<ReviewEntity> reviewsReceived) {
        this.reviewsReceived = reviewsReceived;
    }
    
}
