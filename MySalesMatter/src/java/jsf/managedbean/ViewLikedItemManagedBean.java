/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.LikedItemEntitySessionBeanLocal;
import entity.LikedItemEntity;
import entity.ListingEntity;
import entity.UserEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.UserNotFoundException;

/**
 *
 * @author sylvia
 */
@Named(value = "viewLikedItemManagedBean")
@ViewScoped
public class ViewLikedItemManagedBean implements Serializable {

    @EJB
    private LikedItemEntitySessionBeanLocal likedItemEntitySessionBeanLocal;

    private List<ListingEntity> listings;
    private UserEntity currentUser;

    public ViewLikedItemManagedBean() {
        listings = new ArrayList<>();
        currentUser = new UserEntity();
    }

    @PostConstruct
    public void postConstruct() {
        try {
            currentUser = (UserEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
            List<LikedItemEntity> likedItems = likedItemEntitySessionBeanLocal.getLikedItems(currentUser.getUserId());
            if (!likedItems.isEmpty()) {
                for (LikedItemEntity l : likedItems) {
                    listings.add(l.getListing());
                }
            }
        } catch (UserNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }

    }
    
    public boolean alreadyLiked(Long listingId) {
        try {
            List<LikedItemEntity> likedItems = likedItemEntitySessionBeanLocal.getLikedItems(currentUser.getUserId());
            //List<LikedItemEntity> likedItems = currentUser.getLikedItems();
            if (likedItems != null) {
                for (LikedItemEntity l : likedItems) {
                    if (l.getListing().getListingId().equals(listingId)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (UserNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
        return false;
    }
    
    public UserEntity getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserEntity currentUser) {
        this.currentUser = currentUser;
    }

    public List<ListingEntity> getListings() {
        return listings;
    }

    public void setListings(List<ListingEntity> listings) {
        this.listings = listings;
    }

}
