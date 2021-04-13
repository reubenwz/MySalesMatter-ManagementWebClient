/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.OfferEntitySessionBeanLocal;
import entity.OfferEntity;
import entity.UserEntity;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.DeleteOfferException;

/**
 *
 * @author reuben
 */
@Named(value = "viewPendingOfferManagedBean")
@SessionScoped
public class ViewPendingOfferManagedBean implements Serializable {

    @EJB
    private OfferEntitySessionBeanLocal offerEntitySessionBeanLocal;

    private List<OfferEntity> offers; 
    private UserEntity currentUser;

    public ViewPendingOfferManagedBean() {
        offers = new ArrayList<>();
        currentUser = new UserEntity();
    }

    @PostConstruct
    public void postConstruct() {
        currentUser = (UserEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
        List<OfferEntity> offerItem = currentUser.getOffers();
        
        if (!offerItem.isEmpty()) {
            for (OfferEntity o : offerItem) {
                if (!o.isAccepted()) {
                    offers.add(o);
                }
            }
        }

    }
    
    public List<OfferEntity> retrieveOffers() {
        List<OfferEntity> offerItem = offerEntitySessionBeanLocal.retrieveOffersByUserId(currentUser.getUserId());
        List<OfferEntity> pendingOffers = new ArrayList<>();       
        if (!offerItem.isEmpty()) {
            for (OfferEntity o : offerItem) {
                if (!o.isAccepted()) {
                    pendingOffers.add(o);
                }
            }
        }
        return pendingOffers;
    }
    
    public void deletePendingOffer(ActionEvent event) {
        Long offerId = (Long)event.getComponent().getAttributes().get("offerId");
        System.out.println("**** OFFERID TO DELETE: " + offerId);
        try {
            offerEntitySessionBeanLocal.deleteOffer(offerId);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Offer deleted successfully", null));           
        } catch (DeleteOfferException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting the offer: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting the offer: " + ex.getMessage(), null));
        } 
    }

    public UserEntity getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserEntity currentUser) {
        this.currentUser = currentUser;
    }

    public List<OfferEntity> getOffers() {
        return offers;
    }

    public void setOffers(List<OfferEntity> offers) {
        this.offers = offers;
    }
}
