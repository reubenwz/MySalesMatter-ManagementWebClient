/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.ListingEntity;
import entity.TagEntity;
import java.util.List;

/**
 *
 * @author rtan3
 */
public class CreateListingReq {
    
    private String username;
    private String password;
    private ListingEntity newListingEntity;
    private Long userId;
    private Long categoryId;
    private List<Long> tagIds;

    public CreateListingReq() {
    }

    public CreateListingReq(String username, String password, ListingEntity newListingEntity, Long userId, Long categoryId, List<Long> tagIds) {
        this.username = username;
        this.password = password;
        this.newListingEntity = newListingEntity;
        this.userId = userId;
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

    public ListingEntity getNewListingEntity() {
        return newListingEntity;
    }

    public void setNewListingEntity(ListingEntity newListingEntity) {
        this.newListingEntity = newListingEntity;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
