/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.ListingEntity;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import ejb.session.stateless.ListingEntitySessionBeanLocal;

/**
 *
 * @author reuben
 */
@Named(value = "searchListingsByNameManagedBean")
@ViewScoped
public class SearchListingsByNameManagedBean implements Serializable
{
    @EJB
    private ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;
    
    @Inject
    private ViewListingManagedBean viewListingManagedBean;
    
    private String searchString;
    private List<ListingEntity> listingEntities;
    
    
    
    public SearchListingsByNameManagedBean() 
    {
    }
    
    
    
    @PostConstruct
    public void postConstruct()
    {
        searchString = (String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("listingSearchString");
        
        if(searchString == null || searchString.trim().length() == 0)
        {
            listingEntities = listingEntitySessionBeanLocal.retrieveAllListings();
        }
        else
        {
            listingEntities = listingEntitySessionBeanLocal.searchListingsByName(searchString);
        }
    }
    
    
    
    public void searchListing()
    {
        if(searchString == null || searchString.trim().length() == 0)
        {
            listingEntities = listingEntitySessionBeanLocal.retrieveAllListings();
        }
        else
        {
            listingEntities = listingEntitySessionBeanLocal.searchListingsByName(searchString);
        }
    }

    
    
    public ViewListingManagedBean getViewListingManagedBean() {
        return viewListingManagedBean;
    }

    public void setViewListingManagedBean(ViewListingManagedBean viewListingManagedBean) {
        this.viewListingManagedBean = viewListingManagedBean;
    }
    
    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString)
    {
        this.searchString = searchString;
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listingSearchString", searchString);
    }
    
    public List<ListingEntity> getListingEntities() {
        return listingEntities;
    }

    public void setListingEntities(List<ListingEntity> listingEntities) {
        this.listingEntities = listingEntities;
    }
}

