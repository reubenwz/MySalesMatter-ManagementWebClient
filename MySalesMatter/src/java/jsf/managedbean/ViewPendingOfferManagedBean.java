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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;

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
