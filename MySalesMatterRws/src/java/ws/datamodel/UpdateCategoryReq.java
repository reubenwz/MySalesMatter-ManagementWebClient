/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.CategoryEntity;

/**
 *
 * @author rtan3
 */
public class UpdateCategoryReq {
    
    private String username;
    private String password;
    private CategoryEntity categoryEntity;
    private Long parentCategoryId;

    public UpdateCategoryReq() {
    }

    public UpdateCategoryReq(String username, String password, CategoryEntity categoryEntity, Long parentCategoryId) {
        this.username = username;
        this.password = password;
        this.categoryEntity = categoryEntity;
        this.parentCategoryId = parentCategoryId;
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

    public CategoryEntity getCategoryEntity() {
        return categoryEntity;
    }

    public void setCategoryEntity(CategoryEntity categoryEntity) {
        this.categoryEntity = categoryEntity;
    }

    public Long getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Long parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }
    
    
    
}
