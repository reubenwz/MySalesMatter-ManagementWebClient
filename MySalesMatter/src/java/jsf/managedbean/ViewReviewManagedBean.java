/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.ReviewEntitySessionBeanLocal;
import entity.ReviewEntity;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import util.exception.ReviewNotFoundException;

/**
 *
 * @author sylvia
 */
@Named(value = "viewReviewManagedBean")
@ViewScoped
public class ViewReviewManagedBean implements Serializable {

    private ReviewEntity reviewEntityToView;
    private Long reviewIdToView;
    
    @EJB
    ReviewEntitySessionBeanLocal reviewEntitySessionBeanLocal;
    
    
    
    public ViewReviewManagedBean()
    {
        reviewEntityToView = new ReviewEntity();
    }
    
    
    
    @PostConstruct
    public void postConstruct()
    {        
        setReviewIdEntityToView((Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("reviewIdToView"));
        
        try
        {
            if(reviewIdToView != null)
            {
                reviewEntityToView = reviewEntitySessionBeanLocal.retrieveReviewByReviewId(reviewIdToView);
            }
            else
            {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No review has been selected", null));
            }
        }
        catch(ReviewNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving the review details: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void foo() {        
    }
    
    public void viewReviewDetails(ActionEvent event) throws IOException {
        Long reviewIdToView = (Long) event.getComponent().getAttributes().get("reviewId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("reviewIdToView", reviewIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("/MySalesMatter/systemAdministration/viewReviewDetails.xhtml");
    }

    public ReviewEntity getReviewEntityToView() {
        return reviewEntityToView;
    }

    public void setReviewEntityToView(ReviewEntity reviewEntityToView) {
        this.reviewEntityToView = reviewEntityToView;
    }    

    public Long getReviewIdEntityToView() {
        return reviewIdToView;
    }

    public void setReviewIdEntityToView(Long reviewIdEntityToView) {
        this.reviewIdToView = reviewIdEntityToView;
    }


}
