/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.OfferEntity;
import java.math.BigDecimal;
import java.util.Date;
import util.enumeration.OfferType;

/**
 *
 * @author sylvia
 */
public class CreateOfferReq {
    private String username;
    private String password;
    private BigDecimal totalPrice;
    private Date offerDate;
    private Integer offerType;
    private Date startDate;
    private Date endDate;
    private Long userId;
    private Long listingId;

    public CreateOfferReq(String username, String password, BigDecimal totalPrice, Date offerDate, Integer offerType, Date startDate, Date endDate, Long userId, Long listingId) {
        this.username = username;
        this.password = password;
        this.totalPrice = totalPrice;
        this.offerDate = offerDate;
        this.offerType = offerType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
        this.listingId = listingId;
    }

    
    
    public CreateOfferReq() {
        
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

    public Long getListingId() {
        return listingId;
    }

    public void setListingId(Long listingId) {
        this.listingId = listingId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getOfferDate() {
        return offerDate;
    }

    public void setOfferDate(Date offerDate) {
        this.offerDate = offerDate;
    }

    public Integer getOfferType() {
        return offerType;
    }

    public void setOfferType(Integer offerType) {
        this.offerType = offerType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    
}
