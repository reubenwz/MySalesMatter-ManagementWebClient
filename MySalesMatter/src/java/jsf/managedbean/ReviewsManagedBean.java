/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.ReviewEntitySessionBeanLocal;
import entity.ListingEntity;
import entity.ReviewEntity;
import entity.UserEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import util.exception.UserNotFoundException;

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

    @Inject
    private ViewReviewManagedBean viewReviewManagedBean;

    @EJB
    ReviewEntitySessionBeanLocal reviewEntitySessionBeanLocal;

    public ReviewsManagedBean() {
        reviewsWritten = new ArrayList<>();
        reviewsReceived = new ArrayList<>();
        currentUser = new UserEntity();
    }

    @PostConstruct
    public void postConstruct() {
        try {
            currentUser = (UserEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
            reviewsWritten = reviewEntitySessionBeanLocal.getReviewsByUserId(currentUser.getUserId());
            List<ListingEntity> listings = currentUser.getListings();
            for (ListingEntity l : listings) {
                for (ReviewEntity r : l.getReviews()) {
                    reviewsReceived.add(r);
                }
            }
        } catch (UserNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while loading conversations: " + ex.getMessage(), null));
        }

    }

    public void viewReviewDetails(ActionEvent event) throws IOException {
        Long reviewIdToView = (Long) event.getComponent().getAttributes().get("reviewId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("reviewIdToView", reviewIdToView);
        ViewReviewManagedBean view = new ViewReviewManagedBean();
        view.setReviewIdEntityToView(reviewIdToView);
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

    public ViewReviewManagedBean getViewReviewManagedBean() {
        return viewReviewManagedBean;
    }

    public void setViewReviewManagedBean(ViewReviewManagedBean viewReviewManagedBean) {
        this.viewReviewManagedBean = viewReviewManagedBean;
    }

}
