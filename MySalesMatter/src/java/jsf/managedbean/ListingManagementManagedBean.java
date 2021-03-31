/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CategoryEntitySessionBeanLocal;
import ejb.session.stateless.LikedItemEntitySessionBeanLocal;
import ejb.session.stateless.ListingEntitySessionBeanLocal;
import ejb.session.stateless.OfferEntitySessionBeanLocal;
import ejb.session.stateless.TagEntitySessionBeanLocal;
import ejb.session.stateless.UserEntitySessionBeanLocal;
import entity.BuyOfferEntity;
import entity.CategoryEntity;
import entity.LikedItemEntity;
import entity.ListingEntity;
import entity.OfferEntity;
import entity.RentalOfferEntity;
import entity.TagEntity;
import entity.UserEntity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.event.FileUploadEvent;
import util.exception.CreateNewListingException;
import util.exception.DeleteListingException;
import util.exception.InputDataValidationException;
import util.exception.ListingNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UserNotFoundException;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import util.enumeration.OfferType;
import util.exception.OfferNotFoundException;

/**
 *
 * @author reuben
 */
@Named(value = "listingManagementManagedBean")
@SessionScoped
public class ListingManagementManagedBean implements Serializable {

    @EJB
    private OfferEntitySessionBeanLocal offerEntitySessionBeanLocal;
    @EJB
    private ListingEntitySessionBeanLocal listingEntitySessionBeanLocal;
    @EJB
    private CategoryEntitySessionBeanLocal categoryEntitySessionBeanLocal;
    @EJB
    private TagEntitySessionBeanLocal tagEntitySessionBeanLocal;
    @EJB
    private UserEntitySessionBeanLocal userEntitySessionBeanLocal;
    @EJB
    private LikedItemEntitySessionBeanLocal likedItemEntitySessionBeanLocal;
    

    @Inject
    private ViewListingManagedBean viewListingManagedBean;

    private List<ListingEntity> listingEntities;
    private List<ListingEntity> myListingEntities;
    private List<ListingEntity> filteredListingEntities;

    private ListingEntity newListingEntity;
    private Long categoryIdNew;
    private List<Long> tagIdsNew;
    private List<CategoryEntity> categoryEntities;
    private List<TagEntity> tagEntities;

    private ListingEntity selectedListingEntityToUpdate;
    private Long categoryIdUpdate;
    private List<Long> tagIdsUpdate;
    
    private String tempPicturePath = "";

    private UserEntity currentUser;
    private OfferEntity rentalOfferEntity;
    private ListingEntity selectedListingEntityToMakeRentalOffer;

    public ListingManagementManagedBean() {
        newListingEntity = new ListingEntity();
        currentUser = new UserEntity();
    }

    @PostConstruct
    public void postConstruct() {
        listingEntities = listingEntitySessionBeanLocal.retrieveAllListings();
        categoryEntities = categoryEntitySessionBeanLocal.retrieveAllLeafCategories();
        tagEntities = tagEntitySessionBeanLocal.retrieveAllTags();
        setCurrentUser((UserEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser"));
        myListingEntities = listingEntitySessionBeanLocal.retrieveListingsByUser(currentUser.getUserId());
//        UserEntity user;
//        try {
//            user = userEntitySessionBeanLocal.retrieveUserById(getCurrentUser().getUserId());
//        } catch (UserNotFoundException ex) {
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "User not found" + ex.getMessage(), null));
//        }
    }

    public void viewListingDetails(ActionEvent event) throws IOException {
        Long listingIdToView = (Long) event.getComponent().getAttributes().get("listingId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("listingIdToView", listingIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewListingDetails.xhtml");
    }
    
    public List<ListingEntity> retrieveMyListingEntities() {
        myListingEntities = listingEntitySessionBeanLocal.retrieveListingsByUser(currentUser.getUserId());
        return myListingEntities;
    }

    public boolean alreadyLiked(Long listingId) {
        List<LikedItemEntity> likedItems = currentUser.getLikedItems();
            if (likedItems != null) {
                for (LikedItemEntity l : likedItems) {
                    if (l.getListing().getListingId().equals(listingId)) {
                        return true;
                    }
                }
            }
        return false;
    }

