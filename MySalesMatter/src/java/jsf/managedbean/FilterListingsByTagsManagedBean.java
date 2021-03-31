/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.ListingEntitySessionBeanLocal;
import ejb.session.stateless.TagEntitySessionBeanLocal;
import entity.ListingEntity;
import entity.TagEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author reuben
 */
@Named(value = "filterListingsByTagsManagedBean")
@ViewScoped
public class FilterListingsByTagsManagedBean implements Serializable{

    @EJB
    private TagEntitySessionBeanLocal tagEntitySessionBeanLocal;
    @EJB
    private ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;
    
    @Inject
    private ViewListingManagedBean viewListingManagedBean;
    
    private String condition;
    private List<Long> selectedTagIds;
    private List<SelectItem> selectItems;
    private List<ListingEntity> listingEntities;
    
    
    
    public FilterListingsByTagsManagedBean()
    {
        condition = "OR";
    }
    
    
    
    @PostConstruct
    public void postConstruct()
    {
        List<TagEntity> tagEntities = tagEntitySessionBeanLocal.retrieveAllTags();
        selectItems = new ArrayList<>();
        
        for(TagEntity tagEntity:tagEntities)
        {
            selectItems.add(new SelectItem(tagEntity.getTagId(), tagEntity.getName(), tagEntity.getName()));
        }
        
        
        // Optional demonstration of the use of custom converter
        // FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("TagEntityConverter_tagEntities", tagEntities);
        
        condition = (String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("listingFilterCondition");        
        selectedTagIds = (List<Long>)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("listingFilterTags");
        
        filterListing();
    }
    
    
    
    @PreDestroy
    public void preDestroy()
    {
        // FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("TagEntityConverter_tagEntities", null);
        // FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("TagEntityConverter_tagEntities", null);
    }
    
    
    
    public void filterListing()
    {        
        if(selectedTagIds != null && selectedTagIds.size() > 0)
        {
            listingEntities = listingEntitySessionBeanLocal.filterListingsByTags(selectedTagIds, condition);
        }
        else
        {
            listingEntities = listingEntitySessionBeanLocal.retrieveAllListings();
        }
    }
    
    
    
    public void viewListingDetails(ActionEvent event) throws IOException
    {
        Long listingIdToView = (Long)event.getComponent().getAttributes().get("listingId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("listingIdToView", listingIdToView);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("backMode", "filterListingsByTags");
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewListingDetails.xhtml");
    }

    
    
    public ViewListingManagedBean getViewListingManagedBean() {
        return viewListingManagedBean;
    }

    public void setViewListingManagedBean(ViewListingManagedBean viewListingManagedBean) {
        this.viewListingManagedBean = viewListingManagedBean;
    }
    
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) 
    {
        this.condition = condition;
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listingFilterCondition", condition);
    }
    
    public List<Long> getSelectedTagIds() {
        return selectedTagIds;
    }

    public void setSelectedTagIds(List<Long> selectedTagIds) 
    {
        this.selectedTagIds = selectedTagIds;
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listingFilterTags", selectedTagIds);
    }

    public List<SelectItem> getSelectItems() {
        return selectItems;
    }

    public void setSelectItems(List<SelectItem> selectItems) {
        this.selectItems = selectItems;
    }    

    public List<ListingEntity> getListingEntities() {
        return listingEntities;
    }

    public void setListingEntities(List<ListingEntity> listingEntities) {
        this.listingEntities = listingEntities;
    }
    
}
