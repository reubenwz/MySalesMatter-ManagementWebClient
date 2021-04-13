/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.ListingEntity;
import entity.TagEntity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author rtan3
 */
public class CreateListingReq {
    
    private String username;
    private String password;
    private String name;
    private String description;
    private Date date;
    private String brand;
    private BigDecimal rentalPrice;
    private BigDecimal salePrice;
    private String location;
    private Long userId;
    private Long categoryId;
    private List<Long> tagIds;

    public CreateListingReq() {
    }

    public CreateListingReq(String name, String description, String brand, BigDecimal rentalPrice, BigDecimal salePrice, String location, String username, String password, Long userId, Long categoryId, List<Long> tagIds) {
        this.description = description;
        this.name = name;
        this.date = new Date();
        this.brand = brand;
        this.rentalPrice = rentalPrice;
        this.salePrice = salePrice;
        this.location = location;
        this.username = username;
        this.password = password;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    
}