    public void createNewListing(ActionEvent event) {
        if (categoryIdNew == 0) {
            categoryIdNew = null;
        }

        try {
            System.out.println("temp picture path in create new listing: " + tempPicturePath);
            newListingEntity.setPicturePath(tempPicturePath);
            ListingEntity pe = listingEntitySessionBeanLocal.createNewListing(newListingEntity, categoryIdNew, tagIdsNew, currentUser.getUserId());
            tempPicturePath = "";
            listingEntities.add(pe);

            if (filteredListingEntities != null) {
                filteredListingEntities.add(pe);
            }

            newListingEntity = new ListingEntity();
            categoryIdNew = null;
            tagIdsNew = null;

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Listing created successfully (Listing ID: " + pe.getListingId() + ")", null));
        } catch (InputDataValidationException | CreateNewListingException | UnknownPersistenceException | UserNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new Listing: " + ex.getMessage(), null));
        }
    }

    public void doUpdateListing(ActionEvent event) {
        selectedListingEntityToUpdate = (ListingEntity) event.getComponent().getAttributes().get("listingEntityToUpdate");
        System.out.println(selectedListingEntityToUpdate.getName());

        categoryIdUpdate = selectedListingEntityToUpdate.getCategoryEntity().getCategoryId();
        tagIdsUpdate = new ArrayList<>();

        for (TagEntity tagEntity : selectedListingEntityToUpdate.getTags()) {
            tagIdsUpdate.add(tagEntity.getTagId());
        }
    }

