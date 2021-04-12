/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;


import ejb.session.stateless.ListingEntitySessionBeanLocal;
import entity.ListingEntity;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author Yuki
 */
@Named(value = "carouselViewManagedBean")
@ViewScoped
public class CarouselViewManagedBean implements Serializable{

    @EJB(name = "ListingEntitySessionBeanLocal")
    private ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;
   

    private List<ListingEntity> listingEntities;
    private ListingEntity selectedListing;
    
    public CarouselViewManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        setListingEntities(listingEntitySessionBeanLocal.retrieveAllListings());
    }


    /**
     * @return the selectedListing
     */
    public ListingEntity getSelectedListing() {
        return selectedListing;
    }

    /**
     * @param selectedListing the selectedListing to set
     */
    public void setSelectedListing(ListingEntity selectedListing) {
        this.selectedListing = selectedListing;
    }

    /**
     * @return the listingEntities
     */
    public List<ListingEntity> getListingEntities() {
        return listingEntities;
    }

    /**
     * @param listingEntities the listingEntities to set
     */
    public void setListingEntities(List<ListingEntity> listingEntities) {
        this.listingEntities = listingEntities;
    }

}
