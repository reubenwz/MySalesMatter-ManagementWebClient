/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.ListingEntity;
import java.util.List;

/**
 *
 * @author rtan3
 */
public class UpdateListingReq {
    
    private String username;
    private String password;
    private ListingEntity listingEntity;
    private Long categoryId;
    private List<Long> tagIds;

    public UpdateListingReq() {
    }

    public UpdateListingReq(String username, String password, ListingEntity listingEntity, Long categoryId, List<Long> tagIds) {
        this.username = username;
        this.password = password;
        this.listingEntity = listingEntity;
        this.categoryId = categoryId;
        this.tagIds = tagIds;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ListingEntity getListingEntity() {
        return listingEntity;
    }

    public void setListingEntity(ListingEntity listingEntity) {
        this.listingEntity = listingEntity;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public List<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }
    
    
    
}