    public void updateListing(ActionEvent event) {
        System.out.println(selectedListingEntityToUpdate.getName());
        if (categoryIdUpdate == 0) {
            categoryIdUpdate = null;
        }

        try {
            listingEntitySessionBeanLocal.updateListing(selectedListingEntityToUpdate, categoryIdUpdate, tagIdsUpdate);

            for (CategoryEntity ce : categoryEntities) {
                if (ce.getCategoryId().equals(categoryIdUpdate)) {
                    selectedListingEntityToUpdate.setCategoryEntity(ce);
                    break;
                }
            }

            selectedListingEntityToUpdate.getTags().clear();

            for (TagEntity te : tagEntities) {
                if (tagIdsUpdate.contains(te.getTagId())) {
                    selectedListingEntityToUpdate.getTags().add(te);
                }
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Listing updated successfully", null));
        } catch (ListingNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating listing: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public void deleteListing(ActionEvent event) {
        
        try {
            ListingEntity listingEntityToDelete = (ListingEntity) event.getComponent().getAttributes().get("listingEntityToDelete");
            listingEntitySessionBeanLocal.deleteListing(listingEntityToDelete.getListingId());

            myListingEntities.remove(listingEntityToDelete);
            listingEntities.remove(listingEntityToDelete);

            if (filteredListingEntities != null) {
                filteredListingEntities.remove(listingEntityToDelete);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Listing deleted successfully", null));
        } catch (ListingNotFoundException | DeleteListingException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting product: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
   public void doMakeRentalOffer(ActionEvent event) {
       rentalOfferEntity = new RentalOfferEntity();
       rentalOfferEntity.setOfferType(OfferType.RENTAL);
       selectedListingEntityToMakeRentalOffer = (ListingEntity) event.getComponent().getAttributes().get("listingEntityToMakeOffer");
   }
    
    public void makeRentalOffer(ActionEvent event) {
        System.out.println("make rental offer");
        try {
            System.out.println("setting user below");
            rentalOfferEntity.setUser(currentUser);
            rentalOfferEntity.setOfferDate(new Date());
            rentalOfferEntity.setListing(selectedListingEntityToMakeRentalOffer);
            System.out.println("end date below");
            rentalOfferEntity = (RentalOfferEntity) offerEntitySessionBeanLocal.createNewOffer((OfferEntity) rentalOfferEntity, currentUser.getUserId(), selectedListingEntityToMakeRentalOffer.getListingId());
            System.out.println("total: " + rentalOfferEntity.getTotalPrice());

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Rental Offer made successfully", null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void makeBuyOffer(ActionEvent event) {
        try {
            ListingEntity listingEntityToMakeOffer = (ListingEntity) event.getComponent().getAttributes().get("listingEntityToMakeOffer");
            OfferEntity newOfferEntity = new BuyOfferEntity(listingEntityToMakeOffer.getSalePrice(), new Date(), OfferType.BUY);
            newOfferEntity.setUser(currentUser);
            newOfferEntity.setListing(listingEntityToMakeOffer);
            newOfferEntity.setOfferDate(new Date());
            
            newOfferEntity = offerEntitySessionBeanLocal.createNewOffer(newOfferEntity, currentUser.getUserId(), listingEntityToMakeOffer.getListingId());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Buy Offer made successfully", null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void likeItem(ActionEvent event) {
        try {
            ListingEntity listingEntityToLike= (ListingEntity) event.getComponent().getAttributes().get("listingEntityToLike");
            LikedItemEntity likedItem = likedItemEntitySessionBeanLocal.createNewLikedItem(currentUser.getUserId(), listingEntityToLike.getListingId());
           
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Liked", null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void unlikeItem(ActionEvent event) {
        try {
            ListingEntity listingEntityToUnlike = (ListingEntity) event.getComponent().getAttributes().get("listingEntityToUnlike");
            Long userId = currentUser.getUserId();
//            System.out.println("Listing id: " + listingEntityToUnlike.getListingId());
//            System.out.println("Listing id: " + userId);
            likedItemEntitySessionBeanLocal.unlikeItem(userId, listingEntityToUnlike.getListingId());
           
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Unliked", null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            String newFilePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_1") + System.getProperty("file.separator") + event.getFile().getFileName();

            System.err.println("********** ListingManagementManagedBean.handleFileUpload(): File name: " + event.getFile().getFileName());
            System.err.println("********** ListingManagementManagedBean.handleFileUpload(): newFilePath: " + newFilePath);

            File file = new File(newFilePath);
            tempPicturePath = event.getFile().getFileName();
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = event.getFile().getInputStream();

            while (true) {
                a = inputStream.read(buffer);

                if (a < 0) {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "File uploaded successfully", ""));
            System.out.println("temp picture path in handle: " + tempPicturePath);
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload error: " + ex.getMessage(), ""));
        }
    }

    public ViewListingManagedBean getViewListingManagedBean() {
        return viewListingManagedBean;
    }

    public void setViewListingManagedBean(ViewListingManagedBean viewListingManagedBean) {
        this.viewListingManagedBean = viewListingManagedBean;
    }

    public List<ListingEntity> getListingEntities() {
        return listingEntities;
    }

    public void setListingEntities(List<ListingEntity> listingEntities) {
        this.listingEntities = listingEntities;
    }

    public List<ListingEntity> getFilteredListingEntities() {
        return filteredListingEntities;
    }

    public void setFilteredListingEntities(List<ListingEntity> filteredListingEntities) {
        this.filteredListingEntities = filteredListingEntities;
    }

    public ListingEntity getNewListingEntity() {
        return newListingEntity;
    }

    public void setNewListingEntity(ListingEntity newListingEntity) {
        this.newListingEntity = newListingEntity;
    }

    public Long getCategoryIdNew() {
        return categoryIdNew;
    }

    public void setCategoryIdNew(Long categoryIdNew) {
        this.categoryIdNew = categoryIdNew;
    }

    public List<Long> getTagIdsNew() {
        return tagIdsNew;
    }

    public void setTagIdsNew(List<Long> tagIdsNew) {
        this.tagIdsNew = tagIdsNew;
    }

    public List<CategoryEntity> getCategoryEntities() {
        return categoryEntities;
    }

    public void setCategoryEntities(List<CategoryEntity> categoryEntities) {
        this.categoryEntities = categoryEntities;
    }

    public List<TagEntity> getTagEntities() {
        return tagEntities;
    }

    public void setTagEntities(List<TagEntity> tagEntities) {
        this.tagEntities = tagEntities;
    }

    public ListingEntity getSelectedListingEntityToUpdate() {
        return selectedListingEntityToUpdate;
    }

    public void setSelectedListingEntityToUpdate(ListingEntity selectedListingEntityToUpdate) {
        this.selectedListingEntityToUpdate = selectedListingEntityToUpdate;
    }

    public Long getCategoryIdUpdate() {
        return categoryIdUpdate;
    }

    public void setCategoryIdUpdate(Long categoryIdUpdate) {
        this.categoryIdUpdate = categoryIdUpdate;
    }

    public List<Long> getTagIdsUpdate() {
        return tagIdsUpdate;
    }

    public void setTagIdsUpdate(List<Long> tagIdsUpdate) {
        this.tagIdsUpdate = tagIdsUpdate;
    }

    public UserEntity getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserEntity currentUser) {
        this.currentUser = currentUser;
    }

    public List<ListingEntity> getMyListingEntities() {
        return myListingEntities;
    }

    public void setMyListingEntities(List<ListingEntity> myListingEntities) {
        this.myListingEntities = myListingEntities;
    }

    public OfferEntity getRentalOfferEntity() {
        return rentalOfferEntity;
    }

    public void setRentalOfferEntity(RentalOfferEntity rentalOfferEntity) {
        this.rentalOfferEntity = rentalOfferEntity;
    }

}
