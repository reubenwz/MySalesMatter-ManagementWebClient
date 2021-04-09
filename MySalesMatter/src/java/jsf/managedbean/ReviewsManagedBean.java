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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.primefaces.event.FileUploadEvent;
import util.exception.ReviewNotFoundException;
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
    private Long reviewId;
    private String tempPicturePath = "";
    private String newDescription = "";
    private int newStarRating;
    

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

    public void doUpdateReview(ActionEvent event) {
        setReviewId((Long) event.getComponent().getAttributes().get("reviewId"));
        System.out.println(getReviewId());
    }
    
    public void viewReviewDetails(ActionEvent event) throws IOException {
        Long reviewIdToView = (Long) event.getComponent().getAttributes().get("reviewId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("reviewIdToView", reviewIdToView);
        ViewReviewManagedBean view = new ViewReviewManagedBean();
        view.setReviewIdEntityToView(reviewIdToView);
    }
    
    public void updateReview(ActionEvent event) {
        try {
            System.out.println(getReviewId());
            System.out.println(newStarRating);
            System.out.println(newDescription);
            reviewEntitySessionBeanLocal.updateReview(getReviewId(), newStarRating, newDescription, tempPicturePath);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Review updated successfully", null));
        } catch (ReviewNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating review: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void handleFileUpload(FileUploadEvent event) {
        try {
            String newFilePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_1") + System.getProperty("file.separator") + event.getFile().getFileName();

            File file = new File(newFilePath);
            setTempPicturePath(event.getFile().getFileName());
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = event.getFile().getInputStream();

            while (true) {
                a = inputStream.read(buffer);

                if (a < 0) {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "File uploaded successfully", ""));
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload error: " + ex.getMessage(), ""));
        }
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


    public String getTempPicturePath() {
        return tempPicturePath;
    }

    public void setTempPicturePath(String tempPicturePath) {
        this.tempPicturePath = tempPicturePath;
    }

    public String getNewDescription() {
        return newDescription;
    }

    public void setNewDescription(String newDescription) {
        this.newDescription = newDescription;
    }

    public int getNewStarRating() {
        return newStarRating;
    }

    public void setNewStarRating(int newStarRating) {
        this.newStarRating = newStarRating;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

}
