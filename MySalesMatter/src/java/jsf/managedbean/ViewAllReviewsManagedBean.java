/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.ListingEntitySessionBeanLocal;
import entity.ListingEntity;
import entity.ReviewEntity;
import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.ListingNotFoundException;

/**
 *
 * @author sylvia
 */
@Named(value = "viewAllReviewsManagedBean")
@RequestScoped
public class ViewAllReviewsManagedBean {
    
    @EJB
    private ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;
    
    private List<ReviewEntity> reviewEntities;
    private Long listingIdToView;
    private ListingEntity listingEntityToView;
    
    public ViewAllReviewsManagedBean() {
        listingEntityToView = new ListingEntity();
    }
    
    @PostConstruct
    public void postConstruct() {
        setListingIdToView((Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("listingIdToView"));
        
        try
        {
            if(listingIdToView != null)
            {
                setListingEntityToView(listingEntitySessionBeanLocal.retrieveListingByListingId(listingIdToView));
                setReviewEntities(listingEntityToView.getReviews());
            }
            else
            {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No listing has been selected", null));
            }
        }
        catch(ListingNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving the product details: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }    
    }
    
    public void foo() {        
    }
    
    public void viewReviewDetails(ActionEvent event) throws IOException {
        Long listingIdToView = (Long) event.getComponent().getAttributes().get("listingId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("listingIdToView", listingIdToView);
//        System.out.println("listing id: " + listingIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("/MySalesMatter/systemAdministration/viewAllReviews.xhtml");
    }

    public List<ReviewEntity> getReviewEntities() {
        return reviewEntities;
    }

    public void setReviewEntities(List<ReviewEntity> reviewEntities) {
        this.reviewEntities = reviewEntities;
    }

    public Long getListingIdToView() {
        return listingIdToView;
    }

    public void setListingIdToView(Long ListingIdToView) {
        this.listingIdToView = ListingIdToView;
    }

    public ListingEntity getListingEntityToView() {
        return listingEntityToView;
    }

    public void setListingEntityToView(ListingEntity listingEntityToView) {
        this.listingEntityToView = listingEntityToView;
    }
    
}
