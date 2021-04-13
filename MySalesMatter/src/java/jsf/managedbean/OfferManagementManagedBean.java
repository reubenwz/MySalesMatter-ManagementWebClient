/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.OfferEntitySessionBeanLocal;
import entity.ListingEntity;
import entity.OfferEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.OfferNotFoundException;

/**
 *
 * @author rtan3
 */
@Named(value = "offerManagementManagedBean")
@SessionScoped
public class OfferManagementManagedBean implements Serializable {

    @EJB
    private OfferEntitySessionBeanLocal offerEntitySessionBeanLocal;

    private List<OfferEntity> offerEntities;
    private OfferEntity offerEntityToAccept;
    
    private ListingEntity listingEntity;
    
    
    public OfferManagementManagedBean() {
        this.offerEntities = new ArrayList<>();
    }
    
    @PostConstruct
    public void postConstruct() {
        
    }
    
    public void retrieveOffers(ActionEvent event) {
        listingEntity = (ListingEntity) event.getComponent().getAttributes().get("listingEntityToRetrieveOffers");
        offerEntities = listingEntity.getOffers();
    }
    
    public void acceptOffer(ActionEvent event) {
        offerEntityToAccept = (OfferEntity) event.getComponent().getAttributes().get("offerEntityToAccept");
        System.out.println(offerEntityToAccept.getOfferId());
        try {
            offerEntitySessionBeanLocal.acceptOffer(offerEntityToAccept.getOfferId());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Offer accepted successfully", null));            
        } catch (OfferNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while accepting the offer: " + ex.getMessage(), null));
        }
    }

    public List<OfferEntity> getOfferEntities() {
        return offerEntities;
    }

    public void setOfferEntities(List<OfferEntity> offerEntities) {
        this.offerEntities = offerEntities;
    }

    public OfferEntity getOfferEntityToAccept() {
        return offerEntityToAccept;
    }

    public void setOfferEntityToAccept(OfferEntity offerEntityToAccept) {
        this.offerEntityToAccept = offerEntityToAccept;
    }

    public ListingEntity getListingEntity() {
        return listingEntity;
    }

    public void setListingEntity(ListingEntity listingEntity) {
        this.listingEntity = listingEntity;
    }
    
}
