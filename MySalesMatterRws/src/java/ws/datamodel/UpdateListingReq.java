/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.ListingEntity;
import java.math.BigDecimal;
import java.util.Date;
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
    private String name;
    private String description;
    private String brand;
    private BigDecimal rentalPrice;
    private BigDecimal salePrice;
    private String location;
    private Long listingId;

    public UpdateListingReq() {
    }

    public UpdateListingReq(String name, String description, String brand, BigDecimal rentalPrice, BigDecimal salePrice, String location, String username, String password, Long categoryId, List<Long> tagIds, Long listingId) {
        this.username = username;
        this.password = password;
        this.categoryId = categoryId;
        this.tagIds = tagIds;
        this.description = description;
        this.name = name;
        this.brand = brand;
        this.rentalPrice = rentalPrice;
        this.salePrice = salePrice;
        this.location = location;
        this.listingId = listingId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public BigDecimal getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(BigDecimal rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getListingId() {
        return listingId;
    }

    public void setListingId(Long listingId) {
        this.listingId = listingId;
    }
    
    
    
}