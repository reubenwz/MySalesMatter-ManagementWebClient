/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import ejb.session.stateless.ListingEntitySessionBeanLocal;
import entity.CategoryEntity;
import entity.ListingEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import util.exception.CategoryNotFoundException;

/**
 *
 * @author reuben
 */
@Named(value = "filterListingsByCategoryManagedBean")
@ViewScoped
public class FilterListingsByCategoryManagedBean implements Serializable{

    @EJB
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;
    @EJB
    private ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;
    
    @Inject
    private ViewListingManagedBean viewListingManagedBean;
        
    private TreeNode treeNode;
    private TreeNode selectedTreeNode;
    
    private List<ListingEntity> listingEntities;
    
    
    
    public FilterListingsByCategoryManagedBean() 
    {
    }
    
    
    
    @PostConstruct
    public void postConstruct()
    {
        List<CategoryEntity> categoryEntities = categoryEntitySessionBeanLocal.retrieveAllRootCategories();
        treeNode = new DefaultTreeNode("Root", null);
        
        for(CategoryEntity categoryEntity:categoryEntities)
        {
            createTreeNode(categoryEntity, treeNode);
        }
        
        Long selectedCategoryId = (Long)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("listingFilterCategory");
        
        if(selectedCategoryId != null)
        {
            for(TreeNode tn:treeNode.getChildren())
            {
                CategoryEntity ce = (CategoryEntity)tn.getData();
                
                if(ce.getCategoryId().equals(selectedCategoryId))
                {
                    selectedTreeNode = tn;
                    break;
                }
                else
                {
                    selectedTreeNode = searchTreeNode(selectedCategoryId, tn);
                }            
            }
        }
        
        filterListing();
    }
    
    
    
    public void filterListing()
    {
        if(selectedTreeNode != null)
        {               
            try
            {
                CategoryEntity ce = (CategoryEntity)selectedTreeNode.getData();
                
                listingEntities = listingEntitySessionBeanLocal.filterListingsByCategory(ce.getCategoryId());
            }
            catch(CategoryNotFoundException ex)
            {
                listingEntities = listingEntitySessionBeanLocal.retrieveAllListings();
            }
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
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("backMode", "filterListingsByCategory");
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewListingDetails.xhtml");
    }
    
    
    
    private void createTreeNode(CategoryEntity categoryEntity, TreeNode parentTreeNode)
    {
        TreeNode treeNode = new DefaultTreeNode(categoryEntity, parentTreeNode);
                
        for(CategoryEntity ce:categoryEntity.getSubCategoryEntities())
        {
            createTreeNode(ce, treeNode);
        }
    }
    
    
    
    private TreeNode searchTreeNode(Long selectedCategoryId, TreeNode treeNode)
    {
        for(TreeNode tn:treeNode.getChildren())
        {
            CategoryEntity ce = (CategoryEntity)tn.getData();
            
            if(ce.getCategoryId().equals(selectedCategoryId))
            {
                return tn;
            }
            else
            {
                return searchTreeNode(selectedCategoryId, tn);
            }            
        }
        
        return null;
    }

    
    
    public ViewListingManagedBean getViewListingManagedBean() {
        return viewListingManagedBean;
    }

    public void setViewListingManagedBean(ViewListingManagedBean viewListingManagedBean) {
        this.viewListingManagedBean = viewListingManagedBean;
    }
    
    public TreeNode getTreeNode() {
        return treeNode;
    }

    public void setTreeNode(TreeNode treeNode) {
        this.treeNode = treeNode;
    }
    
    public TreeNode getSelectedTreeNode() {
        return selectedTreeNode;
    }

    public void setSelectedTreeNode(TreeNode selectedTreeNode) 
    {
        this.selectedTreeNode = selectedTreeNode;
        
        
        if(selectedTreeNode != null)
        {            
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listingFilterCategory", ((CategoryEntity)selectedTreeNode.getData()).getCategoryId());
        }
    }

    public List<ListingEntity> getListingEntities() {
        return listingEntities;
    }

    public void setListingEntities(List<ListingEntity> listingEntities) {
        this.listingEntities = listingEntities;
    }
    
}
