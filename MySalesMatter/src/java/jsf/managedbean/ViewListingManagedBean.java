/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.ListingEntitySessionBeanLocal;
import entity.ListingEntity;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.ListingNotFoundException;

/**
 *
 * @author reuben
 */
@Named(value = "viewListingManagedBean")
@ViewScoped
public class ViewListingManagedBean implements Serializable{

    private ListingEntity listingEntityToView;
    private Long listingIdToView;
    
    @EJB
    ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;
    
    
    
    public ViewListingManagedBean()
    {
        listingEntityToView = new ListingEntity();
    }
    
    
    
    @PostConstruct
    public void postConstruct()
    {        
        setListingIdEntityToView((Long)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("listingIdToView"));
        
        try
        {
            if(listingIdToView != null)
            {
                listingEntityToView = listingEntitySessionBeanLocal.retrieveListingByListingId(listingIdToView);
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
    
    public void foo()
    {        
    }
    
    
    public void viewListingDetails(ActionEvent event) throws IOException {
        Long listingIdToView = (Long) event.getComponent().getAttributes().get("listingId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("listingIdToView", listingIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("/MySalesMatter/systemAdministration/viewListingDetails.xhtml");
    }

    
    
    public ListingEntity getListingEntityToView() {
        return listingEntityToView;
    }

    public void setListingEntityToView(ListingEntity listingEntityToView) {
        this.listingEntityToView = listingEntityToView;
    }    

    public Long getListingIdEntityToView() {
        return listingIdToView;
    }

    public void setListingIdEntityToView(Long listingIdEntityToView) {
        this.listingIdToView = listingIdEntityToView;
    }

}
